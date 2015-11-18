package cn.godzilla.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

import cn.godzilla.common.Application;
import cn.godzilla.common.BusinessException;
import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.model.FunRight;
import cn.godzilla.model.User;
import cn.godzilla.service.FunRightService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.UserService;

public abstract class GodzillaApplication extends Application implements Constant{
	
	public static Logger logger = LogManager.getLogger(GodzillaApplication.class);
	
	protected ApplicationContext applicationContext;
	protected static UserService userService;
	protected static FunRightService funRightService;
	protected static OperateLogService operateLogService;
	protected static ServletContext context;
	protected List<String> escapeUrls = new ArrayList<String>();
	protected ReentrantLock PUBLIC_LOCK = new ReentrantLock(true);
	protected ReentrantLock PUBLIC_LOCK1 = new ReentrantLock(true);
	protected ReentrantLock PUBLIC_LOCK2 = new ReentrantLock(true);
	/*
	 * -2.限制并发　发布
	 * 日常环境　每个项目　只允许　一个人发布（如果互相依赖项目　并发发布，还是会出现问题）
	 * 准生产	　所有项目只允许一个人发布
	 * 生产　　　所有项目只允许一个人发布
	 */
	protected static Map<String, ReentrantLock> deploy_lock = new HashMap<String, ReentrantLock>(); 
	//部署进度百分比  <用户名-项目名-profile,百分比>
	protected static ConcurrentHashMap<String, String> processPercent = new ConcurrentHashMap<String, String>(); 
	
	/*protected static ThreadLocal<ConcurrentHashMap<String, String>> processPercent = new ThreadLocal<ConcurrentHashMap<String, String>> () {
		protected ConcurrentHashMap<String, String> initialValue() {
			return new ConcurrentHashMap<String, String>();
		};
	}; //不使用原因：前台进度条 无法获得进度，不是一个线程
	*/
	private static ThreadLocal<String> sidThreadLocal = new ThreadLocal<String> () {
		protected String initialValue() {
			return "";
		};
	};
	
