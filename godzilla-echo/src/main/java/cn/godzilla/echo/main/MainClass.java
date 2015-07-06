package cn.godzilla.echo.main;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

import cn.godzilla.echo.rocketmq.Consumer;
import cn.godzilla.echo.rocketmq.Producer;
import cn.godzilla.echo.websocket.WebSocketServer;

public class MainClass {
	
	public static ConcurrentHashMap<String, Channel> channelsMap = 
			new ConcurrentHashMap<String, Channel>();

	public void start() throws Exception {
		//启动 websocket server监听服务
		WebSocketServer.main(null);
		//mq producer start 将shell输出 存入mq
		Producer.start();
		//mq consumer start 将mq中存入的shell输出 发到 界面端
		Consumer.main(null);
	}
}
