package cn.creditease.godzilla.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Filter {
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Exception ;
}
