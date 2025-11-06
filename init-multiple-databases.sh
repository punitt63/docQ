#!/bin/bash

set -e
set -u

function create_database() {
  local db_name=$1
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    SELECT 'CREATE DATABASE $db_name'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$db_name')\gexec
EOSQL
}

create_database "health_facility"
create_database "keycloak"

echo "Created databases: health_facility and keycloak"

