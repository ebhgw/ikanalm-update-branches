FROM mcr.microsoft.com/mssql/server:2019-latest

# Create a config directory
RUN mkdir -p /usr/config && mkdir /usr/config/db
WORKDIR /usr/config

# Bundle config source
COPY *.sh /usr/config/

# Grant permissions for to our scripts to be executable
# RUN chmod +x /usr/config/entrypoint.sh
# RUN chmod +x /usr/config/configure-db.sh

# Add sql table and data
COPY src/main/sql/V*.sql /usr/config/db/

EXPOSE 1433

ENTRYPOINT ["./entrypoint.sh"]
