package cn.godzilla.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.response.ResponseBodyJson;
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
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/welcome", method=RequestMethod.GET)
	public Object loginPage(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("basePath", BASE_PATH);
		return "/login";
	}

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return sid
	 * NULL_NAMEPASSWORD, NOTEXIST_USER, WRONG_PASSWORD
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public Object login(HttpServletRequest request, HttpServletResponse response) {
		String newsid = StringUtil.getRandom(6);
		super.initContextBySid(newsid);
		String username = StringUtil.getReqPrameter(request, "username");
		String password = StringUtil.getReqPrameter(request, "password");

		ReturnCodeEnum loginReturn = userService.login(username, password, newsid);  
		
		request.setAttribute("sid", newsid);
		return ResponseBodyJson.custom().setAll(loginReturn, newsid, LOGIN).build().log();
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
	 * @param sid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/logout", method=RequestMethod.GET)
	public Object logout(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		userService.logout(sid);//del redis sid-username 
		request.setAttribute("basePath", BASE_PATH);
		return "/login";
	}
	
	/**
	 * 编辑工作台页面  显示权限列表
	 * @param sid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/userAuthList", method=RequestMethod.GET)
	public Object authPage(@PathVariable String sid, HttpServletRequest request) {
		
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
	/**
	 * 添加用户
	 * @param sid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/addUser", method=RequestMethod.POST)
	@ResponseBody
	public Object addUser(@PathVariable String sid, HttpServletRequest request) {
		
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
		
		return ResponseBodyJson.custom().setAll(returnEnum, ADDUSER).build().log();
	}
	@RequestMapping(value="/{sid}/changePassword", method=RequestMethod.POST)
	@ResponseBody
	public Object changePassword(@PathVariable String sid, @RequestParam String oldpassword, @RequestParam String password, HttpServletRequest request) {
		
		ReturnCodeEnum returnEnum = userService.changePassword(getUser(), oldpassword, password);
		
		return ResponseBodyJson.custom().setAll(returnEnum, CHANGEPASSWD).build().log();
	}
	
	/**
	 * 编辑工作台页面   显示窗口
	 * @param sid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/editWorkDesk", method=RequestMethod.GET)
	public Object editWorkDeskPage(@PathVariable String sid, HttpServletRequest request) {
		
		User user = super.getUser();
		if(!"1".equals(user.getIsAdmin()+"")) {
			return "forward:/user/" + sid + "/home";
		}
		String id = StringUtil.getReqPrameter(request, "id", "");
		if(StringUtil.isEmpty(id)) {
			return "forward:/user/" + sid + "/home";
		}
		User edituser = userService.getUserById(id);
		List<Map<String, Object>> userAuthList = userService.getUserAuthList();
		List<Map<String, Object>> userProjects = userService.getUserProjects(edituser.getUserName());
		
		request.setAttribute("userAuthList", userAuthList);
		request.setAttribute("userProjects", userProjects);
		request.setAttribute("editUsername", edituser.getUserName());
		request.setAttribute("user", super.getUser());
		request.setAttribute("basePath", BASE_PATH);
		return "/editWorkDeskPage";
	}
	
	/**
	 * 编辑工作台页面  提交内容
	 * @param sid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/editWorkDesk", method=RequestMethod.POST)
	@ResponseBody
	public Object editWorkDesk(@PathVariable String sid, HttpServletRequest request) {
		
		User user = super.getUser();
		if(!"1".equals(user.getIsAdmin()+"")) {
			return FAILURE;
		}
		
		String selectProjects = StringUtil.getReqPrameter(request, "selectProjects", "");
		String editUsername = StringUtil.getReqPrameter(request, "editUsername", "");
		ReturnCodeEnum returnEnum = userService.updateUserProjects(editUsername, selectProjects);
		
		return ResponseBodyJson.custom().setAll(returnEnum, EDITWORKDESK).build().log();
	}
}
