package com.yuwnloy.nettyclientpool;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;


public abstract class Pool<T> {
    private GenericObjectPool<T> internalPool;

    public Pool() {
    }
    
    protected void createObjectPool(final GenericObjectPool.Config poolConfig,
            PoolableObjectFactory<T> factory){
    	this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
    }

    public T getNettyClient() {
        try {
            return (T) internalPool.borrowObject();
        } catch (Exception e) {
           
        }
        return null;
    }
        
    public void releaseNettyClient(final T resource) {
        try {
            internalPool.returnObject(resource);
        } catch (Exception e) {
           
        }
    }

    protected void releaseBrokenNettyClient(final T resource) {
        try {
            internalPool.invalidateObject(resource);
        } catch (Exception e) {
           
        }
    }

    public void destroy() {
        try {
            internalPool.close();
        } catch (Exception e) {
           
        }
    }
}