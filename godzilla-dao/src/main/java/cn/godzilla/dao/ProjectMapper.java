package cn.godzilla.dao;

import java.util.List;

import cn.godzilla.model.Project;

public interface ProjectMapper {
	
    int insert(Project record);

    int insertSelective(Project record);

	List<Project> queryProjectsByUsername(String username);
}