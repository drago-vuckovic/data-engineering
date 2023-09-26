#!/usr/bin/env python
# coding: utf-8

import argparse
from time import time
import pandas as pd
from sqlalchemy import create_engine


parser = argparse.ArgumentParser(description='Ingest CSV data to Postgres')

# user
# password
# host
# port
# database name
# table name
# url of the csv

parser.add_argument('user', help='user name for postgres')
parser.add_argument('pass', help='password for postgres')
parser.add_argument('host', help='host for postgres')
parser.add_argument('port', help='port for postgres')
parser.add_argument('db', help='database name for postgres')
parser.add_argument('table-name', help='table name where we will write the results to')
parser.add_argument('url', help='user of the csv file')

parser.add_argument('--sum', dest='accumulate', action='store_const', const=sum, 
                    default=max,
                    help='sum the integers (default: find the max)')

args = parser.parse_args()

print(args.accumulate(args.integers))

engine = create_engine('postgresql://root:root@localhost:5432/ny_taxi')
engine.connect()



df = pd.read_csv('yellow_tripdata_2021-01.csv', nrows=100)




print(pd.io.sql.get_schema(df, name= 'yellow_taxi_data'))



pd.to_datetime(df.tpep_pickup_datetime)



df.tpep_pickup_datetime = pd.to_datetime(df.tpep_pickup_datetime)
df.tpep_dropoff_datetime = pd.to_datetime(df.tpep_dropoff_datetime)



print(pd.io.sql.get_schema(df, name= 'yellow_taxi_data'))










print(pd.io.sql.get_schema(df, name= 'yellow_taxi_data', con=engine))



df_iter = pd.read_csv('yellow_tripdata_2021-01.csv', iterator=True, chunksize=100000)



df = next(df_iter)



df



len(df)



df.tpep_pickup_datetime = pd.to_datetime(df.tpep_pickup_datetime)
df.tpep_dropoff_datetime = pd.to_datetime(df.tpep_dropoff_datetime)



df.head(n=0).to_sql(name='yellow_taxi_data', con=engine, if_exists='replace')



get_ipython().run_line_magic('time', "df.to_sql(name='yellow_taxi_data', con=engine, if_exists='append')")







while True:
    t_start = time()
    df = next(df_iter)
    df.tpep_pickup_datetime = pd.to_datetime(df.tpep_pickup_datetime)
    df.tpep_dropoff_datetime = pd.to_datetime(df.tpep_dropoff_datetime)
    df.to_sql(name='yellow_taxi_data', con=engine, if_exists='append')
    t_end = time()
    print ('inserted anouther chunk, took %.3f seconds' % (t_end - t_start))
    