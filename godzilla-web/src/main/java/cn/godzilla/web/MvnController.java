package cn.godzilla.web;

import java.util.HashMap;
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
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnService;

@Component
@RequestMapping("/mvn")
public class MvnController extends GodzillaApplication{
	
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
	
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/process", method=RequestMethod.POST)
	@ResponseBody
	public Object process(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("*****MvnController.process*****");
		
		String processPercent = mvnService.getProcessPercent(sid, projectCode, profile);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("returncode", OK_AJAX);
		resultMap.put("returnmsg", SUCCESS);
		resultMap.put("processPercent", processPercent);		
		return resultMap;
	}
	
}
