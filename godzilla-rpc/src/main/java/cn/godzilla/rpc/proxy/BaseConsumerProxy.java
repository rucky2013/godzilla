package cn.godzilla.rpc.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelFuture;

import cn.godzilla.rpc.api.RpcException;
import cn.godzilla.rpc.client.ClientFactory;
import cn.godzilla.rpc.common.Parameters;
import cn.godzilla.rpc.common.Result;
import cn.godzilla.rpc.serialize.SerializeException;
import cn.godzilla.rpc.serialize.Serializer;
import cn.godzilla.rpc.util.Util;

public abstract class BaseConsumerProxy {
	
	protected static final AtomicLong count = new AtomicLong(0);
	protected static final AtomicLong uniqueId = new AtomicLong(0);
	public static Map<String, Map<String, Object>> locks = new ConcurrentHashMap<String, Map<String, Object>>();
	
	protected ThreadLocal<ChannelFuture> channelFutureLocal;
	protected String className;
	protected String ip;
	static {
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				while(true) {
					System.out.println(count.get());
					try{
						Thread.sleep(1000);
					} catch(InterruptedException e) {
						
					}
				}
			}
		});
	}
	
	public BaseConsumerProxy(final String className, final String remoteAddress) {
		this.className = className;
		this.ip = remoteAddress;
		channelFutureLocal = new ThreadLocal<ChannelFuture>() {
			
			protected ChannelFuture initialValue() {
				return ClientFactory.getClient().getConnection(remoteAddress,
						Util.RPC_DEFAULT_PORT);
			}
		};
	}
	
	protected Object doInterval(String interfaceName, Object[] objs) throws RpcException {
		List<Class<?>> clazzs = new ArrayList<Class<?>>(objs.length);
		List<Object> params = new ArrayList<Object>();
		for(Object obj: objs) {
			clazzs.add(obj.getClass());
			params.add(obj);
		}
		
		Parameters parameters = new Parameters();
		parameters.setInterfaceName(interfaceName);
		parameters.setParameterTypes(clazzs);
		parameters.setParameters(params);
		parameters.setId(uniqueId.getAndIncrement()+"");
		
		int time = 0;
		while(!channelFutureLocal.get().getChannel().isConnected()) {
			System.out.println("channelFutureLocal.get().getChannel().isConnected() 睡眠  className:"+className  + "--ip:"+ip);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(time++>10) {
				Object rm = ProxyFactory.clazzMap.remove(className + ip);
				System.out.println("channelFutureLocal.get().getChannel().isConnected() 连接失败 className:"+className  + "--ip:"+ip);
				System.out.println("channelFutureLocal.get().getChannel().isConnected() 连接失败 rm:---|"+rm);
				throw new RpcException("channelFutureLocal.get().getChannel().isConnected() 连接失败  beyond 10 time try is connected");
			}
		}
		
		try {
			byte[] data = Serializer.serialize(parameters);
			
			channelFutureLocal.get().getChannel().write(data);
			Map<String, Object> lock = new HashMap<String, Object>();
			lock.put("ChannelFuture", channelFutureLocal.get());
			locks.put(parameters.getId(), lock);
			
			synchronized(lock) {
				lock.wait();
			}
			/*data = ClientFactory.getClient().getResult(
					channelFutureLocal.get().getChannel());*/
			Result result = (Result)lock.get("result");
			
			if(!result.isSuccess()) {
				System.out.println("出错啦");
				throw new RpcException("调用失败啦");
			}
			
			return result.getResult();
		} catch(SerializeException e ) {
			System.out.println("SerializeException 出错啦");
			throw new RpcException("SerializeException 出错啦");
		} catch(InterruptedException e ) {
			System.out.println("InterruptedException 出错啦");
			throw new RpcException("InterruptedException 出错啦");
		}
	}
	
}
