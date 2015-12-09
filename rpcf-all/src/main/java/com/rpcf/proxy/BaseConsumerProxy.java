package com.rpcf.proxy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.rpcf.api.RpcException;
import com.rpcf.client.Client;
import com.rpcf.common.Request;
import com.rpcf.common.Response;
import com.rpcf.util.IPEnum;
import com.rpcf.util.RpcUtil;
import com.rpcf.util.RpcfClassLoader;

public class BaseConsumerProxy {
	
	private static final AtomicLong uniqueId = new AtomicLong(0);
	
	public Object doInterval(String interfaceName, String invokeMethod, Object[] objs) throws Throwable{
		List<Class<?>> clazzs = new ArrayList<Class<?>>(objs.length);
		List<Object> params = new ArrayList<Object>();
		for(Object obj: objs) {
			if(obj instanceof List) {
				clazzs.add(List.class);
				params.add(obj);
				continue;
			}
			if(obj instanceof Map) {
				clazzs.add(Map.class);
				params.add(obj);
				continue;
			}
			clazzs.add(obj.getClass());
			params.add(obj);
		}
		Request request = new Request();
		request.setInterfaceName(interfaceName);
		request.setInvokeMethod(invokeMethod);
		request.setParameterTypes(clazzs);
		request.setParameters(params);
		request.setId(uniqueId.getAndIncrement());
		//为了将web（consumer）端sid 传送到 service（provider）端
		request.setAttach(getWebSid());
		
		String projectCode = (String)objs[0];
		String profile = (String)objs[1];
		String ip = IPEnum.getIPbyProjectcodeAndProfile(projectCode, profile);
		int time = 0;
		DefaultFuture future = null;
		while(true) {
			Client client = Client.clients.get(ip+":"+ RpcUtil.getRpcPort());
			if(client==null) {
				try {
					client = new Client(ip, RpcUtil.getRpcPort());
				} catch (Throwable e) {
					e.printStackTrace();
					throw new RpcException("创建client失败:" + ip + ":" + RpcUtil.getRpcPort());
				}
			}
			if(client.getChannel().isOpen()) {
				while(true) {
					if(client.getChannel().isConnected()) {
						future = client.send(request);
						break;
					}
				}
				break;
			} else {
				Client.clients.remove(ip+":"+ RpcUtil.getRpcPort());
				if(time>RpcUtil.getRetryTime()) {
					throw new RpcException("连接失败，请重试ClosedChannelException:" + ip + ":" + RpcUtil.getRpcPort());
				}
				System.out.println("连接失败，重试第"+ ++time + "次");
			}
		}
		Response response = future.get();
		Object result = response.getResult();
		//以后再也不用enum 当作 序列化传递对象
		if(result instanceof Enum) {
			setData(result, response.getAttach());
		}
		return result;
	}
	
	private void setData(Object resultcodeEnum, Object attach) {
		try {
			if(attach==null) return ;
			Object[] parameters = {attach};
			Class[] parameterTypes = {Object.class};
			RpcfClassLoader.invokeClassMethod(resultcodeEnum, "cn.godzilla.common.ReturnCodeEnum", "setData", parameters, parameterTypes); 
			return ;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			System.out.println("调用ReturnCodeEnum.setData()失败");
			return;
		}
	}
	
	//为了将web（consumer）端sid 传送到 service（provider）端
	private Object getWebSid() {
		try {
			return RpcfClassLoader.invokeStaticMethod("cn.godzilla.util.GodzillaWebApplication", "getSid", null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			System.out.println("调用GodzillaWebApplication.getSid()失败");
			return null;
		}
	}
}
