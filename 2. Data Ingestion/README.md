# Data Ingestion

## convert jupyter notebook to a python script

```bash
jupyter nbconvert --to=script upload-data.ipynb
```

which in turn produces `upload-data.py`

- clean out the script by removing comments, reorder imports and save as ingest_data.py


## Data Lake

A Data Lake serves as a centralized repository for aggregating vast volumes of data from diverse origins. This data repository can encompass structured, unstructured, or a combination of data formats.

The primary objective of establishing a Data Lake is to facilitate rapid data ingestion and seamless accessibility for team members.

Key attributes of an ideal Data Lake include:

- Robust Security Measures
- Scalability
- Cost-effective hardware infrastructure support

A Data Lake (DL) and a Data Warehouse (DW) are distinct concepts with several contrasting characteristics:

- Data Processing:

  - Data Lake (DL): Raw, minimally processed, often unstructured data.

  - Data Warehouse (DW): Refined, cleaned, and structured data designed for specific use cases.
  
- Size:

  - Data Lake (DL): Enormous, containing petabytes of data; transformation occurs when needed, with the potential for indefinite storage.

  - Data Warehouse (DW): Smaller in scale compared to DLs; data is preprocessed before ingestion and may undergo periodic purging.

- Nature:

  - Data Lake (DL): Versatile, suitable for various purposes with undefined data.

  - Data Warehouse (DW): Historical and relational data, commonly transactional in nature.

- Users:

  - Data Lake (DL): Typically used by data scientists and data analysts.

  - Data Warehouse (DW): Mainly accessed by business analysts.

- Use Cases:

  - Data Lake (DL): Utilized for tasks like stream processing, machine learning, and real-time analytics.

  - Data Warehouse (DW): Employed for batch processing, business intelligence, and reporting.

Data Lakes emerged as a response to the need to capture and store data before it could be efficiently integrated into Data Warehouses. They serve as repositories for a wide range of data that can be harnessed in various ways, even before the development of the necessary relationships for a Data Warehouse is completed. This allows organizations to collect potentially valuable data from the outset of new projects without wasting valuable information.

### ELT vs ETL

In the realm of data ingestion, Data Warehouses (DWs) typically adhere to the Export, Transform, and Load (ETL) model, while Data Lakes (DLs) opt for Export, Load, and Transform (ELT).

The primary distinction lies in the order of steps. In DWs, ETL (Schema on Write) dictates that data undergoes transformation (preprocessing, etc.) before it reaches its final destination. In contrast, DLs follow ELT (Schema on Read), where data is directly stored without transformations, and any schemas are derived when reading the data from the Data Lake.

However, it's important to note that Data Lakes can risk becoming Data Swamps if proper data management measures are not implemented. This transformation can result in various issues, including:

- Lack of versioning for data.
- Incompatible schemas for the same data.
- Absence of associated metadata.
- Inability to perform joins between different datasets.

For effective management and utilization of Data Lakes, techniques like versioning and metadata are invaluable in preventing them from turning into Data Swamps.

Common cloud providers that offer services for Data Lakes include:

- Google Cloud Platform, with its Cloud Storage.
- Amazon Web Services, providing Amazon S3.
- Microsoft Azure, offering Azure Blob Storage

## Intro to Orchestration with Airflow

A data pipeline is a system or service designed to take in data as input and then process or transform that data in some way before delivering it as output. For instance, it can involve tasks like reading data from a CSV file, applying various transformations to the data, and ultimately storing the processed information as a structured table in a PostgreSQL database.

The script we've developed serves as an illustration of what not to do when constructing a data pipeline because it combines two steps that are better off separated, namely the download and processing steps. This amalgamation can lead to less-than-ideal outcomes, particularly when dealing with factors such as slow internet connections or script testing.

In an optimal setup, each of these steps would exist as distinct entities, possibly in the form of two separate scripts.

In data workflows, it's crucial to have well-defined data flows and clear dependencies. The steps, denoted in capital letters, represent individual jobs within the workflow, and the objects in between symbolize the outputs of those jobs, which serve as dependencies for other jobs. Each job can have its own set of parameters, and there may also be global parameters shared across all jobs.

To manage and automate these data workflows effectively, a Workflow Orchestration Tool is employed. This tool enables the definition of data workflows while also facilitating parameterization. Additionally, it offers tools like history and logging for monitoring and managing the workflows.

The primary focus will be on Apache Airflow, which is a popular tool for orchestrating data workflows. However, it's worth noting that there are other workflow orchestration tools available, such as Luigi, Prefect, Argo, and more, each with its own set of features and capabilities.

### Airflow Architecture

![AA1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Data%20Ingestion/images/1.png)

A typical `Apache Airflow` installation includes several key components that work together to facilitate workflow orchestration and automation. These components are:

- `Scheduler`: The scheduler is responsible for scheduling when and how often tasks (jobs) should be executed. It keeps track of all tasks and their schedules, ensuring that they are executed based on the defined timing and dependencies.

- `Work Queue`: `Airflow` uses a work queue to deliver tasks to the workers. It can use message brokers like `RabbitMQ, Apache Kafka`, or `Celery` as the underlying queuing mechanism.

- `Metadata Database`: A relational database (typically PostgreSQL, MySQL, or SQLite) is used to store metadata and the state of all tasks, job definitions, and historical job runs. This database is essential for maintaining the system's state and history.

- `Web Interface (UI)`: The web-based UI provides an interactive platform for users to monitor and manage workflows. It allows users to view DAGs (Directed Acyclic Graphs), visualize task dependencies, and trigger or pause job runs.

- `Executor`: The executor is responsible for running the tasks on worker nodes. Airflow supports various executors, such as the SequentialExecutor, LocalExecutor, CeleryExecutor, and more, each suited to different use cases and scalability requirements.

