package cn.godzilla.service;

import java.util.List;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.Project;

public interface ProjectService{
	
	public int insert(Project project);

    public int insertSelective(Project project);
    
    public boolean updateByProCode(Project project);
    
    public Project qureyByProCode(String projectCode);
    
    public List<Project> queryAll();
    
    public Project save(Project project);

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
	boolean srcEdit(String srcId, String repositoryUrl, String checkoutPath, String projectCode, String profile);

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
}
