package cn.godzilla.rpc.client;

public class ClientStopException extends RuntimeException {
	
	public ClientStopException () {
		super("客户端已关闭");
	}
}
