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

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.svn.BaseShellCommand;

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
		String tomcatHome = "";
		if("godzilla".equals(projectCode)) {
			clientIp = "10.100.142.65";
			tomcatHome = "/home/godzilla/tomcat-godzilla";
		} else {
			tomcatHome = "/app/tomcat";
		}
		String str = "sh /home/godzilla/gzl/shell/server/restart_server.sh " + clientIp + " " + tomcatHome;
		boolean flag = false;
		if("godzilla".equals(projectCode)) {
			flag = true;
		} else {
			flag = command.execute(str, super.getUser().getUserName());
		}
		
		/*
		 * 3. httpclient 访问  ip:8080/war_name/index.jsp   查找 是否存在 <!--<h5>godzilla</h5>--> 字符串 判断 tomcat是否启动成功
		 */
		Project project = projectService.qureyByProCode(projectCode);
		String warName = project.getWarName();
		String IP = clientIp;
		
		boolean flag4 = false;
		if(projectCode.equals("godzilla")) {
			flag4 = true;
		} else {
			flag4 = super.ifSuccessStartTomcat(IP, warName);
		}
		
		flag = flag && flag4;
		if(flag) {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, TOMCATRESTART, SUCCESS, "tomcat重启SUCCESS");
			return SUCCESS;
		} else {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, TOMCATRESTART, FAILURE, "tomcat重启FAILURE");
			return FAILURE;
		}
	}
}
