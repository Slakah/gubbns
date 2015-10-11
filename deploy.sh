#!/bin/sh

./activator clean stage

rsync -avz --progress target/universal/stage/ jcollier@gubbns.com:~/gubbns/

echo "Starting gubbns.com remotely"
ssh -t jcollier@gubbns.com 'sudo sh -c "cp -r gubbns/* /opt/gubbns/ && service gubbns restart"'

