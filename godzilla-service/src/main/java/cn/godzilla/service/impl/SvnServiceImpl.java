package cn.godzilla.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.command.DefaultShellCommand;
import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.dao.SvnConflictMapper;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.model.SvnConflict;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.service.SvnService;
import cn.godzilla.util.GodzillaServiceApplication;

public class SvnServiceImpl extends GodzillaServiceApplication implements SvnService {
	
	@Autowired
	private ProjectService projectService ;
	@Autowired
	private SvnBranchConfigService svnBranchConfigService;
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private SvnConflictMapper svnConflictMapper;
	
	@Override
	public ReturnCodeEnum svnCommit(String projectCode, String profile) {
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		// mergeStatus 0:无 1:有冲突 2:标记解决
		if("1".equals(project.getMergeStatus())) {
			return ReturnCodeEnum.getByReturnCode(NO_SVNRESOLVED);
		} 
		return svnCommit1(projectCode, profile);
	}
	
	private ReturnCodeEnum svnCommit1(String projectCode, String profile) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode, TEST_PROFILE);
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
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
		
		String operator = super.getUser().getUserName();
		String str="";
		// mergeStatus 0:无 1:有冲突 2:标记解决
		if("0".equals(project.getMergeStatus())) {
			try {
				str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh commit "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
				DefaultShellCommand command = new DefaultShellCommand();
				flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
				
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		} else {
			//"2".equals(project.getMergeStatus())
			String CONFL_URL = this.queryConflictURLById(project.getSvnConflictId());
			try {
				str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh commit_resolve "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp  ;
				DefaultShellCommand command = new DefaultShellCommand();
				flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword(), CONFL_URL);
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}
		
		//shell返回值 ：通过shell 最后一行 echo 
		String shellReturn = shellReturnThreadLocal.get();
		//如果合并成功  1.shell执行返回true 2.shell返回值为 0 
		/**
		#***
		# ErrorCode
		# 1.svn commit failed!
		# 2.some conflicts found in branch!
		# 3.parameter not found
		# 4.some conflicts still found in conflict-branch! Please resolve all of them. 
		# 5.some conflicts still found on branch ,please renew resolve it
		# 6.branches is '' ,no need commit
		#***
		// mergeStatus 0:无 1:有冲突 2:标记解决
		**/
		if("0".equals(shellReturn)){
			//成功则 1.删除  当前分支
			ReturnCodeEnum re = svnBranchConfigService.deletebranchesByProjectCode(projectCode, TEST_PROFILE);
			// 2.更新 项目 project版本号
			boolean flag1 = projectService.refreshProjectVersion(projectCode, profile);
			// 3. 取消冲突标记
			Map<String, String> parameterMap1 = new HashMap<String, String>();
			parameterMap1.put("project_code", projectCode);
			parameterMap1.put("merge_status", "0");
			ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(SERVER_USER, TEST_PROFILE, parameterMap1);
			if(renum1.equals(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS))) {
				return renum1; //修改冲突标识失败
			}
			return ReturnCodeEnum.getByReturnCode(OK_SVNCOMMIT);
		} else if("1".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_SVNCOMMIT);
		} else if("2".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_FOUNDCONFLICT);//存在冲突 
		} else if("3".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_CLIENTPARAM);
		} else if("4".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_SERVERPARAM); 
		} else if("5".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_NEWCONFLICTFOUND); //存在新冲突
		} else if("6".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(NO_CHANGECOMMIT); //没有更改可以提交
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_JAVASHELLCALL);
		}
	}

	@Override
	public ReturnCodeEnum svnResolved(String projectCode, String profile) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode, TEST_PROFILE);
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
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
		// mergeStatus 0:无 1:有冲突 2:标记解决
		if("1".equals(project.getMergeStatus())) {
			try {
				String CONFL_URL = this.queryConflictURLById(project.getSvnConflictId());
				str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh resolve "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
				flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword(), CONFL_URL);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		} 
		
		/**
		#***
		# ErrorCode
		# 1.svn commit failed!
		# 3.parameter not found
		# 4.some conflicts still found in conflict-branch! Please resolve all of them. 
		# 6.branches is '' ,no need commit
		#***
		// mergeStatus 0:无 1:有冲突 2:标记解决
		**/
		String shellReturn = shellReturnThreadLocal.get();
		if("0".equals(shellReturnThreadLocal.get())){
			
			Map<String, String> parameterMap1 = new HashMap<String, String>();
			parameterMap1.put("project_code", projectCode);
			parameterMap1.put("merge_status", "2");
			ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(SERVER_USER, TEST_PROFILE, parameterMap1);
			if(renum1.equals(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS))) {
				return renum1; //修改冲突标识失败
			}
			return ReturnCodeEnum.getByReturnCode(OK_SVNRESOLVED);
		} else if("4".equals(shellReturnThreadLocal.get())) {
			return ReturnCodeEnum.getByReturnCode(NO_STILLHASCONFLICTBRANCH); 
		} else if("6".equals(shellReturnThreadLocal.get())) {
			return ReturnCodeEnum.getByReturnCode(NO_HAVEBRANCHES); 
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_SVNRESOLVE);
		}
		
	}
	
	@Override
	public ReturnCodeEnum svnMerge(String projectCode, String profile) {
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		// mergeStatus 0:无 1:有冲突 2:标记解决
		if("1".equals(project.getMergeStatus())) {
			return ReturnCodeEnum.getByReturnCode(NO_SVNRESOLVED);
		} 
		return svnMerge1(projectCode, profile);
	}
	
	private ReturnCodeEnum svnMerge1(String projectCode, String profile) {
		
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode, TEST_PROFILE);
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
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
		//临时冲突解决分支
		String NEW_CONFL_URL = getRandConflictURL(project.getRepositoryUrl(), projectCode);
		// mergeStatus 0:无 1:有冲突 2:标记解决
		if("0".equals(project.getMergeStatus())) {
			try {
				str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh merge "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
				flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword(), NEW_CONFL_URL);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		} else {
			//"2".equals(project.getMergeStatus())
			String CONFL_URL = this.queryConflictURLById(project.getSvnConflictId());
			try {
				str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh merge_resolve "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
				flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword(), CONFL_URL);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		
		/**
		#***
		# ErrorCode
		# 1.svn commit failed!
		# 2.some conflicts found in branch!
		# 3.parameter not found
		# 4.some conflicts still found in conflict-branch! Please resolve all of them. 
		# 5.some conflicts still found on branch ,please renew resolve it
		# 6.branches is '' ,no need commit
		#***
		// mergeStatus 0:无 1:有冲突 2:标记解决
		**/
		if("0".equals(shellReturnThreadLocal.get())){
			return ReturnCodeEnum.getByReturnCode(OK_SVNMERGE);
		} else if("2".equals(shellReturnThreadLocal.get())) {
			//标记存在冲突　设置冲突分支url
			SvnConflict svnconflict = new SvnConflict();
			svnconflict.setConflictPath(NEW_CONFL_URL);
			svnconflict.setProjectCode(projectCode);
			svnconflict.setProfile(profile);
			svnconflict.setCreator(super.getUser().getUserName());
			int svn_conflict_id = this.addSvnConflict(svnconflict).intValue();
			
			Map<String, String> parameterMap1 = new HashMap<String, String>();
			parameterMap1.put("project_code", projectCode);
			parameterMap1.put("merge_status", "1");
			parameterMap1.put("svn_conflict_id", svn_conflict_id+"");
			ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(SERVER_USER, TEST_PROFILE, parameterMap1);
			if(renum1.equals(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS))) {
				return renum1; //修改冲突标识失败
			}
			return ReturnCodeEnum.getByReturnCode(NO_FOUNDCONFLICT); //合并分支冲突 ,请检出冲突分支，并解决冲突
		} else if("3".equals(shellReturnThreadLocal.get())) {
			return ReturnCodeEnum.getByReturnCode(NO_ERRORCOMMAND); 
		} else if("4".equals(shellReturnThreadLocal.get())) {
			return ReturnCodeEnum.getByReturnCode(NO_STILLHASCONFLICTBRANCH); 
		} else if("5".equals(shellReturnThreadLocal.get())) {
			//标记还有冲突　重新建立解决冲突分支
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("project_code", projectCode);
			parameterMap.put("merge_status", "0");
			ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(SERVER_USER, TEST_PROFILE, parameterMap);
			if(renum1.equals(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS))) {
				return renum1; //修改冲突标识失败
			}
			return this.svnMerge(projectCode, profile); //renew resolve
		} else if("6".equals(shellReturnThreadLocal.get())) {
			return ReturnCodeEnum.getByReturnCode(NO_HAVEBRANCHES); 
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_SVNMERGE);
		}
	}
	
	private String queryConflictURLById(String svnConflictId) {
		SvnConflict svnConflict = svnConflictMapper.selectByPrimaryKey(Long.parseLong(svnConflictId));
		return svnConflict.getConflictPath();
	}

	private Long addSvnConflict(SvnConflict svnconflict) {
		int index = svnConflictMapper.insertSelective(svnconflict);
		return svnconflict.getId();
	}

	/**
	 * 生成唯一  冲突分支 url
	 * @return
	 */
	private static String getRandConflictURL(String trunkUrl, String projectcode) {
		String conflict_url = "";
		try {
			String random6 = StringUtil.getRandom(6);
			String baseUrl = trunkUrl.substring(0, trunkUrl.indexOf("/trunk"));
			DateFormat sdf = new SimpleDateFormat("yyyyMMdd_");
			
			String suffix = projectcode + sdf.format(new Date()) + random6;
			conflict_url = baseUrl + "/conflict/"+suffix;
		} catch(IndexOutOfBoundsException e) {
			throw new BusinessException("请检查主干路径!trunkUrl::"+trunkUrl);
		}
		
		return conflict_url;
	}

	public ReturnCodeEnum getStatus(String projectCode, String profile) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode, TEST_PROFILE);
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
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
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
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

	@Override
	public void setConflictUrl(Project project, String profile) {
		String conflictId = project.getSvnConflictId();
		if(StringUtil.isEmpty(conflictId)) {
			project.setConflictUrl(""); 
			return ;
		}
		SvnConflict svnConflict = svnConflictMapper.selectByPrimaryKey(Long.parseLong(conflictId));
		project.setConflictUrl(svnConflict.getConflictPath()); 
	}
	
}
