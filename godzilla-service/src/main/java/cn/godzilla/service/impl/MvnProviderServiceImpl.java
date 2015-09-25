package cn.godzilla.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.godzilla.common.Application;
import cn.godzilla.model.RpcResult;
import cn.godzilla.mvn.MvnBaseCommand;
import cn.godzilla.service.MvnProviderService;

public class MvnProviderServiceImpl extends Application implements MvnProviderService{
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);


	@Override
	public RpcResult mvnDeploy(String str, String PROJECT_NAME, String projectEnv, String USER_NAME) {
		boolean flag = false;
		MvnBaseCommand command = new MvnBaseCommand();
		flag = command.execute(str, PROJECT_NAME, PROJECT_ENV, USER_NAME);
		logger.info("mvnBuildThreadLocal:::"	+ mvnBuildThreadLocal.get());
		logger.info("mvnERRORThreadLocal:::"	+ mvnERRORThreadLocal.get());
		
		boolean flag2 = mvnBuildThreadLocal.get().equals(SUCCESS)?true:false;
		boolean flag3 = mvnERRORThreadLocal.get().equals(SUCCESS)?true:false;
		if(flag&&flag2&&flag3) {
			return RpcResult.create(SUCCESS);
		} else if(flag){
			return RpcResult.create(FAILURE);
		} else {
			//build failure
			return RpcResult.create(BUILDFAILURE);
		}
	}

	
}
