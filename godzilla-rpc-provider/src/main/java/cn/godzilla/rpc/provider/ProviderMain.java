package cn.godzilla.rpc.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.FunRightService;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.service.SvnService;
import cn.godzilla.service.UserService;

import com.rpcf.api.RpcFactory;
import com.rpcf.factory.GodzillaRpcFactory;

public class ProviderMain {
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
		
		ClientConfigService clientConfigService = (ClientConfigService)context.getBean("clientConfigService");
		FunRightService funRightService =(FunRightService)context.getBean("funRightService");
		MvnService mvnService =(MvnService)context.getBean("mvnService");
		OperateLogService operateLogService =(OperateLogService)context.getBean("operateLogService");
		ProjectService projectService =(ProjectService)context.getBean("projectService");
		PropConfigService propConfigService =(PropConfigService)context.getBean("propConfigService");
		SvnBranchConfigService svnBranchConfigService =(SvnBranchConfigService)context.getBean("svnBranchConfigService");
		SvnService svnService =(SvnService)context.getBean("svnService");
		UserService userService =(UserService)context.getBean("userService");
		
		RpcFactory rpcFactory = new GodzillaRpcFactory();
        //暴露服务
        rpcFactory.export(ClientConfigService.class, clientConfigService);
        rpcFactory.export(FunRightService.class, funRightService);
        rpcFactory.export(MvnService.class, mvnService);
        rpcFactory.export(OperateLogService.class, operateLogService);
        rpcFactory.export(ProjectService.class, projectService);
        rpcFactory.export(PropConfigService.class, propConfigService);
        rpcFactory.export(SvnBranchConfigService.class, svnBranchConfigService);
        rpcFactory.export(SvnService.class, svnService);
        rpcFactory.export(UserService.class, userService);
        
        /*synchronized (ProviderMain.class) {
        	ProviderMain.class.wait(); // block main thread to prevent process exit.
        }*/
    }
}
