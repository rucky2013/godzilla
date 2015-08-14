package cn.godzilla.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.model.OperateLog;
import cn.godzilla.model.Project;
import cn.godzilla.model.User;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.UserService;


@Component
@RequestMapping("/user")
public class UserController extends GodzillaApplication{
	
	public UserController() {
		super();
	}
	private final Logger logger = LogManager.getLogger(UserController.class);
	@Autowired
	UserService userService;
	@Autowired
	PropConfigService propConfigService;
	@Autowired
	private ProjectService projectService ;
	@Autowired
	private OperateLogService operateLogService ;
	
	/**
	 * 登录页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/welcome", method=RequestMethod.GET)
	public Object loginPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("*****UserController.welcome*****");
		request.setAttribute("basePath", BASE_PATH);
		return "/login";
	}
	

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return sid
	 */
	@RequestMapping(value="/login/{username}/{password}", method=RequestMethod.GET)
	public Object login(@PathVariable String username, @PathVariable String password, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("*****UserController.login*****");
		String newsid = StringUtil.getRandom(6);
		logger.info("++|++|++>sid:" + newsid);
		super.initContextBySid(newsid);
		request.setAttribute("sid", newsid);
		//do login 
		ReturnCodeEnum loginReturn = userService.login(username, password, newsid);  
		if(loginReturn == ReturnCodeEnum.NULL_NAMEPASSWORD) {
			request.setAttribute("errorcode", ReturnCodeEnum.NULL_NAMEPASSWORD.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.NULL_NAMEPASSWORD.getReturnMsg());
			request.setAttribute("basePath", BASE_PATH);
			return "/login";
		} else if(loginReturn == ReturnCodeEnum.NOTEXIST_USER) {
			request.setAttribute("errorcode", ReturnCodeEnum.NOTEXIST_USER.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.NOTEXIST_USER.getReturnMsg());
			request.setAttribute("basePath", BASE_PATH);
			return "/login";
		} else if(loginReturn == ReturnCodeEnum.WRONG_PASSWORD) {
			request.setAttribute("errorcode", ReturnCodeEnum.WRONG_PASSWORD.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.WRONG_PASSWORD.getReturnMsg());
			request.setAttribute("basePath", BASE_PATH);
			return "/login";
		} else if(loginReturn == ReturnCodeEnum.OK_LOGIN) {
			return "forward:/user/" + newsid + "/home";
		} else {
			request.setAttribute("errorcode", ReturnCodeEnum.OK_LOGIN.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.OK_LOGIN.getReturnMsg());
			request.setAttribute("basePath", BASE_PATH);
			return "/login";
		}
	}
	
