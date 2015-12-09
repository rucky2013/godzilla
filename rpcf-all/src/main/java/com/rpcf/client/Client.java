package com.rpcf.client;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.rpcf.common.Request;
import com.rpcf.common.Response;
import com.rpcf.network.handler.Decoder;
import com.rpcf.network.handler.Encoder;
import com.rpcf.network.handler.ResultHandler;
import com.rpcf.proxy.DefaultFuture;
import com.rpcf.serialize.Serializer;

public class Client {

	private ClientBootstrap bootstrap;

	private static ChannelFactory channelFactory = new NioClientSocketChannelFactory(Executors.newFixedThreadPool(10), Executors.newFixedThreadPool(10), 2, 8);

	private Channel channel;
	
	public Channel getChannel() {
		return channel;
	}

	public static Map<String, Client> clients = new ConcurrentHashMap<String, Client>(); //ip:port

	public Client(String ip, int port) throws Throwable {
		doOpen();
		doConnect(ip, port);
	}

	public void doOpen() throws Throwable {
		bootstrap = new ClientBootstrap(channelFactory);
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new Decoder(), new ResultHandler(Client.this), new Encoder());
			}
		});
	}

	public void doConnect(String ip, int port) {
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
		Channel newChannel = future.getChannel();
		Channel oldChannel = Client.this.channel;
		if(oldChannel!=null) {
			System.out.println("Close old netty channel " + oldChannel + " on create new netty channel " + newChannel);
		}
		Client.this.channel = newChannel;
		clients.put(ip+":"+port, Client.this);
	}

	public void received(Channel channel, byte[] message) {
		Response response = Serializer.deserializer(message, Response.class);
		DefaultFuture.received(response);
	}
	
	public DefaultFuture send(Request request) throws Exception {
		DefaultFuture future = new DefaultFuture(request);
		byte[] message = Serializer.serialize(request);
		channel.write(message);
		return future;
	}
}
