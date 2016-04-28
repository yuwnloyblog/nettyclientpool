package com.yuwnloy.nettyclientpool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

public class NettyClientPool extends Pool<AbstractNettyClient<?>>{

	public NettyClientPool(Config poolConfig, INettyClientCreater creater){
		super(poolConfig,new NettyClientFactory(creater));
	}

	public NettyClientPool(Config poolConfig, PoolableObjectFactory<AbstractNettyClient<?>> factory) {
		super(poolConfig, factory);
	}
	
	private static class NettyClientFactory extends BasePoolableObjectFactory<AbstractNettyClient<?>> {
		private INettyClientCreater creater;
		public NettyClientFactory(INettyClientCreater creater){
			this.creater = creater;
		}
		@Override
		public AbstractNettyClient<?> makeObject() throws Exception {
			AbstractNettyClient<?> client = this.creater.createNettyClient();
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
