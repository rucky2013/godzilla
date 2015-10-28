package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.dao.ProjectMapper;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnService;
import cn.godzilla.svn.BaseShellCommand;
import cn.godzilla.web.GodzillaApplication;

@Service("projectService")
public class ProjectServiceImpl extends GodzillaApplication implements ProjectService {

	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private SvnService svnService;
	@Autowired
	private BaseShellCommand command;
	@Autowired
	private ClientConfigService clientConfigService;



	@Override
	public Project qureyByProCode(String projectCode) {
		
		return projectMapper.qureyByProCode(projectCode);
	}

	@Override
	public List<Project> queryAll() {
		
		return projectMapper.queryAll();
	}

	@Override
	public ReturnCodeEnum srcEdit(String srcId, String repositoryUrl, String checkoutPath, String projectCode, String profile) {
		/**
		 * 1.update trunk version
		 */
		ReturnCodeEnum versionreturn = svnService.getVersion(repositoryUrl, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return versionreturn;
		}
		String version = svnVersionThreadLocal.get();
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("srcId", srcId);
		parameterMap.put("repositoryUrl", repositoryUrl);
		parameterMap.put("checkoutPath", checkoutPath);
		parameterMap.put("version", version);
		
		int index = projectMapper.updateProjectById(parameterMap);
		/**
		 * 2.update branches version
		 */
		
		if(index>0) {
			return ReturnCodeEnum.getByReturnCode(NO_SRCEDIT);
		} else {
			return ReturnCodeEnum.getByReturnCode(OK_SRCEDIT);
		}
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
		int index = projectMapper.updateVersionByProjectcode(parameterMap);
		
		return index>0;
	}

	@Override
	public List<Project> queryProjectsByUsername(String username) {
		List<Project> projects = projectMapper.queryProjectsByUsername(username);
		return projects;
	}

	@Override
	public ReturnCodeEnum godzillaCommand(String actiion) {
		
		String str = "sh /home/godzilla/gzl/shell/server/godzilla.sh " + actiion;
		boolean flag = false;
		flag = command.execute(str, super.getUser().getUserName(), "", "", "");
		
		if(flag) {
			return ReturnCodeEnum.getByReturnCode(OK_GODZILLA);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_GODZILLA);
		}
	}

}
