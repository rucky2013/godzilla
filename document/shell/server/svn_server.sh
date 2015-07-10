#!/bin/bash

BEGIN_STR=".............................................."

SHELL_NAME=$0	#脚本名称

ACTION=$1	#命令选择

SVN_BRANCH_PATH=$2 #SVN分支

LOCAL_PATH=$3	#本地代码路径

USER_NAME=$4	#SVN用户名

PASSWORD=$5	#SVN密码

SVN_TRUNK_PATH=$6  #SVN主干

PROJECT_NAME=$7   #项目名称

IP=$8  #客户端IP 
USER=godzilla
PORT=2222	#ssh端口号



help()
{
        echo "$BEGIN_STR"
        echo "USE: $SHELL_NAME [merge,commit]"
        echo "ARGS: IP,SVN_BRANCH_PATH,LOCAL_PATH,USER_NAEM,PASSWORD,SVN_TRUNK_PATH,PROJECT_NAME"
        echo "eg: /bin/sh $SHELL_NAME merge svn:127.0.0.1/project/branch/test /home/godzilla/svndata/test admin 123456 svn:127.0.0.1/project/trunk/test test 192.168.1.100"
	exit 1
}

if [[ "$ACTION" == "-help" ]] ; then
	help
	exit 1
else
	if [ -z "$IP" ] || [ -z "$SVN_BRANCH_PATH" ] || [ -z "$LOCAL_PATH" ] || [ -z "$USER_NAME" ] || [ -z "$PASSWORD" ] || [ -z "$SVN_TRUNK_PATH" ] || [ -z "$PROJECT_NAME" ]
        then
        echo "[ERROR]!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	echo "[ERROR] ERROR...............ARGS ERROR ,,,,,,, !!!!!!"
	echo "[ERROR]For Help Please Execute: sh $SHELL_NAME -help!"
	echo "[ERROR]!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"  
        exit 1
        fi
fi

echo "[INFO]****************参数信息***********************"
 
echo "SVN_BRANCH_PATH:${SVN_BRANCH_PATH}"

echo "LOCAL_PATH:${LOCAL_PATH}"

echo "USER_NAME:${USER_NAME}"

echo "PASSWORD:${PASSWORD}"

echo "SVN_TRUNK_PATH:${SVN_TRUNK_PATH}"

echo "PROJECT_NAME:${PROJECT_NAME}"

echo "IP:${IP}"

echo "[INFO]***************参数信息END***********************"

svn_merge(){
	ssh -p $PORT $USER@$IP "/home/godzilla/bash-godzilla/godzilla_svn.sh merge $SVN_BRANCH_PATH $LOCAL_PATH $USER_NAME $PASSWORD $SVN_TRUNK_PATH $PROJECT_NAME"

	exit ;	
}
svn_commit(){
	ssh -p $PORT $USER@$IP "/home/godzilla/bash-godzilla/godzilla_svn.sh commit $SVN_BRANCH_PATH $LOCAL_PATH $USER_NAME $PASSWORD $SVN_TRUNK_PATH $PROJECT_NAME"
	exit ;
}
case $ACTION in
	merge)
		svn_merge
	;;
	commit)
		svn_commit
	;;
	-help)
		help
	;;
esac
