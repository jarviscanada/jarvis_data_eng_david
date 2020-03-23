# Hadoop Project

## Table of Contents
* [Introduction](#introduction)
* [Hadoop Cluster](#hadoop-cluster)
* [Hive Project](#hive-project)
* [Improvements](#improvements)

## Introduction
Purpose of this project is to learn and understand how to use Hadoop and it's components to process Big Data, of which the core components to 
learn would be the MapReduce paradigm, Hadoop Distributed File System (HDFS) and Yet Another Resource Negotiator (YARN).

For this project I have learned about how HDFS, YARN and MapReduce work together to launch Hadoop tasks. In addition to this, I have done a 
Hadoop project that includes provisioning a Hadoop Cluster via Google Cloud Platform (GCP). In addition to this, I have exported data from 
Google Public Data (hosted on Google BigQuery) located: `https://console.cloud.google.com/bigquery?project=dataeng-bootcmap&organizationId=15565582353&p=patents-public-data&d=worldbank_wdi&t=wdi_2016&page=table`
, and moved local Google Storage where I can read into a table stored in my Hadoop cluster I have set up.

For my project, I use Apache Zeppelin to execute HiveQL queries that are sent to Tez, which acts as an Execution Engine and performs the 
task analogous to MapReduce. For the project, I tested caching of the worker nodes, compared Tez distributed jobs compared to the equivalent 
Bash job on the Master Node, learned to use openSerde for better parsing, tested increased efficiency of Hive partitions, tested increased 
efficiency of using Columnar File Optimization and tested the performance improvement by executing queries via spark compared to Tez.

## Hadoop Cluster
![diagram](./assets/diagram.svg)
### Hardware Requirements
1 Master Node with 12 GB memory, and machine type 2vCPUs
	- Primary disk size is 100GB, and is standard persistent disk

2 Worker nodes with 12 GB memory, and machine type 2vCPUs	
	- Primary disk size for both are 100GB, and are standard persistent disk type

### Tools Needed

#### Hadoop
Hadoop is an open source software platform for distributed storage and 
processing of very large datasets via clusters built from commodity 
hardware.

Hadoop is composed of HDFS, YARN and MapReduce/Tez, and relies on 
these 3 tools to run tasks.

#### Hadoop Distributed File System (HDFS)
HDFS is a distributed file system that handles large data running on 
commodity hardware and makes all hardrives in the Hadoop cluster appear 
like a giant data storage system. This cluster can be scaled by adding 
more commodity hardware into the system thus improving performance.

HDFS is composed of a Master (NameNode) that interacts with multiple 
slaves (DataNodes). The NameNode represents files/directories stored 
in HDFS via metadata which includes information such as permissions, 
modification & access times, namespace & disk space quotas. It also 
handles sharding of data blocks to be stored in the DataNodes, of which 
replicas of each block needs to be shared among DataNodes with a minimum 
of at least 3 copies. Actual data of all files and directories will be 
stored in DataNodes. 

#### Yet Another Resourcae Negotiator (YARN)
YARN sits on top of HDFS and manages all resources on the Hadoop cluster.
Once an application requests the YARN Resource Manager (RM) to run a task, 
and distribute the work to the Hadoop cluster, YARN distributes the job among 
the DataNodes in a way to make best use of data locality, for example setting a 
job that the process will work on the same node that a replica of the data block 
is stored in. Also YARN handles the scheduling of jobs that are submitted to it, of 
which it is possible to have a FIFO/Capacity/Fair type schedules. 
YARN uses MapReduce/Spark/Tez once a job is requested to execute the job.

In this project, once a request is made by Hive to execute a query to the YARN Resource 
Manager, the RM will pick available cluster resources to run the application. Tasks will 
be distributed out to DataNodes of which a Node Manager is located that will provide 
computational resources in container form and manage processes running in the container.

As Hive submits a request to the Resource Manager, YARN creates an Application Manager
whose task is to coordinate the exeuction of all tasks (on containers) within the application via monitoring, 
and restoring failed tasks.

#### MapReduce/Tez
MapReduce is a programming model that is suitable for processing and generating big data sets with parallel 
distributed algorithms on a Hadoop cluster. MapReduce tasks a generally composed of 3 parts:<br />
1) A Map function executed on each node where data is stored that will output <key,value> pairs from each node <br />
2) A Shuffle and Sort phase, which is done by the application by default to send all common keys to a single node
 to be Reduced<br />
3) A Reduce function that executes only on the nodes where the Shuffle and Sort phase has sent data to. The Reduce 
function will aggregate the set of <key,value> pairs on the node and will output a single combined list.<br />

In this project, HiveQL queries written will be translated into a MapReduce/Tez jobs. Because Tez is more efficient 
relative to MapReduce, all HiveQL queries have been executed via Tez jobs.

#### Hive
Apache Hive is a data warehouse system that sits on YARN, and was created to make interactions with Hadoop more similar to working with RDBMS. 
By integrating Hadoop cluster with a RDBMS like language, it makes it possible for Developers familiar with the SQL world 
able to quickly transition to work with writing distributed programs since Hive translates SQL queries to MapReduce/Tez jobs 
on cluster.

Unlike real RDBMS', Hive maintains a metastore that impacts a structure the user defines on the unstructured data that is stored on HDFS. This is 
called schema-on-read where the database applies the schema on read tasks, which takes the existing stored data and imparts an unstrict schema 
as the data is read.

The actual Hive data will be stored on HDFS. Via SerDe (serializer/deseralizer), Hive can read in data from a table, and write it back out to HDFS 
in any custom format according to the SerDe inputted by user (including unstructured data).

#### Zeppelin
Is a web-based notebook that allows the user to save and analyze queries. A paragraph in Zeppelin is used to describe a block of code that 
can be executed to Hadoop.


## Hive Project
For this project, we will be using Zeppelin to store and execute Hive queries, as seen below. Please read the image from bottom up. 
![hadoopCapture](./assets/hadoop_screenshot.JPG)

## Improvements
1) Explore how bucketing may improve query performance
2) Size of data worked on was 1.8GB zipped, (2.27GB unzipped) which is not much. May be better to work on larger datasets in future.
3) Add a component to further explore the differences the sorting and aggregating Hive functions:
	- ORDER BY<br />
	- DISTRIBUTED BY<br />
	- SORT BY<br />
	- CLUSTER BY<br />
