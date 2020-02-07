# image to be used
FROM postgres:9.6-alpine

# scripts to be init
COPY ./sql_ddl/schema.sql /docker-entrypoint-initdb.d
