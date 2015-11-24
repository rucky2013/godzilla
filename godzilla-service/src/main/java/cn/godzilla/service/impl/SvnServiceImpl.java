package cn.godzilla.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.command.CommandEnum;
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
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode, TEST_PROFILE);
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		
		String BRANCHES = getBranchesByBranchConfigs(profile, svnBranchConfigs);
		
		String TRUNK_PATH = project.getRepositoryUrl();
		String SVN_USERNAME = project.getSvnUsername();
		String SVN_PASSWORD = project.getSvnPassword();
		// mergeStatus 0:无 1:有冲突 2:标记解决
		if(NO_CONFLICT.equals(project.getMergeStatus())) {
				String commandStr = SH_SVN_CLIENT + BLACKSPACE + COM_COMMIT + BLACKSPACE + TRUNK_PATH + BLACKSPACE + QUATE+ BRANCHES + QUATE + BLACKSPACE + projectCode + BLACKSPACE + SVN_USERNAME + BLACKSPACE + SVN_PASSWORD;
				DefaultShellCommand commandShell = new DefaultShellCommand();
				commandShell.execute(commandStr, CommandEnum.COMMIT);
		} else if(HAS_CONFLICT.equals(project.getMergeStatus())) {
			return ReturnCodeEnum.getByReturnCode(NO_SVNRESOLVED);
		} else if(OK_RESOLVED.equals(project.getMergeStatus())) {
			String CONFL_URL = this.queryConflictURLById(project.getSvnConflictId());
			String commandStr = SH_SVN_CLIENT + BLACKSPACE + COM_COMMIT_RESOLVE + BLACKSPACE +TRUNK_PATH+ BLACKSPACE + QUATE + BRANCHES + QUATE + BLACKSPACE + projectCode + BLACKSPACE + SVN_USERNAME + BLACKSPACE + SVN_PASSWORD + CONFL_URL;
			DefaultShellCommand command = new DefaultShellCommand();
			command.execute(commandStr, CommandEnum.COMMIT_RESOLVE);
		} else {
			//never reach here.
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
		**/
		if("0".equals(shellReturn)){
			//成功则 1.删除  当前分支
			ReturnCodeEnum re = svnBranchConfigService.deletebranchesByProjectCode(projectCode, TEST_PROFILE);
			// 2.更新 项目 project版本号
			boolean flag1 = projectService.refreshProjectVersion(projectCode, profile);
			// 3. 取消冲突标记
			Map<String, String> parameterMap1 = new HashMap<String, String>();
			parameterMap1.put("project_code", projectCode);
			parameterMap1.put("merge_status", NO_CONFLICT);
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
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode, TEST_PROFILE);
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		
		String BRANCHES = getBranchesByBranchConfigs(profile, svnBranchConfigs);
		String TRUNK_PATH = project.getRepositoryUrl();
		String SVN_USERNAME = project.getSvnUsername();
		String SVN_PASSWORD = project.getSvnPassword();
		// mergeStatus 0:无 1:有冲突 2:标记解决
		if(HAS_CONFLICT.equals(project.getMergeStatus())) {
			String CONFL_URL = this.queryConflictURLById(project.getSvnConflictId());
			String commandStr = SH_SVN_CLIENT + BLACKSPACE + COM_RESOLVE + TRUNK_PATH + BLACKSPACE + QUATE + BRANCHES + QUATE + BLACKSPACE + projectCode + BLACKSPACE + SVN_USERNAME + BLACKSPACE + SVN_PASSWORD + BLACKSPACE + CONFL_URL;
			DefaultShellCommand command = new DefaultShellCommand();
			command.execute(commandStr, CommandEnum.RESOLVE);
		} 
		
		/**
		#***
		# ErrorCode
		# 1.svn commit failed!
		# 3.parameter not found
		# 4.some conflicts still found in conflict-branch! Please resolve all of them. 
		# 6.branches is '' ,no need commit
		#***
		**/
		String shellReturn = shellReturnThreadLocal.get();
		if("0".equals(shellReturn)){
			
			Map<String, String> parameterMap1 = new HashMap<String, String>();
			parameterMap1.put("project_code", projectCode);
			parameterMap1.put("merge_status", OK_RESOLVED);
			ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(SERVER_USER, TEST_PROFILE, parameterMap1);
			if(renum1.equals(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS))) {
				return renum1; //修改冲突标识失败
			}
			return ReturnCodeEnum.getByReturnCode(OK_SVNRESOLVED);
		} else if("4".equals(shellReturn)) {
			return ReturnCodeEnum.getByReturnCode(NO_STILLHASCONFLICTBRANCH); 
		} else if("6".equals(shellReturn)) {
			return ReturnCodeEnum.getByReturnCode(NO_HAVEBRANCHES); 
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_SVNRESOLVE);
		}
		
	}
	
	@Override
	public ReturnCodeEnum svnMerge(String projectCode, String profile) {
		
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode, TEST_PROFILE);
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		
		String BRANCHES = getBranchesByBranchConfigs(profile, svnBranchConfigs);
		String TRUNK_PATH = project.getRepositoryUrl();
		String SVN_USERNAME = project.getSvnUsername();
		String SVN_PASSWORD = project.getSvnPassword();
		//临时冲突解决分支
		String NEW_CONFL_URL = getRandConflictURL(project.getRepositoryUrl(), projectCode);
		// mergeStatus 0:无 1:有冲突 2:标记解决
		if(NO_CONFLICT.equals(project.getMergeStatus())) {
			String commandStr = SH_SVN_CLIENT + BLACKSPACE + COM_MERGE + BLACKSPACE + TRUNK_PATH + BLACKSPACE + QUATE + BRANCHES + QUATE + BLACKSPACE + projectCode + BLACKSPACE + SVN_USERNAME + BLACKSPACE + SVN_PASSWORD + BLACKSPACE + NEW_CONFL_URL;
			DefaultShellCommand command = new DefaultShellCommand();
			command.execute(commandStr, CommandEnum.MERGE);
		} if(HAS_CONFLICT.equals(project.getMergeStatus())) {
			return ReturnCodeEnum.getByReturnCode(NO_SVNRESOLVED);
		} else if(OK_RESOLVED.equals(project.getMergeStatus())) {
			String CONFL_URL = this.queryConflictURLById(project.getSvnConflictId());
			String commandStr = SH_SVN_CLIENT + BLACKSPACE + COM_MERGE_RESOLVE + BLACKSPACE + TRUNK_PATH + BLACKSPACE + QUATE + BRANCHES + QUATE + BLACKSPACE + projectCode + BLACKSPACE + SVN_USERNAME + BLACKSPACE + SVN_PASSWORD + BLACKSPACE + CONFL_URL;
			DefaultShellCommand command = new DefaultShellCommand();
			command.execute(commandStr, CommandEnum.MERGE_RESOLVE);
		} else {
			//never reach here.
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
		**/
		String shellReturn = shellReturnThreadLocal.get();
		
		if("0".equals(shellReturn)){
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
			parameterMap1.put("merge_status", HAS_CONFLICT);
			parameterMap1.put("svn_conflict_id", svn_conflict_id+"");
			ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(SERVER_USER, TEST_PROFILE, parameterMap1);
			if(renum1.equals(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS))) {
				return renum1; //修改冲突标识失败
			}
			return ReturnCodeEnum.getByReturnCode(NO_FOUNDCONFLICT); //合并分支冲突 ,请检出冲突分支，并解决冲突
		} else if("3".equals(shellReturn)) {
			return ReturnCodeEnum.getByReturnCode(NO_ERRORCOMMAND); 
		} else if("4".equals(shellReturn)) {
			return ReturnCodeEnum.getByReturnCode(NO_STILLHASCONFLICTBRANCH); 
		} else if("5".equals(shellReturn)) {
			//标记还有冲突　重新建立解决冲突分支 , 20151124 需要重新合并 0状态是对的
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("project_code", projectCode);
			parameterMap.put("merge_status", NO_CONFLICT);
			ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(SERVER_USER, TEST_PROFILE, parameterMap);
			if(renum1.equals(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS))) {
				return renum1; //修改冲突标识失败
			}
			return this.svnMerge(projectCode, profile); //renew resolve
		} else if("6".equals(shellReturn)) {
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

	@Override
	public ReturnCodeEnum getStatus(String projectCode, String profile) {
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode, TEST_PROFILE);
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		
		String TRUNK_PATH = project.getRepositoryUrl();
		String BRANCHES = getBranchesByBranchConfigs(profile, svnBranchConfigs);
		String SVN_USERNAME = project.getSvnUsername();
		String SVN_PASSWORD = project.getSvnPassword();
		
		String commandStr =SH_SVN_CLIENT + BLACKSPACE + COM_STATUS + BLACKSPACE + TRUNK_PATH + BLACKSPACE + QUATE + BRANCHES + QUATE + BLACKSPACE + projectCode + BLACKSPACE + SVN_USERNAME + BLACKSPACE + SVN_PASSWORD + BLACKSPACE;
		DefaultShellCommand command = new DefaultShellCommand();
		command.execute(commandStr, CommandEnum.INFO); //svn info
		
		String shellReturn = shellReturnThreadLocal.get();
		
		if("0".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(OK_SVNSTATUS);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_SVNSTATUS);
		}
	}
	
	private String getBranchesByBranchConfigs(String profile, List<SvnBranchConfig> svnBranchConfigs) {
		String branches = "";
		for(SvnBranchConfig sbc: svnBranchConfigs) {
			branches = sbc.getBranchUrl() + ",";
		}
		if(StringUtil.isEmpty(branches)) {
			branches = EMPTY_BRANCH;
		} else {
			branches = branches.substring(0, branches.length()-1);
		}
		//准生产 与生产环境  直接使用trunk 版本
		if(!TEST_PROFILE.equals(profile)) {
			branches = EMPTY_BRANCH;
		}
		return branches;
	}

	@Override
	public ReturnCodeEnum getVersion(String trunkPath, String projectCode) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, TEST_PROFILE) ;
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		isEmpty(clientConfig, projectCode+"项目的clientconfig 未初始化") ;
		
		String BRANCHES = EMPTY_BRANCH;
		String TRUNK_PATH = project.getRepositoryUrl();
		String SVN_USERNAME = project.getSvnUsername();
		String SVN_PASSWORD = project.getSvnPassword();
		
		String commandStr =SH_SVN_CLIENT + BLACKSPACE + COM_VERSION + BLACKSPACE + TRUNK_PATH + BLACKSPACE + QUATE + BRANCHES + QUATE + BLACKSPACE +  projectCode + BLACKSPACE + SVN_USERNAME + BLACKSPACE + SVN_PASSWORD + BLACKSPACE ;
		DefaultShellCommand command = new DefaultShellCommand();
		command.execute(commandStr, CommandEnum.VERSION); //svn log
		
		//shell返回值 ：通过shell 最后一行 echo 
		String shellReturn = shellReturnThreadLocal.get();
		//如果合并成功  1.shell执行返回true 2.shell返回值为 0 
		if("0".equals(shellReturn)){
			return ReturnCodeEnum.getByReturnCode(OK_SVNVERSION);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_SVNVERSION);
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
