package com.rpcf.benchmark.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloServiceImpl implements HelloService {

	@Override
	public String helloWorld(String projectCode, String profile, String attr) throws NumberFormatException, InterruptedException {
		System.out.println("projectCode:" + projectCode + ";profile:" + profile);
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssSSS");
		String startTime = sdf.format(new Date());
		Thread.sleep(Long.parseLong(attr)*1000);
		String endTime = sdf.format(new Date());
		return "startTime:" + startTime + "; sleeptime:" + attr + ";endTime:" + endTime;
	}
}
