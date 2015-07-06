package cn.godzilla.rpc.server.init;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RpcInitServerServlet extends HttpServlet implements ApplicationContextAware {

	private ApplicationContext application;
	@Override
	public void setApplicationContext(ApplicationContext application) throws BeansException {
		this.application = application;
	}
	
	
	@Override
	public final void init() throws ServletException {
		
		
		
	}
	
	
	

}
