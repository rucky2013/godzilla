package cn.godzilla.echo.main;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import cn.godzilla.echo.rocketmq.Consumer;
import cn.godzilla.echo.rocketmq.Producer;
import cn.godzilla.echo.websocket.WebSocketServer;

@Component
public class MainClass {
	
	public static ConcurrentHashMap<String, Channel> channelsMap = 
			new ConcurrentHashMap<String, Channel>();

	static {
		try {
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		//mq producer start 将shell输出 存入mq
		Producer.start();
		//mq consumer start 将mq中存入的shell输出 发到 界面端
		Consumer.main(null);
	}
	
	public static void main(String args[]) throws Exception {
		start();
	}
}
