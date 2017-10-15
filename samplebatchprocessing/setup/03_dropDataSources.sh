#!/bin/bash
# Set environment variables
echo "1. Setting environment variables"
source 00_setEnv.sh
echo "Glassfish bin path "$usr_glassfish_bin_path
echo "-------------------------------------------"

# Export path
export PATH=$PATH:$usr_glassfish_bin_path

# Drop JDBC data source
echo "2. Drop JDBC data source"
asadmin delete-jdbc-resource jdbc/$glf_data_source

# Drop JDBC connection pool
echo "3. Drop JDBC connection pool"
asadmin delete-jdbc-connection-pool jdbc/$glf_connection_pool_name

# Check created connection pool and JDBC datasource
echo "4. Listing JDBC connection pools and datasources (for check purpose)"
asadmin list-jdbc-connection-pools
asadmin list-jdbc-resources