	/**
	 * 登录用户 设置其 sid 存到 当前线程 threadlocal
	 * @param sid
	 */
	protected void initContextBySid(String newsid) {
		logger.info("++|++|++>sid:" + newsid);
		sidThreadLocal.set(newsid);
	}
	/**
	 * 根据sid判断用户 是否登录态
	 * @param userService2
	 * @param sid
	 */
	protected ReturnCodeEnum checkUser(UserService userService, String sid) {
		ReturnCodeEnum userStatus = userService.checkUserStatusBySid(sid);
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
	
	/**
	 * 初始化 当前jvm缓存  
	 * @param userService
	 * @param sid
	 */
	protected void initContext(UserService userService, String sid) {
		sidThreadLocal.set(sid);
	}
	
	public static User getUser() {
		String sid = getSid();
		return userService.getUserBySid(sid) ;
	}
	
	public static String getSid() {
		return sidThreadLocal.get();
	}
	
	protected void distroyContext() {
		sidThreadLocal.set(null);
		echoMessageThreadLocal.set("");
		shellReturnThreadLocal.set("");
		projectcodeThreadLocal.set("");
		profileThreadLocal.set("");
	}
	
	protected static List<FunRight> getFunRights() {
		String username = getUser().getUserName();
		List<FunRight> funRightList = funRightService.findFunRightsByUsername(username);
		return funRightList;
	}
	
	protected static String getBranchNameByBranchUrl(String branchUrl) {
		String branchName = "";
		if(branchUrl.endsWith("/")) {
			branchName = branchUrl.substring(branchUrl.lastIndexOf("/", branchUrl.length()-2)+1, branchUrl.length()-2);
		} else {
			branchName = branchUrl.substring(branchUrl.lastIndexOf("/")+1);
		}
		return branchName;
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
	/**
	 * 日常环境  通过访问 index.jsp 判断 是否 项目启动成功
	 * 其他环境暂不需要
	 * @param IP
	 * @param war_name
	 * @return
	 */
	protected boolean ifSuccessStartTomcat(String IP, String war_name) {
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		int timeout = DEFAULT_TIMEOUT ;
		int i = 0;
		while (true) {
			String test_url = "http://" + IP + ":8080/" + war_name + "/index.jsp";
			try {
				RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectionRequestTimeout(10000).build();
				CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build();
				HttpResponse response = client.execute(new HttpGet(test_url));
			
	            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
	                /*HttpEntity entity = response.getEntity();
	                InputStreamReader insr = new InputStreamReader(entity.getContent());
	                int respInt = insr.read();
	                while (respInt != -1) {
	                    rs.append((char) respInt);
	                    respInt = insr.read();
	                }*/
	            	//20151102 不检查标识,返回200即成功
	            	return true;
	            } 
	            //if(i>10) {
	            	if(HttpStatus.SC_NOT_FOUND == response.getStatusLine().getStatusCode()){
		            	//如果信息码 为 4xx 或者 5xx 则退出
		            	return false;
		            } else if(HttpStatus.SC_INTERNAL_SERVER_ERROR == response.getStatusLine().getStatusCode()) {
		            	return false;
		            }
	           //}
			} catch (IOException e1) {
				System.out.println("---httpclient 报错啦---");
			}
	        //time out seconds : return false;
	        i++;
	        if(i>=timeout) {
	        	return false;
	        }
		}
	}
	
	/**
	 * 
	 * |快速判断|首页展示启动与否使用|
	 * 日常环境  通过访问 index.jsp 判断 是否 项目启动成功
	 * 其他环境暂不需要
	 * project.state
	 * 1.已启动
	 * 0.未知
	 * @param IP
	 * @param war_name
	 * @return
	 */
	protected boolean ifSuccessStartProject(String IP, String war_name) {
		
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		String test_url = "http://" + IP + ":8080/" + war_name + "/index.jsp";
		StringBuilder rs = new StringBuilder();
		try {
			RequestConfig config = RequestConfig.custom().setSocketTimeout(100).setConnectionRequestTimeout(100).setConnectTimeout(1000).build();
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build();
			HttpResponse response = client.execute(new HttpGet(test_url));
		
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                /*HttpEntity entity = response.getEntity();
                InputStreamReader insr = new InputStreamReader(entity.getContent());
                int respInt = insr.read();
                while (respInt != -1) {
                    rs.append((char) respInt);
                    respInt = insr.read();
                }*/
            	return true;
            } 
        	if(HttpStatus.SC_NOT_FOUND == response.getStatusLine().getStatusCode()){
            	//如果信息码 为 4xx 或者 5xx 则退出
            	return false;
            } else if(HttpStatus.SC_INTERNAL_SERVER_ERROR == response.getStatusLine().getStatusCode()) {
            	return false;
            }
		} catch (IOException e1) {
			System.out.println("---httpclient 报错啦=---");
			//e1.printStackTrace();
		}
		return false;
	}
	
	protected void isEmpty(Object o) {
		if(o == null) 
			throw new BusinessException("数据为null->" + o.getClass().getName());
		if(o instanceof String) {
			if(StringUtil.isEmpty((String)o)) {
				throw new BusinessException("字符串为空");
			}
		}
	}
	
	protected void isEmpty(Object o, String errorMsg) {
		if(o == null) 
			throw new BusinessException(errorMsg);
		if(o instanceof String) {
			if(StringUtil.isEmpty((String)o)) {
				throw new BusinessException(errorMsg);
			}
		}
	}
	
	protected void isEmpty(Object o, String errorCode, String errorMsg) {
		if(o == null) 
			throw new BusinessException(errorCode, errorMsg);
		if(o instanceof String) {
			if(StringUtil.isEmpty((String)o)) {
				throw new BusinessException(errorCode, errorMsg);
			}
		}
	}
	
}
