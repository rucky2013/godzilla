package cn.godzilla.dao;

import java.util.List;

import cn.godzilla.model.Project;

public interface ProjectMapper {
    
    int insert(Project project);

    int insertSelective(Project project);
    
    boolean updateByProCode(Project project);
    
    Project qureyByProCode(String projectCode);
    
    List<Project> queryAll();
}