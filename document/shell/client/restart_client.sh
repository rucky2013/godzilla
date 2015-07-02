#!/bin/sh
#重启tomcat
#TOMCAT_HOME=/app/tomcat

SHELL_NAME=$0
TOMCAT_HOME=$1
TOMCAT_BIN=

if [ "$TOMCAT_HOME" == "" ] ; then
{
	echo "[ERROR] 请输入TOMCAT_HOME参数,如: /bin/sh $0 /app/tomcat " ;
	exit ;
}
fi

if [ ! -d $TOMCAT_HOME ] ; then 
{
	echo "[ERROR] TOMCAT应用服务器不存在！";
	exit ;
}
fi

##判断TOMCAT_HOME 路径最后一个字符是否含有"/"
if [ "${TOMCAT_HOME%?}/" == "$TOMCAT_HOME" ] ; then
{
        TOMCAT_BIN=${TOMCAT_HOME}bin ;

}
else
        TOMCAT_BIN=${TOMCAT_HOME}/bin ;
fi

pidlist=`ps -ef|grep "$TOMCAT_BIN"|grep -v "grep"|awk '{print $2}'`

if [ "$pidlist" = "" ]
   then
       echo "[WARNING] Tomcat没有运行!"
else
  echo "[INFO] TOMCAT应用服务器进程号 : $pidlist"
  kill -9 $pidlist
  echo "[INFO] 杀掉TOMCAT应用服务器进程 : $pidlist"
  echo "[INFO] Stop Tomcat Success！"
fi
echo "[INFO] 启动TOMCAT......"
cd $TOMCAT_BIN
./startup.sh ;

echo "[INFO] 启动TOMCAT SUCCESS!"
