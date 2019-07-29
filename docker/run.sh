#!/usr/bin/env bash
docker run -d --mount type=volume,destination=/var/opt/mssql --name ${2} --hostname ${1}-mssql -p ${3:-1433}:1433 ${1}