# Data Warehouse and BigQuery

In the realm of data science and data processing systems, there are two primary types: Online Analytical Processing (OLAP) and Online Transaction Processing (OLTP) systems.

A straightforward way to distinguish between these two systems is that OLTP systems are often referred to as "classic databases" and are designed for handling day-to-day transactional operations, while OLAP systems are tailored for advanced data analytics purposes.

| Aspect                  | OLTP                                                 | OLAP                                                         |
|-------------------------|------------------------------------------------------|--------------------------------------------------------------|
| **Purpose**             | Control and run essential business operations in real time | Plan, solve problems, support decisions, discover hidden insights |
| **Data updates**        | Short, fast updates initiated by the user          | Data periodically refreshed with scheduled, long-running batch jobs |
| **Database design**     | Normalized databases for efficiency                | Denormalized databases for analysis                          |
| **Space requirements**  | Generally small if historical data is archived     | Generally large due to aggregating large datasets            |
| **Backup and recovery** | Regular backups required to ensure business continuity and meet legal and governance requirements | Lost data can be reloaded from OLTP database as needed in lieu of regular backups |
| **Productivity**        | Increases productivity of end users                | Increases productivity of business managers, data analysts, and executives |
| **Data view**           | Lists day-to-day business transactions               | Multi-dimensional view of enterprise data                    |
| **User examples**       | Customer-facing personnel, clerks, online shoppers | Knowledge workers such as data analysts, business analysts, and executives |

A Data Warehouse (DW) is an Online Analytical Processing (OLAP) solution designed for reporting and data analysis. In contrast to Data Lakes, which typically follow the Extract, Load, Transform (ELT) model, Data Warehouses commonly employ the Extract, Transform, Load (ETL) model for data processing and storage.

A DW receives data from different data sources which is then processed in a staging area before being ingested to the actual warehouse (a database) and arranged as needed. DWs may then feed data to separate Data Marts; smaller database systems which end users may use for different purposes.

