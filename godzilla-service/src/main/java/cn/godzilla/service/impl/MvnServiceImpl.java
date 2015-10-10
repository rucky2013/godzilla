package cn.godzilla.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileFilter;

import org.apache.http.client.fluent.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.model.RpcResult;
import cn.godzilla.rpc.api.RpcException;
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
	@Autowired
	private BaseShellCommand command;
	
	private Map<String, PropConfigProviderService> propConfigProviderServices = 
				new HashMap<String, PropConfigProviderService>();
	private Map<String, MvnProviderService> mvnProviderServices = 
			new HashMap<String, MvnProviderService>();
	
	@Override
	public ReturnCodeEnum doDeploy(String srcUrl, String projectCode, String profile, String parentVersion) {
		
		/*
		 * -2.限制并发　发布
		 * 测试环境　每个项目　只允许　一个人发布（如果互相依赖项目　并发发布，还是会出现问题）
		 * 准生产	　所有项目只允许一个人发布
		 * 生产　　　所有项目只允许一个人发布
		 */
		Lock lock = PUBLIC_LOCK;
		boolean hasAC = false;
		try {
			if(profile.equals(TEST_PROFILE)) {
				lock = super.deploy_lock.get(projectCode);
				hasAC = lock.tryLock(5, TimeUnit.SECONDS);
				if(!hasAC) 
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} else {
				lock = super.deploy_lock.get(profile);
				hasAC = lock.tryLock(5, TimeUnit.SECONDS);
				if(!hasAC) 
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			}
			
		
		
		String sid = super.getSid();
		String pencentkey = sid + "-" + projectCode + "-" + profile;
		
		Project project = projectService.qureyByProCode(projectCode);
		
		String webPath = project.getWebPath();
		ClientConfig clientconfig = clientConfigService.queryDetail(projectCode, profile);
		String IP = clientconfig.getRemoteIp();
		/*
		 * -1.svn合并  只有测试环境需要branches 线上直接使用trunk版本
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
			return ReturnCodeEnum.getByReturnCode(NO_SVNMERGE);
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
		processPercent.put(pencentkey, "15");
		/*
		 * 1.替换pom文件 配置变量   
		 * 20150908 parentVersion 改为由 mvn命令更改
		 * percent 50%
		 */
		boolean flag1 = false;
		try {
			PropConfigProviderService propConfigProviderService = propConfigProviderServices.get(IP);
			
			RpcResult result = propConfigProviderService.propToPom(projectCode, srcUrl, webPath, profile, parentVersion, clientconfig);
			flag1 = result.getRpcCode().equals("0")?true:false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!flag1){
			return ReturnCodeEnum.getByReturnCode(NO_CHANGEPOM);
		}
		processPercent.put(pencentkey, "30");
		
		final String processKey1 = sid + "-" + projectCode + "-" + profile;
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.currentThread().sleep(1000);
						String processValue = GodzillaApplication.processPercent.get(processKey1);
						int prVal = Integer.parseInt(processValue);
						if(prVal>=75) break;
						GodzillaApplication.processPercent.put(processKey1, ++prVal+"");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t1.start();/**/
		/*
		 * 2.mvn deploy  3.将sh命令>queue
		 * percent 90%
		 */
		boolean flag2 = false;
		RpcResult result = null;
		if("godzilla".equals(projectCode)) {
			flag2 = true;
		} else {
			try {
				MvnProviderService mvnProviderService = mvnProviderServices.get(IP);
				String username = GodzillaApplication.getUser().getUserName();
				result = this.deployProject(mvnProviderService, username, srcUrl, projectCode, profile, IP, parentVersion);
				flag2 = result.getRpcCode().equals("0")?true:false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		t1.interrupt();
		processPercent.put(pencentkey, "85");
		if(flag1&&flag2) {
			
			/*
			 * 存在定义 问题
			 * end.部署成功  更新 部署版本号  
			 */
			//boolean flag3 = this.updateDeployVersion(projectCode, profile);
			
			/*
			 * 3. 测试环境 ：httpclient 访问  ip:8080/war_name/index   查找 是否存在 <!--<h5>godzilla</h5>--> 字符串 判断 tomcat是否启动成功
			 */
			String warName = project.getWarName();
			boolean flag4 = false;
			/*if(projectCode.equals("godzilla")) {
				flag4 = true;
			} else {
				if(TEST_PROFILE.equals(profile)) {
					flag4 = super.ifSuccessStartTomcat(IP, warName);
				} else {
					flag4 = true;
				}
			}*/
			// bug:tomcat hot deploy cannot success for netty-project
			//workaround: restart for test,project start success.
			if(TEST_PROFILE.equals(profile)) {
				flag4 = this.restartTomcat(projectCode, profile);
			} else {
				flag4 = true;
			}
			
			
			if(flag4) {
				return ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY);
			} else {
				return ReturnCodeEnum.getByReturnCode(NO_MVNDEPLOY);
			}
			
		} else if(!flag1) {
		} else if(!flag2) {
			if(result.getRpcMsg().equals(BUILDFAILURE)) {
				return ReturnCodeEnum.getByReturnCode(NO_MVNBUILD);
			} else if(result.getRpcMsg().equals(NOSETPROPS)){
				return ReturnCodeEnum.getByReturnCode(NO_MVNSETPROPS);
			}
			
		} 
		
		
		} catch(InterruptedException e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
		} finally {
			try {
				lock.unlock();
			} /*catch(InvocationTargetException e2) {
				return ReturnCodeEnum.getByReturnCode(NO_HASKEYDEPLOY);
			} */catch(IllegalMonitorStateException e1) {
				return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} 
			
		}
		return ReturnCodeEnum.getByReturnCode(NO_MVNDEPLOY);
	}
	
	
	public RpcResult deployProject(MvnProviderService mvnProviderService, String username, String srUrl, String projectCode, String profile, String IP, String parentVersion) {
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
			String str = "sh "+shell+" deploy "+POM_PATH+" "+USER_NAME+" "+PROJECT_NAME+" "+ PROJECT_ENV +" " +parentVersion;
			
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
		} else if(NOSETPROPS.equals(result.getRpcMsg())) {
			mvnCmdLogService.addMvnCmdLog(username, projectCode, profile, commands, "mvn deploy 执行失败, 含有${XX}未设置配置项");
		} else {
			mvnCmdLogService.addMvnCmdLog(username, projectCode, profile, commands, "mvn deploy 执行失败");
		}
		return result;
	}
	
	private boolean updateDeployVersion(String projectCode, String profile) {
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		
		/*ReturnCodeEnum versionreturn = svnService.getVersion(trunkPath, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return false;
		}*/
		
		String deployVersion = svnVersionThreadLocal.get();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("deploy_version", deployVersion);
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);
		
		int update = clientConfigService.updateDeployVersionByCodeAndProfile(parameterMap);
		
		return update>0;
	}

	private void initRpc(String linuxIp) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
		
		while(true) {
			try {
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
				break;
			} catch(RpcException e) {
				continue;
			}
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
		
		Project project = projectService.qureyByProCode(projectCode);
		String warName= project.getWarName();
		//1.5 获取 war包 文件名
		File warfile = this.searchFile(new File(ctxPath), warName);
		
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
		Project project = projectService.qureyByProCode(projectCode);
		
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
		flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
		
		if(flag) {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, COPYWAR, SUCCESS, "copy war success");
			return true;
		} else {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, COPYWAR, FAILURE, "copy war failure");
			return false;
		}
		
	}
	
	public boolean restartTomcat(String projectCode, String profile) {
		boolean flag;
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		// 暂时先不考虑权限
		Project project = projectService.qureyByProCode(projectCode);

		/*String str = PropertiesUtil.getProperties().get("server.shell.restart.path") +" " + clientIp + " "
				+ PropertiesUtil.getProperties().get("client.tomcat.home.path");*/
		String tomcatHome = "";
		if("godzilla".equals(projectCode)) {
			clientIp = "10.100.142.65";
			tomcatHome = "/home/godzilla/tomcat-godzilla";
		} else {
			tomcatHome = "/app/tomcat";
		}
		String str = "sh /home/godzilla/gzl/shell/server/restart_server.sh " + clientIp + " " + tomcatHome + " " + project.getWarName();
		
		if("godzilla".equals(projectCode)) {
			flag = true;
		} else {
			flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
		}
		
		/*
		 * 3. httpclient 访问  ip:8080/war_name/index.jsp   查找 是否存在 <!--<h5>godzilla</h5>--> 字符串 判断 tomcat是否启动成功
		 */
		String warName = project.getWarName();
		String IP = clientIp;
		
		
		if(projectCode.equals("godzilla")) {
			flag = flag&&true;
		} else {
			if(TEST_PROFILE.equals("TEST")) {
				flag = flag&&super.ifSuccessStartTomcat(IP, warName);
			} else {
				flag = flag&&true;
			}
			
		}
		return flag;
	}
	
}
