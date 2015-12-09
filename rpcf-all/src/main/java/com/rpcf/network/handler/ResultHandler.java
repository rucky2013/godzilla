package com.rpcf.network.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.rpcf.client.Client;

public class ResultHandler extends SimpleChannelUpstreamHandler {
	
	private final Client client;
	
	public ResultHandler(Client client) {
		this.client = client;
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		client.received(e.getChannel(), (byte[])e.getMessage());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getCause().printStackTrace();
	}
}
