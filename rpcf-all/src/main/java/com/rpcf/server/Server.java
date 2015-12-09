package com.rpcf.server;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.util.StringUtils;

import com.rpcf.common.Entry;
import com.rpcf.common.Request;
import com.rpcf.common.Response;
import com.rpcf.method.MethodSupport;
import com.rpcf.network.handler.Decoder;
import com.rpcf.network.handler.Encoder;
import com.rpcf.network.handler.ParametersHandler;
import com.rpcf.serialize.Serializer;
import com.rpcf.util.RpcfClassLoader;

public class Server {
	private final int port;

	private final List<BlockingDeque<Entry<Channel, byte[]>>> deques;

	private volatile boolean isStart;

	private MethodSupport methodSupport;

	private final int threadCount;

	private ChannelFactory factory;

	private ServerBootstrap serverBootstrap;

	private ExecutorService executor;

	private AtomicLong pos;

	public Server(int port, int threadCount) {
		this.port = port;
		this.threadCount = threadCount;
		isStart = false;
		methodSupport = new MethodSupport();
		deques = new ArrayList<BlockingDeque<Entry<Channel, byte[]>>>(8);
		for (int index = 0; index < 8; ++index) {
			deques.add(new LinkedBlockingDeque<Entry<Channel, byte[]>>());
		}
		pos = new AtomicLong(0);
	}

	public synchronized void start() {
		if (!isStart) {
			isStart = true;

			factory = new NioServerSocketChannelFactory(Executors.newFixedThreadPool(2), Executors.newFixedThreadPool(8), 8);
			serverBootstrap = new ServerBootstrap(factory);
			serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new Decoder(), new ParametersHandler(Server.this), new Encoder());
				}
			});
			serverBootstrap.setOption("child.keepAlive", true);
			serverBootstrap.bind(new InetSocketAddress(port));

			executor = Executors.newFixedThreadPool(threadCount);
			for (int count = 0; count < threadCount; ++count) {
				final int seed = count % 8;
				executor.execute(new Runnable() {
					public void run() {
						Server.this.execute(seed);
					}
				});
			}
		} else {
			throw new ServerStartException();
		}
	}

	public synchronized void stop() {
		if (isStart) {
			isStart = false;
		}
	}

	public void execute(int seed) {
		while (isStart) {
			Class<?>[] clazzArray = new Class<?>[1];

			Entry<Channel, byte[]> entry = null;
			try {
				entry = deques.get(seed++ % 8).takeFirst();
				System.out.println(Thread.currentThread().getName());
			} catch (InterruptedException e) {
				continue;
			}
			Request request = null;
			try {
				request = Serializer.deserializer(entry.getColumn(), Request.class);
				
				//为了将web（consumer）端sid 传送到 service（provider）端
				Object attach = request.getAttach();
				//初始化所有threadlocal
				initThreadLocals();
				setServiceSid(attach);
				Object result = methodSupport.invoke(request.getInterfaceName(), request.getInvokeMethod(), request.getParameterTypes().toArray(clazzArray), request.getParameters().toArray());
				clearServiceSid();
				//销毁所有threadlocal
				destroyThreadLocals();
				Response response = Response.success(result, request.getId());
				//以后再也不用enum 当作 序列化传递对象
				if(result instanceof Enum) {
					response.setAttach(getData(result));
				}
				byte[] resultBytes = Serializer.serialize(response);
				//System.out.println(result.getClass() + ",resultBytes size:" + resultBytes.length);
				entry.getRow().write(resultBytes);
			} catch (Exception e) {
				e.printStackTrace();
				entry.getRow().write(this.getFailure("方法调用失败(没有此方法)" + e.getMessage(), request.getId()));
			}
		}
	}
	
	private Object getData(Object resultcodeEnum) {
		try {
			return RpcfClassLoader.invokeClassMethod(resultcodeEnum, "cn.godzilla.common.ReturnCodeEnum", "getData", null, null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			System.out.println("调用ReturnCodeEnum.getData()失败");
			return null;
		}
	}

	private void destroyThreadLocals() {
		try {
			RpcfClassLoader.invokeStaticMethod("cn.godzilla.util.GodzillaServiceApplication", "destroyThreadLocals", null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			System.out.println("方法调用失败GodzillaServiceApplication.destroyThreadLocals");
		}
	}

	private void initThreadLocals() {
		try {
			RpcfClassLoader.invokeStaticMethod("cn.godzilla.util.GodzillaServiceApplication", "initThreadLocals", null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			System.out.println("方法调用失败GodzillaServiceApplication.initThreadLocals");
		}
	}

	//为了将web（consumer）端sid 传送到 service（provider）端
	private void setServiceSid(Object attach) {
		if(attach==null||StringUtils.isEmpty(attach)) return;
		Object[] parameters = {attach};
		try {
			RpcfClassLoader.invokeStaticMethod("cn.godzilla.util.GodzillaServiceApplication", "setSid", parameters);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			System.out.println("方法调用失败GodzillaServiceApplication.setSid");
		}
		
	}
	
	private void clearServiceSid() {
		try {
			RpcfClassLoader.invokeStaticMethod("cn.godzilla.util.GodzillaServiceApplication", "clearSid", null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			System.out.println("方法调用失败GodzillaServiceApplication.clearSid");
		}
	}

	public byte[] getFailure(String msg, Long reqId) {
		byte[] failbytes = Serializer.serialize(Response.fail(msg, reqId));
		return failbytes;
	}

	public void putParameters(Channel channel, byte[] parameters) {
		if (isStart) {
			deques.get((int) (pos.getAndIncrement() % 8)).offerLast(Entry.<Channel, byte[]> getEntry(channel, parameters));
		} else {
			throw new ServerStopException();
		}
	}

	public void register(String className, Object impl) {
		methodSupport.register(className, impl);
	}
}
