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

#svn co http://svn.caiwu.corp/svn/fso-java/hera/trunk/hera
#svn merge http://svn.caiwu.corp/svn/fso-java/hera/branch/hera-gzl --non-interactive
#svn merge http://svn.caiwu.corp/svn/fso-java/hera/branch/hera-gzl-1 --non-interactive
#svn st|grep ^C | awk '{print $2}' |xargs svn resolved
#/home/godzilla/gzl/shell/client/godzilla_svn_wl.sh MERGE  http://svn.caiwu.corp/svn/fso-java/hera/trunk/hera 'http://svn.caiwu.corp/svn/fso-java/hera/branch/hera-gzl-1,http://svn.caiwu.corp/svn/fso-java/hera/branch/hera-gzl' http://localhost:8080/process-callback.do hera admin v-gaoyang@creditease.cn 888888 http://svn.caiwu.corp/svn/fso-java/hera/conflict/hera2015110341

SHELL_NAME=$0			#脚本名称

ACTION=$1				#命令选择
SVN_TRUNK=$2  			#SVN主干
SVN_BRANCHES=$3 		#SVN所有分支(以,分隔)
CALL_BACK_URL=$4 		#回调url
PROJECT_NAME=$5			#项目名(checkout目录名)
USER_NAME=$6			#操作人
SVNUSERNAME=$7			#svn
SVNPASSWORD=$8			#svn
CONFL_URL=$9			#冲突解决分支

if true; then
echo "**********************参数信息***********************"
echo "SHELL_NAME:${SHELL_NAME}"
echo "ACTION:${ACTION}"
echo "SVN_TRUNK:${SVN_TRUNK}"
echo "SVN_BRANCHES:${SVN_BRANCHES}"
echo "CALL_BACK_URL:${CALL_BACK_URL}"
echo "PROJECT_NAME:${PROJECT_NAME}"
echo "USER_NAME:${USER_NAME}" 
echo "SVNUSERNAME:${SVNUSERNAME}" 
echo "SVNPASSWORD:${SVNPASSWORD}" 
echo "CONFL_URL:${CONFL_URL}" 
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
svnuser=" --username=${SVNUSERNAME} --password=${SVNPASSWORD}"


function common() {

	#***
	# 1.清空 本地路径
	#***
	echo "1.清空 本地路径$BEGIN_STR"  
	if [ -d $G_PATH ];then
		echo "清除目录$G_PATH $BEGIN_STR" ;
		time=`date +%F-%H-%M-%S`
		temp1=${G_PATH}"/work/"${PROJECT_NAME}
		echo "temp1:${temp1}"
		if [ -d ${temp1} ];then
			echo "mv${temp1}"
			rm -rf ${temp1}
		fi;
		temp3=${G_PATH}"/work/conflict"
		if [ -d ${temp3} ];then
			echo "rm ${temp3}"
			rm -rf ${temp3}
		fi;
		temp2=${G_PATH}"/conflict"
		echo "temp2:${temp2}"
		if [ -d ${temp2} ];then
			echo "mv${temp2}"
			rm -rf ${temp2}
		fi;
		rm -rf ${G_PATH}"/work/*"
		mkdir ${temp1}
		mkdir ${temp2}
	fi;
	
	#***
	# 2.检出 主干代码
	#***
	echo "2.开始检出代码$BEGIN_STR" 
	#SVN主干
	cd $srcpath
	
	svn co $SVN_TRUNK $srcpath"/"$PROJECT_NAME $svnuser --non-interactive  >/dev/null
	
	###如果分支为空则退出
	if [ "$SVN_BRANCHES" = "empty" ];then 
		echo 6
		exit 6 
	fi
}
#***
# .第一次合并冲突auto-resolve
#conflict.log 冲突文件列表存放文件
#***
function getconflicts() {
	cd ${srcpath}"/"${PROJECT_NAME}
	for name in `grep -r ">>>>>>>" .|grep -v ".svn"| awk -F ":" '{print $1}'`
		do
			svn resolved $name >/dev/null
			fn=$(basename $name)
			mydir=${name%$fn}
			mkdir -p ../conflict/$mydir
			cp $name ../conflict/$mydir
	done
	echo "have conflict"
	echo "CONFL_URL:"${CONFL_URL}
	cd ..
	svn import conflict/ ${CONFL_URL} -m "conflicts of merge from godzilla_auto_merge" $svnuser --non-interactive >/dev/null
}


