#!/bin/bash
# Set environment variables
echo "1. Setting environment variables"
source 00_setEnv.sh
echo "--------------------------------"

# Prepate handy PostgreSQL password file
export pg_pass_file=~/.pgpass
export pg_db_password_line=$pg_db_host:$pg_db_port:$db_name:$pg_db_user:$pg_db_password
echo $pg_db_password_line > $pg_pass_file
chmod 0600 $pg_pass_file

# Drop schema (including tables)
echo "Dropping schema (including tables): "$schema_name
export pg_schema_drop_command="drop schema $schema_name cascade;"
psql -L log/04_dropSchema.log -h localhost -d $db_name -U $pg_db_user -w -c "$pg_schema_drop_command"

# Check schema dropped
echo "Checking if schema dropped"
export pg_schema_check_command="select distinct table_catalog, table_schema, table_name, table_type from information_schema.tables where table_schema = '$schema_name';"
psql -L log/01_setupSchema.log -h localhost -d $db_name -U $pg_db_user -w -c "$pg_schema_check_command"

# Modify password file for the default database
export pg_db_password_line=$pg_db_host:$pg_db_port:$connection_check_db_name:$pg_db_user:$pg_db_password
echo $pg_db_password_line > $pg_pass_file

# Drop DB
echo "Dropping DB "$db_name
dropdb -h $pg_db_host -p $pg_db_port -U $pg_db_user -w $db_name

# Check DB dropped
echo "Checking if DB dropped"
export pg_db_check_command="select datname, datistemplate from pg_database where datistemplate=false and datname = '$db_name';"
psql -L log/04_dropSchema.log -h localhost -d $connection_check_db_name -U $pg_db_user -w -c "$pg_db_check_command"

# Delete handy PostgreSQL password file
rm $pg_pass_file


