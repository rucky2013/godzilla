package cn.godzilla.web;

import java.util.Date;
import java.util.List;

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
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.UserService;


@Component
@RequestMapping("/user")
public class UserController extends SuperController{
	
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
			List<Project> projects = projectService.queryAll();
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
		} else {
			request.setAttribute("errorcode", ReturnCodeEnum.OK_LOGIN.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.OK_LOGIN.getReturnMsg());
			request.setAttribute("basePath", BASE_PATH);
			return "/login";
		}
	}
	/**
	 * 退出
	 * 
	 * @param sid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/logout/{sid}", method=RequestMethod.GET)
	public Object logout(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("*****UserController.logout*****");
		userService.logout(sid);//del redis sid-username 
		request.setAttribute("basePath", BASE_PATH);
		return "/login";
	}
}
