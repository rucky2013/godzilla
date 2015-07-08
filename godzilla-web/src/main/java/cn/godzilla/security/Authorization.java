package cn.godzilla.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.godzilla.common.BusinessException;
import cn.godzilla.web.SuperController;


/**
 * 授权
 * 
 * @author 201407280166
 *
 */
public class Authorization extends SuperController implements Filter,ApplicationContextAware{
	
	private final Logger logger = LogManager.getLogger(Authorization.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("Authorization init");
		escapeUrls.add("/user/welcome");
		escapeUrls.add("/user/login/");
		escapeUrls.add("");
		escapeUrls.add("");
		escapeUrls.add("");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, BusinessException {
		logger.info("authorization");
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}
	
	private boolean escapeUrl(ServletRequest request) {
		String pathInfo = ((HttpServletRequest)request).getRequestURI();
		for(String escapeUrl:escapeUrls) {
			if(pathInfo.contains(escapeUrl)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
