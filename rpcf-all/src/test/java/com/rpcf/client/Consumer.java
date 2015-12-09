package com.rpcf.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rpcf.benchmark.service.HelloService;

@Component(value="consumer")
public class Consumer {

	@Autowired
	private HelloService helloService;

	public HelloService getHelloService() {
		return helloService;
	}

	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}

	public void hello() throws InterruptedException {
		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				String msg1 = null;
				try {
					msg1 = helloService.helloWorld("test", "test", "7");
				} catch (NumberFormatException | InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("msg1:" + msg1);
			}
		};
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				String msg2 = null;
				try {
					msg2 = helloService.helloWorld("test", "test", "2");
				} catch (NumberFormatException | InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("msg2: " + msg2);
			}
		};
		
		Thread thread1 = new Thread(runnable1);
		Thread thread2 = new Thread(runnable2);
		thread1.start();
		Thread.sleep(2000);
		thread2.start();
	}
}
