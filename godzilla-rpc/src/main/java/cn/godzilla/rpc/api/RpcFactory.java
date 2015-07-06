package cn.godzilla.rpc.api;

public interface RpcFactory {
	
	<T> void export(Class<T> type, T serviceObject);
	
	<T> T getReference(Class<T> type, String ip);
	
	int getClientThreads();
	
	String getAuthorId();
}
