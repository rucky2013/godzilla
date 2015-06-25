package cn.creditease.godzilla.security.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.creditease.godzilla.security.Authentication;
import cn.creditease.godzilla.security.Authorization;

public class FilterChain implements Filter{
	
	private int index = 0;
	private List<Filter> filterlist = new ArrayList<Filter>();
	
	public FilterChain addFilter(Filter filter) {
		filterlist.add(filter);
		return this;
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Exception {
		if(index == filterlist.size()) {
			index = 0;
			return ;
		}
		filterlist.get(index++).doFilter(request, response, this);
		
	}
	
	public static void main(String args[] ) throws Exception {
		Authorization auth1 = new Authorization();
		Authentication auth2 = new Authentication();
		
		FilterChain chain = new FilterChain();
		chain.addFilter(auth1);
		
		FilterChain chain2 = new FilterChain();
		chain2.addFilter(auth2).addFilter(auth2);
		
		chain.addFilter(chain2);
		
		chain.doFilter(null, null, chain);
	}

}
