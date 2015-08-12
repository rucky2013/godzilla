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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.model.ClientConfig;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.svn.BaseShellCommand;
import cn.godzilla.web.util.PropertiesUtil;

@Controller
@RequestMapping(value = "tomcat")
public class TomcatController extends GodzillaApplication{

	private final static Logger logger = LogManager.getLogger(TomcatController.class);

	@Autowired
	private ClientConfigService clientConfigService;
	
	@RequestMapping(value = "/{sid}/{projectCode}/{profile}/restart", method = RequestMethod.GET)
	@ResponseBody
	public Object restart(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
	
		logger.info("*******TomcatController.restart*******");
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		// 暂时先不考虑权限

		BaseShellCommand command = new BaseShellCommand();

		/*String str = PropertiesUtil.getProperties().get("server.shell.restart.path") +" " + clientIp + " "
				+ PropertiesUtil.getProperties().get("client.tomcat.home.path");*/
		if("godzilla".equals(projectCode)) {
			clientIp = "10.100.142.65";
		}
		String str = "/home/godzilla/gzl/shell/server/restart_server.sh " + clientIp + " /home/godzilla/tomcat-godzilla";
		boolean flag = command.execute(str, super.getUser().getUserName());
		if(flag) {
			return SUCCESS;
		} else {
			return FAILURE;
		}
	}
}
