package cn.godzilla.common;

public enum ProfileEnum {
	
	TEST("测试环境","test"),
	PRO_ONLINE("预发标准环境","pro_online"),
	ONLINE("生产标准环境", "online");
	
	
	private final String desc;
	private final String code;
	
	private ProfileEnum(String desc,String code) {
		
		this.desc = desc ;
		
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public String getCode() {
		return code;
	}

	
}
