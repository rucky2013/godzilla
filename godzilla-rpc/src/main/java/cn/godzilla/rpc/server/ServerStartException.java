package cn.godzilla.rpc.server;

public class ServerStartException extends RuntimeException {
	
	public ServerStartException() {
		super("服务器已启动");
	}
}
