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
  
  - run posgresql container from the following docker command

  ```bash
      sudo docker run -it \
      -e POSTGRES_USER=root \
      -e POSTGRES_PASSWORD=root \
      -e POSTGRES_DB=ny_taxi \
      -v "$(pwd)"/ny_taxi_postgres_data:/var/lib/postgresql/data \
      -p 5432:5432 \
      postgres:13
  ```

  - run pgadmin

    ```bash
    docker run -it \
      -e PGADMIN_DEFAULT_EMAIL="admin@admin.com" \
      -e PGADMIN_DEFAULT_PASSWORD="root" \
      -p 8080:80 \
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

        ```bash
        docker network create pg-network
        ```

        

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

