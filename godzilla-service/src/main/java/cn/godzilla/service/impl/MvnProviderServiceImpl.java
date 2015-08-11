package cn.godzilla.service.impl;

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
import cn.godzilla.service.MvnProviderService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.PropConfigProviderService;
import cn.godzilla.web.GodzillaApplication;

public class MvnProviderServiceImpl implements MvnProviderService{
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);
	
	public RpcResult deployProject(String username, String srUrl, String projectCode, String profile, String IP) {
		boolean flag = false;
		try {
			String POM_PATH = srUrl + "/pom.xml";
			String USER_NAME = username;
			String PROJECT_NAME = projectCode;
			
			MvnBaseCommand command = new MvnBaseCommand();
			String shell = SHELL_CLIENT_PATH.endsWith("/")
							?(SHELL_CLIENT_PATH+ "godzilla_mvn.sh")
									:(SHELL_CLIENT_PATH+"/" +"godzilla_mvn.sh");
			String str = "sh "+shell+" deploy "+POM_PATH+" "+USER_NAME+" "+PROJECT_NAME+" "+ PROJECT_ENV ;
			flag = command.execute(str, PROJECT_NAME, PROJECT_ENV, USER_NAME);
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return RpcResult.create(FAILURE);
		}
		if(flag) {
			return RpcResult.create(SUCCESS);
		} else {
			return RpcResult.create(FAILURE);
		}
		
	}
	
	
	
}
