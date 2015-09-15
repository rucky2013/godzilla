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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;

@Controller
@RequestMapping(value = "tomcat")
public class TomcatController extends GodzillaApplication{

	private final static Logger logger = LogManager.getLogger(TomcatController.class);

	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private MvnService mvnService;
	@RequestMapping(value = "/{sid}/{projectCode}/{profile}/restart", method = RequestMethod.GET)
	@ResponseBody
	public Object restart(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
	
		logger.info("*******TomcatController.restart*******");
		boolean flag = false;
		
		flag = mvnService.restartTomcat(projectCode, profile);
		
		if(flag) {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, TOMCATRESTART, SUCCESS, "tomcat重启SUCCESS");
			return ResponseBodyJson.custom().setAll(OK_AJAX, SUCCESS, "").build();
		} else {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, TOMCATRESTART, FAILURE, "tomcat重启FAILURE");
			return ResponseBodyJson.custom().setAll(NO_AJAX, FAILURE, "").build();
		}
	}
	
}
