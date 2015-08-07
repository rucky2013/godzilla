package cn.godzilla.rpc.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.benchmark.service.HelloService;
import cn.godzilla.rpc.main.Util;
import cn.godzilla.service.MvnProviderService;
import cn.godzilla.service.PropConfigProviderService;

public class ProviderMain {
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
		HelloService helloService = (HelloService)context.getBean("helloService");
		PropConfigProviderService propConfigProviderService = (PropConfigProviderService)context.getBean("propConfigProviderService");
		MvnProviderService mvnProviderService =(MvnProviderService)context.getBean("mvnProviderService");
		RpcFactory rpcFactory = Util.getRpcFactoryImpl();
        //暴露服务
        rpcFactory.export(HelloService.class, helloService);
        rpcFactory.export(PropConfigProviderService.class, propConfigProviderService);
        rpcFactory.export(MvnProviderService.class, mvnProviderService);
        
        /*synchronized (ProviderMain.class) {
        	ProviderMain.class.wait(); // block main thread to prevent process exit.
        }*/
    }
}
