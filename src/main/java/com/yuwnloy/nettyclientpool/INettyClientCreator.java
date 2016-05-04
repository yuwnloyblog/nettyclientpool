package com.yuwnloy.nettyclientpool;

public interface INettyClientCreator {
	public AbstractNettyClient<?> createNettyClient();
}
