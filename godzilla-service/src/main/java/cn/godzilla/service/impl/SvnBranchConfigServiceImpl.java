package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.SvnBranchConfigMapper;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.web.GodzillaApplication;

@Service("svnBranchConfigService")
public class SvnBranchConfigServiceImpl extends GodzillaApplication implements SvnBranchConfigService {

	@Autowired
	private SvnBranchConfigMapper dao;
	
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
	public boolean addNewBranch(String projectCode, String branchUrl, String currentVersion) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		String branchName = super.getBranchNameByBranchUrl(branchUrl);
		
		parameterMap.put("branchName", branchName);
		parameterMap.put("branchUrl", branchUrl);
		parameterMap.put("projectCode", projectCode);
		parameterMap.put("currentVersion", currentVersion);
		parameterMap.put("createBy", super.getUser().getUserName());
		parameterMap.put("status", "0");
		
		int index = dao.insertBranch(parameterMap);
		
		return index>0;
	}

	@Override
	public boolean editBranch(String id, String branchUrl, String currentVersion) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("id", id);
		parameterMap.put("branchUrl", branchUrl);
		parameterMap.put("currentVersion", currentVersion);

		parameterMap.put("createBy", super.getUser().getUserName());
		parameterMap.put("status", "0");
		
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


}
