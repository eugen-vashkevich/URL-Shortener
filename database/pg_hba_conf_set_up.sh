#!/bin/bash
set -euo pipefail

PG_HBA_CONF_PATH="/var/lib/postgresql/data/pg_hba.conf"
REPL_USER="${POSTGRES_REPLICATION_USER}"
POSTGRES_USER="${POSTGRES_USER}"

echo "host    replication     ${REPL_USER}    0.0.0.0/0               scram-sha-256" >> "$PG_HBA_CONF_PATH"

echo "host    all             ${POSTGRES_USER} 0.0.0.0/0               scram-sha-256" >> "$PG_HBA_CONF_PATH"
