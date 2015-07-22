package cn.godzilla.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.godzilla.model.User;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.UserService;

import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/prop")
public class PropController {
 
	private final Logger logger = LogManager.getLogger(PropController.class);
	@Autowired
	UserService userService;
	@Autowired
	PropConfigService propConfigService;
	
	/**
	 * 进入配置修改页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode1}", method=RequestMethod.GET)
	public Object welcome(@PathVariable String sid, @PathVariable String projectCode1,HttpServletRequest request, HttpServletResponse response) {

		logger.debug("*****UserController.welcome*****");
		
		User user = new User();
		user.setUserName("aa");
		user.setLastLoginTime(new Date());
		user.setDepartName("dept");
		
		projectCode1 = "apollo";
		Map<String, Object> propTestMap = new HashMap<String, Object>();
		propTestMap.put("com.xuanyuan.crm.ip", "10.100.139.234");
		propTestMap.put("com.xuanyuan.timeout", "3000");
		propTestMap.put("com.xuanyuan.callphp.url", "www.baidu.com");
		String propTest = JSON.toJSONString(propTestMap);
		
		request.setAttribute("user", user);
		request.setAttribute("projectCode", "11");
		request.setAttribute("propTest", propTest);
		request.setAttribute("basePath", "godzilla-web");
		return "/query";
	}

}
