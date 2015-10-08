package cn.godzilla.rpc.benchmark.service;

import cn.godzilla.rpc.benchmark.dataobject.Person;

/**
 * @author ding.lid
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public Person helloPerson(Person in) {
        return in;
    }
    @Override
    public String helloWorld(String in) {
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return in+":"+System.currentTimeMillis();
    }
}
