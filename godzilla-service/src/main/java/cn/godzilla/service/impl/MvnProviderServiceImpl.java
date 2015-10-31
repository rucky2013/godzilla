package cn.godzilla.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.common.Application;
import cn.godzilla.dao.ProjectMapper;
import cn.godzilla.model.Project;
import cn.godzilla.model.RpcResult;
import cn.godzilla.mvn.MvnBaseCommand;
import cn.godzilla.mvn.ShCommand;
import cn.godzilla.service.MvnProviderService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;

public class MvnProviderServiceImpl extends Application implements MvnProviderService{
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);

	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private ProjectMapper projectMapper;
	@Override
	public RpcResult mvnDeploy(String str1, String projectCode, String projectEnv, String username, String realname, String profile) {
		mvnBuildThreadLocal.set("FAILURE");
		mvnERRORThreadLocal.set("SUCCESS");
		
		MvnBaseCommand command1 = new MvnBaseCommand();
		String mvnlog = command1.execute(str1, projectCode, PROJECT_ENV, username);
		logger.info("mvnBuildThreadLocal:::"	+ mvnBuildThreadLocal.get());
		logger.info("mvnERRORThreadLocal:::"	+ mvnERRORThreadLocal.get());
		
		//20151030 暂时不用此检查//判断　是否含有${XX}未设置配置项 
		boolean flag4 = shellReturnThreadLocal.get().equals("4")?true:false;
		/*if(flag4) {
			return RpcResult.create(NOSETPROPS); 
		}*/
		boolean flag2 = mvnBuildThreadLocal.get().equals(SUCCESS)?true:false;
		boolean flag3 = mvnERRORThreadLocal.get().equals(SUCCESS)?true:false;
		//保存项目lib jar信息列表
		String jarlog = "";
		if(flag2&&flag3){
			Project project = projectMapper.qureyByProCode(projectCode);
			String libpath = project.getLibPath();
			String str2 = "/home/godzilla/gzl/shell/client/godzilla_mvn.sh showlib " + libpath;
			ShCommand command2 = new ShCommand();
			jarlog = command2.execute(str2);
			
		}
		//保存mvn部署日志
		int logid = operateLogService.addOperateLog(mvnlog, jarlog);
		
		mvnBuildThreadLocal.set("FAILURE");
		mvnERRORThreadLocal.set("SUCCESS");
		if(flag2&&flag3) {
			return RpcResult.create(SUCCESS, logid);
		} else {
			//build failure
			return RpcResult.create(BUILDFAILURE, logid);
		} 
		//return RpcResult.create(FAILURE);
	}

	
}
