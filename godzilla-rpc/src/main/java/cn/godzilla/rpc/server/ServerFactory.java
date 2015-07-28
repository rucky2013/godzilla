package cn.godzilla.rpc.server;

import cn.godzilla.common.Constant;


public class ServerFactory {
	private static Server server;
	
	static {
		server = new Server(Constant.RPC_DEFAULT_PORT, 10);
		server.start();
	}
	
	public static Server getServer() {
		return server;
	}
}
