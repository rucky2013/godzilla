package com.rpcf.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.channel.Channel;

import com.rpcf.common.Request;
import com.rpcf.common.Response;

public class DefaultFuture {
	
	private static final Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<Long, DefaultFuture>();
	
	private final long id;
	
	private volatile Response response;
	
	private final Lock lock = new ReentrantLock();
	
	private final Condition done = lock.newCondition();
	
	public DefaultFuture(Request request) {
		this.id = request.getId();
		FUTURES.put(id, this);
	}
	
	public Response get() {
		if(!isDone()) {
			lock.lock();
			try {
				while(!isDone()) {
					done.await();
					if(isDone()) {
						break;
					}
				}
			} catch(InterruptedException e ) {
				e.printStackTrace();
			} catch(Throwable e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
		return response;
	}
	
	private boolean isDone() {
		return response!=null;
	}
	
	public static void received(Response res) {
		long id = res.getId();
		DefaultFuture future = FUTURES.remove(id);
		future.lock.lock();
		try {
			future.response = res;
			if(future.done!=null) {
				future.done.signal();
			}
		} finally {
			future.lock.unlock();
		} 
	}
}
