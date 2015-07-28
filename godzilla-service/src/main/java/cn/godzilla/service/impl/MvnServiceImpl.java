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
import cn.godzilla.service.MvnService;
import cn.godzilla.service.PropConfigProviderService;
import cn.godzilla.web.SuperController;

@Service("mvnService")
public class MvnServiceImpl implements MvnService{
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);
	
	@Autowired
	private ClientConfigService clientConfigService;

	private Map<String, PropConfigProviderService> propConfigProviderServices = 
				new HashMap<String, PropConfigProviderService>();
	
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
		boolean flag2 = this.deployProject(projectCode, srcUrl, profile, IP);
		
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
		}
	}

	private boolean deployProject(String srUrl, String projectCode, String profile, String IP) {
		boolean flag = false;
		try {
			String POM_PATH = srUrl + "/pom.xml";
			String USER_NAME = SuperController.getUser().getUserName();
			String PROJECT_NAME = projectCode;
			
			MvnBaseCommand command = new MvnBaseCommand();
			String shell = SHELL_PATH.endsWith("/")
							?(SHELL_PATH+ "mvn_server.sh")
									:(SHELL_PATH+"/" +"mvn_server.sh");
			String str = "sh "+shell+" deploy "+POM_PATH+" "+USER_NAME+" "+PROJECT_NAME+" "+ PROJECT_ENV + IP ;
			flag = command.execute(str, PROJECT_NAME, PROJECT_ENV, USER_NAME);
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return flag;
	}
	
	
	
}
