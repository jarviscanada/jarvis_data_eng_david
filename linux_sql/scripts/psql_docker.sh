#!/bin/bash

#setup arguments
mode=$1
db_password=$2


#define functions

# return 0 if  jrvs-psql grepped, 1 if jrvs-psql not found
verify_created() {
	docker container ls -a -f name=jrvs-psql | grep -q 'jrvs-psql'
}

# return 0 if  jrvs-psql grepped, 1 if jrvs-psql not found
verify_running() {
	docker ps -f name=jrvs-psql | grep -q 'jrvs-psql'
}


#validate arguments
if [ "$#" -lt 1 ]; then
    echo "WARNING: Illegal number of parameters" >&2
	exit 1
elif [[ "${mode,,}" != 'start' && "${mode,,}" != 'stop' ]]; then
	echo "WARNING: First argument must be to 'start' or 'stop' psql_docker" >&2
	exit 1
elif [[ "${mode,,}" = 'start' && "$#" -ne 2 ]]; then
	echo "WARNING: Need to input password if starting psql_docker" >&2
	exit 1
fi

#setup or stop psql docker service
if [ ${mode,,} = 'start' ]; then
	if verify_running; then
		echo "WARNING: jrvs-psql is already running" >&2
		exit 1
	elif verify_created; then
		echo "STARTING already created container jrvs-psql"
		docker container start jrvs-psql
		exit 0
	fi
	sudo systemctl status docker > /dev/null 2>&1 || sudo systemctl start docker
	export PGPASSWORD=${db_password}
	if docker image ls | grep -q '^postgres'; then
		echo "Postgres image found"
	else
		echo "WARNING: No postgres image found, pulling" >&2
		docker pull postgres
	fi

	if docker volume ls -f name=pgdata | grep -q 'pgdata'; then
		echo "docker volume pgdata already exists" >&2
	else
		docker volume create pgdata
		echo "Creating volume pgdata" >&2
	fi
	docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
	if ! verify_created; then
		echo "WARNING: jrvs-psql could not be created" >&2
		exit 1
	fi
	docker container start jrvs-psql
	if  verify_running; then
		psql -h localhost -U postgres -f /home/centos/dev/jarvis_data_eng_David_Yang/linux_sql/sql/ddl.sql #run ddl scripts to create new  table
		echo "SUCCESSFULLY created database & tables"
	else
		echo "WARNING: jrvs-psql instsance not running" >&2
	fi

else # case stop
	if  ! verify_running; then
		echo "WARNING: jrvs-psql is not running" >&2
	else
		docker container stop jrvs-psql
	fi


	docker container rm jrvs-psql 2>/dev/null
	if  ! verify_created; then
		echo "SUCCESSFULLY stopped jrvs-psql container"
	else
		echo "WARNING: attempt to stop jrvs-psql container failed" >&2
	fi
fi

exit 0


