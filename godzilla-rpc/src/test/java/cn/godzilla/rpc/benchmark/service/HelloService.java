package cn.godzilla.rpc.benchmark.service;

import cn.godzilla.rpc.benchmark.dataobject.Person;

/**
 * @author ding.lid
 */
public interface HelloService {
     Person helloPerson(Person in);
     String helloWorld(String in);
}
