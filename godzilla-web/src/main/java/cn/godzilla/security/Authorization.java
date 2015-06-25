package cn.godzilla.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.godzilla.security.filter.Filter;
import cn.godzilla.security.filter.FilterChain;

//认证授权
public class Authorization implements Filter,ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	private final Logger logger = LogManager.getLogger(Authorization.class);
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Exception {
		logger.info("authorization");
		
		chain.doFilter(request, response, chain);
	}

	

}
