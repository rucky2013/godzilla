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

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.service.UserService;

@Controller
@RequestMapping("/demo")
public class WebsocketController {

	private final Logger logger = LogManager.getLogger(WebsocketController.class);

	// demo websocket
	@RequestMapping(method=RequestMethod.GET)
	public Object welcome(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("*****UserController.welcome*****");
		return "/websocket";
	}

}
