package cn.godzilla.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.service.SvnCmdLogService;
import cn.godzilla.service.SvnService;
import cn.godzilla.svn.BaseShellCommand;
import cn.godzilla.web.GodzillaApplication;

@Service("SvnService")
public class SvnServiceImpl extends GodzillaApplication implements SvnService {
	private final Logger logger = LogManager.getLogger(SvnServiceImpl.class);
	
	@Autowired
	private ProjectService projectService ;
	@Autowired
	private SvnBranchConfigService svnBranchConfigService;
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private SvnCmdLogService svnCmdLogService;
	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private BaseShellCommand command;
	@Override
	public ReturnCodeEnum getVersion(String trunkPath, String projectCode) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, TEST_PROFILE) ;
		Project project = projectService.qureyByProCode(projectCode);
		super.isEmpty(clientConfig, projectCode+"项目的clientconfig 未初始化") ;
		
		String clientIp = clientConfig.getRemoteIp();
		String branches = EMPTY_BRANCH;
		boolean flag = false;
		
		String callbackUrl = "http://localhost:8080/process-callback.do";
		String operator = super.getUser().getUserName();
		String str = "";
		try {
			str ="sh /home/godzilla/gzl/shell/server/svn_server_wl.sh version "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		String username = super.getUser().getUserName();
		svnCmdLogService.addSvnCommandLog(username, trunkPath, str, username);
		//shell返回值 ：通过shell 最后一行 echo 
		String shellReturn = shellReturnThreadLocal.get();
		//如果合并成功  1.shell执行返回true 2.shell返回值为 0 
		if(flag&&"0".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(OK_SVNVERSION);
		} else if(flag) {
			return ReturnCodeEnum.getByReturnCode(NO_SVNVERSION);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_JAVASHELLCALL);
		}
	}
	
	@Override
	public ReturnCodeEnum svnCommit(String projectCode, String profile) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		
		boolean flag = false;
		
		String branches = "";
		for(SvnBranchConfig sbc: svnBranchConfigs) {
			branches = sbc.getBranchUrl() + ",";
		}
		if("".equals(branches)) {
			branches = EMPTY_BRANCH;
		} else {
			branches = branches.substring(0, branches.length()-1);
		}
		String callbackUrl = "http://localhost:8080/process-callback.do";
		
		String operator = super.getUser().getUserName();
		String str="";
		try {
			str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh commit "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		//shell返回值 ：通过shell 最后一行 echo 
		String shellReturn = shellReturnThreadLocal.get();
		//如果合并成功  1.shell执行返回true 2.shell返回值为 0 
		
		String username = super.getUser().getUserName();
		svnCmdLogService.addSvnCommandLog(username, trunkPath, str, username);
		
		if(flag&&"0".equals(shellReturn)){
			//成功则 1.删除  当前分支
			int re = svnBranchConfigService.deletebranchesByProjectCode(projectCode);
			// 2.更新 项目 project版本号
			boolean flag1 = projectService.refreshProjectVersion(projectCode, profile);
			return ReturnCodeEnum.getByReturnCode(OK_SVNCOMMIT);
		} else if("1".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_SVNCOMMIT);
		} else if("2".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_FOUNDCONFLICT);
		} else if("3".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_CLIENTPARAM);
		} else if("4".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_SERVERPARAM);
		} else if("5".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_CHANGECOMMIT); //没有更改可以提交
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_JAVASHELLCALL);
		}
	}

	@Override
	public boolean svnMerge(String projectCode, String profile) {
		logger.info("************代码合并Begin***********");
		
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		String localPath=project.getCheckoutPath(); 
		
		boolean flag = false;
		
		String branches = "";
		for(SvnBranchConfig sbc: svnBranchConfigs) {
			branches += sbc.getBranchUrl() + ",";
		}
		if("".equals(branches)) {
			branches = EMPTY_BRANCH;
		} else {
			branches = branches.substring(0, branches.length()-1);
		}
		//准生产 与生产环境  直接使用trunk 版本
		if(!profile.equals("TEST")) {
			branches = EMPTY_BRANCH;
		}
		
		String callbackUrl = "http://localhost:8080/process-callback.do";
		
		String operator = super.getUser().getUserName();
		String str= "";
		try {
			str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh merge "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
			boolean flag4 = shellReturnThreadLocal.get().equals("0")?true:false;
			flag = flag && flag4;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		if(flag){
			String username = super.getUser().getUserName();
			svnCmdLogService.addSvnCommandLog(username, trunkPath, str, username);
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNMERGE, SUCCESS, "代码合并SUCCESS");
			logger.info("************代码合并End**************");
		}else{
			String username = super.getUser().getUserName();
			svnCmdLogService.addSvnCommandLog(username, trunkPath, str, username);
			
			if(shellReturnThreadLocal.get().equals("2")) {
				operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNMERGE, FAILURE, "代码合并FAILURE请先解决冲突");
			} else {
				operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNMERGE, FAILURE, "代码合并FAILURE");
			}
			logger.error("************代码合并Error shellReturnThreadLocal:"+shellReturnThreadLocal.get()+"**************");
		}
		return flag;
	}
}
