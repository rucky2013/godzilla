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
@RequestMapping("project")
public class ProjectController {
	
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
	 * 查询项目列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="list" ,method = RequestMethod.GET)
	@ResponseBody
	public List<Project> list(HttpServletRequest request,HttpServletResponse response){
		
		logger.info("*********查询项目列表*********");
		return projectService.queryAll();
	}
	
	
	
	/**
	 * 根据项目编号、环境类型查询远程客户端IP等信息
	 * @param projectCode 项目编号
	 * @param profile 环境类型
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "client/{projectCode}/{profile}", method = RequestMethod.GET)
	@ResponseBody
	public ClientConfig getClient(
			@PathVariable("projectCode") String projectCode,
			@PathVariable("profile") String profile,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("***********查询客户端配置*********projectCode："+projectCode+",profile:"+profile);
		return clientConfigService.queryDetail(projectCode, profile);
	}
	
	/**
	 * 根据项目编码，环境类型，操作人，查询项目状态
	 * @param projectCode 项目编码
	 * @param operateStaff 操作人
	 * @param profile 环境类型
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "st/{projectCode}/{operateStaff}/{profile}", method = RequestMethod.GET)
	@ResponseBody
	public ProjStatus getStatus(
			@PathVariable("projectCode") String projectCode,
			@PathVariable("operateStaff") String operateStaff,
			@PathVariable("profile") String profile,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("**********查询项目状态********projectCode:" + projectCode
				+  ",operateStaff:" + operateStaff);
		return projStatusService
				.queryDetail(projectCode, operateStaff,profile);
	}
	
	/**
	 * 更新项目状态
	 * @param projectCode
	 * @param operateStaff
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "st/save/{projectCode}/{operateStaff}/{status}", method = RequestMethod.POST)
	@ResponseBody
	public ProjStatus putStatus(@PathVariable("projectCode") String projectCode,
			@PathVariable("operateStaff") String operateStaff,
			@PathVariable("status") Integer status,
			HttpServletRequest request, HttpServletResponse response){
		
		ProjStatus record = new ProjStatus();
		record.setProjectCode(projectCode);
		record.setOperateStaff(operateStaff);
		record.setCurrentStatus(status);
		
		logger.info("**********更新项目状态********projectCode:" + projectCode + ",operateStaff:" + operateStaff);
		
		return projStatusService.save(record);
	}
	
	/**
	 * 查询操作日志
	 * @param projectCode 项目编码
	 * @param profile 环境类型
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "logs/{projectCode}/{profile}", method = RequestMethod.GET)
	@ResponseBody
	public List<OperateLog> getOperateLogs(
			@PathVariable("projectCode") String projectCode,
			@PathVariable("profile") String profile,
			HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("*************查询操作日志************projectCode:"+projectCode+",profile:"+profile);
		
		return operateLogService.queryList(projectCode, profile);
		 
	}
	
	/**
	 * 查询冲突列表
	 * @param projectCode 项目编号
	 * @param branchUrl 分支URL
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "conflicts" ,method = RequestMethod.GET)
	@ResponseBody
	public List<SvnConflict> getSvnConflicts(
			@RequestParam("projectCode") String projectCode,
			@RequestParam("branchUrl") String branchUrl,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("************查看冲突列表*************projectCode:"+projectCode+",branchUrl:"+branchUrl);
		return svnConflictService.queryList(projectCode, branchUrl);
	}

	/**
	 * 项目新增
	 * @param projectCode
	 * @param projectName
	 * @param repositoryUrl
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Project addProject(@RequestParam("projectCode") String projectCode,
			@RequestParam("projectName") String projectName,
			@RequestParam("repositoryUrl") String repositoryUrl,
			HttpServletRequest request, HttpServletResponse response) {
		
		Project project = new Project();
		
		project.setProjectCode(projectCode);
		
		project.setProjectName(projectName);
		
		project.setRepositoryUrl(repositoryUrl);
		
		projectService.insert(project);

		return projectService.qureyByProCode(projectCode);
	}
	
	/**
	 * 项目 insert or update
	 * @param projectCode
	 * @param projectName
	 * @param repositoryUrl
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.PUT)
	@ResponseBody
	public Project saveProject(@RequestParam("projectCode") String projectCode,
			@RequestParam("projectName") String projectName,
			@RequestParam("repositoryUrl") String repositoryUrl,
			HttpServletRequest request, HttpServletResponse response) {
		
		Project project = new Project();
		
		project.setProjectCode(projectCode);
		
		project.setProjectName(projectName);
		
		project.setRepositoryUrl(repositoryUrl);
		
		return projectService.save(project);
		 
	}
	/**
	 * 项目修改
	 * @param projectCode
	 * @param projectName
	 * @param repositoryUrl
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "modify", method = RequestMethod.PUT)
	@ResponseBody
	public Project updateProject(@RequestParam("projectCode") String projectCode,
			@RequestParam("projectName") String projectName,
			@RequestParam("repositoryUrl") String repositoryUrl,
			HttpServletRequest request, HttpServletResponse response){
		
		Project project = new Project();
		
		project.setProjectCode(projectCode);
		
		project.setProjectName(projectName);
		
		project.setRepositoryUrl(repositoryUrl);
		
		int result = projectService.insertSelective(project);
		
		if(result > 0){
			return projectService.qureyByProCode(projectCode);
		}
		return null;
	}
	@RequestMapping(value = "check", method = RequestMethod.GET)
	public ModelAndView checkPorject(@RequestParam("projectCode") String projectCode,
			@RequestParam("profile") String profile,
			HttpServletRequest request, HttpServletResponse response){
		
		
		
		ModelAndView view = new ModelAndView();
		
		view.setViewName("project");
		
		if(StringUtil.isEmpty(projectCode)){
			view.setViewName("index");
			return view;
		}
		if(StringUtil.isEmpty(profile)){
			
			profile = Constant.PROFILE_TEST ;  //TEST
		}
		
		ClientConfig client = clientConfigService.queryDetail(projectCode, profile) ;
		
		String remoteIp = client.getRemoteIp() ;
		
		Project project = projectService.qureyByProCode(projectCode);
		
		String repositoryUrl = project.getRepositoryUrl();
		
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		
		List<OperateLog> operateLogs = operateLogService.queryList(projectCode, profile);
		
		ProjStatus projStatus =  projStatusService.queryDetail(projectCode, "lizw",profile);
		
		if(projStatus == null){
			
			projStatus = new ProjStatus();
			projStatus.setCurrentStatus(0);
			projStatus.setProcessRate(0);
		}
		
		ProjPrivate projPrivate = projPrivateService.queryDetail(projectCode, "lizw");
		
		if(projPrivate == null){
			projPrivate = new ProjPrivate();
			projPrivate.setIfVirtual(Constant.FALSE);  //默认未启用
		}
		
		request.setAttribute("remoteIp", remoteIp);
		request.setAttribute("repositoryUrl", repositoryUrl);
		request.setAttribute("projectCode", projectCode);
		request.setAttribute("svnBranchConfigs", svnBranchConfigs);
		request.setAttribute("operateLogs", operateLogs);
		request.setAttribute("projStatus", projStatus);
		request.setAttribute("profile", profile);
		request.setAttribute("projPrivate", projPrivate);
		
		return view ;
	}
	
}
