package cn.godzilla.common;

public interface Constant {
	
	public final int TRUE = 1;
	public final int FALSE = 0 ;
	
	public final String DEFAULT_VERSION_PARENTPOM = "1.0.0-SNAPSHOT";
	public final String EMPTY_BRANCH = "empty";
	
	public final String SUCCESS = "SUCCESS";
	public final String FAILURE = "FAILURE";
	public final String BUILDFAILURE = "BUILDFAILURE";
	public final String NOSETPROPS = "NOSETPROPS";
	public final String AUDITOR_TEXT = "审核通过";
	public final int DEFAULT_TIMEOUT = 50;//tomcat启动超时时间(second)
	/**
	 * log constant
	 */
	public final int SUCC = 1;
	public final int FAIL = -2;
	public final String DEPLOY = "deploy";
	public final String SRCEDIT = "srcEdit";
	public final String UPDATEPROP = "updateProp";
	public final String VERIFYPROP = "verifyProp";
	public final String SORTPROP = "sortprop";
	public final String BRANCHADD = "branchadd";
	public final String BRANCHEDIT = "branchedit";
	public final String BRANCHDELETE = "branchdelete";
	public final String SVNSTATUS = "svnstatus";
	public final String SVNMERGE = "svnmerge";
	public final String SVNCOMMIT = "svncommit";
	public final String TOMCATRESTART = "tomcatrestart";
	public final String WARDOWNLOAD= "wardownload";
	public final String GODZILLAEX= "godzillaex";
	public final String ADMINOPERATOR= "adminoperator";
	public final String EDITWORKDESK = "editworkdesk";
	public final String ADDUSER = "adduser";
	public final String LOGIN = "login";
	
	public final String NOTYET_VERIFY_STATUS = "0";//未审核
	public final String OK_VERIFY_STATUS = "1";//审核通过
	public final String STOP_VERIFY_STATUS = "2";//未通过
	public final String PROJECT_ENV = "test";
	public final String SHELL_SERVER_PATH = "/home/godzilla/gzl/shell/server";
	public final String SHELL_CLIENT_PATH = "/home/godzilla/gzl/shell/client";
	public final String SAVE_WAR_PATH= "/home/godzilla/gzl/war";
	public final String QUASIPRODUCT_WAR_IP= "10.100.142.71";
	public final String PRODUCT_WAR_IP = "10.100.142.72";
	/**
	 * freemarker ftl url base path
	 */
	public final String BASE_PATH = "godzilla-web";
	
	
	public final String TEST_PROFILE = "TEST";
	public final String QUASIPRODUCT_PROFILE = "QUASIPRODUCT";
	public final String PRODUCT_PROFILE = "PRODUCT";
	/**
	 * echo default value
	 */
	public final int DEFAULT_WEBSOCKET_PORT = 9999;
	public final String DEFAULT_MQ_PRODUCER_NAME = "godzilla-producer";
	public final String DEFAULT_MQ_CONSUMER_NAME = "godzilla-consumer";
	public final String DEFAULT_MQ_NAMESRV_ADDR = "10.100.142.65:9876";
	public final String DEFAULT_MQ_TOPIC = "godzilla";
	/**
	 * rpc default value
	 */
	public final String DEFAULT_POM_BASEPATH = "/home/godzilla/gzl/work/godzilla";
	
	/**
	 * echo KEY NAME 
	 */
	public final String WEBSOCKET_PORT_KEY = "websocket.port";
	public final String MQ_PRODUCER_NAME_KEY = "rocketmq.producer.name";
	public final String MQ_CONSUMER_NAME_KEY = "rocketmq.consumer.name";
	public final String MQ_NAMESRV_ADDR_KEY = "rocketmq.nameserver.address";
	public final String MQ_TOPIC_KEY = "rocketmq.topic";
	
	/**
	 * rpc KEY NAME 
	 */
	public final String POM_BASEPATH_KEY = "rpc.pom.basepath";
	
	
	public final static String PROFILE_TEST = "test";
	public final static String PROFILE_PRO_DEPLOY = "pro_deploy";
	public final static String PROFILE_ONLINE = "online" ;

	public final static String NO_AJAX = "100000";
	public final static String NULL_NAMEPASSWORD = "100001";
	public final static String NOTEXIST_USER = "100002";
	public final static String WRONG_PASSWORD = "100003";
	public final static String UNKNOW_ERROR = "100004";
	public final static String NO_LOGIN = "100005";
	public final static String NO_ADDUPDATEPROP = "100011";
	public final static String NO_VERIFYPROP = "100012";
	public final static String NO_AUTHORITY = "100013";
	public final static String NO_MVNDEPLOY = "100014";
	public final static String NO_CHANGEPOM = "100015";
	public final static String NO_RPCFACTORY = "100016";
	public final static String NO_AUTHORIZATION = "100017";
	public final static String NO_ADDUSER = "100019";
	public final static String NO_SAMEPASSWORD = "100019";
	public final static String NO_UPDATEFUNRIGHT = "100020";
	public final static String NO_CHANGECOMMIT = "100021";
	public final static String NO_SVNCOMMIT = "100022";
	public final static String NO_FOUNDCONFLICT = "100023";
	public final static String NO_CLIENTPARAM = "100024";
	public final static String NO_SERVERPARAM = "100025";
	public final static String NO_JAVASHELLCALL = "100026";
	public final static String NO_SVNVERSION = "100027";
	public final static String NO_GODZILLA = "100029";
	public final static String NO_DELETEBRANCH = "100030";
	public final static String NO_SVNMERGE = "100031";
	public final static String NO_MVNBUILD = "100032";
	public final static String NO_COMMON = "100033";
	public final static String NO_CONCURRENCEDEPLOY = "100034";
	public final static String NO_MVNSETPROPS = "100035";
	public final static String NO_HASKEYDEPLOY = "100036";
	public final static String NO_EXISTUSER = "100037";
	public final static String NO_STARTTOMCAT = "100038";
	public final static String NO_SRCEDIT = "100039";
	public final static String NO_BRANCHADD = "100040";
	public final static String NO_SVNSTATUS = "100041";
	
	public final static String OK_AJAX = "200000";
	public final static String OK_CHECKUSER = "200001";
	public final static String OK_LOGIN = "200002";
	public final static String OK_ADDUPDATEPROP = "200011";
	public final static String OK_VERIFYPROP = "200012";
	public final static String OK_MVNDEPLOY = "200014";
	public final static String OK_AUTHORIZATION = "200017";
	public final static String OK_ADDUSER = "200018";
	public final static String OK_UPDATEFUNRIGHT = "200020";
	public final static String OK_SVNCOMMIT = "200021";
	public final static String OK_SVNVERSION = "200027";
	public final static String OK_SORTPROP = "200028";
	public final static String OK_GODZILLA = "200029";
	public final static String OK_DELETEBRANCH = "200030";
	public final static String OK_SVNMERGE = "200031";
	public final static String OK_STARTTOMCAT = "200038";
	public final static String OK_SRCEDIT = "200039";
	public final static String OK_BRANCHADD = "200040";
	public final static String OK_SVNSTATUS = "200041";
}
