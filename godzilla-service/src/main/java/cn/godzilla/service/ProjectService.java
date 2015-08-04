package cn.godzilla.service;

import java.util.List;

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
	 * @param deployVersion2 
	 * @return
	 */
	boolean srcEdit(String srcId, String repositoryUrl, String checkoutPath, String version, String deployVersion);

}
