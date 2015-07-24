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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.User;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.UserService;

import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/prop")
public class PropController extends SuperController implements Constant{
 
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
	@RequestMapping(value="/{sid}/{projectCode}", method=RequestMethod.GET)
	public Object updatePropPage(@PathVariable String sid, @PathVariable String projectCode,HttpServletRequest request) {

		logger.debug("*****PropController.updatePropPage*****");
		
		StringBuilder propTest = new StringBuilder("");
		StringBuilder propQuasiProduct = new StringBuilder("");
		StringBuilder propProduct = new StringBuilder("");
		
		propConfigService.findPropByUsername(projectCode, propTest, propQuasiProduct, propProduct);
		
		request.setAttribute("user", this.getUser());
		request.setAttribute("projectCode_", projectCode);
		request.setAttribute("propTest", propTest.toString());
		request.setAttribute("propQuasiProduct", propQuasiProduct.toString());
		request.setAttribute("propProduct", propProduct.toString());
		request.setAttribute("basePath", BASE_PATH);
		//request.setAttribute("sid", this.getSid());
		return "/query";
	}
	
	@RequestMapping(value="/{sid}/{projectCode}", method=RequestMethod.POST)
	@ResponseBody
	public Object updateProp(@PathVariable String sid, @PathVariable String projectCode,HttpServletRequest request) {
		
		String propTest = request.getParameter("p1");
		String propQuasiProduct = request.getParameter("p2");
		String propProduct = request.getParameter("p3");
		
		ReturnCodeEnum updateReturn = propConfigService.addOrUpdateProp(projectCode, propTest, propQuasiProduct, propProduct); 
		logger.info("****propTest:" + propTest);
		
		switch(updateReturn) {
		case OK_ADDUPDATEPROP:
			return SUCCESS;
		case NO_ADDUPDATEPROP:
			return FAILURE;
		}
		return FAILURE;
	}

}
