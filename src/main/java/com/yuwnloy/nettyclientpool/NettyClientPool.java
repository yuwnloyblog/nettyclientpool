package com.yuwnloy.nettyclientpool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

public class NettyClientPool extends Pool<AbstractNettyClient<?>>{

	public NettyClientPool(Config poolConfig, INettyClientCreator creator){
		super();
		this.createObjectPool(poolConfig,new NettyClientFactory(creator,this));
	}

	public NettyClientPool(Config poolConfig, PoolableObjectFactory<AbstractNettyClient<?>> factory) {
		super();
		this.createObjectPool(poolConfig, factory);
	}
	
	private static class NettyClientFactory extends BasePoolableObjectFactory<AbstractNettyClient<?>> {
		private INettyClientCreator creator;
		private NettyClientPool pool;
		public NettyClientFactory(INettyClientCreator creator, NettyClientPool pool){
			this.creator = creator;
			this.pool = pool;
		}
		@Override
		public AbstractNettyClient<?> makeObject() throws Exception {
			AbstractNettyClient<?> client = this.creator.createNettyClient();
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
