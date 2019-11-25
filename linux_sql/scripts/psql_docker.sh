#!/bin/bash

#setup arguments
mode=$1
db_password=$2

#validate arguments
if [ "$#" -lt 1 ]; then
    \echo "WARNING: Illegal number of parameters" >&2
	exit 1
elif [[ "${mode,,}" != 'start' && "${mode,,}" != 'stop' ]]; then
	\echo "WARNING: First argument must be to 'start' or 'stop' psql_docker" >&2
	exit 1
elif [[ "${mode,,}" = 'start' && "$#" -ne 2 ]]; then
	\echo "WARNING: Need to input password if starting psql_docker" >&2
	exit 1
fi

#setup or stop psql docker service
if [ ${mode,,} = 'start' ]; then
	if \docker ps | \grep -q 'jrvs-psql'; then
		\echo "WARNING: jrvs-psql is already running" >&2
		exit 1
	fi
	\systemctl status docker >> /dev/null || \systemctl start docker
	\docker pull postgres
	if \grep -q 'PGPASSWORD' ~/.bashrc; then
		\sed -i s/PGPASSWORD=.*/PGPASSWORD=${db_password}/g ~/.bashrc
	else
		\echo "export PGPASSWORD=${db_password}" >> ~/.bashrc
	fi
	\export PGPASSWORD=${db_password}
	\docker volume create pgdata
	\docker run --rm --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
	#psql -h localhost -U postgres -W    #starts psql instance

else # case stop
	if  ! \docker ps | \grep -q 'jrvs-psql'; then
		\echo "WARNING: jrvs-psql is not running" >&2
		exit 1
	fi
	\docker stop jrvs-psql
	\systemctl stop docker
fi

exit 0


