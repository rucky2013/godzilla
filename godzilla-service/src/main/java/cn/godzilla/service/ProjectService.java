package cn.godzilla.service;

import java.util.List;
import java.util.Map;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.Project;

public interface ProjectService{
	
    
    public Project queryByProCode(String projectCode);
    
    public List<Project> queryAll();
    
    /**
	 * 源代码设置-->添加或修改源代码设置
	 * @param projectCode
	 * @param repositoryUrl
	 * @param checkoutPath
	 * @param version
	 * @param deployVersion
     * @param profile 
     * @param projectCode 
	 * @param deployVersion2 
	 * @return
	 */
    ReturnCodeEnum srcEdit(String srcId, String repositoryUrl, String checkoutPath, String projectCode, String profile);

	/**
	 * 更新  项目 版本号
	 * @param projectCode
	 * @param profile
	 * @return
	 */
	public boolean refreshProjectVersion(String projectCode, String profile);

	public List<Project> queryProjectsByUsername(String username);

	/**
	 * 
	 * @param string
	 * @return ReturnCodeEnum:
	 * 			OK_GODZILLA  , NO_GODZILLA
	 */
	public ReturnCodeEnum godzillaCommand(String string);
	/**
	 * 修改project表 merge_status标记
	 * 0：0:无 
	 * 1：1:有
	 * 2：2:标记解决
	 * @param parameterMap
	 */
	public ReturnCodeEnum editMergestatusByProjectCode(Map<String, String> parameterMap);

}
