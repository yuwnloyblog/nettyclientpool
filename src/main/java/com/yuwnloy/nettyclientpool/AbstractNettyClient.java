package com.yuwnloy.nettyclientpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public abstract class AbstractNettyClient<T> {
	private int timeout = -1;
	private final LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>(10);
	private static EventLoopGroup group = new NioEventLoopGroup();
	
	private String host;
	private int port;
	private Channel channel = null;
	public AbstractNettyClient(String host, int port){
		this.host = host;
		this.port = port;
	}
	public AbstractNettyClient(String host, int port, int timeout){
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}
	public String getHost(){
		return this.host;
	}
	public int getPort(){
		return this.port;
	}
	/**
	 * connect to server.
	 */
	protected void connect() {
		try {
			close();
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							setChannelPipeline(pipeline);
							pipeline.addLast("handler", new NettyClientHandler<T>(queue));
						}
					});
			this.channel = b.connect(this.host, this.port).sync().channel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract void setChannelPipeline(ChannelPipeline pipeline);
	/**
	 * send the msg by channel.
	 * @param msg
	 * @return
	 */
	public boolean sendMsg(T msg) {
		boolean result = true;
		if (msg!=null) {
			try {
				if (!this.isValid()) {
					this.close();
					connect();
				}
				if (this.isValid()) {
					this.channel.writeAndFlush(this.preHandleMsg(msg)).sync();
				} else {
					result = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
	
	public T syncSendMsg(T msg){
		if(msg==null){
			return null;
		}
		this.queue.clear();
		boolean ret = this.sendMsg(msg);
		T result = null;
		if(ret){
			try {
				if(timeout<=0)
					result = this.queue.take();
				else
					result = this.queue.poll(timeout, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	protected Object preHandleMsg(T msg){
		return msg;
	}
	
	public void close() {
		try {
			if (channel != null) {
				channel.close().sync();
				this.channel = null;
				// System.out.println("ch.close");
			} else {
				// System.out.println("ch=null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean isValid(){
		if(this.channel!=null&&this.channel.isOpen()&&this.channel.isWritable()){
			return true;
		}
		return false;
	}
	public void shutdown(){
		this.close();
		if(this.group!=null){
			this.group.shutdownGracefully();
			this.channel = null;
			this.group = null;
		}
	}
	private NettyClientPool pool;
	protected void setNettyClientPool(NettyClientPool pool){
		this.pool = pool;
	}
	public void release(){
		if(this.pool!=null){
			this.pool.releaseNettyClient(this);
		}
	}
}
