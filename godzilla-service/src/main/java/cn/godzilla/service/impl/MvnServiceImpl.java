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
import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.main.Util;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnProviderService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.PropConfigProviderService;
import cn.godzilla.service.SvnService;
import cn.godzilla.web.GodzillaApplication;

@Service("mvnService")
public class MvnServiceImpl extends GodzillaApplication implements MvnService {
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);
	
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private SvnService svnService;
	
	private Map<String, PropConfigProviderService> propConfigProviderServices = 
				new HashMap<String, PropConfigProviderService>();
	private Map<String, MvnProviderService> mvnProviderServices = 
			new HashMap<String, MvnProviderService>();
	
	@Override
	public ReturnCodeEnum doDeploy(String srcUrl, String projectCode, String profile) {
		String sid = super.getSid();
		String pencentkey = sid + "-" + projectCode + "-" + profile;
		
		ClientConfig clientconfig = clientConfigService.queryDetail(projectCode, profile);
		String IP = clientconfig.getRemoteIp();
		
		/*
		 * -1.svn合并提交主干
		 * if svn commit success or svn no changecommit
		 * 		continue
		 * else 
		 * 		break
		 */
		ReturnCodeEnum svnreturn = svnService.svnCommit(projectCode, profile);
		
		if(svnreturn==ReturnCodeEnum.OK_SVNCOMMIT||svnreturn==ReturnCodeEnum.NO_CHANGECOMMIT){
			logger.info("************svn合并提交主干 成功**************");
		} else if(svnreturn==ReturnCodeEnum.NO_SVNCOMMIT){
			logger.error("************mvn部署 第-1步:svn合并提交主干 失败**************");
			return svnreturn;
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
			RpcResult result = propConfigProviderService.propToPom(projectCode, srcUrl, profile);
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
		try {
			MvnProviderService mvnProviderService = mvnProviderServices.get(IP);
			String username = GodzillaApplication.getUser().getUserName();
			RpcResult result = mvnProviderService.deployProject(username, srcUrl, projectCode, profile, IP);
			flag2 = result.getRpcCode().equals("0")?true:false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		t1.interrupt();
		processPercent.put(pencentkey, "100");
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

	@Override
	public String getProcessPercent(String sid, String projectCode, String profile) {
		
		String pencentkey = sid + "-" + projectCode + "-" + profile;
		
		String percent = processPercent.get(pencentkey)==null?"0":processPercent.get(pencentkey);
		
		return percent;
	}

	
}
