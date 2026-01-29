#!/usr/bin/env bash
set -e

DB_NAME="hoteladmin"
DB_USER="postgres"

DB_EXISTS=$(psql -U "$DB_USER" -tAc "SELECT 1 FROM pg_database WHERE datname='${DB_NAME}'")

if [ "$DB_EXISTS" != "1" ]; then
  psql -U "$DB_USER" -c "CREATE DATABASE ${DB_NAME};"
fi

psql -U "$DB_USER" -d "$DB_NAME" -f schema.sql
psql -U "$DB_USER" -d "$DB_NAME" -f data.sql

echo "Done."
