package cn.godzilla.service.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.RpcResult;
import cn.godzilla.mvn.MvnBaseCommand;
import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.main.Util;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnCmdLogService;
import cn.godzilla.service.MvnProviderService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.PropConfigProviderService;
import cn.godzilla.web.GodzillaApplication;

public class MvnProviderServiceImpl implements MvnProviderService{
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);


	@Override
	public RpcResult mvnDeploy(String str, String PROJECT_NAME, String projectEnv, String USER_NAME) {
		boolean flag = false;
		MvnBaseCommand command = new MvnBaseCommand();
		flag = command.execute(str, PROJECT_NAME, PROJECT_ENV, USER_NAME);
		if(flag) {
			return RpcResult.create(SUCCESS);
		} else {
			return RpcResult.create(FAILURE);
		}
	}

	
}
