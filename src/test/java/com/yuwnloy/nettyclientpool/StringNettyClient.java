package com.yuwnloy.nettyclientpool;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class StringNettyClient extends AbstractNettyClient<String>{
	public StringNettyClient(String host, int port) {
		super(host, port);
		this.connect();
	}

	@Override
	public void setChannelPipeline(ChannelPipeline pipeline) {
		Charset cs = Charset.forName("UTF-8");
		pipeline.addFirst("frameDecoder", new LineBasedFrameDecoder(1024));
		pipeline.addLast("decoder", new StringDecoder(cs));
		pipeline.addLast("encoder", new StringEncoder(cs));
		
	}
	@Override
	protected Object preHandleMsg(String msg) {
		byte[] req = (msg+System.getProperty("line.separator")).getBytes();
		ByteBuf buf = Unpooled.buffer(req.length);
		buf.writeBytes(req);
		return buf;
	}
	
	
}
