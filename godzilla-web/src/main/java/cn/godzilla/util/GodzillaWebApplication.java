package cn.godzilla.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import cn.godzilla.common.Application;
import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.model.FunRight;
import cn.godzilla.model.User;
import cn.godzilla.service.FunRightService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.UserService;

/**
 * 
 * @author 201407280166
 *
 */
public abstract class GodzillaWebApplication extends Application {
	
	protected static ApplicationContext applicationContext;
	
	protected static UserService userService;
	
	protected static FunRightService funRightService;
	
	protected static OperateLogService operateLogService;
	
	protected static ServletContext context;
	
	protected List<String> escapeUrls = new ArrayList<String>();
	/*
	 * -2.限制并发　发布
	 * 日常环境　每个项目　只允许　一个人发布（如果互相依赖项目　并发发布，还是会出现问题）
	 * 准生产	　所有项目只允许一个人发布
	 * 生产　　　所有项目只允许一个人发布
	 */
	protected static Map<String, ReentrantLock> deploy_lock = new HashMap<String, ReentrantLock>(); 
	
	/*protected static ThreadLocal<ConcurrentHashMap<String, String>> processPercent = new ThreadLocal<ConcurrentHashMap<String, String>> () {
		protected ConcurrentHashMap<String, String> initialValue() {
			return new ConcurrentHashMap<String, String>();
		};
	}; //不使用原因：前台进度条 无法获得进度，不是一个线程
	*/
	
	/**
	 * 初始化 当前jvm缓存  
	 * @param userService
	 * @param sid
	 */
	protected void initWebContext(String sid) {
		sidThreadLocal.set(sid);
	}
	
	/**
	 * 登录用户 设置其 sid 存到 当前线程 threadlocal
	 * @param sid
	 */
	protected void initContextBySid(String newsid) {
		sidThreadLocal.set(newsid);
	}
	/**
	 * 根据sid判断用户 是否登录态
	 * @param userService2
	 * @param sid
	 */
	protected ReturnCodeEnum checkUser(String sid) {
		ReturnCodeEnum userStatus = userService.checkUserStatusBySid(SERVER_USER, TEST_PROFILE, sid);
		return userStatus;
	}
	/**
	 * 根据projectcode 判断当前用户是否 有此项目权限
	 * @param projectCode
	 * @return
	 */
	protected static ReturnCodeEnum checkFunright(String projectCode) {
		List<FunRight> funRightList = getFunRights();
		
		for(FunRight fr:funRightList) {
			if(fr.getProjectCode().equals(projectCode)) {
				return ReturnCodeEnum.getByReturnCode(OK_AUTHORIZATION);
			}
		}
		
		return ReturnCodeEnum.getByReturnCode(NO_AUTHORIZATION);
	}
	/**
	 * 初始化 projectcode and profile threadlocal 
	 * 
	 * @param projectcode
	 * @param profile
	 */
	protected void initProjectThreadLocal(String projectcode, String profile) {
		projectcodeThreadLocal.set(projectcode);
		profileThreadLocal.set(profile);
	}
	
	
	
	public static User getUser() {
		String sid = getSid();
		return userService.getUserBySid(SERVER_USER, TEST_PROFILE, sid) ;
	}
	
	public static String getSid() {
		return sidThreadLocal.get();
	}
	
	protected void distroyWebContext() {
		sidThreadLocal.set("");
	}
	
	protected static List<FunRight> getFunRights() {
		String username = getUser().getUserName();
		List<FunRight> funRightList = funRightService.findFunRightsByUsername(SERVER_USER, TEST_PROFILE, username);
		return funRightList;
	}
	
	/**
	 * url 的 第二个字符为 sid   例如 请求为   /godzilla-web/usr/${sid}/${projectcode}/getUser.do?XX
	 * @param request
	 * @return sid
	 * @throws Exception 
	 */
	protected String getSidFromUrl(ServletRequest request) throws BusinessException {
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
		String sid = pathInfo.substring(second+1, third);
		return sid;
	}
	
	protected boolean escapeUrl(ServletRequest request) {
		String pathInfo = ((HttpServletRequest)request).getContextPath()  + ((HttpServletRequest)request).getRequestURI() + (((HttpServletRequest)request).getPathInfo()==null ? "":((HttpServletRequest)request).getPathInfo());
		
		for(String escapeUrl:escapeUrls) {
			if(pathInfo.contains(escapeUrl)) {
				return true;
			}
		}
		return false;
	}
	/*public static void main(String args[]) {
		String branchUrl = "http://10.100.142.37:9090/svn/fso/godzilla/branch/godzilla-bug2/";
		System.out.println(getBranchNameByBranchUrl(branchUrl));
	}*/
	
	
	
}
