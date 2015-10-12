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
	ssh -p 2222 godzilla@10.100.142.71 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.72 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.73 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.74 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.75 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.76 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.77 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.78 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.79 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.80 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.81 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.82 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.83 "rm -rf /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.84 "rm -rf /home/godzilla/gzl"
	
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
	
	ssh -p 2222 godzilla@10.100.142.71 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.72 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.73 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.74 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.75 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.76 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.77 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.78 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.79 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.80 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.81 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.82 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.83 "chmod 755 -R /home/godzilla/gzl"
	ssh -p 2222 godzilla@10.100.142.84 "chmod 755 -R /home/godzilla/gzl"
}


## 杀掉客户端线程
killclients() {
	ssh -p 2222 godzilla@10.100.142.71 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.72 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.73 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.74 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.75 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.76 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.77 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.78 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.79 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.80 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.81 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.82 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.83 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.84 "ps -ef|grep godzilla-rpc-provider.jar |grep -v "grep"| awk '{print $2}'|xargs kill -9"
}

## debug开启客户端线程
startclientsdebug() {
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

TOMCAT_HOME=/app/tomcat
TOMCAT_BIN=/app/tomcat/bin

## 关闭tomcat
stoptomcats() {
	ssh -p 2222 godzilla@10.100.142.71 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.72 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.73 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.74 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.75 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.76 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.77 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.78 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.79 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.80 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.81 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.82 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.83 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
	ssh -p 2222 godzilla@10.100.142.84 "ps -ef|grep '$TOMCAT_BIN'|grep -v 'grep'|awk '{print $2}'|xargs kill -9"
}

## 开启tomcat
starttomcats() {
	ssh -p 2222 godzilla@10.100.142.71 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.72 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.73 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.74 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.75 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.76 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.77 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.78 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.79 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.80 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.81 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.82 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.83 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
	ssh -p 2222 godzilla@10.100.142.84 "source $HOME/.bash_profile;cd $TOMCAT_BIN;./startup.sh ;"
}



echo $1

case $1 in
	upgrade)
		java_env
		scp_gzl
		killclients
		startclients
		##startclientsdebug
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
		##startclientsdebug
		exit_code=$?
		exit $exit_code
	;;
	stoptomcats)
		stoptomcats
	;;
	starttomcats)
		stoptomcats
		starttomcats
	;;
	*)
     	echo "parameter not found"
     	echo 4
     	exit 4
     ;;
esac