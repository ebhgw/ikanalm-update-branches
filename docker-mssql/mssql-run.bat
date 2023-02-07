rem env variable to container should use " instead of '
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=mssql1Ipw" -p 1433:1433 -v sqlvolume:/var/opt/mssql -d ebomitali/local-mssql-server