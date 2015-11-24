#!/bin/sh

SHELL_NAME=$0
TOMCAT_HOME=$1
WARDIR=$2
TOMCAT_BIN="${TOMCAT_HOME}/bin"

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
echo "WARDIR: $WARDIR"
DIR="./webapps/$WARDIR"
echo "DIR: $DIR"
cd $TOMCAT_HOME
rm -rf $DIR
cd $TOMCAT_BIN
./startup.sh ;

echo "[INFO] 启动TOMCAT SUCCESS!"
tail -f /app/tomcat/logs/catalina.out 
