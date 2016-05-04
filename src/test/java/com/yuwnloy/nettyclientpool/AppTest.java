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
		  NettyClientPool pool = new NettyClientPool(poolConfig,new INettyClientCreator(){

			@Override
			public AbstractNettyClient<?> createNettyClient() {
				return new StringNettyClient("127.0.0.1",8080);
			}});
		  for(int i=0;i<10;i++){
		  new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				StringNettyClient s = (StringNettyClient) pool.getNettyClient();
				  String sa =  s.syncSendMsg("Query Time Order");
				  System.out.println(sa+":");
			}
			  
		  }).start();
		  }
	  }
}
