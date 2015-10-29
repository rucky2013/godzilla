package cn.godzilla.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Loader {
	
	public static Class getClass(String clazz) throws ClassNotFoundException {
		try {
			return getTCL().loadClass(clazz);
		} catch (ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return Class.forName(clazz);
	}
	
	private static ClassLoader getTCL() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = null;
		try {
			method = Thread.class.getMethod("getContextClassLoader", null);
		} catch(NoSuchMethodException e) {
			return null;
		}
		return (ClassLoader)method.invoke(Thread.currentThread(), null);
	}
	
}
