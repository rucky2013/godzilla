package cn.godzilla.rpc.client;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;

import cn.godzilla.rpc.common.Result;
import cn.godzilla.rpc.network.handler.Decoder;
import cn.godzilla.rpc.network.handler.Encoder;
import cn.godzilla.rpc.network.handler.ResultHandler;
import cn.godzilla.rpc.proxy.BaseConsumerProxy;
import cn.godzilla.rpc.serialize.Serializer;

public class Client {

	private final int port;

	private final String remoteAddress;

	private ClientBootstrap bootstrap;

	private ChannelFactory factory;

	private volatile boolean isStart;

	final private ConcurrentMap<Channel, byte[]> resultMap;

	public Client(int port, String remoteAddress) {
		this.port = port;
		this.remoteAddress = remoteAddress;
		resultMap = new ConcurrentHashMap<Channel, byte[]>();
		isStart = false;
	}

	public synchronized void start() {
		if (isStart) {
			throw new ClientStartException();
		} else {
			isStart = true;

			factory = new NioClientSocketChannelFactory(
					Executors.newFixedThreadPool(10),
					Executors.newFixedThreadPool(10), 2, 8);

			bootstrap = new ClientBootstrap(factory);
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new Decoder(), new ResultHandler(
							Client.this), new Encoder());
				}

			});
			bootstrap.setOption("tcpKeepAlive", true);
		}
	}

	public synchronized void stop() {
		if (!isStart) {
			throw new ClientStopException();
		} else {
			isStart = false;
		}
	}

	public void putResult(Channel channel, byte[] result) {
		if (isStart) {

			Result result_ = Serializer.deserializer(result, Result.class);
			String responseId = result_.getId();
			Map<String, Object> lock = BaseConsumerProxy.locks.get(responseId);
			lock.put("result", result_);
			synchronized (lock) {
				lock.notify();
			}
		} else {
			throw new ClientStopException();
		}
	}

	public ChannelFuture getConnection() {
		if (isStart) {
			return bootstrap
					.connect(new InetSocketAddress(remoteAddress, port));
		} else {
			throw new ClientStopException();
		}
	}

	public ChannelFuture getConnection(String remoteAddress, int port) {
		if (isStart) {
			return bootstrap
					.connect(new InetSocketAddress(remoteAddress, port));
		} else {
			throw new ClientStopException();
		}
	}

}
