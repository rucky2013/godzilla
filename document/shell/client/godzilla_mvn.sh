#!/bin/bash

HOME=$HOME

SHELL_NAME=$0

ACTION=$1

POM_PATH=$2

USER_NAME=$3

PROJECT_NAME=$4

PROJECT_ENV=$5

PARENT_VERSION=$6

G_PATH="/home/godzilla/gzl"
# 本地路径
srcpath=$G_PATH"/work"

WHO=`whoami`

echo POM_PATH:$POM_PATH
echo USER_NAME:$USER_NAME
echo PROJECT_NAME:$PROJECT_NAME
echo PROJECT_ENV:$PROJECT_ENV
echo PARENT_VERSION:$PARENT_VERSION

if [ $WHO == "root" ] ;then
	
	echo "ERROR !Can't support for user root !"
	exit 1
fi
if [ -z $1 ] ;then

echo "ERROR! FOR HELP , /bin/sh $SHELL_NAME -help"

exit 1

fi

BEGIN_STR="..................................."

source $HOME/.bash_profile

info()
{
	echo "SHELL_NAME $SHELL_NAME"
	echo "HOME $HOME"
	echo "whoami $WHO"
	mvn --version
}
#install
deploy()
{
	echo "install $BEGIN_STR" ;
	if [ -z $POM_PATH ] || [ -z $PROJECT_ENV ] ; then
		echo "[ERROR!!!!]$BEGIN_STR"
		echo "[ERROR!!!!] ARGS ERROR.........ERROR!......"
		exit 1
	fi 
	echo $srcpath
	cd $srcpath
	echo $PROJECT_NAME
	cd $PROJECT_NAME
	mvn versions:set -DnewVersion=${PARENT_VERSION}
	mvn -N versions:update-child-modules
	
	mvn clean deploy -f $POM_PATH -P$PROJECT_ENV
}
deploy1()
{
	echo "deploy $BEGIN_STR"
	mvn clean deploy -f $POM_PATH -P$PROJECT_ENV
}
clean()
{
	echo "clean $BEGIN_STR"
	mvn clean -f $POM_PATH
}
help()
{
	echo "USAGE: /bin/sh $SHELL_NAME [ install | deploy | clean | -help | info ] POM_PATH ,USER_NAME ,PROJECT_NAME ,PROJECT_ENV"
	echo "eg : /bin/sh godzilla_mvn.sh deploy /home/godzilla/svndata/projectname/pom.xml zhongweili2 projectname dev-test "
}
case $ACTION in

	install)
		install
	;;
	deploy)
		deploy
	;;
	clean)
		clean
	;;
	-help)
		help
	;;
	info)
		info
	;;
	*)
		echo "ERROR !ARGS ERROR ,For Help  /bin/sh $SHELL_NAME -help "
		exit 1
	;;
esac
