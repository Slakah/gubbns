#!/bin/sh
set -uxe
rm -rf target/universal/
sbt dist
eval $(docker-machine env scrub)
docker-compose build
docker-compose up -d
