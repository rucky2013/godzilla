package cn.godzilla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.godzilla.svn.BaseShellCommand;
import cn.godzilla.web.util.PropertiesUtil;

@Controller
@RequestMapping(value = "tomcat")
public class TomcatController {

	private final static Logger logger = LogManager.getLogger(TomcatController.class);

	@RequestMapping(value = "restart", method = RequestMethod.GET)
	public void restart(@RequestParam("ip") String ip,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("*******重启Tomcat Start*******"+ip);

		// 暂时先不考虑权限

		BaseShellCommand command = new BaseShellCommand();

		String str = PropertiesUtil.getProperties().get("server.shell.restart.path") +" " + ip + " "
				+ PropertiesUtil.getProperties().get("client.tomcat.home.path");

		command.execute(str);
		
		logger.info("*******重启Tomcat End*******"+ip);
	}
}
