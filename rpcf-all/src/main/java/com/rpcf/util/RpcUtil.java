package com.rpcf.util;

public class RpcUtil implements DefaultConfig{
	
	private static int port;
	
	public static int getRpcPort(){
		return port==0?DEFAULT_RCP_PORT:port;
	}
	
	private static int retry_time;
	
	public static int getRetryTime() {
		return retry_time==0?DEFAULT_RETRY_TIME:retry_time;
	}
	
	
}