![DW1](https://github.com/drago-vuckovic/data-engineering/blob/main/3.%20Data%20Warehouse/images/01.jpeg)

BigQuery (BQ) is a Data Warehouse solution provided by Google Cloud Platform. Here are some key features and characteristics of BigQuery:

BQ is serverless, which means there are no servers to manage or database software to install. Google takes care of this management, and it's transparent to customers.

It is highly scalable and offers high availability, with Google managing the underlying software and infrastructure.

## BigQuery

BigQuery provides built-in features for Machine Learning, Geospatial Analysis, Business Intelligence, and more, making it a versatile data analytics tool.

BQ maximizes flexibility by separating data analysis and storage into different compute engines, allowing customers to budget accordingly and reduce costs.

Some alternative Data Warehouse solutions from other cloud providers include AWS Redshift (Amazon Web Services) and Azure Synapse Analytics (Microsoft Azure). These services offer similar capabilities for data analytics and storage, catering to different cloud platforms.

### Pricing

BigQuery pricing is structured with two main components: processing and storage. Here's an overview of the pricing details:

Storage: The cost of storage is fixed at US$0.02 per GB per month. You can check the current storage pricing on the provided link.

Data Processing:

On-demand Pricing (Default): It costs US$5 per TB per month, with the first TB of the month being free. You pay for the processing resources you use as you go.

Flat-rate Pricing: This model is based on the number of pre-requested slots (virtual CPUs). A minimum of 100 slots is required for flat-rate pricing, which costs US$2,000 per month. Queries consume slots, and if you run out of slots, additional queries must wait for others to finish to free up slots. On-demand pricing doesn't have this limitation. Flat-rate pricing is cost-effective when processing more than 400TB of data per month.

When running queries in BigQuery, the top-right corner of the window will display an approximation of the data size to be processed by the query. After the query has run, the actual amount of processed data will appear in the Query results panel in the lower half of the window. This information can be useful for quickly estimating the cost of the query.

It's important to review Google Cloud's official pricing documentation regularly, as pricing may change over time, and specific details may vary.

### External Tables

BigQuery supports external tables, allowing you to query data from various sources directly from BigQuery, even if the data itself is not stored in BigQuery. Here's how you can work with external tables in BigQuery:

- Creating an External Table:

    You can create an external table from a CSV or Parquet file stored in a Google Cloud Storage bucket using a query like this:

    ```sql
    CREATE OR REPLACE EXTERNAL TABLE `taxi-rides-ny.nytaxi.external_yellow_tripdata`
    OPTIONS (
    format = 'CSV',
    uris = ['gs://nyc-tl-data/trip data/yellow_tripdata_2019-*.csv', 'gs://nyc-tl-data/trip data/yellow_tripdata_2020-*.csv']
    );
    ```

    In this example, the query creates an external table based on two CSV files. BigQuery automatically determines the table schema and data types based on the file contents. However, please note that BigQuery cannot determine the processing costs associated with external tables.

- Importing an External Table into BigQuery: If you want to work with the data as a regular internal table within BigQuery, you can import the data. For instance, you can create a new internal table by copying the contents of the external table, like this:

    ```sql
    CREATE OR REPLACE TABLE taxi-rides-ny.nytaxi.yellow_tripdata_non_partitoned AS
    SELECT * FROM taxi-rides-ny.nytaxi.external_yellow_tripdata;
    ```

This query imports the data from the external table into a new internal table, allowing you to work with it more efficiently in BigQuery.

External tables are a useful feature in BigQuery, enabling you to analyze data from various sources without the need to move or duplicate the data into BigQuery's storage.

### Partitions

In BigQuery, tables can be partitioned into multiple smaller tables, which can significantly improve performance and reduce costs because BigQuery processes less data per query. Here's an overview of partitioning in BigQuery:

- Types of Partitioning:

  - Time-unit column:
  
    Tables can be partitioned based on a TIMESTAMP, DATE, or DATETIME column in the table. This is particularly useful when queries often filter data based on date.

  - Ingestion time:

    Tables can be partitioned based on the timestamp when BigQuery ingests the data. This can be handy for analyzing data based on the time it was ingested.

  - Integer range:

    Tables can be partitioned based on an integer column, which is useful when data has a natural integer-based grouping.

For time-unit and ingestion time columns, you can choose partition intervals like daily (default), hourly, monthly, or yearly. It's essential to note that BigQuery limits the number of partitions to 4000 per table. If you need more partitions, consider using clustering as well.

Example of Creating a Partitioned Table:

```sql
CREATE OR REPLACE TABLE taxi-rides-ny.nytaxi.yellow_tripdata_partitioned
PARTITION BY
  DATE(tpep_pickup_datetime) AS
SELECT * FROM taxi-rides-ny.nytaxi.external_yellow_tripdata;
```

This query creates a partitioned table based on the tpep_pickup_datetime column. BigQuery will identify partitioned tables with a specific icon, and you can see the partitioning field and its datatype in the Details tab of the table.

Querying a Partitioned Table:

- Querying a partitioned table is similar to querying a non-partitioned table. However, the amount of processed data can vary significantly. For instance, querying a non-partitioned table might process around 1.6GB of data, while querying a partitioned table could reduce it to approximately 106MB when the partitioning is used efficiently.

Checking Partition Statistics:

- You can check the number of rows in each partition of a partitioned table using a query like this:

    ```sql
    SELECT table_name, partition_id, total_rows
    FROM `nytaxi.INFORMATION_SCHEMA.PARTITIONS`
    WHERE table_name = 'yellow_tripdata_partitioned'
    ORDER BY total_rows DESC;
    ```

    This query helps you identify data imbalances and biases in your partitions and is useful for optimizing your partitioning strategy.

### Clustering

Clustering in BigQuery involves rearranging a table based on the values of its columns to order the data according to specific criteria. Clustering can be done based on one or multiple columns, up to a maximum of four. The order in which clustering columns are specified is crucial, as it determines the column priority in the clustering.

Clustering can significantly improve query performance and reduce costs, especially for large datasets. It is particularly beneficial for queries that involve filter clauses and aggregating data.

Important Notes:

- Clustering is most effective on larger datasets, and tables with less than 1GB in size may not show significant improvement. In fact, applying clustering and partitioning to very small tables could lead to increased costs due to additional metadata reads and maintenance.

- Clustering columns must be top-level, non-repeated columns, and specific datatypes are supported for clustering, including DATE, BOOL, GEOGRAPHY, INT64, NUMERIC, BIGNUMERIC, STRING, TIMESTAMP, and DATETIME.

Example of Creating a Partitioned and Clustered Table:

```sql
CREATE OR REPLACE TABLE taxi-rides-ny.nytaxi.yellow_tripdata_partitioned_clustered
PARTITION BY DATE(tpep_pickup_datetime)
CLUSTER BY VendorID AS
SELECT * FROM taxi-rides-ny.nytaxi.external_yellow_tripdata;
```

This query creates a table that is both partitioned by `tpep_pickup_datetime` and clustered by `VendorID`. The Details tab for the table will display the fields used for partitioning and clustering.

- Querying a Clustered Table:

    Querying a clustered table is similar to querying a non-clustered table. However, the amount of processed data can be significantly reduced. For example, a query to a non-clustered, partitioned table might process about 1.1GB of data, whereas the same query to a partitioned and clustered table could reduce the processing to around 865MB of data.

    Clustering can be a powerful optimization technique in BigQuery, especially when used in combination with partitioning, and it's particularly effective for improving the performance of specific types of queries.

### Partitioning vs Clustering

When working with BigQuery, you have the option to use partitioning and clustering, but there are important differences between these techniques to consider when deciding which one to use for your specific scenario:

- Clustering:

    Cost Benefit: The cost benefit of clustering is not known in advance. BigQuery cannot estimate the reduction in cost before running a query.

    Granularity: Clustering provides high granularity, meaning you can use multiple criteria to sort the table based on multiple columns.

    Flexibility: Clusters are "fixed in place." Once you define clustering, it remains constant.

    Query Benefits: Clustering benefits queries that commonly use filters or aggregation against multiple particular columns.

    Number of Clusters: There is no limit to the number of clusters, making it useful when dealing with columns or groups of columns with a large cardinality.

- Partitioning:

    Cost Benefit: The cost of partitioning is known upfront. BigQuery can estimate the amount of data to be processed before running a query.

    Granularity: Partitioning offers low granularity, as only a single column can be used to partition the table.

    Flexibility: Partitions can be added, deleted, modified, or even moved between storage options, offering more flexibility.

    Query Benefits: Partitioning is particularly beneficial when you filter or aggregate on a single column.

    Number of Partitions: Partitioning is limited to 4000 partitions and cannot be used in columns with larger cardinality.

When to Choose Clustering Over Partitioning:

Choose clustering when partitioning would result in a small amount of data per partition.
Opt for clustering when partitioning would result in over 4000 partitions.
Consider clustering if your mutation operations frequently modify the majority of partitions in the table, such as writing to the table every few minutes and writing to most of the partitions each time.

- Automatic Reclustering:

    BigQuery includes automatic reclustering. When new data is written to a table, it can be written to blocks that overlap with the key ranges in previously written blocks, potentially weakening the sort property of the table. BigQuery will perform automatic reclustering in the background to restore the sort properties of the table.

- For Partitioned Tables:

    Clustering is maintained for data within the scope of each partition.

These differences should guide your decision when considering whether to use partitioning, clustering, or a combination of both for your specific use case in BigQuery.