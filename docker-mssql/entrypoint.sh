#!/bin/bash

# Start the script to create the DB and user
chmod +x /usr/config/configure-db.sh
/usr/config/configure-db.sh &

# Start SQL Server
/opt/mssql/bin/sqlservr


