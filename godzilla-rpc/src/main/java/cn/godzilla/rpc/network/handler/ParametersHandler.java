package cn.godzilla.rpc.network.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import cn.godzilla.rpc.server.Server;

public class ParametersHandler extends SimpleChannelUpstreamHandler {

	private final Server server;

	public ParametersHandler(Server server) {
		this.server = server;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		byte[] parameters = (byte[])e.getMessage();
		server.putParameters(e.getChannel(), parameters);
	}

}
