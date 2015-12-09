package com.rpcf.server;

import com.rpcf.api.RpcFactory;
import com.rpcf.benchmark.service.HelloService;
import com.rpcf.benchmark.service.HelloServiceImpl;
import com.rpcf.factory.GodzillaRpcFactory;

/**
 * @author ding.lid
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {
        RpcFactory rpcFactory = new GodzillaRpcFactory();

        rpcFactory.export(HelloService.class, new HelloServiceImpl());

        synchronized (ServerMain.class) {
            ServerMain.class.wait(); // block main thread to prevent process exit.
        }
    }
}
