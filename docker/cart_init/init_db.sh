#!/usr/bin/env bash
echo 'Please wait while SQL Server 2017 warms up'
sleep 20s

echo 'create database after 20 seconds of wait'
SQL_DIR=sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P ${SA_PASSWORD} -i ${SQL_DIR}/create_cart_db.sql

echo 'Create schemas....'
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P ${SA_PASSWORD} -d cart_service -i ${SQL_DIR}/Tables/*.sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P ${SA_PASSWORD} -d cart_service -i ${SQL_DIR}/ForeignKeys/*.sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P ${SA_PASSWORD} -d cart_service -i ${SQL_DIR}/Triggers/*.sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P ${SA_PASSWORD} -d cart_service -i ${SQL_DIR}/StoredProcedures/*.sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P ${SA_PASSWORD} -d cart_service -i ${SQL_DIR}/UserFunctions/*.sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P ${SA_PASSWORD} -d cart_service -i ${SQL_DIR}/Views/*.sql

echo 'Insert seed data....'
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P ${SA_PASSWORD} -d cart_service -i ${SQL_DIR}/SeedData/*.sql

echo 'Finished initializing the database'
