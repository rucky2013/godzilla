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
		
		//mq producer start 将shell输出 存入mq
		Producer.start();
		
	}
	
	public static void main(String args[]) throws Exception {
		start();
	}
}
