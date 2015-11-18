package cn.godzilla.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.OperateLog;
import cn.godzilla.model.Project;
import cn.godzilla.model.RpcResult;
import cn.godzilla.rpc.api.RpcException;
import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.main.Util;
import cn.godzilla.service.ClientConfigService;
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
	private BaseShellCommand command;
	
	private Map<String, PropConfigProviderService> propConfigProviderServices = 
				new HashMap<String, PropConfigProviderService>();
	private Map<String, MvnProviderService> mvnProviderServices = 
			new HashMap<String, MvnProviderService>();
	
	@Override
	public ReturnCodeEnum doDeploy(String projectCode, String profile, String parentVersion, String pencentkey) {
		/*
		 * -2.限制并发　发布
		 * 日常环境　每个项目　只允许　一个人发布（如果互相依赖项目　并发发布，还是会出现问题）
		 * 准生产	　所有项目只允许一个人发布
		 * 生产　　　所有项目只允许一个人发布
		 */
		Lock lock = PUBLIC_LOCK;
		boolean hasAC = false;
		try {
			if(profile.equals(TEST_PROFILE)) {
				lock = GodzillaApplication.deploy_lock.get(projectCode);
				hasAC = lock.tryLock(1, TimeUnit.SECONDS);
				if(!hasAC) 
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} else {
				lock = GodzillaApplication.deploy_lock.get(profile);
				hasAC = lock.tryLock(1, TimeUnit.SECONDS);
				if(!hasAC) 
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			}
			
			ReturnCodeEnum deployReturn = this.doDeploy1(projectCode, profile, parentVersion, pencentkey);
			
			if(deployReturn.equals(ReturnCodeEnum.OK_MVNDEPLOY)) {
				GodzillaApplication.processPercent.put(pencentkey, "100");
			} else {
				GodzillaApplication.processPercent.put(pencentkey, "0");
			}
			final String pencentkeyF = pencentkey;
			new Thread() {
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					GodzillaApplication.processPercent.put(pencentkeyF, "0");
				}
			}.start();
			
			return deployReturn;
		} catch(InterruptedException e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_INTERRUPTEDEX);
		} catch(BusinessException e){
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_SYSTEMEX).setSystemEXMsg(e.getErrorMsg());
		} catch(Throwable e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_SYSTEMEX).setSystemEXMsg(e.getMessage());
		} finally {
			try {
				lock.unlock();
			} /*catch(InvocationTargetException e2) {
				return ReturnCodeEnum.getByReturnCode(NO_HASKEYDEPLOY);
			} */
			catch(IllegalMonitorStateException e1) {
				return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} 
			
		}
	}
	public ReturnCodeEnum doDeploy1(String projectCode, String profile, String parentVersion, String pencentkey) {
		
		String sid = super.getSid();
		
		Project project = projectService.queryByProCode(projectCode);
		
		String webPath = project.getWebPath();
		String parentPomPath = project.getCheckoutPath();
		ClientConfig clientconfig = clientConfigService.queryDetail(projectCode, profile);
		String IP = clientconfig.getRemoteIp();
		/*
		 * -1.svn合并  只有日常环境需要branches 线上直接使用trunk版本
		 * depresed-1.svn合并提交主干  
		 *  不应该提交  20150820
		 * if svn commit success or svn no changecommit
		 * 		continue
		 * else 
		 * 		break
		 */
		ReturnCodeEnum renum1 = svnService.svnMerge(projectCode, profile);
		
		if(renum1.equals(ReturnCodeEnum.getByReturnCode(OK_SVNMERGE))||renum1.equals(ReturnCodeEnum.getByReturnCode(NO_HAVEBRANCHES))){
		} else {
			return renum1;
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
		RpcResult result1 = null;
		try {
			PropConfigProviderService propConfigProviderService = propConfigProviderServices.get(IP);
			
			result1 = propConfigProviderService.propToPom(projectCode, webPath, profile, parentVersion, clientconfig);
			flag1 = result1.getRpcCode().equals("0")?true:false;
		} catch(RpcException e){
			return ReturnCodeEnum.getByReturnCode(NO_RPCEX);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!flag1){
			if(result1!=null &&result1.getRpcCode().equals(RpcResult.LOOSEPROP)){
				return ReturnCodeEnum.getByReturnCode(NO_LOOSEPROP).setData(result1.getData());
			}
			return ReturnCodeEnum.getByReturnCode(NO_CHANGEPOM);
		}
		processPercent.put(pencentkey, "30");
		
		final String processKey1 = pencentkey;
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
		RpcResult result2 = null;
		if("godzilla".equals(projectCode)) {
			flag2 = true;
		} else {
			try {
				MvnProviderService mvnProviderService = mvnProviderServices.get(IP);
				String username = GodzillaApplication.getUser().getUserName();
				result2 = this.deployProject(mvnProviderService, username, webPath, projectCode, profile, IP, parentVersion, parentPomPath, super.getUser().getRealName());
				flag2 = result2.getRpcCode().equals("0")?true:false;
			} catch(RpcException e){
				return ReturnCodeEnum.getByReturnCode(NO_RPCEX);
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		t1.interrupt();
		processPercent.put(pencentkey, "85");
		Long logid = result2.getLogid();
		if(0>=logid){
			return ReturnCodeEnum.getByReturnCode(NO_MVNBUILDLOG);
		} else if(!flag2) {
			//20151030 暂时不用此检查
			/*if(result.getRpcMsg().equals(NOSETPROPS)) 
				return ReturnCodeEnum.getByReturnCode(NO_MVNSETPROPS);*/
			return ReturnCodeEnum.getByReturnCode(NO_MVNBUILD).setData(logid);
		}
		
		/*
		 * 存在定义 问题
		 * end.部署成功  更新 部署版本号  
		 */
		//boolean flag3 = this.updateDeployVersion(projectCode, profile);
		
		/*
		 * 3. 日常环境 ：httpclient 访问  ip:8080/war_name/index   查找 是否存在 <!--<h5>godzilla</h5>--> 字符串 判断 tomcat是否启动成功
		 */
		boolean flag4 = false;
		// bug:tomcat hot deploy cannot success for netty-project
		//workaround: restart for test,project start success.
		if(TEST_PROFILE.equals(profile)) {
			flag4 = this.restartTomcat(projectCode, profile, logid).equals(ReturnCodeEnum.getByReturnCode(OK_STARTTOMCAT));
		} else {
			flag4 = true;
		}
		
		if(flag4) {
			return ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY).setData(logid);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_RESTARTTOMCAT).setData(logid);
		}
	}
	
	
	public RpcResult deployProject(MvnProviderService mvnProviderService, String username, String webPath, String projectCode, String profile, String IP, String parentVersion, String parentPomPath, String realname) {
		boolean flag2 = false;
		RpcResult result = null;
		try {
			String POM_PATH = webPath + "/pom.xml";
			String PARENTPOM_PATH = parentPomPath + "/pom.xml";
			String USER_NAME = username;
			String PROJECT_NAME = projectCode;
			
			
			String shell = SHELL_CLIENT_PATH.endsWith("/")
							?(SHELL_CLIENT_PATH+ "godzilla_mvn.sh")
									:(SHELL_CLIENT_PATH+"/" +"godzilla_mvn.sh");
			String str = "sh "+shell+" deploy "+POM_PATH+" "+USER_NAME+" "+PROJECT_NAME+" "+ PROJECT_ENV +" " +parentVersion + " " + PARENTPOM_PATH;
			
			result = mvnProviderService.mvnDeploy(str, projectCode, PROJECT_ENV, USER_NAME, realname, profile);
			flag2 = result.getRpcCode().equals("0")?true:false;
		}  catch(RpcException e){
			throw new RpcException(e);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return RpcResult.create(FAILURE, 0L);
		}
		if(flag2) {
			//mvn deploy 执行成功
		} else if(NOSETPROPS.equals(result.getRpcMsg())) {
			//mvn deploy 执行失败, 含有${XX}未设置配置项
		} else {
			//mvn deploy 执行失败
		}
		return result;
	}
	
	private void initRpc(String linuxIp) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
		RpcFactory rpcFactory1= null;
		rpcFactory1 = Util.getRpcFactoryImpl();
		
		if(propConfigProviderServices.get(linuxIp)==null||"".equals(propConfigProviderServices.get(linuxIp))) {
			
			PropConfigProviderService propConfigProviderService = rpcFactory1.getReference(PropConfigProviderService.class, linuxIp);
			if(propConfigProviderService!=null) {
				propConfigProviderServices.put(linuxIp, propConfigProviderService);
			}
		}
		if(mvnProviderServices.get(linuxIp)==null||"".equals(mvnProviderServices.get(linuxIp))) {
			MvnProviderService mvnProviderService = rpcFactory1.getReference(MvnProviderService.class, linuxIp);
			if(mvnProviderService!=null) {
				mvnProviderServices.put(linuxIp, mvnProviderService);
			}
		}
	}

	@Override
	public ReturnCodeEnum getProcessPercent(String pencentkey) {
		
		String processPercent = GodzillaApplication.processPercent.get(pencentkey)==null?"0":GodzillaApplication.processPercent.get(pencentkey);
		
		if(processPercent.equals("100")) {
			GodzillaApplication.processPercent.put(pencentkey, "0");
		}
		
		return ReturnCodeEnum.getByReturnCode(OK_QUERYPERCENT).setData(processPercent);
	}
	
	@Override
	public ReturnCodeEnum showdeployLog(HttpServletResponse response, String projectCode, String profile, Long logid) {
		if(logid<=0) {
			return ReturnCodeEnum.getByReturnCode(NO_DEPLOYLOGID);//id错误
		} 
		
		OperateLog log =  operateLogService.queryLogById(logid);
		if(StringUtil.isEmpty(log.getDeployLog())&&StringUtil.isEmpty(log.getCatalinaLog())) 
			return ReturnCodeEnum.getByReturnCode(NO_STOREDEPLOYLOG);//日志记录失败  20151118 添加catalina日志
		//setData为给前台显示后台信息，而不输出日志
		return ReturnCodeEnum.getByReturnCode(OK_SHOWDEPLOYLOG).setData(log.getDeployLog()+log.getCatalinaLog());
	}
	@Override
	public ReturnCodeEnum showwarInfo(HttpServletResponse response, String projectCode, String profile, Long logid) {
		if(logid<=0) {
			return ReturnCodeEnum.getByReturnCode(NO_SHOWWARINFOID);//id错误
		} 
		
		OperateLog log =  operateLogService.queryLogById(logid);
		if(StringUtil.isEmpty(log.getDeployLog())) 
			return ReturnCodeEnum.getByReturnCode(NO_SHOWWARINFO);//日志记录失败
		//setData为给前台显示后台信息，而不输出日志
		return ReturnCodeEnum.getByReturnCode(OK_SHOWWARINFO).setData(log.getWarInfo());
	}
	
	@Override
	public ReturnCodeEnum downLoadWar(HttpServletResponse response, String projectCode, String profile) {
		/**
		 * 1.限制并发　待定
		 * 日常环境  待定
		 * 准生产	待定
		 * 生产　 待定
		 **/
		return this.downLoadWar1(response, projectCode, profile);
		
	}
	
	private ReturnCodeEnum downLoadWar1(HttpServletResponse response, String projectCode, String profile) {
			
		java.io.BufferedOutputStream bos = null;
		java.io.BufferedInputStream bis = null;
		String ctxPath = SAVE_WAR_PATH ;
		//String downLoadPath = ctxPath + "/" + projectCode + ".war";
		//logger.info("****downloadpath : "+ downLoadPath);
		//1.ssh scp 到本地
		this.copyWar(projectCode, profile);
		
		Project project = projectService.queryByProCode(projectCode);
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
		Project project = projectService.queryByProCode(projectCode);
		
		String tomcatHome = "";
		
		if("TEST".equals(profile)) {
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
		String str = "sh /home/godzilla/gzl/shell/server/copywar_server.sh " + clientIp + " " + tomcatHome + " " + SAVE_WAR_PATH;
		boolean flag = false;
		flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
		
		if(flag) {
			return true;
		} else {
			return false;
		}
		
	}
	@Override
	public ReturnCodeEnum restartTomcat(String projectCode, String profile, Long logid) {
		
		/**
		 * 1.限制并发　
		 * 日常环境　每个项目　只允许　一个人重启（如果互相依赖项目　并发发布，还是会出现问题）
		 * 准生产	　NO_RESTARTEFFECT
		 * 生产　　　NO_RESTARTEFFECT
		 **/
		
		Lock lock = PUBLIC_LOCK;
		boolean hasAC = false;
		try {
			if(TEST_PROFILE.equals(profile)) {
				lock = GodzillaApplication.deploy_lock.get(projectCode);
				hasAC = lock.tryLock(1, TimeUnit.SECONDS);
				if(!hasAC)
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} else {
				return ReturnCodeEnum.getByReturnCode(NO_RESTARTEFFECT);
			}
			return this.restartTomcat1(projectCode, profile, logid);
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
	}
	private ReturnCodeEnum restartTomcat1(String projectCode, String profile, Long logid) {
		boolean flag =false;
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		// 暂时先不考虑权限
		Project project = projectService.queryByProCode(projectCode);

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
		final String IP = clientIp;
		
		
		//tail -500 catalina.out > db
		//setData为给 统一日志 传输 更新日志ID，不传送给前台信息
		final BaseShellCommand catalinaCommand = new BaseShellCommand();
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				catalinaCommand.tailLogUtilNotify("sh /home/godzilla/gzl/shell/server/catalinalog_server.sh " + IP);
			}
		}).start();
		
		if(projectCode.equals("godzilla")) {
			flag = flag&&true;
		} else {
			if(TEST_PROFILE.equals("TEST")) {
				flag = flag&&super.ifSuccessStartTomcat(IP, warName);
			} else {
				flag = flag&&true;
			}
		}
		catalinaCommand.lock.lock();
		catalinaCommand.done.signal();
		catalinaCommand.lock.unlock();
		if(logid == 0) {
			//restart button
			logid = operateLogService.addOperateLog(catalinaCommand.catalinaLog);
		} else {
			//deploy button
			operateLogService.updateOperateLog(catalinaCommand.catalinaLog, logid);
		}
		
		if(flag) {
			return ReturnCodeEnum.getByReturnCode(OK_STARTTOMCAT).setData(logid);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_STARTTOMCAT).setData(logid);
		}
	}
}
