package cn.godzilla.rpc.client;

import cn.godzilla.rpc.util.Constant;

public class ClientFactory {

	private static Client client = new Client(Constant.DEFAULT_PORT, "127.0.0.1");
	
	static{
		client = new Client(Constant.DEFAULT_PORT, "127.0.0.1");
		client.start();
	}

	public static Client getClient() {
		return client;
	}

}
