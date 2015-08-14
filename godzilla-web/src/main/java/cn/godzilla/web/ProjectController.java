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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.OperateLog;
import cn.godzilla.model.ProjPrivate;
import cn.godzilla.model.ProjStatus;
import cn.godzilla.model.Project;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.model.SvnConflict;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjPrivateService;
import cn.godzilla.service.ProjStatusService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.service.SvnConflictService;


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
	ProjStatusService projStatusService;
	
	@Autowired
	OperateLogService operateLogService;
	
	@Autowired
	SvnConflictService svnConflictService;
	
	@Autowired
	ProjPrivateService projPrivateService;
	
	private final Logger logger = LogManager.getLogger(ProjectController.class);
	
	/**
	 * 源代码设置 -->添加或修改源代码设置
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/srcEdit", method = RequestMethod.GET)
	@ResponseBody
	public Object srcEdit(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request) {
		
		//权限验证??
		String srcId = StringUtil.getReqPrameter(request, "srcId");
		String repositoryUrl = StringUtil.getReqPrameter(request, "repositoryUrl");
		String checkoutPath = StringUtil.getReqPrameter(request, "checkoutPath");
		
		boolean flag = projectService.srcEdit(srcId, repositoryUrl, checkoutPath, projectCode, profile);
		
		if(flag){
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SRCEDIT, SUCCESS, "源代码设置SUCCESS");
			logger.info("************源代码设置End**************");
			return SUCCESS;
		}else{
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SRCEDIT, FAILURE, "源代码设置FAILURE");
			logger.error("************源代码设置Error**************");
			return FAILURE;
		}
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
		
		//权限验证??
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		Project project = projectService.qureyByProCode(projectCode);
		//刷新项目 版本
		projectService.refreshProjectVersion(projectCode, profile);
		project = projectService.qureyByProCode(projectCode);
		//刷新分支 版本
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
