package cn.godzilla.service;

import java.util.List;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.SvnBranchConfig;

public interface SvnBranchConfigService {
	
	
	List<SvnBranchConfig> queryListByProjectCode(String projectCode, String profile);

	/**
	 * 分支设置-->添加分支  
	 * 注：20151103 清除project表 merge_status标记,初始化为0
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
	 * 注：20151103 清除project表 merge_status标记,初始化为0
	 * @param projectCode
	 * @param id 
	 * @return
	 */
	ReturnCodeEnum deletebranchesByProjectCode(String projectCode, String profile);
	/**
	 * 删除已提交分支
	 * 注：20151103 清除project表 merge_status标记,初始化为0
	 * @param projectCode
	 * @param id 
	 * @return
	 */
	ReturnCodeEnum deletebranchesByProjectCode(String projectCode, String profile, String id);
	/**
	 * 刷新分支版本
	 * @param svnBranchConfigs
	 */
	ReturnCodeEnum refreshBranchesVersion(String projectCode, String profile, List<SvnBranchConfig> svnBranchConfigs);

	
}
