#!/bin/bash
set -e

file="$1"
shift
cmd="$@"

until [ -f $file ]; do
  >&2 echo "$file does not exist, sleeping..."
  sleep 2
done

echo "$file exists"
exec /docker-entrypoint.sh "$cmd"
