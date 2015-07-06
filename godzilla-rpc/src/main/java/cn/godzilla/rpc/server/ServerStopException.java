package cn.godzilla.rpc.server;

public class ServerStopException extends RuntimeException {
	
	public ServerStopException () {
		super("服务器已关闭");
	}
}
