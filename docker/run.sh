#!/usr/bin/env bash
docker run -d --mount type=volume,destination=/var/opt/mssql --name cart_service --hostname cart_service-mssql -p ${3:-1434}:1434 cart_service