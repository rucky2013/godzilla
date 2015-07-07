package cn.godzilla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.godzilla.common.Constant;
import cn.godzilla.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController implements Constant{

	private static final String ReturnCodeEnum = null;

	private final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	UserService userService;

	// 登录页
	@RequestMapping(value="/welcome", method=RequestMethod.GET)
	public Object welcome(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("*****UserController.welcome*****");
		return "/welcome";
	}

	//登录
	@RequestMapping(value="/login/{username}/{password}", method=RequestMethod.GET)
	public Object login(@PathVariable String username, @PathVariable String password, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("*****UserController.login*****");
		String sid = 
		ReturnCodeEnum loginReturn = userService.login(username, password);  //do login 
		
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
			return "/loginsuccess";
		default:
			request.setAttribute("errorcode", ReturnCodeEnum.OK_LOGIN.getReturnCode());
			request.setAttribute("errormsg", ReturnCodeEnum.OK_LOGIN.getReturnMsg());
			return "/login";
		}
	}
	
	@RequestMapping(value="/logout/{sid}", method=RequestMethod.GET)
	public Object logout(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("*****UserController.logout*****");
		
		//del redis sid-username 
		return "/logout";
	}

}
