package cn.creditease.godzilla.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.creditease.godzilla.security.filter.Filter;
import cn.creditease.godzilla.security.filter.FilterChain;

//权限校验
public class Authentication implements Filter {

	private final Logger logger = LogManager.getLogger(Authentication.class);
	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Exception {
		logger.info("authentication");
		
		chain.doFilter(request, response, chain);
	}

}
