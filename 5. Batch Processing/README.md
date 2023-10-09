# Apache Spark

- It is an open-source multi-language unified analytics engine for large-scale data processing.

- It is an engine because it processes data.

- `Spark` can operate in cluster environments comprising multiple nodes, where each node is responsible for fetching and transforming data.

- `Spark` is a multi-language platform as it allows native usage of Java and Scala, and provides wrappers for Python, R, and various other programming languages.

- The Python wrapper for Spark is `PySpark`.

- Spark is capable of handling both batch and streaming data. When dealing with streaming data, it treats the data stream as a series of small batches, applying similar processing techniques as those used for regular batches.

- Spark finds its application in data transformation within a Data Lake environment.

- While tools like `Hive`, `Presto`, and `Athena` (an AWS-managed Presto service) enable the expression of jobs through SQL queries, there are situations demanding intricate operations that are challenging, if not impossible, to articulate solely using SQL. In such cases, Spark emerges as the preferred tool, particularly for tasks like machine learning model implementation.

## Native install in Linux

- Having Java installed is a prerequisite

```bash
wget <https://dlcdn.apache.org/spark/spark-3.5.0/spark-3.5.0-bin-hadoop3.tgz>
tar xzfv spark-3.5.0-bin-hadoop3.tgz
rm spark-3.5.0-bin-hadoop3.tgz
sudo mv spark-3.5.0-bin-hadoop3 /opt/spark
nano ~/.zshrc
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin
```

- Test the installation

  ```bash
  val data = 1 to 10000
  val distData = sc.parallelize(data)
  distData.filter(_ < 10).collect()
  ```

  - the output should be:

    ```bash
    res4: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
    ```

- Setting up PySpark

  ```bash
  export PYTHONPATH="${SPARK_HOME}/python/:$PYTHONPATH"
  export PYTHONPATH="${SPARK_HOME}/python/lib/py4j-0.10.9.7-src.zip:$PYTHONPATH"
  ```

## Installing using `conda`

- create an environment file (e.g. `env_batch.yml`)

- create a Conda environment

  ```bash
  conda env create -f env_batch.yml
  ```
  
  `env_batch.yml`

    ```bash
        name: batch_env
        channels:
        - defaults
        dependencies:
        - python=3.9
        - jupyter
        - pyspark
     ```
  
  ```bash
  conda activate batch_env
  ```

## Using `jupyter notebook` with `Spark`

```bash
import pyspark

pyspark.__version__
pyspark.__file__

from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .master("local[*]") \
    .appName('test') \
    .getOrCreate()

!wget https://s3.amazonaws.com/nyc-tlc/misc/taxi+_zone_lookup.csv

df = spark.read \
    .option("header", "true") \
    .csv('taxi+_zone_lookup.csv')

df.show()

df.write.parquet('zones')

!ls zones/
```

- `SparkSession` is the class of the object that we instantiate.

  - `builder` is the builder method

- `master()` sets the Spark master URL to connect to

  - The `local` string means that Spark will run on a local cluster

  - `[*]` means that Spark will run with as many CPU cores as possible

- `appName()` defines the name of our application/session. This will show in the Spark UI

- `getOrCreate()` will create the session or recover the object if it was previously created

- Similarlly to `pandas`, `Spark` can read `CSV` files into dataframes (a tabular data structure). 

- Unlike `pandas`, `Spark` can handle much bigger datasets but it’s unable to infer the datatypes of each column.

