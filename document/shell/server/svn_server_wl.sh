#!/bin/bash

#***
# ErrorCode
# 1.svn commit failed!
# 2.some conflicts found in branch!
# 3.parameter not found
# 4.some conflicts still found in conflict-branch! Please resolve all of them. 
# 5.some conflicts still found on branch ,please renew resolve it
# 6.branches is '' ,no need commit
#***

SHELL_NAME=$0			#脚本名称
ACTION=$1				#命令选择
SVN_TRUNK=$2  			#SVN主干 version命令时 操作地址
SVN_BRANCHES=$3 		#SVN所有分支(以,分隔)
CALL_BACK_URL=$4 		#回调url
PROJECT_NAME=$5			#项目名(checkout目录名)
USER_NAME=$6			#操作人
IP=$7 					#客户端IP 
SVNUSERNAME=$8 			#客户端IP 
SVNPASSWORD=$9 			#客户端IP 
CONFL_URL=${10}			#冲突解决分支

USER=godzilla
PORT=2222				#ssh端口号

if false; then
echo "[INFO]****************参数信息***********************"
echo "SHELL_NAME:${SHELL_NAME}"
echo "ACTION:${ACTION}"
echo "SVN_TRUNK:${SVN_TRUNK}"
echo "SVN_BRANCHES:${SVN_BRANCHES}"
echo "CALL_BACK_URL:${CALL_BACK_URL}"
echo "PROJECT_NAME:${PROJECT_NAME}"
echo "USER_NAME:${USER_NAME}"
echo "IP:${IP}"
echo "CONFL_URL:${CONFL_URL}"
echo "[INFO]***************参数信息END***********************"
fi

case $1 in
	#1. svn合并,未配置冲突标记
	merge)
		IP=$7
		#echo "|||/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh MERGE $2 $3 $4 $5 $6 $8 $9 ${10}"
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh MERGE $2 $3 $4 $5 $6 $8 $9 ${10}"
		exit_code=$?
		exit $exit_code		
	;;
	#2. svn合并,已配置冲突标记
	merge_resolve)
		IP=$7
		#echo "|||/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh MERGE_RESOLVE $2 $3 $4 $5 $6 $8 $9 ${10}"
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh MERGE_RESOLVE $2 $3 $4 $5 $6 $8 $9 ${10}"
		exit_code=$?
		exit $exit_code	
	;;
	#3. 标记解决,检查是否还有冲突标记
	resolve)
		IP=$7
		#echo "|||/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh RESOLVE $2 $3 $4 $5 $6 $8 $9 ${10}"
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh RESOLVE $2 $3 $4 $5 $6 $8 $9 ${10}"
		exit_code=$?
		exit $exit_code	
	;;
	#4.提交,未标记有冲突
	commit)
		IP=$7
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh COMMIT $2 $3 $4 $5 $6 $8 $9 ${10}"
		exit_code=$?
		exit $exit_code
	;;
	#5.提交,标记有冲突,且标记解决
	commit_resolve)
		IP=$7
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh COMMIT_RESOLVE $2 $3 $4 $5 $6 $8 $9 ${10}"
		exit_code=$?
		exit $exit_code
	;;
	#6.显示主干状态
	status)
		IP=$7
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh STATUS $2 $3 $4 $5 $6 $8 $9 ${10}"
		exit_code=$?
		exit $exit_code
	;;
	#7.获取版本号
	version)
		IP=$7
		ssh -p $PORT $USER@$IP "/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh VERSION $2 $3 $4 $5 $6 $8 $9 ${10}"
		exit_code=$?
		exit $exit_code
	;;
	*)
     	echo "parameter not found"
     	echo 4
     	exit 4
     ;;
esac
