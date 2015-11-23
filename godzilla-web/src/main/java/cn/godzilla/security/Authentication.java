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
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.service.FunRightService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.UserService;
import cn.godzilla.util.GodzillaWebApplication;

/**
 * 身份验证
 * 
 * @author 201407280166
 *
 */
public class Authentication extends GodzillaWebApplication implements Filter {

	private final Logger logger = LogManager.getLogger(Authentication.class);
	
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//logger.info("Authentication init身份验证");
		escapeUrls.add("/user/welcome");
		escapeUrls.add("/user/login");
		escapeUrls.add("/process");
		
		context = filterConfig.getServletContext();  
		applicationContext = WebApplicationContextUtils.getWebApplicationContext(context); 
		userService = (UserService)applicationContext.getBean("userService");
		funRightService = (FunRightService)applicationContext.getBean("funRightService");
		operateLogService = (OperateLogService)applicationContext.getBean("operateLogService");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest)request;
		try {
			if(!this.escapeUrl(request)) {
				String sid = super.getSidFromUrl(request);
				ReturnCodeEnum userStatus = this.checkUser(userService, sid);
				//logger.info(">>>|>>request sid : " + sid);
				if(userStatus == ReturnCodeEnum.NO_LOGIN) {
					throw new BusinessException("还未登录或sid失效");
				} else if(userStatus == ReturnCodeEnum.OK_CHECKUSER) {
					this.initContext(userService, sid); //将sid保存到 threadlocal
				} else{
					//never reach here
					throw new BusinessException("验证sid,未知异常");
				}
			}
		} catch(BusinessException e1) {
			logger.error(e1.getMessage());
			String prefix = req.getContextPath();
			resp.sendRedirect(prefix+"/user/welcome.do");
			return ;
		}
		chain.doFilter(request, response);
		distroyContext(); //清空 threadlocal
	}
	

	@Override
	public void destroy() {

	}
}
