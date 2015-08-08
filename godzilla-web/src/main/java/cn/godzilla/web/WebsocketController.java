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

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.echo.rocketmq.Consumer;
import cn.godzilla.echo.websocket.WebSocketServer;
import cn.godzilla.service.UserService;

@Component
@RequestMapping("/demo")
public class WebsocketController {

	private final Logger logger = LogManager.getLogger(WebsocketController.class);

	// demo websocket
	@RequestMapping(method=RequestMethod.GET)
	public Object welcome(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("*****WebsocketController.welcome*****");
		return "/echo/websocket_test";
	}
	
	public static void start() throws Exception {
		//启动 websocket server监听服务
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					WebSocketServer.main(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		//mq consumer start 将mq中存入的shell输出 发到 界面端
		Consumer.main(null);
	}
}
