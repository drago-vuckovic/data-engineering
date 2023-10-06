# Spark

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

![Spark1](https://github.com/yourusername/yourrepository/raw/main/path/to/your/image.png)

![Spark2](https://github.com/yourusername/yourrepository/raw/main/path/to/your/image.png)

- You may generate a Python script using:

  - `jupyter nbconvert --to script BatchProcessing.ipynb`
