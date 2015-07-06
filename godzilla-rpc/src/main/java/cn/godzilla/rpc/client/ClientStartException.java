package cn.godzilla.rpc.client;

public class ClientStartException extends RuntimeException {
	
	public ClientStartException () {
		super("客户端已经启动");
	}
}
