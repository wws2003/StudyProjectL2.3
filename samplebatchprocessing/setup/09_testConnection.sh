#!/bin/bash
# Set environment variables
echo "1. Setting environment variables"
source 00_setEnv.sh
echo "--------------------------------"

# Prepate handy PostgreSQL password file
export pg_pass_file=~/.pgpass
export pg_db_password_line=$pg_db_host:$pg_db_port:$connection_check_db_name:$pg_db_user:$pg_db_password
echo $pg_db_password_line > $pg_pass_file
chmod 0600 $pg_pass_file

# Check DB connection (actually list all available DB then exit)
echo "2.Showing some privileges of current user $pg_db_user"
export pg_user_check_command="select rolname, rolcreatedb, rolcanlogin from pg_roles where rolname = '$pg_db_user';"
psql -L log/09_testConnection.log -h localhost -d $connection_check_db_name -U $pg_db_user -w -c "$pg_user_check_command"

# Delete handy PostgreSQL password file
rm $pg_pass_file
