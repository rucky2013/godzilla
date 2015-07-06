package cn.godzilla.common;

public interface Constant {
	
	public int TRUE = 1;
	
	public int FALSE = 0 ;
	
	//echo
	public String DEFAULT_WEBSOCKET_PORT = "8099";
	public String DEFAULT_MQ_PRODUCER_NAME = "godzilla-producer";
	public String DEFAULT_MQ_CONSUMER_NAME = "godzilla-consumer";
	public String DEFAULT_MQ_NAMESRV_ADDR = "10.100.142.65:9876";
	public String DEFAULT_MQ_TOPIC = "godzilla";
	
	
	//------------------KEY NAME--------
	//echo
	public String WEBSOCKET_PORT_KEY = "websocket.port";
	public String MQ_PRODUCER_NAME_KEY = "rocketmq.producer.name";
	public String MQ_CONSUMER_NAME_KEY = "rocketmq.consumer.name";
	public String MQ_NAMESRV_ADDR_KEY = "rocketmq.nameserver.address";
	public String MQ_TOPIC_KEY = "rocketmq.topic";
}
