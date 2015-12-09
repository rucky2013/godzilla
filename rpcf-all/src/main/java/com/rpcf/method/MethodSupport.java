package com.rpcf.method;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.cglib.reflect.FastClass;

import com.rpcf.api.RpcException;

public class MethodSupport {

	private final ConcurrentMap<String, FastClassWrapper> fastClassMap = new ConcurrentHashMap<String, FastClassWrapper>();

	public Object invoke(String interfaceName, String invokeMethod, Class<?>[] types,
			Object[] parameters) throws InvocationTargetException,
			RpcException {
		FastClassWrapper wrapper = fastClassMap.get(interfaceName);
		if(wrapper == null){
			throw new RpcException("方法所在实现类尚未注册，interfaceName:" + interfaceName);
		}
		return wrapper.fastClass.invoke(invokeMethod, types, wrapper.obj, parameters);
	}

	public void register(String className, Object impl) {
		FastClass clazz = FastClass.create(impl.getClass());
		FastClassWrapper wrapper = new FastClassWrapper(clazz, impl);
		fastClassMap.putIfAbsent(className, wrapper);
	}

	private static class FastClassWrapper {

		public final FastClass fastClass;

		public final Object obj;

		public FastClassWrapper(FastClass fastClass, Object obj) {
			this.fastClass = fastClass;
			this.obj = obj;
		}

	}

}
