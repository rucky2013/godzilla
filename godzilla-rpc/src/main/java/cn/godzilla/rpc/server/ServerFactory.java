package cn.godzilla.rpc.server;

import cn.godzilla.rpc.util.Util;



public class ServerFactory {
	private static Server server;
	
	static {
		server = new Server(Util.RPC_DEFAULT_PORT, 8);
		server.start();
	}
	
	public static Server getServer() {
		return server;
	}
}
