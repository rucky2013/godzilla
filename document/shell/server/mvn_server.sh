#!/bin/bash

BEGIN_STR=".............................................."

SHELL_NAME=$0

ACTION=$1

POM_PATH=$2

USER_NAME=$3

PROJECT_NAME=$4

PROJECT_ENV=$5

IP=$6  #客户端IP 
USER=godzilla
PORT=2222	#ssh端口号



help()
{
        echo "$BEGIN_STR"
        echo "USE: $SHELL_NAME [install]"
        echo "ARGS: POM_PATH,USER_NAME,PROJECT_NAME,PROJECT_ENV,IP"
        echo "eg : /bin/sh godzilla_mvn.sh deploy /home/godzilla/svndata/projectname/pom.xml zhongweili2 projectname dev-test 192.168.1.100"
	exit 1
}

echo "[INFO]****************参数信息***********************"
 
echo "POM_PATH:${POM_PATH}"

echo "USER_NAME:${USER_NAME}"

echo "PROJECT_NAME:${PROJECT_NAME}"

echo "PROJECT_ENV:${PROJECT_ENV}"

echo "IP:${IP}"

echo "[INFO]***************参数信息END***********************"


if [[ "$ACTION" == "-help" ]] ; then
	help
	exit 1
else
	if [ -z "$IP" ] || [ -z "$PROJECT_NAME" ]
        then
        echo "[ERROR]!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	echo "[ERROR] ERROR...............ARGS ERROR ,,,,,,, !!!!!!"
	echo "[ERROR]For Help Please Execute: sh $SHELL_NAME -help!"
	echo "[ERROR]!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"  
        exit 1
        fi
fi



mvn_deploy(){
	ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_mvn.sh deploy $POM_PATH $USER_NAME $PROJECT_NAME $PROJECT_ENV $IP"
	
	exit ;	
}
case $ACTION in
	deploy)
		mvn_deploy
	;;
	-help)
		help
	;;
esac
