#!/bin/bash
set -e

url="$1"
shift
cmd="$@"

until $(curl --output /dev/null --silent --head --fail $url); do
  >&2 echo "$url is unavailable, sleeping..."
  sleep 1
done

echo "$url is available"
exec $cmd
