package com.yuwnloy.nettyclientpool;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyClientHandler<T> extends ChannelHandlerAdapter{
	private LinkedBlockingQueue<T> queue;
	public NettyClientHandler(LinkedBlockingQueue<T> queue){
		this.queue = queue;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx){
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception{
		if(this.queue!=null){
			this.queue.put((T)msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		ctx.close();
	}
}
