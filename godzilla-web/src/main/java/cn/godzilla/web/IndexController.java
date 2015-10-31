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
public class IndexController extends GodzillaApplication{
	
	private final Logger logger = LogManager.getLogger(IndexController.class);
	@Autowired
	private ProjectService projectService ;
	@Autowired
	private OperateLogService operateLogService ;
	@Autowired
	ClientConfigService clientConfigService;
	@Autowired
	SvnBranchConfigService svnBranchConfigService ;
	@Autowired
	MvnService mvnService;
	/**
	 * 跳到登录页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public Object loginPage1(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("basePath", BASE_PATH);
		return "/login";
	}
	
	/**
	 * 目前gesilla 不需要判断是否热部署
	 * 跳到index页
	 * 用途：判断热部署是否成功
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public Object loginPage2(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("basePath", BASE_PATH);
		return "/login";
	}
	
	/**
	 * 主界面
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/project/{sid}/{projectCode}/{profile}/projectConfig", method = RequestMethod.GET)
	public Object projectConfig(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request) {
		
		//权限验证??
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		
		//刷新项目 版本
		projectService.refreshProjectVersion(projectCode, profile);
		Project project = projectService.qureyByProCode(projectCode);
		//刷新分支 版本
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		svnBranchConfigService.refreshBranchesVersion(svnBranchConfigs);
		svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		List<OperateLog> operateLogs = operateLogService.queryList(projectCode, profile);
		
		request.setAttribute("username", GodzillaApplication.getUser().getUserName());
		request.setAttribute("clientConfig", clientConfig);
		request.setAttribute("svnBranchConfigs", svnBranchConfigs);
		request.setAttribute("operateLogs", operateLogs);
		request.setAttribute("project", project);
		request.setAttribute("basePath", BASE_PATH);
		request.setAttribute("user", super.getUser());
		return "gesila1";
	}
	
	/**
	 * 下载war包
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/project/{sid}/{projectCode}/{profile}/download", method=RequestMethod.GET) 
	public Object download(@PathVariable("sid") String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletResponse response) {
		
		ReturnCodeEnum returnEnum = mvnService.downLoadWar(response, projectCode, profile);
		
		ResponseBodyJson.custom().setAll(returnEnum, WARDOWNLOAD).build().log();
		
		return null;
	}
	/**
	 * 显示打包命令执行信息
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param logid
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/project/{sid}/{projectCode}/{profile}/showdeployLog", method=RequestMethod.POST) 
	@ResponseBody
	public Object showdeployLog(@PathVariable("sid") String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("logid") String logid, HttpServletResponse response) {
		
		ReturnCodeEnum returnEnum = mvnService.showdeployLog(response, projectCode, profile, logid);
		return ResponseBodyJson.custom().setAll(returnEnum, SHOWDEPLOYLOG).build().log();
	}
	/**
	 * 显示war包信息
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param logid
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/project/{sid}/{projectCode}/{profile}/showwarInfo", method=RequestMethod.POST) 
	@ResponseBody
	public Object showwarInfo(@PathVariable("sid") String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("logid") String logid, HttpServletResponse response) {
		
		ReturnCodeEnum returnEnum = mvnService.showwarInfo(response, projectCode, profile, logid);
		return ResponseBodyJson.custom().setAll(returnEnum, SHOWWARINFO).build().log();
	}
	
	
	/**
	 * 项目重新启动(tomcat)
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/project/{sid}/{projectCode}/{profile}/restart", method = RequestMethod.GET)
	@ResponseBody
	public Object restart(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
	
		ReturnCodeEnum returnEnum = mvnService.restartTomcat(projectCode, profile);
		
		return ResponseBodyJson.custom().setAll(returnEnum, TOMCATRESTART).build().log();
	}
	
	/**
	 * 源代码设置 -->添加或修改源代码设置
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/project/{sid}/{projectCode}/{profile}/srcEdit", method = RequestMethod.POST)
	@ResponseBody
	public Object srcEdit(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request) {
		
		String srcId = StringUtil.getReqPrameter(request, "srcId");
		String repositoryUrl = StringUtil.getReqPrameter(request, "repositoryUrl");
		String checkoutPath = StringUtil.getReqPrameter(request, "checkoutPath");
		
		ReturnCodeEnum returnEnum = projectService.srcEdit(srcId, repositoryUrl, checkoutPath, projectCode, profile);
		
		return ResponseBodyJson.custom().setAll(returnEnum, SRCEDIT).build().log();
	}
}
