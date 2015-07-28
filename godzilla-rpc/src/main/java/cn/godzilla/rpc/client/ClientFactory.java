package cn.godzilla.rpc.client;

import cn.godzilla.common.Constant;


public class ClientFactory {

	private static Client client = new Client(Constant.RPC_DEFAULT_PORT, "127.0.0.1");
	
	static{
		client = new Client(Constant.RPC_DEFAULT_PORT, "127.0.0.1");
		client.start();
	}

	public static Client getClient() {
		return client;
	}

}
