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

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.model.OperateLog;
import cn.godzilla.model.Project;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.UserService;


@Controller
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
		request.setAttribute("sid", newsid);
		
		ReturnCodeEnum loginReturn = userService.login(username, password, newsid);  //do login 
		
		switch(loginReturn) {
		case NULL_NAMEPASSWORD: 
			request.setAttribute("errorcode", ReturnCodeEnum.NULL_NAMEPASSWORD.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.NULL_NAMEPASSWORD.getReturnMsg());
			return "/login";
		case NOTEXIST_USER:
			request.setAttribute("errorcode", ReturnCodeEnum.NOTEXIST_USER.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.NOTEXIST_USER.getReturnMsg());
			return "/login";
		case WRONG_PASSWORD:
			request.setAttribute("errorcode", ReturnCodeEnum.WRONG_PASSWORD.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.WRONG_PASSWORD.getReturnMsg());
			return "/login";
		case OK_LOGIN:
			List<Project> projects = projectService.queryAll();
			List<OperateLog> logs = operateLogService.queryAll(Long.MAX_VALUE);
			request.setAttribute("projects", projects);
			request.setAttribute("logs", logs);
			request.setAttribute("basePath", BASE_PATH);
			return "/index";
		default:
			request.setAttribute("errorcode", ReturnCodeEnum.OK_LOGIN.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.OK_LOGIN.getReturnMsg());
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
	@RequestMapping(value="/{sid}/logout", method=RequestMethod.GET)
	public Object logout(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("*****UserController.logout*****");
		userService.logout(sid);//del redis sid-username 
		return "/logout";
	}
	
	
	
}
