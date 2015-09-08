package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.dao.SvnBranchConfigMapper;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.service.SvnService;
import cn.godzilla.web.GodzillaApplication;

@Service("svnBranchConfigService")
public class SvnBranchConfigServiceImpl extends GodzillaApplication implements SvnBranchConfigService {

	@Autowired
	private SvnBranchConfigMapper dao;
	@Autowired
	private SvnService svnService;
	@Override
	public int insert(SvnBranchConfig svnBranchConfig) {

		return dao.insert(svnBranchConfig);
	}

	@Override
	public int update(SvnBranchConfig svnBranchConfig) {

		return dao.update(svnBranchConfig);
	}

	@Override
	public List<SvnBranchConfig> queryListByProjectCode(String projectCode) {

		return dao.queryListByProjectCode(projectCode);
	}

	@Override
	public boolean addNewBranch(String projectCode, String profile, String branchUrl) {
		
		ReturnCodeEnum versionreturn = svnService.getVersion(branchUrl, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return false;
		}
		
		String currentVersion = svnVersionThreadLocal.get();
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		String branchName = super.getBranchNameByBranchUrl(branchUrl);
		
		parameterMap.put("branchName", branchName);
		parameterMap.put("branchUrl", branchUrl);
		parameterMap.put("projectCode", projectCode);
		parameterMap.put("currentVersion", currentVersion);
		parameterMap.put("createVersion", currentVersion);
		parameterMap.put("createBy", super.getUser().getUserName());
		parameterMap.put("status", "1");
		
		int index = dao.insertBranch(parameterMap);
		
		return index>0;
	}

	@Override
	public boolean editBranch(String projectCode, String profile, String id, String branchUrl) {
		
		ReturnCodeEnum versionreturn = svnService.getVersion(branchUrl, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return false;
		}
		
		String currentVersion = svnVersionThreadLocal.get();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("id", id);
		parameterMap.put("branchUrl", branchUrl);
		parameterMap.put("currentVersion", currentVersion);

		parameterMap.put("createBy", super.getUser().getUserName());
		parameterMap.put("status", "1");
		
		int index = dao.update(parameterMap);
		
		return index>0;
	}

	@Override
	public int deletebranchesByProjectCode(String projectCode) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("project_code", projectCode);
		int index = dao.updateByProjectCode(parameterMap);
		return index;
	}

	@Override
	public boolean refreshBranchesVersion(List<SvnBranchConfig> svnBranchConfigs) {
		
		boolean flag = false;
		for(SvnBranchConfig branch: svnBranchConfigs){
			ReturnCodeEnum versionreturn = svnService.getVersion(branch.getBranchUrl(), branch.getProjectCode());
			if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
				return false;
			}
			String currentVersion = svnVersionThreadLocal.get();
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("id", branch.getId()+"");
			parameterMap.put("current_version", currentVersion);
			int index = dao.updateVersionById(parameterMap);
			flag = flag&& (index>0);
		}
		
		return flag;
	}

	@Override
	public ReturnCodeEnum deleteBranch(String projectCode, String profile, String id) {
		
		boolean flag = false;
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("id", id);
		
		flag = dao.deletebranchById(parameterMap);
		
		if(flag) {
			return ReturnCodeEnum.getByReturnCode(OK_DELETEBRANCH);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_DELETEBRANCH);
		}
	}


}
