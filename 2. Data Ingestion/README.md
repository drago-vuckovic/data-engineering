# Ingestion Script

## convert jupyter notebook to a python script

```bash
jupyter nbconvert --to=script upload-data.ipynb
```

which in turn produces `upload-data.py`

- clean out the script by removing comments, reorder imports and save as ingest_data.py

- 