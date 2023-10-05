# Data Ingestion README

## convert jupyter notebook to a python script

```bash
jupyter nbconvert --to=script upload-data.ipynb
```

which in turn produces `upload-data.py`

- clean out the script by removing comments, reorder imports and save as ingest_data.py

- A file named `google_credentials.json` needs to be stored in `$GOME/.google/credentials/` directory.
- Use `docker-compose` version `v2.x+` for compatibility

## Setting up Airflow with Docker

- Create a new `airflow` subdirectory in your work directory.

- Download the official Docker-compose YAML file for the latest Airflow version.

  `curl -LfO` <https://airflow.apache.org/docs/apache-airflow/2.2.3/docker-compose.yaml>

- The Airflow user

  - for MacOS, create a new `.env` in the same direcotry as the `docker-compose.yaml` file with the follwing content:

    `AIRFLOW_UID=50000`

