package cn.godzilla.rpc.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelFuture;

import cn.godzilla.common.Constant;
import cn.godzilla.rpc.client.ClientFactory;
import cn.godzilla.rpc.common.Parameters;
import cn.godzilla.rpc.common.Result;
import cn.godzilla.rpc.serialize.SerializeException;
import cn.godzilla.rpc.serialize.Serializer;

public abstract class BaseConsumerProxy {
	
	private static final AtomicLong count = new AtomicLong(0);
	
	private ThreadLocal<ChannelFuture> channelFutureLocal;
	
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
	
	public BaseConsumerProxy(final String remoteAddress) {
		channelFutureLocal = new ThreadLocal<ChannelFuture>() {
			
			protected ChannelFuture initialValue() {
				return ClientFactory.getClient().getConnection(remoteAddress,
						Constant.RPC_DEFAULT_PORT);
			}
		};
	}
	
	protected Object doInterval(String interfaceName, Object[] objs) {
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
		
		while(!channelFutureLocal.get().getChannel().isConnected())
			;
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
			}
			
			return result.getResult();
		} catch(SerializeException e ) {
			System.out.println("SerializeException 出错啦");
			return null;
		} catch(InterruptedException e ) {
			System.out.println("InterruptedException 出错啦");
			return null;
		}
	}
	
}
