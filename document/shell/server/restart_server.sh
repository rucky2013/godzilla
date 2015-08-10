#!/bin/sh

SHELL_NAME=$0
IP=$1
TOMCAT_HOME=$2
REMOTE_BASH_PATH=/home/godzilla/gzl/shell/client/restart_client.sh
PORT=2222
USER=godzilla

if [ ! -n "$IP" ] || [ ! -n "$TOMCAT_HOME" ] ;then 
{
	echo "[ERROR] 缺少参数 ！ /bin/sh $0 IP TOMCAT_HOME ;"
	echo "[ERROR] EG: /bin/sh $0 192.168.1.12 /opt/tomcat "
	exit ;
}
fi

echo "[INFO] IP:$IP" ;
echo "[INFO] TOMCAT_HOME:$TOMCAT_HOME"

ssh -p $PORT $USER@$IP "$REMOTE_BASH_PATH $TOMCAT_HOME"

exit ;

