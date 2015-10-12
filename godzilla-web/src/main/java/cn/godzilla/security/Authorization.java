package cn.godzilla.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.service.FunRightService;
import cn.godzilla.web.GodzillaApplication;


/**
 * 授权
 * 
 * @author 201407280166
 *
 */
public class Authorization extends GodzillaApplication implements Filter {
	
	private final Logger logger = LogManager.getLogger(Authorization.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("Authorization init权限验证");
		escapeUrls.add("/user/welcome");
		escapeUrls.add("/user/login");
		
		escapeUrls.add("/user/logout");
		escapeUrls.add("/userAuthList");
		escapeUrls.add("/editWorkDesk");
		escapeUrls.add("/home");
		escapeUrls.add("/addUser");
		escapeUrls.add("/process");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, BusinessException {
		logger.info("authorization");
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest)request;
		
		try {
			if(!this.escapeUrl(request)) {
				String projectcode = this.getProjectcodeFromUrl(request);
				ReturnCodeEnum projectStatus = super.checkFunright(projectcode);
				if(projectStatus == ReturnCodeEnum.NO_AUTHORIZATION) {
					throw new BusinessException("没有项目权限");
				} else if(projectStatus == ReturnCodeEnum.OK_AUTHORIZATION) {
					
				}
			} 
		} catch(BusinessException e1) {
			logger.error(e1.getMessage());
			String sid = super.getSidFromUrl(request);
			String prefix = req.getContextPath();
			resp.sendRedirect(prefix+"/user/"+sid+"/home.do");
			return ;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}
	
	
	/**
	 * url 的 第3个字符为 projectcode   例如 请求为   /godzilla-web/usr/${sid}/${projectcode}/getUser.do?XX
	 * @param request
	 * @return sid
	 * @throws Exception 
	 */
	private String getProjectcodeFromUrl(ServletRequest request) throws BusinessException {
		String pathInfo = ((HttpServletRequest)request).getRequestURI();
		
		int start = pathInfo.indexOf("/", 1);
		if(start <0) 
			throw new BusinessException("url is wrong");
		int second = pathInfo.indexOf("/", start+1);
		if(second <0) 
			throw new BusinessException("url is wrong");
		int third = pathInfo.indexOf("/", second+1);
		if(third <0) 
			throw new BusinessException("url is wrong");
		int four = pathInfo.indexOf("/", third+1);
		if(four<0) 
			throw new BusinessException("url is wrong");
		String projectcode = pathInfo.substring(third+1, four);
		logger.info(">>>|>>request projectcode : " + projectcode);
		return projectcode;
	}

}
