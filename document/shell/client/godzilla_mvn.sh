#!/bin/bash

#***
# ErrorCode
# 1.
#***

HOME=$HOME
SHELL_NAME=$0			#脚本名称
ACTION=$1				#命令选择
POM_PATH=$2  			#web pom路径
USER_NAME=$3  			#操作人
PROJECT_NAME=$4  		#项目名称
PROJECT_ENV=$5  		#打包profile id
PARENT_VERSION=$6  		#发布版本
PARENTPOM_PATH=$7  		#父pom 路径

G_PATH="/home/godzilla/gzl"
# 本地路径
srcpath=$G_PATH"/work"

if false; then
BEGIN_STR="..................................."
echo POM_PATH:$POM_PATH
echo USER_NAME:$USER_NAME
echo PROJECT_NAME:$PROJECT_NAME
echo PROJECT_ENV:$PROJECT_ENV
echo PARENT_VERSION:$PARENT_VERSION
echo PARENTPOM_PATH:$PARENTPOM_PATH
BEGIN_STR="..................................."
fi

source $HOME/.bash_profile
/app/maven/bin/mvn --version

deploy()
{
	cd $srcpath
	cd $PROJECT_NAME
	
	/app/maven/bin/mvn versions:set -DnewVersion=${PARENT_VERSION}
	/app/maven/bin/mvn -N versions:update-child-modules
	
	#PARENTPOM_PATH 为父pom
	/app/maven/bin/mvn clean deploy -f $PARENTPOM_PATH -P$PROJECT_ENV
}
showlib() {
	ls $POM_PATH
}

case $ACTION in
	#***
	# 1.部署与打包
	#***
	deploy)
		deploy
	;;
	#***
	#2.显示web项目lib目录jar包列表
	#***
	showlib)
		showlib
	;;
	*)
		echo "ERROR !ARGS ERROR"
		echo 1
		exit 1
	;;
esac
