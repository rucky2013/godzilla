#!/bin/sh

SHELL_NAME=$0
IP=$1
PORT=2222
USER=godzilla

ssh -p $PORT $USER@$IP "tail -f /app/tomcat/logs/catalina.out "

exit ;