	/**
	 * 登陆后  工作台主页面
	 * @param sid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/home", method=RequestMethod.GET)
	public Object home(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		
		List<Project> projects = projectService.queryProjectsByUsername(super.getUser().getUserName());
		List<OperateLog> logs = operateLogService.queryAll(Long.MAX_VALUE);
		if(logs.size()==0){
			super.getUser().setLastOperation(null);
		} else {
			Date lastOpera = logs.get(logs.size()-1).getExecuteTime();
			super.getUser().setLastOperation(lastOpera);
		}
		
		request.setAttribute("projects", projects);
		request.setAttribute("logs", logs);
		request.setAttribute("basePath", BASE_PATH);
		request.setAttribute("user", super.getUser());
		
		return "/index";
	}
	
	
	/**
	 * 退出
	 * 
	 * @param sid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/logout", method=RequestMethod.GET)
	public Object logout(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("*****UserController.logout*****");
		userService.logout(sid);//del redis sid-username 
		request.setAttribute("basePath", BASE_PATH);
		return "/login";
	}
	
	//权限
	
	/**
	 * 显示权限主界面
	 * @param sid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/userAuthList", method=RequestMethod.GET)
	public Object authPage(@PathVariable String sid, HttpServletRequest request) {
		
		logger.debug("*****UserController.authPage*****");
		User user = super.getUser();
		if(!"1".equals(user.getIsAdmin()+"")) {
			return "forward:/user/" + sid + "/home";
		}
		List<Map<String, Object>> userAuthList = userService.getUserAuthList();
		
		request.setAttribute("userAuthList", userAuthList);
		request.setAttribute("user", super.getUser());
		request.setAttribute("basePath", BASE_PATH);
		return "/operation";
	}
	
	@RequestMapping(value="/{sid}/addUser", method=RequestMethod.GET)
	@ResponseBody
	public Object addUser(@PathVariable String sid, HttpServletRequest request) {
		
		logger.debug("*****UserController.addUser*****");
		User user = super.getUser();
		if(!"1".equals(user.getIsAdmin()+"")) {
			return FAILURE;
		}
		String username = StringUtil.getReqPrameter(request, "username", "");
		String password = StringUtil.getReqPrameter(request, "password", "");
		String confirm = StringUtil.getReqPrameter(request, "confirm", "");
		String departname = StringUtil.getReqPrameter(request, "departname", "");
		
		//password is md5 
		ReturnCodeEnum returnEnum = userService.addUser(username, password, confirm, departname);
		
		if(returnEnum.equals(ReturnCodeEnum.getByReturnCode(NULL_NAMEPASSWORD))) {
			return FAILURE;
		} else if(returnEnum.equals(ReturnCodeEnum.getByReturnCode(NO_ADDUSER))){
			return FAILURE;
		} else if(returnEnum.equals(ReturnCodeEnum.getByReturnCode(NO_SAMEPASSWORD))){
			return FAILURE;
		} else if(returnEnum.equals(ReturnCodeEnum.getByReturnCode(OK_ADDUSER))){
			return SUCCESS;
		} else {
			return SUCCESS;
		}
	}
	
	/**
	 * 显示编辑工作台页面   窗口
	 * @param sid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/editWorkDesk", method=RequestMethod.GET)
	public Object editWorkDeskPage(@PathVariable String sid, HttpServletRequest request) {
		
		logger.debug("*****UserController.editWorkDeskPage*****");
		User user = super.getUser();
		if(!"1".equals(user.getIsAdmin()+"")) {
			return "forward:/user/" + sid + "/home";
		}
		String editUsername = StringUtil.getReqPrameter(request, "editUsername", "");
		if(StringUtil.isEmpty(editUsername)) {
			return "forward:/user/" + sid + "/home";
		}
		List<Map<String, Object>> userAuthList = userService.getUserAuthList();
		List<Map<String, Object>> userProjects = userService.getUserProjects(editUsername);
		
		request.setAttribute("userAuthList", userAuthList);
		request.setAttribute("userProjects", userProjects);
		request.setAttribute("editUsername", editUsername);
		request.setAttribute("user", super.getUser());
		request.setAttribute("basePath", BASE_PATH);
		return "/operation02";
	}
	
	/**
	 * 提交工作台 内容
	 * @param sid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/editWorkDesk", method=RequestMethod.POST)
	@ResponseBody
	public Object editWorkDesk(@PathVariable String sid, HttpServletRequest request) {
		
		logger.debug("*****UserController.editWorkDesk*****");
		User user = super.getUser();
		if(!"1".equals(user.getIsAdmin()+"")) {
			return FAILURE;
		}
		
		String selectProjects = StringUtil.getReqPrameter(request, "selectProjects", "");
		String editUsername = StringUtil.getReqPrameter(request, "editUsername", "");
		ReturnCodeEnum returnEnum = userService.updateUserProjects(editUsername, selectProjects);
		
		if(returnEnum.equals(ReturnCodeEnum.getByReturnCode(NO_UPDATEFUNRIGHT))) {
			return FAILURE;
		} else if(returnEnum.equals(ReturnCodeEnum.getByReturnCode(OK_UPDATEFUNRIGHT))){
			return SUCCESS;
		} else {
			return FAILURE;
		}
	}
}
