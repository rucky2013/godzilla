package com.rpcf.server;

import com.rpcf.util.RpcUtil;

public class ServerFactory {
	private static Server server;
	
	static {
		server = new Server(RpcUtil.getRpcPort(), 8);
		server.start();
	}
	
	public static Server getServer() {
		return server;
	}
}
