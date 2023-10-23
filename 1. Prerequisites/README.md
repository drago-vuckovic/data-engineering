# data-engineering

## resources

- <https://github.com/DataTalksClub/data-engineering-zoomcamp>

- <https://dezoomcamp.streamlit.app/Week_1_Introduction_&_Prerequisites>

- <https://github.com/DataTalksClub/data-engineering-zoomcamp/tree/main/week_1_basics_n_setup/2_docker_sql>

- <https://github.com/DataTalksClub/data-engineering-zoomcamp/blob/main/dataset.md>

- <https://www.nyc.gov/assets/tlc/downloads/pdf/data_dictionary_trip_records_yellow.pdf>

## steps

- install python and add to PATH

    `export PATH="$PATH:/home/drago/.local/bin"`

- Docker

  - create `pipeline.py` which would be used as a data pipeline

    ```python
    import sys
    import pandas as pd
    print(sys.argv)
    day = sys.argv[1]
    print('job finished successfully for day: ')
    ```

  - create a Dockerfile

    ```bash
    FROM python:3.9
    RUN pip install pandas
    WORKDIR /app
    COPY pipeline.py pipeline.py
    ENTRYPOINT [ "python", "pipeline.py" ]
    ```

  - build the docker image

    - `docker build -t test:pandas .` builds the docker image from the Dockerfile in the current directory

  - start the container from the image

    - `docker run -it test:pandas`
  
  - run posgresql container from the following docker commands

    - create a docker network

      ```bash
      docker network create pg-network
      ```

      ```bash
          sudo docker run -it \
          -e POSTGRES_USER=root \
          -e POSTGRES_PASSWORD=root \
          -e POSTGRES_DB=ny_taxi \
          -v "$(pwd)"/ny_taxi_postgres_data:/var/lib/postgresql/data \
          -p 5432:5432 \
          --network=pg-network \
          --name pg-database \
          postgres:13
      ```

  - run pgadmin

    ```bash
    docker run -it \
      -e PGADMIN_DEFAULT_EMAIL="admin@admin.com" \
      -e PGADMIN_DEFAULT_PASSWORD="root" \
      -p 8080:80 \
      --network=pg-network \
      --name pgadmin-2 \
      dpage/pgadmin4
    ```

    - go to the localhost:8080/ and type in the email and password as in the docker command:

      - email: <admin@admin.com>

      - password: root

      - create a server group and a server connection

        - in the connection tab at the host name/address type in: `localhost`

        - username: `root`

        - password: `root`

      - create a docker network

- install pgcli

  - `pip install pgcli`

  - run pgcli

      `pgcli -h localhost -p 5432 -u root -d ny_taxi`

    - password same as in the docker command (root)

    - test the connection to the database

      - `select 1`

    - list the tables

      - `\dt`

      - there should be the same as in the docker command - 'yellow_taxi_data'

      - show the columns types

        - `\d yellow_taxi_data;`

      - show the imported data

          `SELECT COUNT(1) FROM yellow_taxi_data;`

- run jupyter

  - install the prerequisits

    - `pip install psycopg2-binary`

    - `pip install jupyter`

  - create a new notebook a rename it to `upload-data`
  
  ```python
  import pandas as pd
  pd.__version__
  ```

