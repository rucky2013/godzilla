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
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.OperateLogService;

@Component
@RequestMapping("/mvn")
public class MvnController extends GodzillaApplication{
	
	private final Logger logger = LogManager.getLogger(MvnController.class);
	
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private MvnService mvnService;
	@Autowired
	private OperateLogService operateLogService;
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
		String parentVersion = StringUtil.getReqPrameter(request, "parentVersion", "");
		String parentVersionSuffix = StringUtil.getReqPrameter(request, "parentVersionSuffix", "");
		
		String pencentkey = sid + "-" + projectCode + "-" + profile;
		processPercent.put(pencentkey, "0");
		ReturnCodeEnum deployReturn = mvnService.doDeploy(srcUrl, projectCode, profile, parentVersion+parentVersionSuffix);
		
		processPercent.put(pencentkey, "100");
		
		operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, DEPLOY, FAILURE, deployReturn.getReturnMsg());
		return ResponseBodyJson.custom().setAll(deployReturn).build();
	}
	
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/process", method=RequestMethod.POST)
	@ResponseBody
	public Object process(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("*****MvnController.process*****");
		
		String processPercent = mvnService.getProcessPercent(sid, projectCode, profile);
		if(processPercent.equals("100")) {
			String pencentkey = sid + "-" + projectCode + "-" + profile;
			super.processPercent.put(pencentkey, "0");
		}
		return ResponseBodyJson.custom().setAll(OK_AJAX, SUCCESS, processPercent).build();
	}
	
}
