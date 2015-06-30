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
@RequestMapping("proj")
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
	 * 根据项目编号projectCode，查询分支列表
	 * @param projectCode 项目编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="branchs/{projectCode}" ,method = RequestMethod.GET)
	@ResponseBody
	public List<SvnBranchConfig> getBranchs(@PathVariable("projectCode") String projectCode ,HttpServletRequest request,HttpServletResponse response){
		
		logger.info("***********查询分支列表********projectCode:"+projectCode);
		return svnBranchConfigService.queryListByProjectCode(projectCode) ;
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
	 * @param profile 环境类型
	 * @param operateStaff 操作人
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "st/{projectCode}/{profile}/{operateStaff}", method = RequestMethod.GET)
	@ResponseBody
	public ProjStatus getStatus(
			@PathVariable("projectCode") String projectCode,
			@PathVariable("profile") String profile,
			@PathVariable("operateStaff") String operateStaff,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("**********查询项目状态********projectCode:" + projectCode
				+ ",profile:" + profile + ",operateStaff:" + operateStaff);
		return projStatusService
				.queryDetail(projectCode, profile, operateStaff);
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
	 * 查看私有配置 是否启用虚拟主干等
	 * @param projectCode 项目编号
	 * @param userName 用户名
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "private/{projectCode}/{userName}" ,method = RequestMethod.GET)
	@ResponseBody
	public ProjPrivate getProjPrivate(
			@PathVariable("projectCode") String projectCode,
			@PathVariable("userName") String userName,
			HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("**************查看私有配置***********projectCode:"+projectCode+",userName:"+userName);
		
		return projPrivateService.queryDetail(projectCode,userName);
	}
}
