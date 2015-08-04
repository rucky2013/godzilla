package cn.godzilla.service;

import java.util.List;

import cn.godzilla.model.SvnBranchConfig;

public interface SvnBranchConfigService {
	
	int insert(SvnBranchConfig svnBranchConfig);
	
	int update(SvnBranchConfig svnBranchConfig);
	
	List<SvnBranchConfig> queryListByProjectCode(String projectCode);

	/**
	 * 分支设置-->添加分支  
	 * @param projectCode
	 * @param branchUrl
	 * @param currentVersion
	 * @return
	 */
	boolean addNewBranch(String projectCode, String branchUrl, String currentVersion);

	/**
	 * 分支编辑  保存  
	 * @param id
	 * @param branchUrl
	 * @param currentVersion
	 * @return
	 */
	boolean editBranch(String id, String branchUrl, String currentVersion);

	
}
