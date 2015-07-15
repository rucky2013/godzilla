package cn.godzilla.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import cn.godzilla.service.UserService;
import cn.godzilla.web.context.GodzillaContext;

public abstract class SuperController {
	
	protected ApplicationContext applicationContext;
	protected List<String> escapeUrls = new ArrayList<String>();
	
	private ThreadLocal<GodzillaContext> gozillaThreadLocal = new ThreadLocal<GodzillaContext>() {
		protected GodzillaContext initialValue() {
			return new GodzillaContext();
		};
	};
	
	private ThreadLocal<String> sidThreadLocal = new ThreadLocal<String> () {
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
	 * 初始化 当前jvm缓存  
	 * @param userService
	 * @param sid
	 */
	public void initContext(UserService userService, String sid) {
		sidThreadLocal.set(sid);
	}
	
	public String getSid() {
		return sidThreadLocal.get();
	}
	
	public void distroyContext() {
		sidThreadLocal.set(null);
	}
	
}
