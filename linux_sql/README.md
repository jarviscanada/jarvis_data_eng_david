# 1) Architecture of project #
![my_architecture](./assets/linuxSqlArchitecture.jpg)

# 2) Explanation of files #
###### 1) ./scripts/host_info.sh ######
Collects the host's hardware information and inserts into host_info table. Run once on bash agent start.
###### 2) ./scripts/host_usage.sh ######
Collects host usage data - CPU & RAM, and inserts into host_usage table. Is executed every minute using chrontab.
###### 3) ./scripts/psql_docker.sh ######
Uses docker to setup psql server
###### 4) ./sql/ddl.sql ######
Contains create_table definitions for host_info and host_usage tables
###### 5) ./sql/queries.sql ######
Contains queries to return analysis of tables. Queries include:
	1. Grouping hosts by hardware information
	2. Printing the average memory use of all hosts
	3. Detects whether node failure occurs in any of the hosts
