package com.yuwnloy.nettyclientpool;

public interface INettyClientCreater {
	public AbstractNettyClient<?> createNettyClient();
}
