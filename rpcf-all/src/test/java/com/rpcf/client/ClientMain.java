package com.rpcf.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientMain {
	public static final String DEFAULT_SPRING_CONFIG = "classpath*:*.xml";

	static ClassPathXmlApplicationContext context;
    
	public static void main(String args[]) throws InterruptedException {
		String configPath = DEFAULT_SPRING_CONFIG;
		context = new ClassPathXmlApplicationContext(configPath.split("[,\\s]+"));
        context.start();
        Consumer consumer = (Consumer)context.getBean("consumer");
        consumer.hello();
        Thread.sleep(5000);
	}
}
