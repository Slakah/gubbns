#!/bin/sh

sh activator clean

sh activator stage

cd target/universal

rsync -avz --progress stage gubbns@gubbns.com:~/

# TODO - Use a systemd service to do this
echo "Starting gubbns.com remotely";
ssh gubbns@gubbns.com "cd stage; [ -f RUNNING_PID ] && kill \$(cat RUNNING_PID); sh bin/blog -mem 250 > logs/run.log & exit;"
