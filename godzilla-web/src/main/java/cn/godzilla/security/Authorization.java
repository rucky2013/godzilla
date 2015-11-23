package cn.godzilla.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.util.GodzillaWebApplication;


/**
 * 授权
 * 
 * @author 201407280166
 *
 */
public class Authorization extends GodzillaWebApplication implements Filter {
	
	private final Logger logger = LogManager.getLogger(Authorization.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//logger.info("Authorization init权限验证");
		escapeUrls.add("/user/welcome");
		escapeUrls.add("/user/login");
		
		escapeUrls.add("/user/logout");
		escapeUrls.add("/userAuthList");
		escapeUrls.add("/editWorkDesk");
		escapeUrls.add("/home");
		escapeUrls.add("/addUser");
		escapeUrls.add("/process");
		escapeUrls.add("/changePassword");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, BusinessException {
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
				//init projectcode and profile threadlocal
				String profile = this.getProfileFromUrl(request);
				super.initProjectThreadLocal(projectcode, profile);
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
	
	private String getProfileFromUrl(ServletRequest request) {
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
		int five = pathInfo.indexOf("/", four+1);
		if(five<0) 
			throw new BusinessException("url is wrong");
		String profile = pathInfo.substring(four+1, five);
		//logger.info(">>>|>>request profile : " + profile);
		return profile;
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
		//logger.info(">>>|>>request projectcode : " + projectcode);
		return projectcode;
	}

}
