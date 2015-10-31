package cn.godzilla.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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
	private OperateLogService operateLogService;
	@Autowired
	private BaseShellCommand command;
	
	@Override
	public ReturnCodeEnum svnCommit(String projectCode, String profile) {
		/**
		 * 1.限制并发　
		 * 测试环境,准生产,生产　每个项目　只允许　一个人提交(i.svn操作会清空work目录ii.改变svn主干代码,对其他所有操作都有影响) 
		 **/
		Lock lock1 = PUBLIC_LOCK;
		Lock lock2 = PUBLIC_LOCK1;
		Lock lock3 = PUBLIC_LOCK2;
		boolean hasAC1 = false;
		boolean hasAC2 = false;
		boolean hasAC3 = false;
		try {
			lock1 = GodzillaApplication.deploy_lock.get(projectCode);
			hasAC1 = lock1.tryLock(1, TimeUnit.SECONDS);
			if(!hasAC1)
				return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			lock2 = GodzillaApplication.deploy_lock.get(QUASIPRODUCT_PROFILE);
			hasAC2 = lock2.tryLock(1, TimeUnit.SECONDS);
			if(!hasAC2)
				return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			lock3 = GodzillaApplication.deploy_lock.get(PRODUCT_PROFILE);
			hasAC3 = lock3.tryLock(1, TimeUnit.SECONDS);
			if(!hasAC3)
				return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			
			return this.svnCommit1(projectCode, profile);
		} catch(InterruptedException e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
		} finally {
			try {
				lock1.unlock();
				lock2.unlock();
				lock3.unlock();
			} /*catch(InvocationTargetException e2) {
				return ReturnCodeEnum.getByReturnCode(NO_HASKEYDEPLOY);
			} */catch(IllegalMonitorStateException e1) {
				return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} 
		}
	}
	
	private ReturnCodeEnum svnCommit1(String projectCode, String profile) {
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
		
		if(flag&&"0".equals(shellReturn)){
			//成功则 1.删除  当前分支
			ReturnCodeEnum re = svnBranchConfigService.deletebranchesByProjectCode(projectCode);
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
	public ReturnCodeEnum svnMerge(String projectCode, String profile) {
		
		/**
		 * 1.限制并发　
		 * 测试环境 每个项目　只允许　一个人merge(i.svn操作会清空work目录)
		 * 准生产   只允许　一个人merge(i.svn操作会清空work目录)
		 * 生产　 只允许　一个人merge(i.svn操作会清空work目录)
		 **/
		Lock lock1 = PUBLIC_LOCK;
		boolean hasAC1 = false;
		try {
			if(TEST_PROFILE.equals(profile)) {
				lock1 = GodzillaApplication.deploy_lock.get(projectCode);
				hasAC1 = lock1.tryLock(1, TimeUnit.SECONDS);
				if(!hasAC1) 
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} else {
				lock1 = GodzillaApplication.deploy_lock.get(profile);
				hasAC1 = lock1.tryLock(1, TimeUnit.SECONDS);
				if(!hasAC1)
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			}
			return this.svnMerge1(projectCode, profile);
		} catch(InterruptedException e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
		} finally {
			try {
				lock1.unlock();
			} /*catch(InvocationTargetException e2) {
				return ReturnCodeEnum.getByReturnCode(NO_HASKEYDEPLOY);
			} */catch(IllegalMonitorStateException e1) {
				return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} 
		}
	}
	private ReturnCodeEnum svnMerge1(String projectCode, String profile) {
		
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		
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
			return ReturnCodeEnum.getByReturnCode(OK_SVNMERGE);
		}else{
			if(shellReturnThreadLocal.get().equals("2")) {
				return ReturnCodeEnum.getByReturnCode(NO_FOUNDCONFLICT); //合并分支冲突
			} else {
				return ReturnCodeEnum.getByReturnCode(NO_SVNMERGE);
			}
		}
	}
	
	public ReturnCodeEnum getStatus(String projectCode, String profile) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		String localPath=project.getCheckoutPath(); 
		
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
		String str = "";
		try {
			str ="sh /home/godzilla/gzl/shell/server/svn_server_wl.sh status "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		if(flag) {
			return ReturnCodeEnum.getByReturnCode(OK_SVNSTATUS);
		}
		return ReturnCodeEnum.getByReturnCode(NO_SVNSTATUS);
	}
	
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
}
