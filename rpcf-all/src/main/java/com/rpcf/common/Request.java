package com.rpcf.common;

import java.util.List;

import org.jboss.netty.channel.Channel;

public class Request {
	
	private Long id;
	
	private String interfaceName;
	
	private String invokeMethod;
	
	private List<Class<?>> parameterTypes;
	
	private List<?> parameters;
	
	private Object attach;
	
	private Channel channel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Object getAttach() {
		return attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}

	public String getInvokeMethod() {
		return invokeMethod;
	}

	public void setInvokeMethod(String invokeMethod) {
		this.invokeMethod = invokeMethod;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public List<Class<?>> getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(List<Class<?>> parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public List<?> getParameters() {
		return parameters;
	}

	public void setParameters(List<?> parameters) {
		this.parameters = parameters;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
}
