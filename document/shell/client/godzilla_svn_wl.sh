#!/bin/bash

BEGIN_STR=".............................................."
#***
# 1. example：
# sh /home/godzilla/gzl/shell/client/godzilla_svn_wl.sh $ACTION $SVN_TRUNK $SVN_BRANCHES $CALL_BACK_URL $PROJECT_NAME $USER_NAME
#***

#***
# ErrorCode
# 1.svn commit failed!
# 2.some conflicts still found!
# 3.parameter not found
#***

SHELL_NAME=$0			#脚本名称

ACTION=$1				#命令选择
SVN_TRUNK=$2  			#SVN主干
SVN_BRANCHES=$3 		#SVN所有分支(以,分隔)
CALL_BACK_URL=$4 		#回调url
PROJECT_NAME=$5			#项目名(checkout目录名)
USER_NAME=$6			#操作人

if false; then
echo "**********************参数信息***********************"
echo "SHELL_NAME:${SHELL_NAME}"
echo "ACTION:${ACTION}"
echo "SVN_TRUNK:${SVN_TRUNK}"
echo "SVN_BRANCHES:${SVN_BRANCHES}"
echo "CALL_BACK_URL:${CALL_BACK_URL}"
echo "PROJECT_NAME:${PROJECT_NAME}"
echo "USER_NAME:${USER_NAME}" 
echo "********************参数信息END***********************"
fi

G_PATH="/home/godzilla/gzl"
# 本地路径
srcpath=$G_PATH"/work"
# 冲突存放路径
conflictpath=$G_PATH"/conflict"

# 回收站路径
RECYCLE_PATH="/home/godzilla/recycle/"
# 日志路径
logpath="/home/godzilla/log/"
# 修改为你的svn用户名和密码,将使用此用户密码访问你的svn
svnuser=" --username=wanglin --password=1"


function callback() {
	#***
	# 回调shell运行百分比
	#***
	curl -G -d "process="10 CALL_BACK_URL 2>null
}

function common() {

	#***
	# 1.清空 本地路径
	#***
	echo "1.清空 本地路径$BEGIN_STR"  
	if [ -d $G_L_PATH ];then
		echo "清除目录$G_L_PATH $BEGIN_STR" ;
		time=`date +%F-%H-%M-%S`
		mv $G_PATH"/work" "$RECYCLE_PATH$time" ;
		mv $G_PATH"/conflict" "$RECYCLE_PATH$time" ;
		mkdir $G_PATH"/work"
		mkdir $G_PATH"/conflict"
	fi;
	
	#***
	# 2.检出 主干代码
	#***
	echo "2.开始检出代码$BEGIN_STR" 
	#SVN主干
	#SVN_TRUNK=$2
	echo "SVN_TRUNK:${SVN_TRUNK}"
	#svn co http://10.100.142.37:9090/svn/fso/godzilla/trunk /home/godzilla/gzl/work --username=wanglin --password=1 --non-interactive
	cd $srcpath
	mkdir ${PROJECT_NAME}
	#echo $SVN_TRUNK $srcpath"/"$PROJECT_NAME $svnuser --non-interactive
	svn co $SVN_TRUNK $srcpath"/"$PROJECT_NAME $svnuser --non-interactive > null 
	
	#***
	# 3.合并分支代码
	#***
	#svn merge http://10.100.142.37:9090/svn/fso/godzilla/branch/test-svn-b1 --username=wanglin --password=1 --non-interactive 
	#SVN_BRANCHES=$3
	
	echo "3.合并分支代码$BEGIN_STR"
	cd $srcpath/$PROJECT_NAME
	echo "SVN_BRANCHES:${SVN_BRANCHES}"
	branch_array=${SVN_BRANCHES//,/ }    #这里是将var中的,替换为空格  
	for element in $branch_array; do  
	    svn merge $element $svnuser --non-interactive   
	    svn st|grep '^[ ]*C'
		if [ $? == 0 ] ;then
			echo "Error: some conflicts still found! Please resolve all of them. " $element 
			exit 2
		fi;
	done  ;
	echo "合并分支:"$element" finished! no conflict"
}

function commit() {
	#***
	# 4.提交到主干代码
	#***
	echo "4.提交到主干代码$BEGIN_STR"
	cd $srcpath/$PROJECT_NAME
	svn ci . -m "合并分支 提交人:"$USER_NAME" `date "+%Y%m%d %H:%M:%S" ` " $svnuser --non-interactive   > null
	if [ $? == 1 ];then
		echo "[Error]: svn commit failed! shell abort!"
		exit 1 
	fi;
}

	#***
	# 0.功能不清晰，暂时这么写
	#***
function status() {

	#***
	# 1.清空 本地路径
	#***
	echo "1.清空 本地路径$BEGIN_STR" > null
	if [ -d $G_L_PATH ];then
		echo "清除目录$G_L_PATH $BEGIN_STR" ;  > null
		time=`date +%F-%H-%M-%S`
		mv $G_PATH"/work" "$RECYCLE_PATH$time" ;
		mv $G_PATH"/conflict" "$RECYCLE_PATH$time" ;
		mkdir $G_PATH"/work"
		mkdir $G_PATH"/conflict"
	fi;
	
	#***
	# 2.检出 主干代码
	#***
	echo "2.开始检出代码$BEGIN_STR"  > null
	#SVN主干
	#SVN_TRUNK=$2
	echo "SVN_TRUNK:${SVN_TRUNK}" > null 
	#svn co http://10.100.142.37:9090/svn/fso/godzilla/trunk /home/godzilla/gzl/work --username=wanglin --password=1 --non-interactive
	cd $srcpath
	mkdir ${PROJECT_NAME}
	#echo $SVN_TRUNK $srcpath"/"$PROJECT_NAME $svnuser --non-interactive
	svn co $SVN_TRUNK $srcpath"/"$PROJECT_NAME $svnuser --non-interactive > null  
	
	#***
	# 3.显示状态
	#***
	echo "0.显示主干状态"  > null 
	cd $srcpath/$PROJECT_NAME
	svn info
}
case $ACTION in
	#------
	#初始合并分支到主干working copy
	#------
	MERGE)
		common
		exit_code=$?
		echo "exit_code:${exit_code}"
		exit $exit_code	
	;;
	#------
	#合并后提交主干
	#------
	COMMIT)
		common
		commit
		exit_code=$?
		echo "exit_code:${exit_code}"
		exit $exit_code		
	;;
	#------
	#显示主干状态
	#注:功能不清晰，暂时这么写
	#  只将 svn info 信息输出,其他信息>null
	#------
	STATUS)
		status
		exit_code=$?
		echo "exit_code:${exit_code}"
		exit $exit_code
	;;
	*)
     echo "parameter not found"
     exit 3
     ;;
esac
