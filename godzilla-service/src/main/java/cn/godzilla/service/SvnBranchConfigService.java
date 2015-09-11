package cn.godzilla.service;

import java.util.List;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.SvnBranchConfig;

public interface SvnBranchConfigService {
	
	int insert(SvnBranchConfig svnBranchConfig);
	
	int update(SvnBranchConfig svnBranchConfig);
	
	List<SvnBranchConfig> queryListByProjectCode(String projectCode);

	/**
	 * 分支设置-->添加分支  
	 * @param projectCode
	 * @param profile
	 * @param branchUrl
	 * @return
	 */
	boolean addNewBranch(String projectCode, String profile, String branchUrl);

	/**
	 * 分支编辑  保存  
	 * @param id
	 * @param branchUrl
	 * @param currentVersion
	 * @return
	 */
	boolean editBranch(String projectCode, String profile, String id, String branchUrl);
	/**
	 * 删除已提交分支
	 * @param projectCode
	 * @return
	 */
	int deletebranchesByProjectCode(String projectCode);
	/**
	 * 刷新分支版本
	 * @param svnBranchConfigs
	 */
	boolean refreshBranchesVersion(List<SvnBranchConfig> svnBranchConfigs);

	/**
	 * 
	 * @param projectCode
	 * @param profile
	 * @param id
	 * @return ReturnCodeEnum
	 * 			OK_DELETEBRANCH, NO_DELETEBRANCH
	 */
	ReturnCodeEnum deleteBranch(String projectCode, String profile, String id);

	
}
