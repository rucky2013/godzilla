package cn.godzilla.rpc.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

public class ProxyFactory {

	
	//单机 rpc
	/*public static Object getConsumerProxy(Class<?> clazz, String ip) {
		Object obj = clazzMap.get(clazz);
		if(obj ==null) {
			synchronized (clazz) {
				if(clazzMap.contains(clazz)) {
					return clazzMap.get(clazz);
				} else {
					Enhancer enhancer = new Enhancer();
					enhancer.setSuperclass(BaseConsumerProxy.class);
					enhancer.setInterfaces(new Class[] {clazz});
					enhancer.setCallbacks(new Callback[] {
							new MethodInterceptor() {
								
								public Object intercept(Object obj,
										Method method, Object[] objs,
										MethodProxy methodproxy)throws Throwable {
									return ((BaseConsumerProxy)obj)
											.doInterval(method.getDeclaringClass()
													.getName()
													+"." + method.getName(), 
													objs);
								}
							}, NoOp.INSTANCE});
					enhancer.setCallbackFilter(new CallbackFilter() {
						
						public int accept(Method obj) {
							if(obj.getName().equals("doInterval")) {
								return 1;
							} else {
								return 0;
							}
						}
					});
					
					obj = enhancer.create(new Class[] {String.class},
							new Object[] {ip});
					clazzMap.put(clazz, obj);
					return obj;
					
				}
			}
		} else {
			return obj;
		}
	} */
	//<class-name + ip, objImpl> 建立多机 rpc
	static final ConcurrentHashMap<String, Object> clazzMap = 
			new ConcurrentHashMap<String, Object>();
		
	public static Object getConsumerProxy(Class<?> clazz, String ip) {
		Object obj = clazzMap.get(clazz.getName()+ip);
		if(obj ==null) {
			synchronized (clazz) {
				if(clazzMap.contains(clazz.getName()+ip)) {
					return clazzMap.get(clazz.getName()+ip);
				} else {
					Enhancer enhancer = new Enhancer();
					enhancer.setSuperclass(BaseConsumerProxy.class);
					enhancer.setInterfaces(new Class[] {clazz});
					enhancer.setCallbacks(new Callback[] {
							new MethodInterceptor() {
								
								public Object intercept(Object obj,
										Method method, Object[] objs,
										MethodProxy methodproxy)throws Throwable {
									return ((BaseConsumerProxy)obj)
											.doInterval(method.getDeclaringClass()
													.getName()
													+"." + method.getName(), 
													objs);
								}
							}, NoOp.INSTANCE});
					enhancer.setCallbackFilter(new CallbackFilter() {
						
						public int accept(Method obj) {
							if(obj.getName().equals("doInterval")) {
								return 1;
							} else {
								return 0;
							}
						}
					});
					
					obj = enhancer.create(new Class[] {String.class, String.class},
							new Object[] {clazz.getName(), ip});
					clazzMap.put(clazz.getName()+ip, obj);
					return obj;
					
				}
			}
		} else {
			return obj;
		}
	} 
	
	
	public static Object getProviderProxy(Class<?> clazz) {
		return null;
	}
}



