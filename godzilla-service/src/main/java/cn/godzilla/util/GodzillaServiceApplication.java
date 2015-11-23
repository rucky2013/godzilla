package cn.godzilla.util;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import cn.godzilla.common.Application;
import cn.godzilla.model.User;
import cn.godzilla.service.FunRightService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.UserService;

public class GodzillaServiceApplication extends Application {
	
	protected static ApplicationContext applicationContext;
	
	protected static UserService userService;
	
	protected static FunRightService funRightService;
	
	protected static OperateLogService operateLogService;
	
	protected static ServletContext context;
	
	//部署进度百分比  <用户名-项目名-profile,百分比>
	protected static ConcurrentHashMap<String, String> processPercent = new ConcurrentHashMap<String, String>(); 
		
	public static void clearSid() {
		sidThreadLocal.set("");
	}
	
	public static void setSid(Object sid) {
		if(sid==null||StringUtils.isEmpty(sid)) return;
		sidThreadLocal.set((String)sid);
	}

	public static User getUser() {
		String sid = getSid();
		return userService.getUserBySid(SERVER_USER, TEST_PROFILE, sid);
	}

	public static String getSid() {
		return sidThreadLocal.get();
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
	 * 初始化 projectcode and profile threadlocal 
	 * 
	 * @param projectcode
	 * @param profile
	 */
	protected static void initProjectThreadLocal(String projectcode, String profile) {
		projectcodeThreadLocal.set(projectcode);
		profileThreadLocal.set(profile);
	}
	
	protected static void initThreadLocals() {
		sidThreadLocal.set("");
		projectcodeThreadLocal.set(SERVER_USER);
		profileThreadLocal.set(TEST_PROFILE);
		echoMessageThreadLocal.set("");
		shellReturnThreadLocal.set("");
		catalinaLogThreadLocal.set("");
		jarlogThreadLocal.set("");
		svnVersionThreadLocal.set("");
		mvnBuildThreadLocal.set(SUCCESS);
	}
	
	protected static void distroyThreadLocals() {
		sidThreadLocal.set("");
		projectcodeThreadLocal.set(SERVER_USER);
		profileThreadLocal.set(TEST_PROFILE);
		echoMessageThreadLocal.set("");
		shellReturnThreadLocal.set("");
		catalinaLogThreadLocal.set("");
		jarlogThreadLocal.set("");
		svnVersionThreadLocal.set("");
		mvnBuildThreadLocal.set(SUCCESS);
	}

}
