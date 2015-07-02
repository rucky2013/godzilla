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

WORK_PATH=$HOME/svndata/$USER_NAME #代码路径

WORK_PATH_RECYCLE=$HOME/recycle/$USER_NAME #回收站路径
help()
{
        echo "$BEGIN_STR"
        echo "USE: $SHELL_NAME {merge|commit}"
        echo "ARGS: SVN_BRANCH_PATH,LOCAL_PATH,USER_NAEM,PASSWORD,SVN_TRUNK_PATH,PROJECT_NAME"
        echo "eg: /bin/sh $SHELL_NAME merge svn:127.0.0.1/project/branch/test /home/godzilla/svndata/test admin 123456 svn:127.0.0.1/project/trunk/test test"
	exit 1
}

if [[ "$ACTION" == "-help" ]] ; then
	help
	exit 1
else
	if [ -z "$SVN_BRANCH_PATH" ] || [ -z "$LOCAL_PATH" ] || [ -z "$USER_NAME" ] || [ -z "$PASSWORD" ] || [ -z "$SVN_TRUNK_PATH" ] || [ -z "$PROJECT_NAME" ]
        then
        echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	echo "!!!!!!ERROR ERROR ,,,,,,,,,ARGS ERROR ,,,,,,, !!!!!!"
	echo "!For Help Please Execute: /bin/sh $SHELL_NAME -help!"
	echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"  
        exit 1
        fi
fi

echo "**********************参数信息***********************"
 
echo "SVN_BRANCH_PATH:${SVN_BRANCH_PATH}"

echo "LOCAL_PATH:${LOCAL_PATH}"

echo "USER_NAME:${USER_NAME}"

echo "PASSWORD:${PASSWORD}"

echo "SVN_TRUNK_PATH:${SVN_TRUNK_PATH}"

echo "PROJECT_NAME:${PROJECT_NAME}"

echo "WORK_PATH:${WORK_PATH}" 

echo "WORK_PATH_RECYCLE:${WORK_PATH_RECYCLE}"

echo "********************参数信息END***********************"

checkoutbranch()
{
	echo "开始检出代码$BEGIN_STR"
	svn co $SVN_BRANCH_PATH $LOCAL_PATH --username $USER_NAME --password $PASSWORD
}
rmlocalpath()
{
	if [ -d $LOCAL_PATH ]
	then
	echo "清除目录$LOCAL_PATH $BEGIN_STR" ;
	time=`date +%F-%H-%M-%S`
	mv $LOCAL_PATH "$WORK_PATH_RECYCLE$time" ;
	fi
}
merge2localbranch(){
	
	echo "代码合并$BEGIN_STR"
	svn merge $SVN_TRUNK_PATH $LOCAL_PATH --accept postpone --username $USER_NAME --password $PASSWORD
}
resolve(){
	echo "检查冲突$BEGIN_STR"
	file=`svn st $LOCAL_PATH |awk '{ if($1 == "C") { print $2 }}'` && test -z $file || svn resolve --accept working $file --username $USER_NAME --password $PASSWORD
}
add(){
	echo "检查新增$BEGIN_STR"
	file=`svn st $LOCAL_PATH |awk '{ if($1 == "?") { print $2 }}'` && test -z $file || svn add $file --username $USER_NAME --password $PASSWORD
}
rm(){
	echo "检查移除$BEGIN_STR"
	file=`svn st $LOCAL_PATH |awk '{ if($1 == "!") { print $2 }}'` && test -z $file || svn rm $file --username $USER_NAME --password $PASSWORD
}
checkin(){
	echo "提交代码$LOCAL_PATH ......" 
	
	svn ci $LOCAL_PATH -m $USER_NAME --username $USER_NAME --password $PASSWORD
}

svn_merge(){
	
	echo "$BEGIN_STR 开始处理分支合并 $BEGIN_STR"
	rmlocalpath ;
	checkoutbranch ;
	merge2localbranch ;
	resolve ;
	add ;
	rm ;
	checkin ;
	echo "$BEGIN_STR 分支合并处理完成 $BEGIN_STR"
}

checkouttrunk(){
	echo "检出主干 $BEGIN_STR"
	svn co "$SVN_TRUNK_PATH" "$LOCAL_PATH" --username "$USER_NAME" --password "$PASSWORD"
}

merge2localtrunk(){
	echo "合并主干 $BEGIN_STR"
	
	###将分支的代码合并到本地主干副本，冲突文件已分支为准
	svn merge $SVN_TRUNK_PATH $LOCAL_PATH --accept theirs-full --username $USER_NAME --password $PASSWORD
}

svn_commit()
{
	echo "$BEGIN_STR 提交到主干流程开始 $BEGIN_STR"	
	
	rmlocalpath ;
	checkouttrunk ;
	merge2localtrunk ;
	add ;
	rm ;
	checkin ;
	echo "$BEGIN_STR 提交到主干流程结束 $BEGIN_STR"	
	echo $BEGIN_STR
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
