package cn.godzilla.rpc.network.handler;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

public class Decoder extends ReplayingDecoder<DecodeState> {

	private int length;

	public Decoder() {
		super(DecodeState.READ_LENGTH);
		length = 0;
	}

	@Override
	protected Object decode(ChannelHandlerContext arg0, Channel arg1,
			ChannelBuffer arg2, DecodeState arg3) throws Exception {
		switch (arg3) {
		case READ_LENGTH:
			length = arg2.readInt();
			this.checkpoint(DecodeState.READ_CONTENT);
		case READ_CONTENT:
			byte[] data = arg2.readBytes(length).array();
			length = 0;
			this.checkpoint(DecodeState.READ_LENGTH);
			return data;
		default:
			throw new Exception("读取错误");
		}
	}

}
