package cn.godzilla.rpc.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.benchmark.service.HelloService;
import cn.godzilla.rpc.main.Util;
import cn.godzilla.service.PropConfigService;

public class ProviderMain {
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
		HelloService helloService = (HelloService)context.getBean("helloService");
		PropConfigService propConfigService = (PropConfigService)context.getBean("propConfigService");
		RpcFactory rpcFactory = Util.getRpcFactoryImpl();
        //暴露服务
        rpcFactory.export(HelloService.class, helloService);
        rpcFactory.export(PropConfigService.class, propConfigService);
        /*synchronized (ProviderMain.class) {
        	ProviderMain.class.wait(); // block main thread to prevent process exit.
        }*/
    }
}