- downdload the compressed data:

  - `wget https://github.com/DataTalksClub/nyc-tlc-data/releases/download/yellow/yellow_tripdata_2021-01.csv.gz`

    - uncompress the data

    - display the data

      - `less yellow_tripdata_2021-01.csv`

      - `head -n 100 yellow_tripdata_2021-01.csv`

    - copy (n) rows to a new file

      - `head -n 100 yellow_tripdata_2021-01.csv > yellow_head.csv`

    - count the lines in the file:

      - `wc -l yellow_tripdata_2021-01.csv`

  ```python
  df = pd.read_csv('yellow_tripdata_2021-01.csv', nrows=100)
  ```

  - generate a schema for postgresql

    - `print(pd.io.sql.get_schema(df, name= 'yellow_taxi_data'))`

    - change the data type for TEXT columns to be timestamp

      - `pd.to_datetime(df.tpep_pickup_datetime)`

    - apply to the data frame (df)

      ```python
      df.tpep_pickup_datetime = pd.to_datetime(df.tpep_pickup_datetime)
      df.tpep_dropoff_datetime = pd.to_datetime(df.tpep_dropoff_datetime)
      ```

  - generate DDL statements for postgresql

    - install prerequisites

      ```python
      pip install sqlalchemy
      ```

    ```python
    from sqlalchemy import create_engine
    engine = create_engine('postgresql://root:root@localhost:5432/ny_taxi')
    engine.connect()
    print(pd.io.sql.get_schema(df, name= 'yellow_taxi_data', con=engine))
    ```

    ```python
    df_iter = pd.read_csv('yellow_tripdata_2021-01.csv', iterator=True, chunksize=100000)
    df = next(df_iter)
    len(df)
    df.head(n=0)
    ```
  
  - list the tables from the database

    - at pgcli type

    ```pgcli
    \dt
    \d yellow_taxi_data;
    ```

  - continue with the jupyter script

    ```python
    df.tpep_pickup_datetime = pd.to_datetime(df.tpep_pickup_datetime)
    df.tpep_dropoff_datetime = pd.to_datetime(df.tpep_dropoff_datetime)
    df.head(n=0).to_sql(name='yellow_taxi_data', con=engine, if_exists='replace')
    %time df.to_sql(name='yellow_taxi_data', con=engine, if_exists='append')
    from time import time
    while True:
    t_start = time()
    df = next(df_iter)
    df.tpep_pickup_datetime = pd.to_datetime(df.tpep_pickup_datetime)
    df.tpep_dropoff_datetime = pd.to_datetime(df.tpep_dropoff_datetime)
    df.to_sql(name='yellow_taxi_data', con=engine, if_exists='append')
    t_end = time()
    print ('inserted anouther chunk, took %.3f seconds' % (t_end - t_start))

    ```

## Generate the SSH Key Pair

- To generate a new SSH key pair (public and private key), use the following command:

```bash
ssh-keygen -t rsa -b 4096 -C "drago@vuckovic.co"
```

  - This email address is used as a label to help identify the key later if needed.

`-t rsa`: Specifies the key type as RSA.

`-b 4096`: Sets the key length to 4096 bits (recommended for increased security).

`-C "drago@vuckovic.co"`: Provides a comment to help identify the key (you can use any comment you like).

Choose a Location: The ssh-keygen command will prompt you to choose a location to save the keys. By default, it saves the keys in your home directory under ~/.ssh/id_rsa (private key) and ~/.ssh/id_rsa.pub (public key). You can press Enter to accept the default location or specify a different one if you prefer.

Choose a Passphrase (Optional): You can choose to secure your private key with a passphrase. This adds an extra layer of security but requires you to enter the passphrase each time you use the key. If you want to set a passphrase, enter it when prompted.

Key Generation: The ssh-keygen command will generate your SSH key pair. It will display a message indicating that your keys have been created.

View Your Public Key: You can view the content of your public key by using a command like cat. For example:

```bash
cat ~/.ssh/id_rsa.pub
```

This will display the public key in the terminal. You can copy and paste this public key when needed to authorize access to your SSH server or services.

## Google Cloud Platform (GCP)

- Includes a range of hosted services for compute, storage and application development that run on Google hardware

### Initial Setup

