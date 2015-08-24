#!/bin/sh

SHELL_NAME=$0
PORT=2222
USER=godzilla

#scp -P 2222 godzilla@10.100.142.84:/app/tomcat/webapps/cupid.war /home/godzilla/gzl/war
scp -P ${PORT} ${USER}@$1:$2 $3

exit ;

