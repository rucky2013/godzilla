package cn.godzilla.service.impl;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.command.DefaultShellCommand;
import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.dao.ProjectMapper;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.OperateLog;
import cn.godzilla.model.Project;
import cn.godzilla.model.RpcResult;
import cn.godzilla.mvn.MvnBaseCommand;
import cn.godzilla.mvn.ShCommand;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.SvnService;
import cn.godzilla.svn.BaseShellCommand;
import cn.godzilla.util.GodzillaServiceApplication;

import com.rpcf.api.RpcException;

//propConfigService svnService projectService clientConfigService
public class MvnServiceImpl extends GodzillaServiceApplication implements MvnService {
	
	private final Logger logger = LogManager.getLogger(MvnServiceImpl.class);
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SvnService svnService;
	@Autowired
	private PropConfigService propConfigService;
	
	@Override
	public ReturnCodeEnum doDeploy(String projectCode, String profile, String parentVersion, String pencentkey) {
		
		processPercent.put(pencentkey, "0");
		
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		
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
		processPercent.put(pencentkey, "15");
		/*
		 * 1.替换pom文件 配置变量   
		 * 20150908 parentVersion 改为由 mvn命令更改
		 * percent 50%
		 */
		boolean flag1 = false;
		try {
			RpcResult result = propConfigService.propToPom(projectCode, webPath, profile, parentVersion, clientconfig);
			flag1 = result.getRpcCode().equals("0")?true:false;
		} catch(RpcException e){
			return ReturnCodeEnum.getByReturnCode(NO_RPCEX);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!flag1){
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
						String processValue = GodzillaServiceApplication.processPercent.get(processKey1);
						int prVal = Integer.parseInt(processValue);
						if(prVal>=75) break;
						GodzillaServiceApplication.processPercent.put(processKey1, ++prVal+"");
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
				String username = GodzillaServiceApplication.getUser().getUserName();
				result = this.deployProject(this, username, webPath, projectCode, profile, IP, parentVersion, parentPomPath, super.getUser().getRealName());
				flag2 = result.getRpcCode().equals("0")?true:false;
			} catch(RpcException e){
				return ReturnCodeEnum.getByReturnCode(NO_RPCEX);
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		t1.interrupt();
		processPercent.put(pencentkey, "85");
		int logid = result.getLogid();
		if(0>=logid){
			return ReturnCodeEnum.getByReturnCode(NO_MVNBUILDLOG);
		} else if(!flag2) {
			//20151030 暂时不用此检查
			/*if(result.getRpcMsg().equals(NOSETPROPS)) 
				return ReturnCodeEnum.getByReturnCode(NO_MVNSETPROPS);*/
			return ReturnCodeEnum.getByReturnCode(NO_MVNBUILD).setData(logid);
		}
		
		/*
		 * 3. 日常环境 ：httpclient 访问  ip:8080/war_name/index   判断200 tomcat是否启动成功
		 */
		String warName = project.getWarName();
		boolean flag4 = false;
		// bug:tomcat hot deploy cannot success for netty-project
		//workaround: restart for test,project start success.
		if(TEST_PROFILE.equals(profile)) {
			flag4 = this.restartTomcat(projectCode, profile).equals(ReturnCodeEnum.getByReturnCode(OK_STARTTOMCAT));
		} else {
			flag4 = true;
		}
		//setData为给 统一日志 传输 更新日志ID，不传送给前台信息
		
		
		if(flag4) {
			processPercent.put(pencentkey, "100");
			final String pencentkeyF = pencentkey;
			new Thread() {
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					processPercent.put(pencentkeyF, "0");
				}
			}.start();
			return ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY).setData(logid);
		} else {
			processPercent.put(pencentkey, "0");
			return ReturnCodeEnum.getByReturnCode(NO_RESTARTTOMCAT).setData(logid);
		}
	}
	
	 public ReturnCodeEnum restartTomcat(String projectCode, String profile) {
		boolean flag =false;
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		// 暂时先不考虑权限
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);

		/*String str = PropertiesUtil.getProperties().get("server.shell.restart.path") +" " + clientIp + " "
				+ PropertiesUtil.getProperties().get("client.tomcat.home.path");*/
		String tomcatHome = "/app/tomcat";
		String str = "sh /home/godzilla/gzl/shell/server/restart_server.sh " + clientIp + " " + tomcatHome + " " + project.getWarName();
		
		DefaultShellCommand command = new DefaultShellCommand();
		
		if("godzilla".equals(projectCode)) {
			flag = true;
		} else {
			flag = command.execute(str + " " + project.getSvnUsername()+ " " + project.getSvnPassword() ,);
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
		
		if(flag) {
			return ReturnCodeEnum.getByReturnCode(OK_STARTTOMCAT);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_STARTTOMCAT);
		}
	}
	
	
	public RpcResult deployProject(MvnService mvnService, String username, String webPath, String projectCode, String profile, String IP, String parentVersion, String parentPomPath, String realname) {
		boolean flag2 = false;
		RpcResult result = null;
		String commands = "";
		try {
			String POM_PATH = webPath + "/pom.xml";
			String PARENTPOM_PATH = parentPomPath + "/pom.xml";
			String USER_NAME = username;
			String PROJECT_NAME = projectCode;
			
			
			String shell = SHELL_CLIENT_PATH.endsWith("/")
							?(SHELL_CLIENT_PATH+ "godzilla_mvn.sh")
									:(SHELL_CLIENT_PATH+"/" +"godzilla_mvn.sh");
			String str = "sh "+shell+" deploy "+POM_PATH+" "+USER_NAME+" "+PROJECT_NAME+" "+ PROJECT_ENV +" " +parentVersion + " " + PARENTPOM_PATH;
			
			result = mvnService.mvnDeploy(str, projectCode, PROJECT_ENV, USER_NAME, realname, profile);
			commands = str;
			flag2 = result.getRpcCode().equals("0")?true:false;
		}  catch(RpcException e){
			throw new RpcException(e);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return RpcResult.create(FAILURE, 0);
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
	
	@Override
	public ReturnCodeEnum getProcessPercent(String projectCode, String profile, String pencentkey) {
		
		String processPercent = GodzillaServiceApplication.processPercent.get(pencentkey)==null?"0":GodzillaServiceApplication.processPercent.get(pencentkey);
		
		if(processPercent.equals("100")) {
			GodzillaServiceApplication.processPercent.put(pencentkey, "0");
		}
		
		return ReturnCodeEnum.getByReturnCode(OK_QUERYPERCENT).setData(processPercent);
	}
	
	@Override
	public ReturnCodeEnum showdeployLog(String projectCode, String profile, String logid, HttpServletResponse response) {
		if(StringUtil.isEmpty(logid) || logid.equals("0")) {
			return ReturnCodeEnum.getByReturnCode(NO_DEPLOYLOGID);//id错误
		} 
		
		OperateLog log =  operateLogService.queryLogById(projectCode, profile, logid);
		if(StringUtil.isEmpty(log.getDeployLog())) 
			return ReturnCodeEnum.getByReturnCode(NO_STOREDEPLOYLOG);//日志记录失败
		//setData为给前台显示后台信息，而不输出日志
		return ReturnCodeEnum.getByReturnCode(OK_SHOWDEPLOYLOG).setData(log.getDeployLog());
	}
	@Override
	public ReturnCodeEnum showwarInfo(String projectCode, String profile, String logid, HttpServletResponse response) {
		if(StringUtil.isEmpty(logid) || logid.equals("0")) {
			return ReturnCodeEnum.getByReturnCode(NO_SHOWWARINFOID);//id错误
		} 
		
		OperateLog log =  operateLogService.queryLogById(projectCode, profile, logid);
		if(StringUtil.isEmpty(log.getDeployLog())) 
			return ReturnCodeEnum.getByReturnCode(NO_SHOWWARINFO);//日志记录失败
		//setData为给前台显示后台信息，而不输出日志
		return ReturnCodeEnum.getByReturnCode(OK_SHOWWARINFO).setData(log.getWarInfo());
	}
	
	//=======================
	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private ProjectMapper projectMapper;
	@Override
	public RpcResult mvnDeploy(String projectCode, String profile, String str1, String projectEnv, String username, String realname) {
		mvnBuildThreadLocal.set("FAILURE");
		mvnERRORThreadLocal.set("SUCCESS");
		
		MvnBaseCommand command1 = new MvnBaseCommand();
		String mvnlog = command1.execute(str1, projectCode, PROJECT_ENV, username);
		logger.info("mvnBuildThreadLocal:::"	+ mvnBuildThreadLocal.get());
		logger.info("mvnERRORThreadLocal:::"	+ mvnERRORThreadLocal.get());
		
		//20151030 暂时不用此检查//判断　是否含有${XX}未设置配置项 
		boolean flag4 = shellReturnThreadLocal.get().equals("4")?true:false;
		/*if(flag4) {
			return RpcResult.create(NOSETPROPS); 
		}*/
		boolean flag2 = mvnBuildThreadLocal.get().equals(SUCCESS)?true:false;
		boolean flag3 = mvnERRORThreadLocal.get().equals(SUCCESS)?true:false;
		//保存项目lib jar信息列表
		String jarlog = "";
		if(flag2&&flag3){
			Project project = projectMapper.qureyByProCode(projectCode);
			String libpath = project.getLibPath();
			String str2 = "/home/godzilla/gzl/shell/client/godzilla_mvn.sh showlib " + libpath;
			ShCommand command2 = new ShCommand();
			jarlog = command2.execute(str2);
			
		}
		//保存mvn部署日志
		int logid = operateLogService.addOperateLog(projectCode, profile, mvnlog, jarlog);
		
		mvnBuildThreadLocal.set("FAILURE");
		mvnERRORThreadLocal.set("SUCCESS");
		if(flag2&&flag3) {
			return RpcResult.create(SUCCESS, logid);
		} else {
			//build failure
			return RpcResult.create(BUILDFAILURE, logid);
		} 
	}
	
}