- `Workers`: Workers are responsible for executing the actual tasks. They pull tasks from the work queue and run them as specified in the DAG. Airflow supports distributed task execution across multiple worker nodes.

- `DAGs` (Directed Acyclic Graphs): DAGs define the structure of the workflow, specifying the order of tasks and their dependencies. Airflow uses Python scripts to create DAGs, making it easy to define complex workflows with code.

- Additional components (not shown in the diagram)

  - `Redis`: Redis is a popular message broker that is used to forward messages from the scheduler to worker nodes. It acts as an intermediary for task distribution, helping ensure tasks are executed as scheduled. Redis is commonly used as the message queue for Apache Airflow.

  - `Flower`: Flower is a web-based monitoring tool for Apache Celery, which is often used as the task execution component in Airflow. Flower provides a real-time dashboard that allows users to monitor the health and status of worker nodes, track the progress of task execution, and view worker statistics. By default, it is accessible at port 5555.

  - `airflow-init`: The airflow-init component is an initialization service for Apache Airflow. It can be customized to suit specific installation and configuration needs. This service is essential for setting up the Airflow environment according to the organization's requirements and preferences.

  - `Plugins`: Airflow allows for custom operators, sensors, and hooks to be added as plugins. These plugins extend Airflow's functionality and make it easier to integrate with various data sources and systems.

  - Configuration Files: Airflow's configuration files specify settings such as the database connection, authentication, and other system-wide settings. Configuration can be fine-tuned to meet specific requirements.

  - Authentication and Security: Airflow provides options for user authentication and authorization, ensuring that only authorized users can access and modify workflows and data.

These components collectively enable the automation and management of data workflows, making Apache Airflow a powerful tool for orchestrating complex data processing tasks.

## Setting up Airflow with Docker

- Create a new `airflow` subdirectory in your work directory.

- Download the official Docker-compose YAML file for the latest Airflow version.

  `curl -LfO` <https://airflow.apache.org/docs/apache-airflow/2.2.3/docker-compose.yaml>

- The Airflow user

  - for MacOS, create a new `.env` in the same direcotry as the `docker-compose.yaml` file with the follwing content:

    `AIRFLOW_UID=50000`

  - if *nix systems used, generate an `.env` file with the appropriate UID using the follwing command:

    - `echo -e "AIRFLOW_UID"=$(id -u) > .env`

  - for Windows:

    - `echo AIRFLOW_UID=%USERPROFILE%`

- A file named `google_credentials.json` needs to be stored in `$HOME/.google/credentials/` directory.

- Use `docker-compose` version `v2.x+` for compatibility

### A complete setup

- Create a New Directory:

  Create a new subdirectory for your Airflow project in your working directory. You can do this with the following command in your terminal:

  ```bash
  mkdir airflow
  cd airflow
  ```

- Download Docker-Compose YAML File:

  Download the official Docker-Compose YAML file for the latest Airflow version using curl. The following command will download the file into your "airflow" directory:

  `curl -LfO 'https://airflow.apache.org/docs/apache-airflow/2.2.3/docker-compose.yaml'

- Set Up Airflow User:

  Depending on your operating system, you'll set up the Airflow user differently:

  - For macOS:

    Create a new `.env` file in the same directory as the `docker-compose.yaml` file with the following content:

    `AIRFLOW_UID=50000`

  - For other operating systems (Linux/Windows):
  
    You can generate the .env file with the appropriate user ID (UID) using the following command:

    `echo -e "AIRFLOW_UID=$(id -u)" > .env`

    This command will set the AIRFLOW_UID to the user ID that matches your current system user.

- We need to customize the base Airflow Docker image, or you may download a GCP-ready Airflow Dockerfile from this [link](https://github.com/ziritrion/dataeng-zoomcamp/blob/main/2_data_ingestion/airflow/Dockerfile).

  - We use the base `Apache Airflow` image as the base.
  - We install the `GCP SDK CLI` tool so that Airflow can communicate with our `GCP` project.
  - We also need to provide a requirements.txt file to install Python dependencies. The dependencies are:
    - `apache-airflow-providers-google` so that Airflow can use the `GCP SDK`.
    - `pyarrow` , a library to work with parquet files.

- To modify the x-airflow-common service definition inside the docker-compose.yaml file as per your requirements, you can follow these steps:

  - Comment or delete the image field and add the build section with the appropriate configuration:

    ```docker
    x-airflow-common:
    # Comment or delete the 'image' field
    # image: apache/airflow:2.2.0
    build:
      context: .
      dockerfile: ./Dockerfile
    ```

  - Add a volume to point to the folder where you stored the credentials JSON file:

    ```docker
      volumes:
        - ~/.google/credentials/:/.google/credentials:ro
    ```

  - Add two new environment variables, `GOOGLE_APPLICATION_CREDENTIALS` and `AIRFLOW_CONN_GOOGLE_CLOUD_DEFAULT`, as follows:

    ```bash
    - GOOGLE_APPLICATION_CREDENTIALS: /.google/credentials/google_credentials.json
    - AIRFLOW_CONN_GOOGLE_CLOUD_DEFAULT: 'google-cloud-platform://?extra__google_cloud_platform__key_path=/.google/credentials/google_credentials.json'
    ```

  - Add two new environment variables for your GCP project ID and GCP bucket:

    ```bash
    - GCP_PROJECT_ID: '<your_gcp_project_id>'
    - GCP_GCS_BUCKET: '<your_bucket_id>'
    ```

  - Change the `AIRFLOW__CORE__LOAD_EXAMPLES` value to `false` to prevent `Airflow` from populating its interface with `DAG` examples:

    ```bash
    - AIRFLOW__CORE__LOAD_EXAMPLES: 'false'
    ```
