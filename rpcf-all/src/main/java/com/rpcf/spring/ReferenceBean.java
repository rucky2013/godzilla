package com.rpcf.spring;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.rpcf.proxy.BaseConsumerProxy;

public class ReferenceBean<T> extends BaseConsumerProxy implements FactoryBean<T>, ApplicationContextAware, InitializingBean, DisposableBean {

	private transient ApplicationContext applicationContext;

	private String id;

	private String interfaceName;

	private Class<?> interfaceClass;

	private transient volatile T ref;

	private transient volatile boolean destroyed = false;

	private transient volatile boolean initialized = false;

	@Override
	public void destroy() throws Exception {
		destroyed = true;
		return;
	}

	@Override
	public T getObject() throws Exception {
		return get();
	}

	private T get() {
		if (destroyed) {
			throw new IllegalStateException("Already destoryed!");
		}
		if (ref == null) {
			init();
		}
		return ref;
	}

	private void init() {
		if (initialized) {
			return;
		}

		if (interfaceName == null || interfaceName.length() == 0) {
			throw new IllegalStateException("<rpcf:reference interface=\"\" /> interface not allow null!");
		}
		initialized = true;

		ref = createProxy();
	}

	@SuppressWarnings("unchecked")
	private T createProxy() {
		Object obj = null;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(BaseConsumerProxy.class);
		enhancer.setInterfaces(new Class[] { getInterfaceClass() });
		enhancer.setCallbacks(new Callback[] { new MethodInterceptor() {

			public Object intercept(Object obj, Method method, Object[] objs, MethodProxy methodproxy) throws Throwable {
				return ((BaseConsumerProxy) obj).doInterval(method.getDeclaringClass().getName(), method.getName(), objs);
			}
		}, NoOp.INSTANCE });
		enhancer.setCallbackFilter(new CallbackFilter() {
			public int accept(Method obj) {
				if (obj.getName().equals("doInterval")) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		obj = enhancer.create(new Class[] {}, new Object[] {});
		// 创建服务代理
		return (T) obj;
	}

	@Override
	public Class<?> getObjectType() {
		return getInterfaceClass();
	}

	public Class<?> getInterfaceClass() {
		if (interfaceClass != null) {
			return interfaceClass;
		}

		try {
			if (interfaceName != null && interfaceName.length() > 0) {
				this.interfaceClass = Class.forName(interfaceName, true, Thread.currentThread().getContextClassLoader());
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return interfaceClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
