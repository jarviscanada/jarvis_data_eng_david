# Spark/Scala Project

## Introduction
The purpose of this project is to learn to use big data platform Apache 
Spark to process data. The focus of this project is aimed towards 
evaluating Spark RDD and Spark Structured API, and is covered by solving 
business problems covering these topics via Zeppelin Notebook. From this 
project I have learned the difference between the 2 tools and how to 
apply them to solve problems.

## Spark Architecture
### Architecture Components 
**Browser/Web UI:** Tool used to access Zeppelin Notebook to access RDD/DataFrame code/SQL</ br>
**Zeppelin NoteBook:** Provides a Scala/shell environment to execute code</ br>
**Cluster Manager**: Is used to maintain the cluster of machines that will run 
Spark Applications. Is split between Cluster Manager Driver in Master 
Node (Same as YARN RM) and Cluster Worker Processes that exist in the Worker Nodes. 
The Cluster Manager Driver prompts worker nodes for resources available, and the 
Cluster Worker Processes here will respond with resource information.</ br>
**Spark Driver:** Controls execution of Spark Applications and maintains the 
state and tasks of the Executors. Works with Cluster Manager to get information about 
physical resources, and then uses this to launch Executors.</ br>
SparkSession is entry to underlying Spark functionality and allows programming Spark with DataFrame/SQL, and 
is a tool to perform computation across a Spark cluster.</ br>
A SparkContext is the entry point for low-level API functionality, and can be used to create RDD's. Is accessed via 
SparkSession.</ br>
**Spark Executors:** The processes that performs the tasks requiured to complete the 
Spark Application run. After running task, will report back to Spark Driver about results 
whether failed or successful etc.

**Spark Architecture diagram in cluster mode:**
![sparkArchitecture](./assets/update_sparkAchitecture.svg)

## Spark RDD Project
RDD's are the low-level component of Spark, of which most of it is built upon. 
RDD's are immutable, partitioned collection of records that can be processed in 
parallel. Because all records in a Spark RDD are basically Scala Objects, this gives 
the user a high level of control to manipulate and interact with the data. 

The purpose of this project is to learn to use RDD's to solve business problems. To 
achieve this, I used RDD transformations and actions to solve problems in a Zeppelin 
worksheet seen below:
![RDD](./assets/rdd_project.jpg)


## Spark Structured API Project
Spark Structured API is a newer high-level component of Spark, and is composed of 
DataFrames, DataSets and SparkSQL.</ br>
**DataFrame:** Represent table of data with rows & columns - a collection of objects with type `Row`. It 
is equivalent to a SQL table, but is distributedly stored, which is good for stored more/performing 
faster computation on.</ br>
**DataSet:** DataSet API allows user to assign a Scala class to records within a DataFrame for manipulation. 
This assignment will be type-safe, so object can only be viewed of the given type. DataSet is a well-defined DataFrame
**SparkSQL:** SQL Queries</ br>
Overall Structured API's are prefereed since they have the benefits of Spark SQL's optimized execution engine and
is easier/more straightforward to use as a higher-level API compared to low-level RDD.

The purpose of this project is to learn to use Structured API's to solve business problems. To 
achieve this, I used dataset and SQL to solve problems in a Zeppelin worksheet seen below:
![structuredAPI](./assets/dataframe_project.jpg)
