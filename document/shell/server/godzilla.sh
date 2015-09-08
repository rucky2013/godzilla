#!/bin/bash


#***
# ErrorCode
# 4.XX.sh parameter not found
#***

## 初始化java环境变量
java_env() {
	set java environment
	
	JAVA_HOME=/opt/jdk1.7.0_71
	
	export JRE_HOME=/opt/jdk1.7.0_71/jre
	
	export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
	
	export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
}


## scp脚本工具到客户端
scp_gzl() {
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.66:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.71:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.72:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.73:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.74:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.75:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.76:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.77:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.78:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.79:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.80:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.81:/home/godzilla/ 
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.82:/home/godzilla/
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.83:/home/godzilla/
	scp -P 2222 -r /home/godzilla/gzl/gzl godzilla@10.100.142.84:/home/godzilla/
}


## 杀掉客户端线程
killclients() {
	ssh -p 2222 godzilla@10.100.142.66 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.71 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.72 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.73 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.74 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.75 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.76 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.77 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.78 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.79 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.80 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.81 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.82 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.83 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.84 "ps -ef|grep godzilla-rpc-provider.jar |head -1| awk '{print $2}'|xargs kill -9"
}

## debug开启客户端线程
startclientsdebug() {
	ssh -p 2222 godzilla@10.100.142.66 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &" 
	ssh -p 2222 godzilla@10.100.142.71 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.72 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.73 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.74 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.75 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.76 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.77 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.78 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.79 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.80 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.81 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.82 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.83 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.84 "/opt/jdk1.7.0_71/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=7070,server=y,suspend=y -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
}

## 开启客户端线程
startclients() {
	ssh -p 2222 godzilla@10.100.142.66 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &" 
	ssh -p 2222 godzilla@10.100.142.71 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.72 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.73 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.74 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.75 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.76 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.77 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.78 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.79 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.80 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.81 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.82 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.83 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
	ssh -p 2222 godzilla@10.100.142.84 "/opt/jdk1.7.0_71/bin/java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &"
}


echo $1

case $1 in
	upgrade)
		java_env
		scp_gzl
		killclients
		startclients
		exit_code=$?
		exit $exit_code		
	;;
	stopclients)
		java_env
		killclients
		exit_code=$?
		exit $exit_code
	;;
	startclients)
		java_env
		killclients
		startclients
		exit_code=$?
		exit $exit_code
	;;
	*)
     	echo "parameter not found"
     	echo 4
     	exit 4
     ;;
esac