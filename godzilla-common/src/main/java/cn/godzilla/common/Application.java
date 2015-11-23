package cn.godzilla.common;

/**
 * 哥斯拉全局变量 threadlocal
 * @author 201407280166
 *
 */
public class Application implements Constant {
	
	protected static ThreadLocal<String> projectcodeThreadLocal = 
			new ThreadLocal<String>() {
		protected String initialValue() {
			return "godzilla";
		};
	};
	protected static ThreadLocal<String> profileThreadLocal = 
			new ThreadLocal<String>() {
		protected String initialValue() {
			return "TEST";
		};
	};
	
	protected static ThreadLocal<String> echoMessageThreadLocal = 
			new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		};
	};
	protected static ThreadLocal<String> shellReturnThreadLocal = 
			new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		};
	};
	protected static ThreadLocal<String> svnVersionThreadLocal = 
			new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		};
	};
	protected static ThreadLocal<String> mvnBuildThreadLocal = 
			new ThreadLocal<String>() {
		protected String initialValue() {
			return FAILURE;
		};
	};
	protected static ThreadLocal<String> mvnERRORThreadLocal = 
			new ThreadLocal<String>() {
		protected String initialValue() {
			return SUCCESS;
		};
	};
}
