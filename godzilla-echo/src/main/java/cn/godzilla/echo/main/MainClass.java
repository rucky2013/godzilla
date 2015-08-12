package cn.godzilla.echo.main;

import io.netty.channel.Channel;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.godzilla.common.Application;
import cn.godzilla.echo.rocketmq.Producer;

import com.alibaba.rocketmq.client.exception.MQClientException;

@Component
public class MainClass extends Application implements ApplicationListener{
	
	private final Logger logger = LogManager.getLogger(MainClass.class);
	
	public static ConcurrentHashMap<String, Channel> channelsMap = 
			new ConcurrentHashMap<String, Channel>();
	
	private boolean started = false;
	/**
	 * 由于希望该程序在spring初始化完毕后自动启动，故监听spring context初始化完毕消息
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		if(event instanceof ContextRefreshedEvent) {
			if(!started) {
				if(isEcho) this.start();
				started = true;
			} 
		} else if(event instanceof ContextClosedEvent) {
			if(isEcho) this.stop();
		}
		
	}
	
	private Producer producer = new Producer();
	
	public void start() {
		//mq producer start 将shell输出 存入mq
		logger.info("*****mq producer starting ...*****");
		logger.info("*****                     ...");
		logger.info("*****                     ...");
		try {
			producer.start();
		} catch (IOException | MQClientException e) {
			e.printStackTrace();
		}
		logger.info("*****mq producer started!*****");
	}
	
	public void stop() {
		logger.info("*****mq producer stoping  ...*****");
		logger.info("*****                     ...");
		logger.info("*****                     ...");
		producer.shutdown();
		logger.info("*****mq producer stoped!*****");
	}
}
