package cn.godzilla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.PropConfigService;

@Component
@RequestMapping("/mvn")
public class MvnController extends SuperController{
	
	private final Logger logger = LogManager.getLogger(MvnController.class);
	
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private MvnService mvnService;
	/**
	 * 部署
	 * 源码路径/项目名/环境类型/
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/deploy", method=RequestMethod.POST)
	@ResponseBody
	public Object deploy(@PathVariable String sid,@PathVariable String projectCode,@PathVariable String profile,HttpServletRequest request, HttpServletResponse response) {

		logger.debug("*****MvnController.deploy*****");
		String srcUrl = StringUtil.getReqPrameter(request, "srcUrl");
		ReturnCodeEnum deployReturn = mvnService.doDeploy(srcUrl, projectCode, profile);
		
		if(deployReturn == ReturnCodeEnum.OK_MVNDEPLOY) {
			return SUCCESS;
		} else if(deployReturn == ReturnCodeEnum.NO_MVNDEPLOY) {
			
		} else if(deployReturn == ReturnCodeEnum.NO_CHANGEPOM) {
			return FAILURE;
		} else {
			return FAILURE;
		}
		return FAILURE;
	}
	
}
