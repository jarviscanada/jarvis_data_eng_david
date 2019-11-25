#!/bin/bash

#setup arguments
mode=$1
db_password=$2

#validate arguments
if [ "$#" -lt 1 ]; then
    echo "Illegal number of parameters"
	exit 1
elif [[ "${mode,,}" != 'start' && "${mode,,}" != 'stop' ]]; then
	echo "First argument must be to 'start' or 'stop' psql_docker"
	exit 1
elif [[ "${mode,,}" = 'start' && "$#" -ne 2 ]]; then
	echo "Need to input password if starting psql_docker"
	exit 1
fi

#setup or stop psql docker service
if [ ${mode,,} = 'start' ]; then
	systemctl status docker || systemctl start docker
	docker pull postgres
	export PGPASSWORD=${db_password}
	docker volume create pgdata
	docker run --rm --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
	#psql -h localhost -U postgres -W    #starts psql instance

else # case stop
	docker stop jrvs-psql
	systemctl stop docker
fi


exit 0


