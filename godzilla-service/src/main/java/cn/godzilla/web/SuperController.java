package cn.godzilla.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.FunRight;
import cn.godzilla.model.User;
import cn.godzilla.service.FunRightService;
import cn.godzilla.service.UserService;
import cn.godzilla.web.context.GodzillaContext;

public abstract class SuperController implements Constant{
	
	protected ApplicationContext applicationContext;
	protected List<String> escapeUrls = new ArrayList<String>();
	protected static UserService userService;
	protected static FunRightService funRightService;
	
	private static ThreadLocal<GodzillaContext> gozillaThreadLocal = new ThreadLocal<GodzillaContext>() {
		protected GodzillaContext initialValue() {
			return new GodzillaContext();
		};
	};
	
	private static ThreadLocal<String> sidThreadLocal = new ThreadLocal<String> () {
		protected String initialValue() {
			return "";
		};
	};
	
	/**
	 * 登录用户 设置其 sid 存到 当前线程 threadlocal
	 * @param sid
	 */
	private void initContextBySid(String sid) {
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
	
	public void distroyContext() {
		sidThreadLocal.set(null);
	}
	
	public static List<FunRight> getFunRights() {
		String username = getUser().getUserName();
		List<FunRight> funRightList = funRightService.findFunRightsByUsername(username);
		return funRightList;
	}
	
	public static boolean checkFunright(String projectCode) {
		List<FunRight> funRightList = getFunRights();
		
		for(FunRight fr:funRightList) {
			if(fr.getProjectCode().equals(projectCode)) {
				return true;
			}
		}
		
		return false;
	}
	
}
