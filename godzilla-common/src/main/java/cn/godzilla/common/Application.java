package cn.godzilla.common;

public class Application {
	protected boolean isEcho = false;
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
}
