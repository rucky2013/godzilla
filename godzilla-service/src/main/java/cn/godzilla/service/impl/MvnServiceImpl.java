package cn.godzilla.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.model.RpcResult;
import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.main.Util;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnCmdLogService;
import cn.godzilla.service.MvnProviderService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigProviderService;
import cn.godzilla.service.SvnService;
import cn.godzilla.svn.BaseShellCommand;
import cn.godzilla.web.GodzillaApplication;

@Service("mvnService")
public class MvnServiceImpl extends GodzillaApplication implements MvnService {
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);
	@Autowired
	private OperateLogService operateLogService;
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
	
	
	public ReturnCodeEnum downLoadWar(HttpServletResponse response, String projectCode, String profile) {
		
		java.io.BufferedOutputStream bos = null;
		java.io.BufferedInputStream bis = null;
		String ctxPath = SAVE_WAR_PATH ;
		//String downLoadPath = ctxPath + "/" + projectCode + ".war";
		//logger.info("****downloadpath : "+ downLoadPath);
		//1.ssh scp 到本地
		this.copyWar(projectCode, profile);
		
		
		//1.5 获取 war包 文件名
		File warfile = this.searchFile(new File(ctxPath), projectCode);
		
		//2.输出
		try {
			long fileLength = warfile.length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment;filename=" + 
					new String(warfile.getName().getBytes("utf-8"),"ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			
			bis = new BufferedInputStream(new FileInputStream(warfile));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while(-1!=(bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bis!=null)
					bis.close();
				if(bos!=null) 
					bos.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	private File searchFile(final File folder, final String keyWord) {
		File[] findFolders = folder.listFiles(new FilenameFilter() {// 运用内部匿名类获得文件
			@Override
			public boolean accept(File dir, String name) {
				logger.info("-----filename: "  + name);
				// 目录或文件包含关键字
				boolean flag = name.toLowerCase().contains(keyWord.toLowerCase());
				if(flag)
					return true;
				else
					return false;
			}

        });
		return findFolders[0];
	}
	
	
	private boolean copyWar(String projectCode, String profile) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile);
		String clientIp = clientConfig.getRemoteIp();
		
		BaseShellCommand command = new BaseShellCommand();
		
		String tomcatHome = "";
		
		if("TEST".equals(profile)) {
			clientIp = clientIp;
			tomcatHome = "/app/tomcat";
		} else if("QUASIPRODUCT".equals(profile)) {
			clientIp = QUASIPRODUCT_WAR_IP;
			tomcatHome = "/app/tomcat";
		} else if("PRODUCT".equals(profile)) {
			clientIp = PRODUCT_WAR_IP;
			tomcatHome = "/app/tomcat";
		}
		
		if("godzilla".equals(projectCode)) {
			clientIp = "10.100.142.65";
			tomcatHome = "/home/godzilla/tomcat-godzilla";
		} 
		tomcatHome += "/webapps/*.war";
		String str = "sh /home/godzilla/gzl/shell/server/copywar_server.sh " + clientIp + " " + tomcatHome + " " + SAVE_WAR_PATH;;
		boolean flag = false;
		if("godzilla".equals(projectCode)) {
			flag = true;
		} else {
			flag = command.execute(str, super.getUser().getUserName());
		}
		
		if(flag) {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, COPYWAR, SUCCESS, "copy war success");
			return true;
		} else {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, COPYWAR, FAILURE, "copy war failure");
			return false;
		}
		
	}
	
}
