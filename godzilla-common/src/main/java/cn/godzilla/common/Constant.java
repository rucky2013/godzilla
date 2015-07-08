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
	
	final static String PROFILE_TEST = "test";
	
	final static String PROFILE_PRO_DEPLOY = "pro_deploy";
	
	final static String PROFILE_ONLINE = "online" ;

	final static String NULL_NAMEPASSWORD = "100001";
	final static String NOTEXIST_USER = "100002";
	final static String WRONG_PASSWORD = "100003";
	final static String UNKNOW_ERROR = "100004";
	
	final static String OK_CHECKUSER = "200001";
	final static String OK_LOGIN = "200002";
}
