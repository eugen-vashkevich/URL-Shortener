#!/usr/bin/env bash

set -euo pipefail

REPL_USER="${POSTGRES_REPLICATION_USER}"
REPL_PASSWORD="${POSTGRES_REPLICATION_PASSWORD}"
MASTER_HOST="${POSTGRES_HOST}"
MASTER_HOST_PORT="${POSTGRES_HOST_PORT}"
PGDATA="/var/lib/postgresql/data"

if [ "$(id -u)" = "0" ]; then
  exec gosu postgres "$0" "$@"
fi

  rm -rf "$PGDATA"/*
  mkdir -p "$PGDATA"
  chmod 0700 "$PGDATA"

  export PGPASSWORD="${REPL_PASSWORD}"
  pg_basebackup --pgdata="$PGDATA" -R --slot=replication_slot --host="${MASTER_HOST}" --port="${MASTER_HOST_PORT}" -U "${REPL_USER}" -w
  unset PGPASSWORD

exec postgres \
  -c hot_standby=on \
  -c primary_conninfo="host=${MASTER_HOST} port=5432 user=${REPL_USER} password=${REPL_PASSWORD}" \
  -c listen_addresses='*' \
  -c default_transaction_read_only=on