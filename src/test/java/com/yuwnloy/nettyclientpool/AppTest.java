package com.yuwnloy.nettyclientpool;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	  public static void main(String[] args){
		 // Config poolConfig, String host, int port, INettyClientCreater creater
		  Config poolConfig = new Config();
		  NettyClientPool pool = new NettyClientPool(poolConfig,new INettyClientCreater(){

			@Override
			public AbstractNettyClient<?> createNettyClient() {
				return new StringNettyClient("127.0.0.1",8080);
			}});
		  
		  StringNettyClient s = (StringNettyClient) pool.getNettyClient();
		  String sa =  s.syncSendMsg("Query Time Order");
		  System.out.println(sa);
	  }
}
