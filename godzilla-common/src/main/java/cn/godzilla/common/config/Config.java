package cn.godzilla.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.util.StringUtils;

import cn.godzilla.common.Constant;

public class Config implements Constant {

	public static final String PRC_PROPERTIES = "rpc.properties";
	public static final String ECHO_PROPERTIES = "echo.properties";

	public static Properties rpc_properties;
	public static Properties echo_properties;

	public static final Object lock = new Object();
	private static synchronized void initEchoProperties() throws IOException {
		if (echo_properties == null) {
			echo_properties = new Properties();
			InputStream is = Config.class.getClassLoader()
								.getResourceAsStream(ECHO_PROPERTIES);
			echo_properties.load(is);
			try {
				is.close();
			} catch (Throwable t) {
				// ignore
			}
		}
	}
	private static synchronized void initRpcProperties() throws IOException {
		if (rpc_properties == null) {
			rpc_properties = new Properties();
			InputStream is = Config.class.getClassLoader()
								.getResourceAsStream(PRC_PROPERTIES);
			rpc_properties.load(is);
			try {
				is.close();
			} catch (Throwable t) {
				// ignore
			}
		}
	}

	public static String getWebsocketPort() throws IOException {
		initEchoProperties();
		String websocket_port = !StringUtils.isEmpty(
				echo_properties.getProperty(WEBSOCKET_PORT_KEY)) ? 
						echo_properties.getProperty(WEBSOCKET_PORT_KEY) :
							DEFAULT_WEBSOCKET_PORT;
		return websocket_port;
	}
	
	public static String getMqProducerName() throws IOException {
		initEchoProperties();
		String mq_producer_name = !StringUtils.isEmpty(
				echo_properties.getProperty(MQ_PRODUCER_NAME_KEY)) ? 
						echo_properties.getProperty(MQ_PRODUCER_NAME_KEY) : 
							DEFAULT_MQ_PRODUCER_NAME;
		return mq_producer_name;
	}

	public static String getMqConsumerName() throws IOException {
		initEchoProperties();
		String mq_consumer_name = !StringUtils.isEmpty(
				echo_properties.getProperty(MQ_CONSUMER_NAME_KEY)) ? 
						echo_properties.getProperty(MQ_CONSUMER_NAME_KEY) : 
							DEFAULT_MQ_CONSUMER_NAME;
		return mq_consumer_name;
	}

	public static String getMqNamesrvAddr() throws IOException {
		initEchoProperties();
		String mq_namesrv_addr = !StringUtils.isEmpty(
				echo_properties.getProperty(MQ_NAMESRV_ADDR_KEY)) ? 
						echo_properties.getProperty(MQ_NAMESRV_ADDR_KEY) : 
							DEFAULT_MQ_NAMESRV_ADDR;
		return mq_namesrv_addr;
	}

	public static String getMqTopic() throws IOException {
		initEchoProperties();
		String mq_topic = !StringUtils.isEmpty(
				echo_properties.getProperty(MQ_TOPIC_KEY)) ? 
						echo_properties.getProperty(MQ_TOPIC_KEY) : 
							DEFAULT_MQ_TOPIC;
		return mq_topic;
	}
	
	public static String getPomBasepath(String ex) throws IOException {
		initRpcProperties();
		String pom_basepath = !StringUtils.isEmpty(
				rpc_properties.getProperty(POM_BASEPATH_KEY)) ?
						rpc_properties.getProperty(POM_BASEPATH_KEY) :
							DEFAULT_POM_BASEPATH;
		return pom_basepath;
	}

}
