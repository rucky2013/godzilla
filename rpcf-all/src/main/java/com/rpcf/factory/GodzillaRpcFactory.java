package com.rpcf.factory;

import com.rpcf.api.RpcFactory;
import com.rpcf.server.ServerFactory;

public class GodzillaRpcFactory implements RpcFactory{
	
	public static final int CLIENT_THREADS = 80;
	
	public <T> void export(Class<T> type, T serviceObject) {
		ServerFactory.getServer().register(type.getName(), serviceObject);
	}
	
	public int getClientThreads() {
		return CLIENT_THREADS;
	}
	
	public String getAuthorId(){
		return "godzilla";
	}
}