- Create an account with a Google email ID
- Setup a [project](https://console.cloud.google.com/) and note down the "Project ID"
- Setup [service account and authentication](https://cloud.google.com/docs/authentication/getting-started) for the project
  - grant `Viewer` role to begin with
  - download service account-keys (.json) for the auth
  - download [SDK](https://cloud.google.com/sdk/docs/quickstart) for local setup
  - set environment variable to point to your downloaded GCP keys:

    ```bash
    export GOOGLE_APPLICATION_CREDENTIALS="<path/to/your/service-account-authkeys>.json"

    # Refresh token/session, and verify authentication
    gcloud auth application-default login
    ```

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/01.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/02.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/03.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/04.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/05.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/06.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/07.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/08.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/09.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/10.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/11.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/12.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/13.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/14.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/15.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/16.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/17.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/18.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/19.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/20.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/21.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/22.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/23.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/24.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/25.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/26.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/27.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/28.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/29.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/30.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/31.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/32.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/33.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/34.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/35.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/36.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/37.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/38.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/39.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/40.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/41.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/42.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/43.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/44.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/45.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/46.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/47.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/48.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/49.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/50.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/51.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/52.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/53.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/54.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/55.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/56.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/57.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/58.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/59.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/60.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/61.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/62.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/63.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/64.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/65.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/66.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/67.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/68.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/69.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/70.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/71.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/72.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/73.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/74.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/75.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/76.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/77.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/78.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/79.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/80.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/81.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/82.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/83.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/84.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/85.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/86.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/87.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/88.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/89.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/90.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/91.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/92.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/93.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/94.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/95.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/96.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/97.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/98.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/98.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/100.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/101.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/102.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/103.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/104.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/105.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/106.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/107.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/108.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/109.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/110.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/111.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/112.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/113.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/114.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/115.png)

![GCP1](https://github.com/drago-vuckovic/data-engineering/blob/main/1.%20Prerequisites/images/116.png)


### Setup for Access

1. IAM Roles for Service account:

    - At the IAM section of IAM & Admin <https://console.cloud.google.com/iam-admin/iam>

    - Click the Edit principal icon for your service account

    - Add these roles in addition to `Viewer`:
      - Storage Admin + Storage Object Admin + BigQuery Admin

2. Enable these APIs for your project:

    https://console.cloud.google.com/apis/library/iam.googleapis.com
    https://console.cloud.google.com/apis/library/iamcredentials.googleapis.com

3. Please ensure `GOOGLE_APPLICATION_CREDENTIALS` `env-var` is set.

    `export GOOGLE_APPLICATION_CREDENTIALS="<path/to/your/service-account-authkeys>.json"`

### Upload the public key to GCP

To upload your SSH public key to Google Cloud Platform (GCP), you can add it to the metadata of your Google Cloud user account. 

- Generate Your SSH Key Pair: If you haven't already generated an SSH key pair, follow the instructions in the previous response to generate one. Make sure you have your SSH public key file (~/.ssh/id_rsa.pub) ready.

- Open the Google Cloud Console:

  - Visit the Google Cloud Console at https://console.cloud.google.com/ and sign in to your Google Cloud account.

  - Navigate to the Compute Engine SSH Keys Page:

  a. Click on the menu icon (☰) in the upper left corner to open the navigation menu.

  b. Under "Compute," select "Compute Engine."

  c. In the left sidebar, under "Metadata," select "SSH Keys."

  Add Your SSH Public Key:

  a. Click the "Edit" button at the top of the page.

  b. In the "SSH Keys" section, click the "Add item" button.

  c. In the "Key" field, paste the content of your SSH public key file (~/.ssh/id_rsa.pub).

  d. Optionally, provide a name or description for the key in the "Name" field (e.g., "My SSH Key").

  e. Click the "Save" button to add the SSH key.

- Confirm the Key Addition:

  - After adding the SSH key, you should see it listed in the "SSH Keys" section. Ensure that the key you added matches your public key.

Now, your SSH public key is associated with your Google Cloud account, and you can use it to authenticate when connecting to instances or services within GCP.

When you create a new Google Compute Engine (GCE) instance or use other GCP services that require SSH access, you can specify your SSH key as a metadata item, and it will be automatically added to the instance for authentication

## Create a VM in GCP

- Open Google Cloud Console:

  - Visit the Google Cloud Console at https://console.cloud.google.com/ and sign in to your Google Cloud account.

  - Select Your Project:

    If you have multiple projects, ensure that you have selected the project in which you want to create the VM. You can select the project from the project selector located at the top of the console.

  - Navigate to Compute Engine:

    In the Google Cloud Console, click on the menu icon (☰) in the upper left corner to open the navigation menu. Under "Compute," select "Compute Engine."

  - Create a VM Instance:

      a. On the Compute Engine page, click the "Create" button to create a new VM instance.

      b. In the "Create an instance" page, you'll need to provide the following information:

      Name: Enter a name for your VM instance.

      Region and Zone: Choose the region and zone where you want to create the VM. This determines the physical location of your VM.

      Machine Type: Select the desired machine type for your VM. This determines the CPU and memory configuration.

      Boot Disk: Choose an operating system for your VM by selecting an image. You can also customize the size and type of the boot disk.

      Firewall: You can specify firewall rules to allow or deny incoming traffic to your VM. You can leave the default settings or customize them as needed.

      Networking: Configure network settings for your VM, including the VPC (Virtual Private Cloud) network and subnet it will be associated with. You can also assign a static external IP address if required.

      Identity and API access: You can configure service account and access scopes if needed.

      SSH Keys: You can add your SSH public key to allow SSH access to the VM. This is optional, and you can manage SSH access using other methods, including the metadata SSH key option.

      c. After configuring the settings, review them to ensure they are correct.

      d. Click the "Create" button at the bottom of the page to create the VM instance.

  - Wait for VM Creation:

    Google Cloud will start provisioning your VM instance. The time it takes to create the VM may vary depending on the configuration and the selected image.

- Access Your VM:

Once the VM is created, you can access it via SSH using the Google Cloud Console's SSH button or by using an SSH client. If you added your SSH key during VM creation, you can use it to log in.

## SSH Into The VM

To SSH into a Virtual Machine (VM) in Google Cloud Platform (GCP), you can use the Google Cloud Console's built-in SSH feature or an SSH client on your local machine. Here are the steps for both methods:

- Method 1: Using Google Cloud Console SSH

  Open Google Cloud Console:

  - Visit the Google Cloud Console at https://console.cloud.google.com/ and sign in to your Google Cloud account.

  - Navigate to Compute Engine:

    In the Google Cloud Console, click on the menu icon (☰) in the upper left corner to open the navigation menu. Under "Compute," select "Compute Engine."

  - Locate Your VM:

    On the "VM instances" page, you will see a list of your VM instances. Find the VM you want to SSH into and locate the "SSH" button on the right-hand side of the VM entry.

  - Click the SSH Button:

    Click the "SSH" button next to your VM's name. Google Cloud Console will open an SSH terminal session in your browser, and you will be logged into your VM.

- Method 2: Using an SSH Client (Local)

  Open a Terminal on Your Local Machine:

  - You need an SSH client on your local machine to use this method. Most Linux and macOS systems have SSH pre-installed. For Windows, you can use tools like PuTTY or Windows Subsystem for Linux (WSL).

  - Determine the External IP Address of Your VM:

    You can find the external IP address of your VM in the Google Cloud Console under the "VM instances" page.

  - SSH into Your VM:

    Open a terminal on your local machine and use the following command to SSH into your VM, replacing <VM_EXTERNAL_IP> with your VM's external IP address:

    ```bash
    ssh username@<VM_EXTERNAL_IP>
    ```

    - username: The username you use to log in to the VM. This typically depends on the operating system image you selected when creating the VM. Common usernames include "ubuntu," "ec2-user," or "gce-user."
  <VM_EXTERNAL_IP>: Replace this with the actual external IP address of your VM.
  For example, if you are using the "ubuntu" user on a VM with IP address "123.45.67.89," the command would look like:

    ```  bash
      ssh ubuntu@123.45.67.89
    ```

  - Authenticate:

    If this is your first time connecting to the VM, you may be prompted to confirm the authenticity of the host by typing "yes." Afterward, you'll need to enter the password or key passphrase associated with the user account on the VM.

    Once you successfully SSH into your VM, you will have command-line access to the VM's shell, and you can perform tasks on the VM as needed.

## Install Terraform

- Update the package list to ensure you have the latest information about available packages:

```bash
sudo apt update
```

- Install the required packages for adding HashiCorp's GPG key and HTTPS transport method for APT:

```bash
sudo apt install -y gnupg software-properties-common curl
```

- Add the HashiCorp GPG key to your system:

```bash
curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
```

- Add the official HashiCorp repository to your APT sources:

```bash
echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
```

- Update your package list again:

```bash
sudo apt update
```

- Finally, install Terraform:

```bash
sudo apt install terraform
```

```bash
terraform -install-autocomplete
```