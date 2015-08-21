package cn.godzilla.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.dao.PropConfigMapper;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.model.PropConfig;
import cn.godzilla.model.RpcResult;
import cn.godzilla.mvn.MvnBaseCommand;
import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.main.Util;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnCmdLogService;
import cn.godzilla.service.MvnProviderService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigProviderService;
import cn.godzilla.service.SvnService;
import cn.godzilla.web.GodzillaApplication;

@Service("mvnService")
public class MvnServiceImpl extends GodzillaApplication implements MvnService {
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);
	
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SvnService svnService;
	@Autowired
	private MvnCmdLogService mvnCmdLogService;
	
	private Map<String, PropConfigProviderService> propConfigProviderServices = 
				new HashMap<String, PropConfigProviderService>();
	private Map<String, MvnProviderService> mvnProviderServices = 
			new HashMap<String, MvnProviderService>();
	
	@Override
	public ReturnCodeEnum doDeploy(String srcUrl, String projectCode, String profile, String parentVersion) {
		String sid = super.getSid();
		String pencentkey = sid + "-" + projectCode + "-" + profile;
		
		ClientConfig clientconfig = clientConfigService.queryDetail(projectCode, profile);
		String IP = clientconfig.getRemoteIp();
		
		/*
		 * -1.svn合并  
		 * depresed-1.svn合并提交主干  
		 *  不应该提交  20150820
		 * if svn commit success or svn no changecommit
		 * 		continue
		 * else 
		 * 		break
		 */
		boolean flag = svnService.svnMerge(projectCode, profile);
		
		if(flag){
			logger.info("************svn合并   成功**************");
		} else {
			logger.error("************mvn部署 第-1步:svn合并   失败**************");
			return ReturnCodeEnum.getByReturnCode(NO_MVNDEPLOY);
		} 
		/*
		 * 0.get RpcFactory and init rpcservice
		 * percent 30%
		 */
		try {
			this.initRpc(IP);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IOException e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_RPCFACTORY);
		}
		processPercent.put(pencentkey, "30");
		/*
		 * 1.替换pom文件 配置变量
		 * percent 50%
		 */
		boolean flag1 = false;
		try {
			PropConfigProviderService propConfigProviderService = propConfigProviderServices.get(IP);
			
			RpcResult result = propConfigProviderService.propToPom(projectCode, srcUrl, profile, parentVersion, clientconfig);
			flag1 = result.getRpcCode().equals("0")?true:false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!flag1){
			return ReturnCodeEnum.getByReturnCode(NO_CHANGEPOM);
		}
		processPercent.put(pencentkey, "50");
		
		final String processKey1 = sid + "-" + projectCode + "-" + profile;
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.currentThread().sleep(1000);
						String processValue = GodzillaApplication.processPercent.get(processKey1);
						int prVal = Integer.parseInt(processValue);
						if(prVal>=95) break;
						GodzillaApplication.processPercent.put(processKey1, ++prVal+"");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t1.start();
		/*
		 * 2.mvn deploy  3.将sh命令>queue
		 * percent 90%
		 */
		boolean flag2 = false;
		if("godzilla".equals(projectCode)) {
			flag2 = true;
		} else {
			try {
				MvnProviderService mvnProviderService = mvnProviderServices.get(IP);
				String username = GodzillaApplication.getUser().getUserName();
				RpcResult result = this.deployProject(mvnProviderService, username, srcUrl, projectCode, profile, IP);
				flag2 = result.getRpcCode().equals("0")?true:false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		t1.interrupt();
		processPercent.put(pencentkey, "100");
		if(flag1&&flag2) {
			
			/*
			 * end.部署成功  更新 部署版本号 
			 */
			boolean flag3 = this.updateDeployVersion(projectCode, profile);
			return ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY);
		}
		return ReturnCodeEnum.getByReturnCode(NO_MVNDEPLOY);
	}
	
	

	public RpcResult deployProject(MvnProviderService mvnProviderService, String username, String srUrl, String projectCode, String profile, String IP) {
		boolean flag2 = false;
		RpcResult result = null;
		String commands = "";
		try {
			String POM_PATH = srUrl + "/pom.xml";
			String USER_NAME = username;
			String PROJECT_NAME = projectCode;
			
			
			String shell = SHELL_CLIENT_PATH.endsWith("/")
							?(SHELL_CLIENT_PATH+ "godzilla_mvn.sh")
									:(SHELL_CLIENT_PATH+"/" +"godzilla_mvn.sh");
			String str = "sh "+shell+" deploy "+POM_PATH+" "+USER_NAME+" "+PROJECT_NAME+" "+ PROJECT_ENV ;
			result = mvnProviderService.mvnDeploy(str, PROJECT_NAME, PROJECT_ENV, USER_NAME);
			commands = str;
			flag2 = result.getRpcCode().equals("0")?true:false;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return RpcResult.create(FAILURE);
		}
		if(flag2) {
			mvnCmdLogService.addMvnCmdLog(username, projectCode, profile, commands, "mvn deploy 执行成功");
		} else {
			mvnCmdLogService.addMvnCmdLog(username, projectCode, profile, commands, "mvn deploy 执行失败");
		}
		return result;
	}
	
	private boolean updateDeployVersion(String projectCode, String profile) {
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		
		ReturnCodeEnum versionreturn = svnService.getVersion(trunkPath, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return false;
		}
		
		String deployVersion = svnVersionThreadLocal.get();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("deploy_version", deployVersion);
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);
		
		int update = clientConfigService.updateDeployVersionByCodeAndProfile(parameterMap);
		
		return update>0;
	}

	private void initRpc(String linuxIp) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
		synchronized(propConfigProviderServices) {
			//if(propConfigProviderServices.get(linuxIp)==null||"".equals(propConfigProviderServices.get(linuxIp))) {
				RpcFactory rpcFactory1= null;
				rpcFactory1 = Util.getRpcFactoryImpl();
				PropConfigProviderService propConfigProviderService = rpcFactory1.getReference(PropConfigProviderService.class, linuxIp);
				if(propConfigProviderService!=null) {
					propConfigProviderServices.put(linuxIp, propConfigProviderService);
				}
			//}
			//if(mvnProviderServices.get(linuxIp)==null||"".equals(mvnProviderServices.get(linuxIp))) {
                RpcFactory rpcFactory2= null;
				rpcFactory2 = Util.getRpcFactoryImpl();
				MvnProviderService mvnProviderService = rpcFactory2.getReference(MvnProviderService.class, linuxIp);
				if(mvnProviderService!=null) {
					mvnProviderServices.put(linuxIp, mvnProviderService);
				}
			//}
		}
	}

	@Override
	public String getProcessPercent(String sid, String projectCode, String profile) {
		
		String pencentkey = sid + "-" + projectCode + "-" + profile;
		
		String percent = processPercent.get(pencentkey)==null?"0":processPercent.get(pencentkey);
		
		return percent;
	}
	
	

	
}
