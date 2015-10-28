package cn.godzilla.service;

import java.util.List;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.SvnBranchConfig;

public interface SvnBranchConfigService {
	
	
	List<SvnBranchConfig> queryListByProjectCode(String projectCode);

	/**
	 * 分支设置-->添加分支  
	 * @param projectCode
	 * @param profile
	 * @param branchUrl
	 * @return
	 */
	ReturnCodeEnum addNewBranch(String projectCode, String profile, String branchUrl);

	/**
	 * 分支编辑  保存  
	 * @param id
	 * @param branchUrl
	 * @param currentVersion
	 * @return
	 */
	ReturnCodeEnum editBranch(String projectCode, String profile, String id, String branchUrl);
	/**
	 * 删除已提交分支
	 * @param projectCode
	 * @return
	 */
	ReturnCodeEnum deletebranchesByProjectCode(String projectCode);
	/**
	 * 刷新分支版本
	 * @param svnBranchConfigs
	 */
	ReturnCodeEnum refreshBranchesVersion(List<SvnBranchConfig> svnBranchConfigs);

	
}