function commit() {
	#***
	# 4.提交到主干代码
	#***
	echo "4.提交到主干代码$BEGIN_STR"

	cd ${srcpath}"/"${PROJECT_NAME}
	svn ci . -m "合并分支 提交人:"$USER_NAME" `date "+%Y%m%d %H:%M:%S" ` " $svnuser --non-interactive   >/dev/null
	if [ $? == 0 ];then
		echo ""
	else
		echo "[Error]: svn commit failed! shell abort!"
		echo 1
		exit 1
	fi;
}

function status() {
	svn info $SVN_TRUNK $svnuser --non-interactive
}
function getVersion() {
	version=`svn log ${SVN_TRUNK} $svnuser --non-interactive | grep "^r"| head -1|awk '{print $1}'`
	echo "version${version}"
}

case $ACTION in
	#***
	#1. svn合并,未配置冲突标记
	#	a.如有冲突,创建冲突分支,并返回错误码4
	#	[注:部署使用此合并,merge_status=0]
	#***
	MERGE)
		echo "#1.svn合并,未配置冲突标记$BEGIN_STR"
		#0)
		common
		#a)
		echo "|||cd ${srcpath}"/"${PROJECT_NAME}"
		cd ${srcpath}"/"${PROJECT_NAME}
		echo "SVN_BRANCHES:${SVN_BRANCHES}"
		branch_array=${SVN_BRANCHES//,/ }    #这里是将var中的,替换为,  
		for element in $branch_array; do 
			echo "$element  to merge"
		    svn merge $element $svnuser --non-interactive >/dev/null
		    echo "||合并分支：${element}"
		    svn st|grep '^[ ]*C'
			if [ $? == 0 ]; then
				svn st|grep '^[ ]*C' | awk '{print $2}' |xargs svn resolved
			else 
				echo "this branch "$element" is no conflict"
			fi
		done  ;
		
		#过滤配置文件,如果有冲突则退出
        grep -r ">>>>>>>" .|grep -v ".svn"| awk -F ":" '{print $1}' > ../conflict.log
		
		if [ -s ../conflict.log ]; then
			getconflicts
			echo 2
			exit 2
		fi
		echo "合并分支:"$element" finished! no conflict"
		
		exit_code=$?
		echo "${exit_code}"
		exit $exit_code	
	;;
	#***
	#2. svn合并,已配置冲突标记
	#	0.清空work目录,并检出主干代码
	#	a.合并分支,有冲突则标记解决
	#	b.检出冲突分支,并强制覆盖working目录,检查是否仍然有">>>>>>>"冲突标识 exit 4,5
	#	[注:部署使用此合并,merge_status=2]
	#***
	MERGE_RESOLVE)
		echo "#2.svn合并,已配置冲突标记${BEGIN_STR}"
		#0)
		common
		#a)
		cd ${srcpath}"/"${PROJECT_NAME}
		echo "SVN_BRANCHES:${SVN_BRANCHES}"
		branch_array=${SVN_BRANCHES//,/ } 	#这里是将var中的,替换为,  
		for element in $branch_array; do
			echo "$element to merge "
			svn merge $element $svnuser --non-interactive >/dev/null
			
			svn st|grep '^[ ]*C'
			if [ $? == 0 ]; then
				svn st|grep '^[ ]*C' |awk '{print $2}' |xargs svn resolved
			else 
				echo "this branch ${element} is no conflict"
			fi
		done;
		
		#b)
		echo "SVN_BRANCHES: ${SVN_BRANCHES}"
		
		if [ -d ../conflict ]; then
			rm -rf ../conflict
		fi
		
		svn export ${CONFL_URL} ../conflict/ --force $svnuser --non-interactive >/dev/null
		
		cp -r ../conflict/* ./
		#检查是否冲突分支仍然含有冲突标识
		grep ">>>>>>>" -r ../conflict/*
		if [ $? == 0 ];then
			echo "Error: some conflicts still found in conflict-branch! Please resolve all of them ."
			echo 4
			exit 4
		fi
		#检查覆盖掉冲突文件后 是否项目仍然含有冲突标识
		grep -r ">>>>>>>" .|grep -v ".svn"| awk -F ":" '{print $1}'  > ../conflict.log
		if [ -s ../conflict.log ]; then
			echo "Error: some conflicts still found in branch ! Please renew resolve all of them."
			echo 5
			exit 5
		fi
		
		exit_code=$?
		echo "${exit_code}"
		exit $exit_code	
	;;
	#******
	#3. 标记解决,检查是否还有冲突标记
	#	0.清空work目录,并检出主干代码
	#	a.检出冲突文件分支
	#	b.判断是否含有冲突标识 exit 4
	#	[注:标记解决使用此方法]
	#******
	RESOLVE)
		#0)
		common
		#a)
		cd ${srcpath}"/"${PROJECT_NAME}
		echo "SVN_BRANCHES:${SVN_BRANCHES}"
		
		if [ -d ../conflict ];then
			rm -rf ../conflict
		fi
		svn export ${CONFL_URL} ../conflict/ --force $svnuser --non-interactive >/dev/null
		#b)
		grep ">>>>>>>" -r ../conflict/* 
		if [ $? == 0 ];then
			echo "Error: some conflicts still found! Please resolve all of them."
			echo 4
			exit 4
		fi
		
		exit_code=$?
		echo "${exit_code}"
		exit $exit_code	
	;;
	#******
	#4.提交,未标记有冲突
	#	0.清空work目录,并检出主干代码
	#	a.合并分支,检查是否仍然有冲突 exit 2
	#	b.提交
	#******
	COMMIT)
		echo "#4.未标记有冲突提交$BEGIN_STR"
		#0)
		common
		#a)
		cd ${srcpath}"/"${PROJECT_NAME}
		echo "SVN_BRANCHES:${SVN_BRANCHES}"
		branch_array=${SVN_BRANCHES//,/ }    #这里是将var中的,替换为,  
		for element in $branch_array; do 

			echo "$element  to merge"
		    svn merge $element $svnuser --non-interactive >/dev/null
		    echo "||合并分支：${element}"
		    svn st|grep '^[ ]*C'
			if [ $? == 0 ]; then
				echo "this branch "$element" has conflict"
				echo 2
				exit 2
			else 
				echo "this branch "$element" is no conflict"
			fi
		done  ;
		
		#b)
		commit
		
		exit_code=$?
		echo "${exit_code}"
		exit $exit_code	
	;;
	#******
	#5.提交,标记有冲突,且标记解决
	#	0.清空work目录,并检出主干代码
	#	a.合并分支,标记解决
	#	b.冲突分支强制覆盖working目录,检查是否仍然有">>>>>>>"冲突标识 exit 4,5
	#	c.提交
	#******
	COMMIT_RESOLVE)
		
		echo "#5.标记解决提交${BEGIN_STR}"
		#0)
		common
		#a)
		cd ${srcpath}"/"${PROJECT_NAME}
		echo "SVN_BRANCHES:${SVN_BRANCHES}"
		branch_array=${SVN_BRANCHES//,/ } 	#这里是将var中的,替换为,  
		for element in $branch_array; do
			echo "$element to merge "
			svn merge $element $svnuser --non-interactive >/dev/null
			
			svn st|grep '^[ ]*C'
			if [ $? == 0 ]; then
				svn st|grep '^[ ]*C' |awk '{print $2}' |xargs svn resolved
			else 
				echo "this branch ${element} is no conflict"
			fi
		done;
		#b)
		echo "SVN_BRANCHES: ${SVN_BRANCHES}"
		
		if [ -d ../conflict ]; then
			rm -rf ../conflict
		fi
		
		svn export ${CONFL_URL} ../conflict/ --force $svnuser --non-interactive  >/dev/null
		
		cp -r ../conflict/* ./
		#检查是否仍然含有冲突标识
		grep ">>>>>>>" -r ../conflict/*
		if [ $? == 0 ];then
			echo "Error: some conflicts still found in conflict-branch! Please resolve all of them ."
			echo 4
			exit 4
		fi
		#检查覆盖掉冲突文件后是否仍然含有冲突标识
		grep -r ">>>>>>>" .|grep -v ".svn"| awk -F ":" '{print $1}'  > ../conflict.log
		if [ -s ../conflict.log ]; then
			echo "Error: some conflicts still found branch! Please renew resolve all of them."
			echo 5
			exit 5
		fi
		#c)
		commit
		
		exit_code=$?
		echo "${exit_code}"
		exit $exit_code		
	;;
	#******
	#6.显示主干状态
	#	注:功能不清晰，暂时这么写
	#  	只将 svn info 信息输出,其他信息
	#******
	STATUS)
		status
		exit_code=$?
		#echo "${exit_code}"
		exit $exit_code
	;;
	#******
	#7.获取版本号
	# 	 通过 svn log 信息输出
	#******
	VERSION)
		getVersion
		exit_code=$?
		echo "${exit_code}"
		exit $exit_code
	;;
	*)
     echo "parameter not found"
	 echo 3
     exit 3
     ;;
esac
