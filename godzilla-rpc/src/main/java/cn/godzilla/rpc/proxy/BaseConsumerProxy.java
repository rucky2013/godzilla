package cn.godzilla.rpc.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelFuture;

import cn.godzilla.common.Constant;
import cn.godzilla.rpc.api.RpcException;
import cn.godzilla.rpc.client.ClientFactory;
import cn.godzilla.rpc.common.Parameters;
import cn.godzilla.rpc.common.Result;
import cn.godzilla.rpc.serialize.SerializeException;
import cn.godzilla.rpc.serialize.Serializer;

public abstract class BaseConsumerProxy {
	
	private static final AtomicLong count = new AtomicLong(0);
	
	private ThreadLocal<ChannelFuture> channelFutureLocal;
	private String className;
	private String ip;
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
						Constant.RPC_DEFAULT_PORT);
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
		
		int time = 0;
		while(!channelFutureLocal.get().getChannel().isConnected()) {
			System.out.println("channelFutureLocal.get().getChannel().isConnected() 睡眠  className:"+className  + "--ip:"+ip);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(time++>10) {
				ProxyFactory.clazzMap.remove(className + ip);
				System.out.println("channelFutureLocal.get().getChannel().isConnected() 连接失败 className:"+className  + "--ip:"+ip);
				throw new RpcException("channelFutureLocal.get().getChannel().isConnected() 连接失败  beyond 10 time try is connected");
			}
		}
		
		try {
			byte[] data = Serializer.serialize(parameters);
			
			channelFutureLocal.get().getChannel().write(data);
			synchronized(channelFutureLocal.get().getChannel()) {
				channelFutureLocal.get().getChannel().wait();
			}
			data = ClientFactory.getClient().getResult(
					channelFutureLocal.get().getChannel());
			Result result = Serializer.deserializer(data, Result.class);
			
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
