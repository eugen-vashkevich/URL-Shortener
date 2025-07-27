#!/usr/bin/env bash

set -euo pipefail

REPL_USER="${POSTGRES_REPLICATION_USER}"
REPL_PASSWORD="${POSTGRES_REPLICATION_PASSWORD}"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
 DO
    \$do\$
    BEGIN
       IF NOT EXISTS (SELECT FROM pg_catalog.pg_user WHERE usename = '${REPL_USER}') THEN
          CREATE USER ${REPL_USER} WITH REPLICATION ENCRYPTED PASSWORD '${REPL_PASSWORD}';
       END IF;
    END
    \$do\$;
    SELECT pg_create_physical_replication_slot('replication_slot')
    WHERE NOT EXISTS (SELECT 1 FROM pg_replication_slots WHERE slot_name = 'replication_slot');
EOSQL
