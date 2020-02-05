#!/usr/bin/bash

sudo systemctl status docker > /dev/null 2>&1 || sudo systemctl start docker

#create a new docker network
docker network create --driver bridge trading-net

docker build -t trading-app . 
docker image ls -f reference=trading-psl

#start a docker container
#attached the container to the trading-net network

docker volume create sbdata

docker run --name trading-psql-demo-local \
-e POSTGRES_PASSWORD=password \
-e POSTGRES_DB=jrvstrading \
-e POSTGRES_USER=postgres \
-v sbdata:/var/lib/postgresql/data \
--network trading-net \
-d -p 5432:5432 jrvs/trading-psql-demo


#Set IEX credential 
set IEX_PUB_TOKEN="pk_d7bbe2f1840841f4883128797042a7bd"


#start trading-app container which is attached to the trading-net docker network
docker run -d --name trading-app-local \
-e "PSQL_URL=jdbc:postgresql://trading-psql-demo-local:5432/jrvstrading" \
-e "PSQL_USER=postgres" \
-e "PSQL_PASSWORD=password" \
-e "IEX_PUB_TOKEN=${IEX_PUB_TOKEN}" \
--network trading-net \
-p 8080:8080 -t trading-app

docker container start trading-psql-demo-local
docker container start trading-app-local

docker ps
