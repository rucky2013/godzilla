package cn.godzilla.rpc.main;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.godzilla.rpc.api.RpcFactory;

/**
 * @author ding.lid
 */
public class Util {


    public static final String RPC_PROPERTIES = "rpc.properties";

    public static final String RPC_FACTORY_IMPL_KEY = "factory.impl";
    
    public static final String RPC_PORT_KEY = "rpc.port";

    public static Properties properties;
    public static final Object lock = new Object();

    private static void initProperties() throws IOException {
        if(properties == null) {
            synchronized (lock) {

                if(properties == null) {

                    properties = new Properties();
                    InputStream is = ServerMain.class.getClassLoader().getResourceAsStream(RPC_PROPERTIES);
                    properties.load(is);
                    try {
                        is.close();
                    }
                    catch (Throwable t) {
                        // ignore
                    }
                }
            }
        }
    }

    public static RpcFactory getRpcFactoryImpl() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        initProperties();
        //Constant.DEFAULT_PORT = Integer.parseInt(properties.getProperty(RPC_PORT_KEY));
        String rpcFactoryClassName = properties.getProperty(RPC_FACTORY_IMPL_KEY);
        return Class.forName(rpcFactoryClassName).asSubclass(RpcFactory.class).newInstance();
    }
}