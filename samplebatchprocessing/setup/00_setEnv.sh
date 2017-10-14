#!/bin/bash
# PostgreSQL server
export pg_db_host=localhost
export pg_db_port=5432

# PostgreSQL user
export pg_db_user=glf_user
export pg_db_password=glf_password

# PostgreSQL database and schema
export connection_check_db_name=postgres
export db_name=hrsample
export schema_name=glf_schema

# Glassfish containing folder
export usr_glassfish_home_prefix=$HOME/servers

# Glassfish bin folder
export usr_glassfish_bin_path=$usr_glassfish_home_prefix/glassfish4/bin


