package cn.godzilla.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.Constant;
import cn.godzilla.common.response.ResponseBodyJson;

import com.alibaba.fastjson.JSON;
import com.rpcf.api.RpcException;

public class BusinessExceptionFilter implements Filter, Constant{
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch(RpcException e1) {
			//e1.printStackTrace();
			response.getWriter().write(JSON.toJSONString(ResponseBodyJson.custom().setAll(e1, RPCEX).build()));
		} catch(BusinessException e) {
			//e.printStackTrace();
			response.getWriter().write(JSON.toJSONString(ResponseBodyJson.custom().setAll(e, GODZILLAEX).build()));
		}
		
	}

	@Override
	public void destroy() {
		
	}
	
}
