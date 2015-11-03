package cn.godzilla.dao;

import java.util.List;
import java.util.Map;

import cn.godzilla.model.Project;

public interface ProjectMapper {
	
    int insert(Project record);

    int insertSelective(Project record);

	List<Project> queryProjectsByUsername(String username);
    
    boolean updateByProCode(Project project);
    
    Project qureyByProCode(String projectCode);
    
    List<Project> queryAll();

	int updateProjectById(Map<String, String> parameterMap);

	int updateVersionByProjectcode(Map<String, String> parameterMap);

	int updateMergestatusByProjectcode(Map<String, String> parameterMap);
}