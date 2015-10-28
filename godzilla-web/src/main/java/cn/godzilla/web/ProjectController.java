package cn.godzilla.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.OperateLog;
import cn.godzilla.model.Project;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnBranchConfigService;

@Controller
@RequestMapping("/project")
public class ProjectController extends GodzillaApplication implements Constant{
	
	@Autowired
	ProjectService projectService ;
	
	@Autowired
	ClientConfigService clientConfigService;
	
	@Autowired
	SvnBranchConfigService svnBranchConfigService ;
	
	@Autowired
	OperateLogService operateLogService;
	
	@Autowired
	MvnService mvnService;
	
	private final Logger logger = LogManager.getLogger(ProjectController.class);
	
	/**
	 * 源代码设置 -->添加或修改源代码设置
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/srcEdit", method = RequestMethod.POST)
	@ResponseBody
	public Object srcEdit(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request) {
		
		//权限验证??
		String srcId = StringUtil.getReqPrameter(request, "srcId");
		String repositoryUrl = StringUtil.getReqPrameter(request, "repositoryUrl");
		String checkoutPath = StringUtil.getReqPrameter(request, "checkoutPath");
		
		ReturnCodeEnum returnenum = projectService.srcEdit(srcId, repositoryUrl, checkoutPath, projectCode, profile);
		
		return ResponseBodyJson.custom().setAll(returnenum, SRCEDIT).build();
	}
	
	/**
	 * 主界面
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/projectConfig", method = RequestMethod.GET)
	public Object projectConfig(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request) {
		
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		Project project = projectService.qureyByProCode(projectCode);
		//太卡先不启用
		/**
		 * <#if project.state == '1'>
				<th colspan="2" align="left">已启动&nbsp;&nbsp;&nbsp;&nbsp;${projectCode}@${clientConfig.remoteIp!'error:没有配置clientConfig.remoteIp'}</th>
			<#else>
				<th colspan="2" align="left">未知状态&nbsp;&nbsp;&nbsp;&nbsp;${projectCode}@${clientConfig.remoteIp!'error:没有配置clientConfig.remoteIp'}</th>
			</#if>
		 */
		//projectService.refreshProjectState(project);
		
		projectService.refreshProjectVersion(projectCode, profile);
		
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		svnBranchConfigService.refreshBranchesVersion(svnBranchConfigs);
		svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		List<OperateLog> operateLogs = operateLogService.queryList(projectCode, profile);
		
		request.setAttribute("username", this.getUser().getUserName());
		request.setAttribute("clientConfig", clientConfig);
		request.setAttribute("svnBranchConfigs", svnBranchConfigs);
		request.setAttribute("operateLogs", operateLogs);
		request.setAttribute("project", project);
		request.setAttribute("basePath", BASE_PATH);
		request.setAttribute("user", super.getUser());
		return "gesila1";
	}
	
}
