package cn.godzilla.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.context.ApplicationContext;

import cn.godzilla.common.Application;
import cn.godzilla.common.BusinessException;
import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.FunRight;
import cn.godzilla.model.User;
import cn.godzilla.service.FunRightService;
import cn.godzilla.service.UserService;

public abstract class GodzillaApplication extends Application implements Constant{
	
	protected ApplicationContext applicationContext;
	protected static UserService userService;
	protected static FunRightService funRightService;
	protected static ServletContext context;
	protected List<String> escapeUrls = new ArrayList<String>();
	
	//部署进度百分比  <用户名-项目名-profile,百分比>
	protected static ConcurrentHashMap<String, String> processPercent = new ConcurrentHashMap<String, String>(); 
	
	private static ThreadLocal<String> sidThreadLocal = new ThreadLocal<String> () {
		protected String initialValue() {
			return "";
		};
	};
	
	/**
	 * 登录用户 设置其 sid 存到 当前线程 threadlocal
	 * @param sid
	 */
	protected void initContextBySid(String sid) {
		sidThreadLocal.set(sid);
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
	
	protected boolean ifSuccessStartTomcat(String IP, String war_name) {
		int timeout = DEFAULT_TIMEOUT * 2;
		int i = 0;
		while (true) {
			String test_url = "http://" + IP + ":8080/" + war_name + "/index.jsp";
			StringBuilder rs = new StringBuilder();
			try {
				RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectionRequestTimeout(10000).build();
				CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build();
				HttpResponse response = client.execute(new HttpGet(test_url));
			
	            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
	                HttpEntity entity = response.getEntity();
	                InputStreamReader insr = new InputStreamReader(entity.getContent());
	                int respInt = insr.read();
	                while (respInt != -1) {
	                    rs.append((char) respInt);
	                    respInt = insr.read();
	                }
	            } else if(true){
	            	//如果信息码 为 4xx 或者 5xx 则退出
	            }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        if (rs.toString().indexOf("<!--<h5>godzilla</h5>-->") != -1) {
	            	return true;
	        } else {
	        	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	        //time out seconds : return false;
	        if(i>=timeout) {
	        	return false;
	        }
		}
	}
}
