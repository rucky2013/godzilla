#!/bin/bash

BEGIN_STR=".............................................."
#***
# 1. example：
# sh /home/godzilla/gzl/shell/server/svn_server_wl.sh $ACTION $SVN_TRUNK $SVN_BRANCHES $CALL_BACK_URL $PROJECT_NAME $USER_NAME $IP
#***

#***
# ErrorCode
# 1.svn commit failed!
# 2.some conflicts still found!
# 3.client.sh parameter not found
# 4.server.sh parameter not found
#***

SHELL_NAME=$0			#脚本名称
ACTION=$1				#命令选择
SVN_TRUNK=$2  			#SVN主干
SVN_BRANCHES=$3 		#SVN所有分支(以,分隔)
CALL_BACK_URL=$4 		#回调url
PROJECT_NAME=$5			#项目名(checkout目录名)
USER_NAME=$6			#操作人
IP=$7 					#客户端IP 

USER=godzilla
PORT=2222				#ssh端口号

echo "[INFO]****************参数信息***********************"

echo "SHELL_NAME:${SHELL_NAME}"
echo "ACTION:${ACTION}"
echo "SVN_TRUNK:${SVN_TRUNK}"
echo "SVN_BRANCHES:${SVN_BRANCHES}"
echo "CALL_BACK_URL:${CALL_BACK_URL}"
echo "PROJECT_NAME:${PROJECT_NAME}"
echo "USER_NAME:${USER_NAME}"
echo "IP:${IP}"

echo "[INFO]***************参数信息END***********************"

case $1 in
	merge)
		IP=$7
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh MERGE $2 $3 $4 $5 $6"
		exit_code=$?
		exit $exit_code		
	;;
	commit)
		IP=$7
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh COMMIT $2 $3 $4 $5 $6"
		exit_code=$?
		exit $exit_code
	;;
	*)
     	echo "parameter not found"
     	exit 4
     ;;
esac
