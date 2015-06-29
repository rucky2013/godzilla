package cn.godzilla.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.dao.ProjectMapper;
import cn.godzilla.model.Project;
import cn.godzilla.service.ProjectService;

public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectMapper dao;
	
	@Override
	public int insert(Project project) {
		
		return dao.insert(project);
	}

	@Override
	public int insertSelective(Project project) {
		
		return dao.insertSelective(project);
	}

	@Override
	public boolean updateByProCode(Project project) {
		
		return dao.updateByProCode(project);
	}

	@Override
	public Project qureyByProCode(String projectCode) {
		
		return dao.qureyByProCode(projectCode);
	}

	@Override
	public List<Project> queryAll() {
		
		return dao.queryAll();
	}

}
