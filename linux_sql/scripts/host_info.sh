#!/bin/bash

#set arguments
psql_host=$1 		#generally localhost
psql_port=$2		#generally 5432
db_name=$3			#generally host_agent
psql_user=$4		#generally postgres
psql_password=$5	#generally D1n034uR


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
lscpu_out=`lscpu`
vm_t=`vmstat -t --unit M`

####### Hardware Specification data  #######
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\)" | awk '{print $2}')
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}')
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{$1=$2=""; \
                print $0;}')
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | awk '{print $3}')
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | sed s/[^0-9]*//g)
total_mem=$(cat /proc/meminfo | grep "^MemTotal" | awk '{print $2}')
timestamp_hw=$(echo "$vm_t" | awk 'END {print $(NF-1), $NF}')



# PSQL command to be run in psql
insert_string=`echo "INSERT INTO PUBLIC.host_info("\
	"id, hostname, cpu_number, cpu_architecture,"\
	"cpu_model, cpu_mhz, L2_cache, total_mem,timestamp"\
	")"\
	"VALUES"\
	"("\
	"DEFAULT, '${hostname}', ${cpu_number},"\
	"'${cpu_architecture}', '${cpu_model}',"\
	"${cpu_mhz}, ${l2_cache}, ${total_mem},'${timestamp_hw}'"\
	");"`

# insert value, return error if DDL constraints not met
if ! psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "$insert_string" >/dev/null ; then
	echo "WARNING: tuple could not be inserted into table" >&2
	exit 1
fi

echo "INSERT SUCCESS"
exit 0
