#!/bin/bash
# Set environment variables
echo "1. Setting environment variables"
source 00_setEnv.sh
echo "Glassfish bin path "$usr_glassfish_bin_path
echo "-------------------------------------------"

# Export path
export PATH=$PATH:$usr_glassfish_bin_path

echo "2. Listing JDBC connection pools and datasources (for check purpose)"
asadmin list-jdbc-connection-pools
asadmin list-jdbc-resources

echo "3. Create new connection pool"

echo "4. Create new data source"
