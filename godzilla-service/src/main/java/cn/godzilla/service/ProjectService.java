package cn.godzilla.service;

import java.util.List;

import cn.godzilla.model.Project;

public interface ProjectService{
	
	public int insert(Project project);

    public int insertSelective(Project project);
    
    public boolean updateByProCode(Project project);
    
    public Project qureyByProCode(String projectCode);
    
    public List<Project> queryAll();

}
