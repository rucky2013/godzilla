package cn.godzilla.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.response.ResponseBodyJson;

import com.alibaba.fastjson.JSON;

public class BusinessExceptionFilter implements Filter{
	
	private final Logger logger = LogManager.getLogger(BusinessExceptionFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch(BusinessException e) {
			e.printStackTrace();
			response.getWriter().write(JSON.toJSONString(ResponseBodyJson.custom().setAll(e).build()));
		}
		
	}

	@Override
	public void destroy() {
		
	}
	
}