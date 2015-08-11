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

@Service("mvnService")
public class MvnServiceImpl implements MvnService{
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);
	
	@Autowired
	private ClientConfigService clientConfigService;

	private Map<String, PropConfigProviderService> propConfigProviderServices = 
				new HashMap<String, PropConfigProviderService>();
	private Map<String, MvnProviderService> mvnProviderServices = 
			new HashMap<String, MvnProviderService>();
	
	@Override
	public ReturnCodeEnum doDeploy(String srcUrl, String projectCode, String profile) {
		
		ClientConfig clientconfig = clientConfigService.queryDetail(projectCode, profile);
		String IP = clientconfig.getRemoteIp();
		
		/**
		 * 0.get RpcFactory and init rpcservice
		 */
		try {
			this.initRpc(IP);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IOException e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_RPCFACTORY);
		}
		/**
		 * 1.替换pom文件 配置变量
		 */
		boolean flag1 = false;
		try {
			PropConfigProviderService propConfigProviderService = propConfigProviderServices.get(IP);
			RpcResult result = propConfigProviderService.propToPom(projectCode, srcUrl, profile);
			flag1 = result.getRpcCode().equals("0")?true:false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!flag1){
			return ReturnCodeEnum.getByReturnCode(NO_CHANGEPOM);
		}
		/**
		 * 2.mvn deploy  3.将sh命令>queue
		 */
		boolean flag2 = false;
		try {
			MvnProviderService mvnProviderService = mvnProviderServices.get(IP);
			String username = GodzillaApplication.getUser().getUserName();
			RpcResult result = mvnProviderService.deployProject(username, srcUrl, projectCode, profile, IP);
			flag2 = result.getRpcCode().equals("0")?true:false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(flag1&&flag2) {
			return ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY);
		}
		return ReturnCodeEnum.getByReturnCode(NO_MVNDEPLOY);
	}
	
	private void initRpc(String linuxIp) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
		synchronized(propConfigProviderServices) {
			if(propConfigProviderServices.get(linuxIp)==null||"".equals(propConfigProviderServices.get(linuxIp))) {
				RpcFactory rpcFactory= null;
				rpcFactory = Util.getRpcFactoryImpl();
				PropConfigProviderService propConfigProviderService = rpcFactory.getReference(PropConfigProviderService.class, linuxIp);
				if(propConfigProviderService!=null) {
					propConfigProviderServices.put(linuxIp, propConfigProviderService);
				}
			}
			if(mvnProviderServices.get(linuxIp)==null||"".equals(mvnProviderServices.get(linuxIp))) {
				RpcFactory rpcFactory= null;
				rpcFactory = Util.getRpcFactoryImpl();
				MvnProviderService mvnProviderService = rpcFactory.getReference(MvnProviderService.class, linuxIp);
				if(mvnProviderService!=null) {
					mvnProviderServices.put(linuxIp, mvnProviderService);
				}
			}
		}
	}

	
}
