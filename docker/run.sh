#!/usr/bin/env bash
docker run -d --mount type=volume,destination=/var/opt/mssql --name cart_service --hostname cart_service-mssql -p 1434:1433 cart_service