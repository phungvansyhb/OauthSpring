FROM postgres:15

COPY src/main/resources/sql/init.sql /docker-entrypoint-initdb.d/