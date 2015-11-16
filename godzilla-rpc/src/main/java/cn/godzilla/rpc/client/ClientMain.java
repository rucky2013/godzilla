package cn.godzilla.rpc.client;

import java.io.IOException;

import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.benchmark.service.HelloService;
import cn.godzilla.rpc.main.Util;

/**
 * @author ding.lid
 */
public class ClientMain {

	public static RpcFactory rpcFactory;

	/*ApplicationContext ac = new FileSystemXmlApplicationContext("applicationContext.xml");

	static {
		try {
			rpcFactory = Util.getRpcFactoryImpl();
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IOException e) {
			e.printStackTrace();
		}
	}*/

	public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, InterruptedException {
		System.out.println("start client");
		test(args);
	}

	public static void test(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, InterruptedException {
		final RpcFactory rpcFactory = Util.getRpcFactoryImpl();
		String serverIp = "127.0.0.1";

		final HelloService reference = rpcFactory.getReference(HelloService.class, serverIp);

		System.out.println("weak");
		String ret = reference.helloWorld("111");
		System.out.println("invoke ok ret" + ret);
	}

}
