#!/bin/bash
. /etc/profile
. ~/.bash_profile

#------------------------------------------------------------------------------------------------
#程序部署目录
_workDir=/apps/zsxc-module-system

#部署程序jar名称
_program=$_workDir/zsxc-module-system-1.0.0.jar

#部署程序jar springboot启动参数如：--server.port=9000 --spring.profiles.active=dev
#不指定参数时，可以为空
_program_param='--spring.profiles.active=dev'

#是否启动远程调试： 0 不开启 1 开启
_is_remote_debug=0

#启动时指定日志输出文件
_default_log_file=../logs/zsxc-module-system.log

#------------------------------------------------------------------------------------------------
_programid=0

cmd=`which java`
tmp=`dirname $0`
cd $tmp

#start project
function  startSA(){
	echo "starting $_program"
	_programid=`ps aux | grep $_program | grep -v grep | awk 'END{print $2}'`
	if [ -z $_programid ]
	then
		case $1 in
		start)
			nohup $cmd -Dfile.encoding=UTF-8  -jar $_program  $_program_param >> $_default_log_file 2>&1 &
			_programid=`ps aux | grep $_program | grep -v grep | awk 'END{print $2}'`

			if [ -z $_programid ]
			then 
				echo "Failed to start program."
			else
				echo "program has started. Process ID:$_programid";
			fi
			;;
		start-f)
			$cmd -Dfile.encoding=UTF-8  -jar $_program  $_program_param 2>&1
			_programid=`ps aux | grep $_program | grep -v grep | awk 'END{print $2}'`

			if [ -z $_programid ]
			then 
				echo "Failed to start program."
			else
				echo "program has started. Process ID:$_programid";
			fi
		;;
		esac
	else
		echo "program is already running. Process ID: $_programid"
		exit -1;
	fi
}

# stop project 
function stopSA(){
	 _programid=`ps aux | grep $_program | grep -v grep | awk 'END{print $2}'`
	if [ -z $_programid ]
	then
		echo "No program"
	else
		echo "program process ID: $_programid"
		echo "Stopping..."
		kill $_programid
		sleep 1
		echo "program stoped."
	fi
}

# look current project status
function statusSA(){
	_programid=`ps aux | grep $_program | grep -v grep | awk 'END{print $2}'`
	if [ -z $_programid ]
	then
		echo "program is stoped"
	else
		echo "program is running"
		echo "process ID: $_programid"
	fi
}


# jenkins app deploy
function jdeploySA(){
    BUILD_ID=dontKillMe
    stopSA
    mv -f $_program $_program.bak
    echo "backup $_program success!"
    mv -f $_workDir/target/* $_workDir
    rm -rf  $_workDir/target/
    cd $_workDir
    if [ $_is_remote_debug -eq 1 ]
    then
        echo "$cmd -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y -jar $_program  $_program_param"
        echo "service application is debugging，please run local application to debug ！"
        $cmd -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y -jar $_program  $_program_param
    else
        startSA start
    fi
}


# use tips
if [ $# -ge 1 ]
then
	case $1 in
	start)
		startSA $1
		;;
	stop)
		stopSA
		;;
	start-f)
		startSA $1
		;;
	status)
		statusSA
        ;;
    restart)
        stopSA
        startSA start
        ;;
    jdeploy)
        jdeploySA
        ;;
	esac
else
	echo "Usage: $0 [start|start-f|stop|status|restart] "
	echo "       <1> ./load.sh start , start program in background."
	echo "       <2> ./load.sh start-f , start program in foreground."
	echo "       <3> ./load.sh stop , stop program."
	echo "       <4> ./load.sh status , show program running status."
	echo "       <5> ./load.sh restart , restart program."
    echo "       <6> other shell command , please ask lingrui."
fi

