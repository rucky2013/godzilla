package cn.godzilla.web;

import java.io.IOException;

import io.netty.channel.DefaultAddressedEnvelope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.rocketmq.client.exception.MQClientException;

import cn.godzilla.echo.rocketmq.Consumer;
import cn.godzilla.echo.websocket.WebSocketServer;

@Component
@RequestMapping("/demo")
public class WebsocketController extends GodzillaApplication implements ApplicationListener {

	private final Logger logger = LogManager.getLogger(WebsocketController.class);

	// demo websocket
	@RequestMapping(method=RequestMethod.GET)
	public Object welcome(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("*****WebsocketController.welcome*****");
		return "/echo/websocket_test";
	}
	
	/*public static void start() throws Exception {
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
	}*/

	private boolean isStart = false;
	
	private WebSocketServer websocket = new WebSocketServer(DEFAULT_WEBSOCKET_PORT);
	/**
	 * 
	 * 1.容器启动时,启动websocket监听
	 *  
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ContextRefreshedEvent) {
			if(isEcho) this.start();
		} else if(event instanceof ContextClosedEvent) {
			if(isEcho) this.stop();
		}
	}
	
	public void start() {
		if(!isStart){
			logger.info("*****websocket starting   ...*****");
			logger.info("*****                     ...");
			logger.info("*****                     ...");
			
			new Thread(new Runnable() {
				@Override
				public void run() {
						try {
							websocket.run();
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}).start();
			logger.info("*****websocket started!*****");
			
			//mq consumer start 将mq中存入的shell输出 发到 界面端
			try {
				Consumer.main(null);
			} catch (InterruptedException | MQClientException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		websocket.stop();
	}
}
