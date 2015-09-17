package cn.godzilla.rpc.client;

import cn.godzilla.rpc.util.Util;

public class ClientFactory {

	private static Client client = new Client(Util.RPC_DEFAULT_PORT, "127.0.0.1");
	
	static{
		client = new Client(Util.RPC_DEFAULT_PORT, "127.0.0.1");
		client.start();
	}

	public static Client getClient() {
		return client;
	}

}