![Spark1](https://github.com/drago-vuckovic/data-engineering/blob/main/5.%20Batch%20Processing/images/1.png)

![Spark2](https://github.com/drago-vuckovic/data-engineering/blob/main/5.%20Batch%20Processing/images/2.png)

- You may also generate a Python script using:

  - `jupyter nbconvert --to script BatchProcessing.ipynb`

## Partitioning and Data Parallelism in Spark

In a Spark cluster, comprising multiple executors, data processing is a pivotal aspect for achieving parallelism and optimizing workloads. Each executor has the capacity to independently process data, contributing to the parallelization of tasks and thereby enhancing efficiency.

In our previous example, we examined the processing of a single large CSV file. However, it's important to note that a file can be read by only a single executor at a time. Consequently, the code executed in the previous scenario is not parallelized, resulting in single-executor processing rather than leveraging the full potential of the cluster.

To address this limitation, we introduce the concept of partitioning. By partitioning a file, we divide it into multiple segments or parts. Each executor can then process a distinct partition concurrently, allowing all executors to work in unison. These segmented portions are referred to as partitions.

In the upcoming demonstration, we will read a CSV file, partition the resulting DataFrame, and convert it into Parquet format. This transformation will generate multiple Parquet files, improving parallelism.

Note: Converting to Parquet is a resource-intensive operation, which may require several minutes to complete.

```python
# Create 24 partitions in our DataFrame
df = df.repartition(24)
# Convert to Parquet and write to 'fhvhv/2021/01/' folder
df.write.parquet('fhvhv/2021/01/')
```

You have the flexibility to monitor the progress of your Spark job using the Spark UI at any time. Jobs are divided into stages, each comprising multiple tasks. Tasks within a stage will not commence until all tasks from the previous stage have been successfully completed.

Upon creating a DataFrame, Spark, by default, generates as many partitions as there are available CPU cores. Each partition corresponds to a task. Therefore, assuming the initial DataFrame was partitioned into 6 segments, the write.parquet() operation will entail 2 stages: the first with 6 tasks and the second with 24 tasks.

In addition to the 24 Parquet files, you will also encounter an empty _SUCCESS file upon successful job completion. This file serves as an indicator of job success.

It's worth noting that any attempts to rewrite files to the same location will result in an error since Spark does not write to non-empty folders. However, you can forcefully overwrite the existing files by specifying the mode argument:

```python
df.write.parquet('fhvhv/2021/01/', mode='overwrite')
```

In Spark, the opposite of partitioning, which involves combining multiple partitions into a single partition, is known as "coalescing."

## Working with Spark DataFrames

In the realm of Spark, data manipulation is predominantly accomplished through the use of DataFrames.

In our previous discussion, we generated Parquet files. These files can be seamlessly converted into a DataFrame as follows:

```python
df = spark.read.parquet('fhvhv/2021/01/')
```

Unlike CSV files, Parquet files inherently contain the schema of the dataset, obviating the need for explicit schema specification during data retrieval. To inspect the schema, you can employ the following command:

```python
df.printSchema()
```

An intriguing facet of Parquet files is their compactness compared to CSV counterparts. This efficiency is attributed to Parquet's data storage methodology, where data types are considered, and storage space is optimized accordingly. Consequently, integer values occupy less space than long or string values, contributing to a reduction in file size.

Spark DataFrames support a multitude of operations reminiscent of those available in the Pandas library:

Column Selection: You can selectively choose columns of interest, yielding a DataFrame containing only the specified columns.

```python
new_df = df.select('pickup_datetime', 'dropoff_datetime', 'PULocationID', 'DOLocationID')
```

Filtering by Value: Filtering allows you to obtain a DataFrame containing records that satisfy specific conditions.

```python
new_df = df.select('pickup_datetime', 'dropoff_datetime', 'PULocationID', 'DOLocationID').filter(df.hvfhs_license_num == 'HV0003')
```

These represent merely a subset of the operations that can be performed on Spark DataFrames. The official Spark documentation website offers an extensive guide on DataFrame operations, providing valuable insights and guidance for effective data manipulation.

In summary, Spark DataFrames serve as a versatile tool for data analysis, supporting a wide array of operations while efficiently handling data schemas, making them a pivotal component in Spark-based data processing workflows.

## Actions vs Transformations in Spark

In the context of Spark, we encounter two distinct categories of operations: "Actions" and "Transformations."

Certain Spark methods are categorized as "lazy," signifying that they do not execute immediately upon invocation. An illustrative example can be found in the final instructions of our previous section: executing these instructions does not yield any new jobs in the Spark UI. However, when we subsequently invoke df.show(), the execution occurs instantly, revealing the DataFrame's contents, and the Spark UI reflects the initiation of a new job.

These "lazy" commands fall under the category of transformations, while the more prompt and immediate commands are classified as actions. The core principle here is that actual computations are triggered solely when actions are explicitly invoked.

For instance, consider the following chain of operations:

```python
df.select(...).filter(...).show()
```

Within this sequence, both select() and filter() are transformations—commands that do not trigger computations at the moment of invocation. The entire instruction chain is evaluated and executed only upon invoking the show() action.

To provide further clarity, here is a brief breakdown of operations:

- List of Transformations (Lazy):

  - Selecting columns
  - Filtering data
  - Joining datasets
  - Grouping data
  - Partitioning data
  - And more...

- List of Actions (Eager):

  - Displaying data (`show(), take(), head()`)
  - Writing and reading data
  - And others...

Spark's distinction between actions and transformations is pivotal for optimizing the execution of computations. Transformations, being lazy, help build a computational plan, while actions act as the catalyst, prompting the actual execution of computations, resulting in efficient and controlled data processing workflows.

## Built-In Functions and User Defined Functions (UDFs) in Spark

In addition to the SQL and Pandas-like commands we've explored thus far, Spark offers a suite of built-in functions that facilitate more intricate data manipulation tasks. As a convention, these functions are imported as follows:

```python
from pyspark.sql import functions as F
```

Here's an example illustrating the usage of built-in functions:

```python
df \
    .withColumn('pickup_date', F.to_date(df.pickup_datetime)) \
    .withColumn('dropoff_date', F.to_date(df.dropoff_datetime)) \
    .select('pickup_date', 'dropoff_date', 'PULocationID', 'DOLocationID') \
    .show()
```

- `withColumn()` is a transformation that appends a new column to the DataFrame.
Important: Adding a new column with the same name as an existing column will overwrite the existing column.

- `select()` is another transformation that selects specific columns.

- `F.to_date()` is a built-in Spark function designed to convert a timestamp to a date format (year, month, and day, without the hour and minute).

A comprehensive list of built-in functions can be found in the official Spark documentation.

Beyond these built-in functions, Spark empowers users to create User Defined Functions (UDFs) to cater to specific, custom data manipulation requirements. This is particularly valuable when formulating complex behaviors that are challenging to express through SQL queries, or when maintaining and testing SQL queries becomes unwieldy.

UDFs are essentially regular functions that are subsequently passed as parameters to a specialized builder. Let's demonstrate the creation of a UDF:

```python
# An example of a UDF that modifies values based on divisibility by 7 or 3
def crazy_stuff(base_num):
    num = int(base_num[1:])
    if num % 7 == 0:
        return f's/{num:03x}'
    elif num % 3 == 0:
        return f'a/{num:03x}'
    else:
        return f'e/{num:03x}'

# Creating the actual UDF
crazy_stuff_udf = F.udf(crazy_stuff, returnType=types.StringType())
```

In this example, F.udf() takes a function (crazy_stuff() in this case) as a parameter and specifies the return type for the function (in our example, it's a string).

While the crazy_stuff() function in this example may appear nonsensical, UDFs prove invaluable for tasks such as machine learning and other intricate operations that aren't well-suited for SQL. Python code is also more amenable to testing compared to SQL.

Subsequently, UDFs can be employed in transformations, much like their built-in counterparts:

```python
df \
    .withColumn('pickup_date', F.to_date(df.pickup_datetime)) \
    .withColumn('dropoff_date', F.to_date(df.dropoff_datetime)) \
    .withColumn('base_id', crazy_stuff_udf(df.dispatching_base_num)) \
    .select('base_id', 'pickup_date', 'dropoff_date', 'PULocationID', 'DOLocationID') \
    .show()
```

Spark offers a wealth of built-in functions and the flexibility to create UDFs to address complex data manipulation tasks. This versatility empowers data engineers and data scientists to efficiently handle diverse data processing needs within Spark.

