package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.tools.javac.resources.version;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.dao.ProjectMapper;
import cn.godzilla.model.Project;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnService;
import cn.godzilla.web.GodzillaApplication;

@Service("projectService")
public class ProjectServiceImpl extends GodzillaApplication implements ProjectService {

	@Autowired
	private ProjectMapper dao;
	@Autowired
	private SvnService svnService;
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

	@Override
	public Project save(Project project) {
		
		Project result = dao.qureyByProCode(project.getProjectCode());
		
		if(result != null && result.getId() > 0){
			
			dao.updateByProCode(project);
			
		}else{
			dao.insertSelective(project);
		}
		return dao.qureyByProCode(project.getProjectCode());
	}
	@Override
	public boolean srcEdit(String srcId, String repositoryUrl, String checkoutPath, String projectCode, String profile) {
		
		ReturnCodeEnum versionreturn = svnService.getVersion(repositoryUrl, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return false;
		}
		String version = svnVersionThreadLocal.get();
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("srcId", srcId);
		parameterMap.put("repositoryUrl", repositoryUrl);
		parameterMap.put("checkoutPath", checkoutPath);
		parameterMap.put("version", version);
		
		int index = dao.updateProjectById(parameterMap);
		
		return index>0;
	}
	
	public boolean refreshProjectVersion(String projectCode, String profile) {
		Project project = this.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		
		ReturnCodeEnum versionreturn = svnService.getVersion(trunkPath, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return false;
		}
		String version = svnVersionThreadLocal.get();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("project_code", projectCode);
		parameterMap.put("version", version);
		int index = dao.updateVersionByProjectcode(parameterMap);
		
		return index>0;
	}

	@Override
	public List<Project> queryProjectsByUsername(String username) {
		List<Project> projects = dao.queryProjectsByUsername(username);
		return projects;
	}

}
