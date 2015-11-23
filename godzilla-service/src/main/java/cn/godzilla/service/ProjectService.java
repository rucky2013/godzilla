package cn.godzilla.service;

import java.util.List;
import java.util.Map;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.Project;

public interface ProjectService{
	
    
    public Project queryByProCode(String projectCode, String profile);
    
    public List<Project> queryAll(String projectCode, String profile);
    
    /**
     * 源代码设置-->添加或修改源代码设置
     * @param repositoryUrl
     * @param checkoutPath
     * @param projectCode
     * @param profile
     * @return
     */
    ReturnCodeEnum srcEdit(String projectCode, String profile, String repositoryUrl, String checkoutPath);

	/**
	 * 更新  项目 版本号
	 * @param projectCode
	 * @param profile
	 * @return
	 */
	public boolean refreshProjectVersion(String projectCode, String profile);

	public List<Project> queryProjectsByUsername(String projectCode, String profile, String username);

	/**
	 * 修改project表 merge_status标记
	 * 0：0:无 
	 * 1：1:有
	 * 2：2:标记解决
	 * @param parameterMap
	 */
	public ReturnCodeEnum editMergestatusByProjectCode(String projectCode, String profile, Map<String, String> parameterMap);

}
