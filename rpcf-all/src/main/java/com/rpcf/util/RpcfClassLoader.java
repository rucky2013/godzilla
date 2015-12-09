package com.rpcf.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcfClassLoader {
	
	/**
	 * 反射调用静态方法，threadcontextclassloader
	 */
	public static Object invokeStaticMethod(String classFullName, String methodName, Object[] parameters) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		Class clazz = getTCL().loadClass(classFullName);
		Class[] parameterTypes = getParameterTypes(parameters);
		Method method = clazz.getMethod(methodName, parameterTypes);
		return method.invoke(clazz, parameters);
	}
	
	/**
	 * 反射调用实类普通方法，threadcontextclassloader
	 */
	public static Object invokeClassMethod(Object targetObj, String classFullName, String methodName, Object[] parameters, Class[] parameterTypes) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class clazz = getTCL().loadClass(classFullName);
		//Class[] parameterTypes = getParameterTypes(parameters);
		Method method = clazz.getMethod(methodName, parameterTypes);
		return method.invoke(targetObj, parameters);
	}
	
	/**
	 * 拿到 参数 的所有数据类型 数组
	 * @param parameters
	 * @return
	 */
	private static Class[] getParameterTypes(Object[] parameters) {
		Class[] parameterTypes = null;
		if(parameters != null) {
			parameterTypes = new Class[parameters.length];
			int i = 0;
			for(Object parameter: parameters) {
				parameterTypes[i++] = parameter.getClass();
			}
		}
		return parameterTypes;
	}

	protected static ClassLoader getTCL() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = null;
		method = Thread.class.getMethod("getContextClassLoader", null);
		return (ClassLoader)method.invoke(Thread.currentThread(), null);
	}

	
}
