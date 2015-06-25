package cn.creditease.godzilla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.creditease.godzilla.common.StringUtil;
import cn.creditease.godzilla.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	UserService userService;

	// 登录页
	@RequestMapping("/welcome")
	public Object welcome(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("*****UserController.welcome*****");
		return "/welcome";
	}

	//登录
	@RequestMapping("/login")
	public Object login(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("*****UserController.login*****");
		String username = StringUtil.getReqPrameter(request, "username");
		String password = StringUtil.getReqPrameter(request, "password");
		//登录验证状态
		int loginState = userService.checkUser(username, password);
		switch(loginState) {
		case -1: 
			request.setAttribute("errorcode", "10001");
			request.setAttribute("errormsg", "用户名或密码为空！");
			return "/login";
		case -2:
			request.setAttribute("errorcode", "10002");
			request.setAttribute("errormsg", "用户名不存在！");
			return "/login";
		case -3:
			request.setAttribute("errorcode", "10003");
			request.setAttribute("errormsg", "密码错误！");
			return "/login";
		default:
			request.setAttribute("errorcode", "10004");
			request.setAttribute("errormsg", "未知错误！");
			return "/login";
		case 0:
			return "/loginsuccess";
		}

	}

}
