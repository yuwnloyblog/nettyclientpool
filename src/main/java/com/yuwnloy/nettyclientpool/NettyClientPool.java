package com.yuwnloy.nettyclientpool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

public class NettyClientPool extends Pool<AbstractNettyClient<?>>{

	public NettyClientPool(Config poolConfig, INettyClientCreater creater){
		super();
		this.createObjectPool(poolConfig,new NettyClientFactory(creater,this));
	}

	public NettyClientPool(Config poolConfig, PoolableObjectFactory<AbstractNettyClient<?>> factory) {
		super();
		this.createObjectPool(poolConfig, factory);
	}
	
	private static class NettyClientFactory extends BasePoolableObjectFactory<AbstractNettyClient<?>> {
		private INettyClientCreater creater;
		private NettyClientPool pool;
		public NettyClientFactory(INettyClientCreater creater, NettyClientPool pool){
			this.creater = creater;
			this.pool = pool;
		}
		@Override
		public AbstractNettyClient<?> makeObject() throws Exception {
			AbstractNettyClient<?> client = this.creater.createNettyClient();
			client.setNettyClientPool(this.pool);
			return client;
		}

		@Override
		public void destroyObject(AbstractNettyClient<?> obj) throws Exception {
			obj.shutdown();
		}

		@Override
		public boolean validateObject(AbstractNettyClient<?> obj) {
			return obj.isValid();
		}
		
	}
}
