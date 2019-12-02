#!/bin/bash

#set arguments
psql_host=$1            #generally localhost
psql_port=$2            #generally 5432
db_name=$3              #generally host_agent
psql_user=$4            #generally postgres
psql_password=$5        #insert password

#validate arguments
if [ "$#" -ne 5 ]; then
    echo "WARNING: You have $# arguments, must input 5 arugments" >&2
	exit 1
fi

#check if arguments given are valid
export PGPASSWORD=$psql_password
if ! psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "\q"  2>/dev/null; then
	echo "WARNING: Connection to psql via user ${psql_user}, hosted on ${psql_host} to "\
		"database: ${db_name} has failed. Please re-enter arguments"
	exit 1
else
	echo "Successfully connected to database ${db_name}"
fi

###### Define Variables to use below ######
vm_t=`vmstat -t --unit M`
hostname=$(hostname -f)
#Query for host_id
sql_srch=`echo "SELECT id FROM host_info WHERE hostname='$hostname'"`

####### Linux Resource #######
timestamp_lr=$(echo "$vm_t" | awk 'END {print $(NF-1), $NF}')
host_id=$(psql -t -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "$sql_srch")
memory_free=$(echo "$vm_t" | awk 'END {print $4}')
cpu_idle=$(echo "$vm_t" |awk 'END {print $15}')
cpu_kernel=$(echo "$vm_t" |awk 'END {print $14}')
disk_io=`vmstat -d --unit M | awk 'END {print $(NF-1)}'`
disk_available=$(df -BM / | awk END{print} | awk '{print $4}' | sed s/[^0-9]*//g)

###### Insert Query ######
insert_string=`echo "INSERT INTO PUBLIC.host_usage( 
	timestamp, host_id, memory_free, cpu_idle,
    cpu_kernel, disk_io, disk_available)
	VALUES(
	'${timestamp_lr}', ${host_id}, ${memory_free}, 
	${cpu_idle},${cpu_kernel},${disk_io},
	${disk_available}
	);"`

# insert value, return error if DDL constraints not met
if ! psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "$insert_string" >/dev/null ; then
	echo "WARNING: tuple could not be inserted into table" >&2
	exit 1
fi

exit 0
