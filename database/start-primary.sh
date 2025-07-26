#!/usr/bin/env bash

set -euo pipefail

exec postgres \
  -c wal_level=replica \
  -c max_wal_senders=3 \
  -c max_replication_slots=3\
  -c hot_standby=on \
  -c listen_addresses='*' \
  -c wal_keep_size=1024