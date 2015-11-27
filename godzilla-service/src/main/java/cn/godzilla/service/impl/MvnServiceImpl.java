package cn.godzilla.service.impl;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.command.Command;
import cn.godzilla.command.CommandEnum;
import cn.godzilla.command.DefaultShellCommand;
import cn.godzilla.command.TailShellCommand;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.OperateLog;
import cn.godzilla.model.Project;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.SvnService;
import cn.godzilla.util.GodzillaServiceApplication;

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
	@Autowired
	private OperateLogService operateLogService;
	
	private volatile boolean isStop = false;
	
	@Override
	public ReturnCodeEnum doDeploy(String projectCode, String profile, String parentVersion, String pencentkey) {
		isStop = false;
		processPercent.put(pencentkey, "10");

		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		String webPath = project.getWebPath();
		String parentPomPath = project.getCheckoutPath();
		ClientConfig clientconfig = clientConfigService.queryDetail(projectCode, profile);
		String IP = clientconfig.getRemoteIp();
		/*
		 * 0.svn合并 只有日常环境需要branches 线上直接使用trunk版本 depresed-1.svn合并提交主干 不应该提交
		 * 20150820 if svn commit success or svn no changecommit continue else
		 * break
		 */
		//merge-branches only when profile is TEST, the others just checkout trunk
		ReturnCodeEnum svnMergeResult = svnService.svnMerge(projectCode, profile);
		if (ReturnCodeEnum.getByReturnCode(NO_SVNRESOLVED).equals(svnMergeResult) 
				|| ReturnCodeEnum.getByReturnCode(NO_SVNCOMMIT).equals(svnMergeResult)
				|| ReturnCodeEnum.getByReturnCode(NO_FOUNDCONFLICT).equals(svnMergeResult)
				|| ReturnCodeEnum.getByReturnCode(NO_CLIENTPARAM).equals(svnMergeResult)
				|| ReturnCodeEnum.getByReturnCode(NO_SERVERPARAM).equals(svnMergeResult)
				|| ReturnCodeEnum.getByReturnCode(NO_NEWCONFLICTFOUND).equals(svnMergeResult)
				|| ReturnCodeEnum.getByReturnCode(NO_CHANGECOMMIT).equals(svnMergeResult)
				|| ReturnCodeEnum.getByReturnCode(NO_JAVASHELLCALL).equals(svnMergeResult)) {
			return svnMergeResult;
		}
		processPercent.put(pencentkey, "11");
		/*
		 * 1.替换pom文件 配置变量 20150908 parentVersion 改为由 mvn命令更改 percent 30%
		 */
		ReturnCodeEnum changePomResult = propConfigService.propToPom(projectCode, webPath, profile, parentVersion, clientconfig);
		if (ReturnCodeEnum.getByReturnCode(NO_CHANGEPOM).equals(changePomResult)||ReturnCodeEnum.getByReturnCode(NO_LOOSEPROP).equals(changePomResult)) {
			return changePomResult;
		}
		processPercent.put(pencentkey, "15");

		increaseProcessPercent(pencentkey);
		/*
		 * 2.mvn deploy 3.将sh命令>queue percent 85%
		 */
		String username = GodzillaServiceApplication.getUser().getUserName();
		ReturnCodeEnum mvnBuildResult = deployProject(username, webPath, projectCode, profile, IP, parentVersion, parentPomPath, super.getUser().getRealName());

		if(ReturnCodeEnum.getByReturnCode(NO_MVNBUILD).equals(mvnBuildResult)||ReturnCodeEnum.getByReturnCode(NO_JAVASHELLCALL).equals(mvnBuildResult)) {
			//保存mvn部署日志
			String deployLog = deployLogThreadLocal.get();
			Long logid = operateLogService.addOperateLog(projectCode, profile, deployLog, "");
			return mvnBuildResult.setData(logid);
		}
		processPercent.put(pencentkey, "85");
		
		/*
		 * 3. 日常环境 ：httpclient 访问 ip:8080/war_name/index 判断200 tomcat是否启动成功
		 */
		if (TEST_PROFILE.equals(profile)) {
			//保存项目lib jar信息列表
			String LIBPATH = project.getLibPath();
			ReturnCodeEnum restartTomcatResult = restartTomcat(projectCode, profile, LIBPATH);
			signalforstop();
			if (ReturnCodeEnum.getByReturnCode(NO_STARTTOMCAT).equals(restartTomcatResult) || ReturnCodeEnum.getByReturnCode(NO_JAVASHELLCALL).equals(restartTomcatResult)) {
				processPercent.put(pencentkey, "0");
				return restartTomcatResult;
			}
			processPercent.put(pencentkey, "100");
			waitSecondAndSetPencentZero(pencentkey);
			//logid 传递
			return ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY).setData(restartTomcatResult.getData());
		} else {
			//no need to start tomcat
			//保存项目lib jar信息列表
			String LIBPATH = project.getLibPath();
			ReturnCodeEnum restartTomcatResult = restartTomcat(projectCode, profile, LIBPATH);
			signalforstop();
			processPercent.put(pencentkey, "100");
			waitSecondAndSetPencentZero(pencentkey);
			return ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY).setData(restartTomcatResult.getData());
		}
	}
	
	//部署成功后，前面请求 pencent 发现其为100时 更改为0 ，或者通过 此线程 等待 100 4秒后变更为0
	private void waitSecondAndSetPencentZero(final String pencentkey) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					//继续往下走，更改processPercent为0
				}
				String processValue = GodzillaServiceApplication.processPercent.get(pencentkey);
				int prVal = Integer.parseInt(processValue);
				if(prVal >= 99) {
					processPercent.put(pencentkey, "0");
				}
			}
		}.start();
	}

	private ReturnCodeEnum deployProject(String username, String webPath, String projectCode, String profile, String IP, String PARENT_VERSION, String PARENTPOM_PATH, String realname) {
		String POM_PATH = webPath + FILENAME_POM;
		String PROJECT_NAME = projectCode;

		String commandStr = SH_MVN_CLIENT + BLACKSPACE + COM_DEPLOY + BLACKSPACE + POM_PATH + BLACKSPACE + PROJECT_NAME + BLACKSPACE + PROJECT_ENV + BLACKSPACE + PARENT_VERSION + BLACKSPACE + PARENTPOM_PATH;
		DefaultShellCommand shellCommand = new DefaultShellCommand();
		shellCommand.execute(commandStr, CommandEnum.MVN);

		boolean ok_mvnbuild = SUCCESS.equals(mvnBuildThreadLocal.get()) ? true : false;
		boolean ok_shell = OK_SHELL.equals(shellReturnThreadLocal.get()) ? true : false;

		if (!ok_mvnbuild) {
			return ReturnCodeEnum.getByReturnCode(NO_MVNBUILD);
		} else if (!ok_shell) {
			logger.error(">>>shell执行失败："+commandStr);
			return ReturnCodeEnum.getByReturnCode(NO_JAVASHELLCALL);
		}
		return ReturnCodeEnum.getByReturnCode(OK_MVNBUILD);
	}
	
	private void signalforstop() {
		isStop = true;
	}
	
	private void increaseProcessPercent(final String pencentkey) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isStop) {
					try {
						Thread.sleep(1000);
						if(isStop) {
							signalforstop();
							break;
						}
						String processValue = GodzillaServiceApplication.processPercent.get(pencentkey);
						int prVal = Integer.parseInt(processValue);
						if (prVal >= 85) {
							Thread.sleep(3000);
						}
						if (prVal >= 98) {
							signalforstop();
							break;
						}
						GodzillaServiceApplication.processPercent.put(pencentkey, ++prVal + "");
					} catch (InterruptedException e) {
						logger.error("increaseProcessPercent-thread is interrupted!");
						signalforstop();
						break;
					}
				}
			}
		}).start();
	}
	

	public ReturnCodeEnum restartTomcat(String projectCode, String profile, String LIBPATH) {
		
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		
		String jarlog = "";
		if(TEST_PROFILE.equals(profile)) {
			String commandStr1 = SH_MVN_CLIENT + BLACKSPACE + COM_SHOWLIB + BLACKSPACE + LIBPATH;
			Command shellCommand = new DefaultShellCommand();
			shellCommand.execute(commandStr1, CommandEnum.LSJAR);
			boolean ok_shell = OK_SHELL.equals(shellReturnThreadLocal.get()) ? true : false;
			if(!ok_shell) {
				logger.error(">>>shell执行失败："+commandStr1);
				return ReturnCodeEnum.getByReturnCode(NO_JAVASHELLCALL);
			}
			jarlog = jarlogThreadLocal.get();
		}
		
		//保存mvn部署日志
		String deployLog = deployLogThreadLocal.get();
		Long logid = operateLogService.addOperateLog(projectCode, profile, deployLog, jarlog);
		
		if(!TEST_PROFILE.equals(profile)) return ReturnCodeEnum.getByReturnCode(OK_STARTTOMCAT).setData(logid); 
		
		//httpclient 访问  ip:8080/war_name/index.jsp   查找 是否存在 <!--<h5>godzilla</h5>--> 字符串 判断 tomcat是否启动成功
		String warName = project.getWarName();
		String IP = clientIp;
		
		String WAR_NAME = project.getWarName();
		
		TailShellCommand shCommand = restartAndTailLog(WAR_NAME);
		
		boolean ok_starttomcat = ifSuccessStartTomcat(IP, warName);
		//暂停存储 catalina 日志
		shCommand.signal();
		//保存catalina日志到DB
		String catalinaLog = shCommand.catalinaLog;
		String shellReturn = shCommand.shellReturn;
		
		if(StringUtil.isEmpty(catalinaLog)) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
		
		catalinaLog = shCommand.catalinaLog;
		shellReturn = shCommand.shellReturn;
		
		operateLogService.updateOperateLog(SERVER_USER, TEST_PROFILE, catalinaLog, logid);
		if(!ok_starttomcat) {
			return ReturnCodeEnum.getByReturnCode(NO_STARTTOMCAT).setData(logid);
		}
		return ReturnCodeEnum.getByReturnCode(OK_STARTTOMCAT).setData(logid);
	}

	private TailShellCommand restartAndTailLog(String WAR_NAME) {
		final String commandStr2 = SH_RESTARTTOMCAT_CLIENT + BLACKSPACE + TOMCAT_HOME + BLACKSPACE + WAR_NAME;
		final TailShellCommand shCommand = new TailShellCommand();
		new Thread() {
			@Override
			public void run() {
				shCommand.execute(commandStr2, CommandEnum.RESTART);
			}
		}.start();
		return shCommand;
	}

	@Override
	public ReturnCodeEnum getProcessPercent(String projectCode, String profile, String pencentkey) {

		String processPercent = GodzillaServiceApplication.processPercent.get(pencentkey) == null ? "0" : GodzillaServiceApplication.processPercent.get(pencentkey);

		if (processPercent.equals("100")) {
			GodzillaServiceApplication.processPercent.put(pencentkey, "0");
		}

		return ReturnCodeEnum.getByReturnCode(OK_QUERYPERCENT).setData(processPercent);
	}

	@Override
	public ReturnCodeEnum showdeployLog(String projectCode, String profile, String logid) {
		if (StringUtil.isEmpty(logid) || logid.equals("0")) {
			return ReturnCodeEnum.getByReturnCode(NO_DEPLOYLOGID);// id错误
		}
		OperateLog log = operateLogService.queryLogById(projectCode, profile, Long.parseLong(logid));
		if(StringUtil.isEmpty(log.getDeployLog())&&StringUtil.isEmpty(log.getCatalinaLog())) 
			return ReturnCodeEnum.getByReturnCode(NO_STOREDEPLOYLOG);//日志记录失败  20151118 添加catalina日志
		//setData为给前台显示后台信息，而不输出日志
		return ReturnCodeEnum.getByReturnCode(OK_SHOWDEPLOYLOG).setData(log.getDeployLog()+log.getCatalinaLog());
	}

	@Override
	public ReturnCodeEnum showwarInfo(String projectCode, String profile, String logid) {
		if (StringUtil.isEmpty(logid) || logid.equals("0")) {
			return ReturnCodeEnum.getByReturnCode(NO_SHOWWARINFOID);// id错误
		}
		OperateLog log = operateLogService.queryLogById(projectCode, profile, Long.parseLong(logid));
		if (StringUtil.isEmpty(log.getWarInfo()))
			return ReturnCodeEnum.getByReturnCode(NO_SHOWWARINFO);// 日志记录失败
		// setData为给前台显示后台信息，而不输出日志
		return ReturnCodeEnum.getByReturnCode(OK_SHOWWARINFO).setData(log.getWarInfo());
	}
}
