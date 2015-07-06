package cn.godzilla.rpc.factory;

import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.proxy.ProxyFactory;
import cn.godzilla.rpc.server.ServerFactory;

public class GodzillaRpcFactory implements RpcFactory{
	
	public static final int CLIENT_THREADS = 80;
	
	public <T> void export(Class<T> type, T serviceObject) {
		ServerFactory.getServer().register(type.getName(), serviceObject);
	}
	
	public <T> T getReference(Class<T> type, String ip) {
		return (T)ProxyFactory.getConsumerProxy(type, ip);
	}
	
	public int getClientThreads() {
		return CLIENT_THREADS;
	}
	
	public String getAuthorId(){
		return "godzilla";
	}
}
