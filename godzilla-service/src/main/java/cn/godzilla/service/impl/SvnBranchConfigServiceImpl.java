package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.dao.SvnBranchConfigMapper;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.service.SvnService;
import cn.godzilla.web.GodzillaApplication;

public class SvnBranchConfigServiceImpl extends GodzillaApplication implements SvnBranchConfigService {

	@Autowired
	private SvnBranchConfigMapper svnBranchConfigMapper;
	@Autowired
	private SvnService svnService;
	@Autowired
	private ProjectService projectService;
	@Override
	public List<SvnBranchConfig> queryListByProjectCode(String projectCode, String profile) {
		return svnBranchConfigMapper.queryListByProjectCode(projectCode);
	}

	@Override
	public ReturnCodeEnum addNewBranch(String projectCode, String profile, String branchUrl) {
		
		ReturnCodeEnum versionreturn = svnService.getVersion(branchUrl, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return versionreturn;
		}
		
		String currentVersion = svnVersionThreadLocal.get();
		Map<String, String> parameterMap1 = new HashMap<String, String>();
		
		parameterMap1.put("project_code", projectCode);
		parameterMap1.put("merge_status", "0");
		//20151103 清除project表 merge_status标记,初始化为0
		ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(projectCode, profile, parameterMap1);
		if(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS).equals(renum1)) {
			return renum1;
		}
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		String branchName = super.getBranchNameByBranchUrl(branchUrl);
		
		parameterMap.put("branchName", branchName);
		parameterMap.put("branchUrl", branchUrl);
		parameterMap.put("projectCode", projectCode);
		parameterMap.put("currentVersion", currentVersion);
		parameterMap.put("createVersion", currentVersion);
		parameterMap.put("createBy", super.getUser().getUserName());
		parameterMap.put("status", "1");
		
		int index = svnBranchConfigMapper.insertBranch(parameterMap);
		if(index>0) {
			return ReturnCodeEnum.getByReturnCode(OK_BRANCHADD);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_BRANCHADD);
		}
	}

	@Override
	public ReturnCodeEnum editBranch(String projectCode, String profile, String id, String branchUrl) {
		
		ReturnCodeEnum versionreturn = svnService.getVersion(branchUrl, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return versionreturn;
		}
		
		String currentVersion = svnVersionThreadLocal.get();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("id", id);
		parameterMap.put("branchUrl", branchUrl);
		parameterMap.put("currentVersion", currentVersion);

		parameterMap.put("createBy", super.getUser().getUserName());
		parameterMap.put("status", "1");
		
		int index = svnBranchConfigMapper.update(parameterMap);
		if(index>0) {
			return ReturnCodeEnum.getByReturnCode(OK_SVNEDIT);
		}
		return ReturnCodeEnum.getByReturnCode(NO_SVNEDIT);
	}


	@Override
	public ReturnCodeEnum refreshBranchesVersion(String projectCode, String profile, List<SvnBranchConfig> svnBranchConfigs) {
		
		boolean flag = false;
		for(SvnBranchConfig branch: svnBranchConfigs){
			ReturnCodeEnum versionreturn = svnService.getVersion(branch.getBranchUrl(), branch.getProjectCode());
			if(!flag||!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
				return versionreturn;
			}
			String currentVersion = svnVersionThreadLocal.get();
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("id", branch.getId()+"");
			parameterMap.put("current_version", currentVersion);
			int index = svnBranchConfigMapper.updateVersionById(parameterMap);
			flag = flag&& (index>0);
		}
		
		return ReturnCodeEnum.getByReturnCode(OK_SVNVERSION);
	}

	@Override
	public ReturnCodeEnum deletebranchesByProjectCode(String projectCode, String profile) {
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("project_code", projectCode);
		parameterMap.put("merge_status", "0");
		//20151103 清除project表 merge_status标记,初始化为0
		ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(projectCode, profile, parameterMap);
		if(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS).equals(renum1)) {
			return renum1;
		}
		int index = svnBranchConfigMapper.updateByProjectCode(parameterMap);
		if(index>0) {
			return ReturnCodeEnum.getByReturnCode(OK_DELETEBRANCH);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_DELETEBRANCH);
		}
	}

	@Override
	public ReturnCodeEnum deletebranchesByProjectCode(String projectCode, String profile, String id) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("project_code", projectCode);
		parameterMap.put("merge_status", "0");
		parameterMap.put("delbranchid", id);
		//20151103 清除project表 merge_status标记,初始化为0
		ReturnCodeEnum renum1 = projectService.editMergestatusByProjectCode(projectCode, profile, parameterMap);
		if(ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS).equals(renum1)) {
			return renum1;
		}
		int index = svnBranchConfigMapper.updateByProjectCode(parameterMap);
		if(index>0) {
			return ReturnCodeEnum.getByReturnCode(OK_DELETEBRANCH);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_DELETEBRANCH);
		}
	}
	
}
