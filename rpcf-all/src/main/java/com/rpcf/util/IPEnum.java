package com.rpcf.util;

import com.rpcf.api.RpcException;


public enum IPEnum {
	
	nuggets_server1("nuggets-server","TEST", "10.100.142.77"),
	nuggets_server2("nuggets-server","QUASIPRODUCT", "10.100.142.71"),
	nuggets_server3("nuggets-server","PRODUCT", "10.100.142.72"),
	xuanyuan4("xuanyuan","TEST", "10.100.142.82"),
	xuanyuan5("xuanyuan","QUASIPRODUCT", "10.100.142.71"),
	xuanyuan6("xuanyuan","PRODUCT", "10.100.142.72"),
	zeus_server7("zeus-server","TEST", "10.100.142.83"),
	zeus_server8("zeus-server","QUASIPRODUCT", "10.100.142.71"),
	zeus_server9("zeus-server","PRODUCT", "10.100.142.72"),
	cupid10("cupid","TEST", "10.100.142.84"),
	cupid11("cupid","QUASIPRODUCT", "10.100.142.71"),
	cupid12("cupid","PRODUCT", "10.100.142.72"),
	fso_lark13("fso-lark","TEST", "10.100.142.80"),
	fso_lark14("fso-lark","QUASIPRODUCT", "10.100.142.71"),
	fso_lark15("fso-lark","PRODUCT", "10.100.142.72"),
	message_center16("message-center","TEST", "10.100.142.79"),
	message_center17("message-center","QUASIPRODUCT", "10.100.142.71"),
	message_center18("message-center","PRODUCT", "10.100.142.72"),
	hera19("hera","TEST", "10.100.142.81"),
	hera20("hera","QUASIPRODUCT", "10.100.142.71"),
	hera21("hera","PRODUCT", "10.100.142.72"),
	new_manager22("new-manager","TEST", "10.100.142.78"),
	new_manager23("new-manager","QUASIPRODUCT", "10.100.142.71"),
	new_manager24("new-manager","PRODUCT", "10.100.142.72"),
	hades25("hades","TEST", "10.100.142.70"),
	hades26("hades","QUASIPRODUCT", "10.100.142.71"),
	hades27("hades","PRODUCT", "10.100.142.72"),
	apollo28("apollo","TEST", "10.100.142.75"),
	apollo29("apollo","QUASIPRODUCT", "10.100.142.71"),
	apollo30("apollo","PRODUCT", "10.100.142.72"),
	uic31("uic","TEST", "10.100.142.85"),
	uic32("uic","QUASIPRODUCT", "10.100.142.71"),
	uic33("uic","PRODUCT", "10.100.142.72"),
	uicm34("uicm","TEST", "10.100.142.86"),
	uicm35("uicm","QUASIPRODUCT", "10.100.142.71"),
	uicm36("uicm","PRODUCT", "10.100.142.72"),
	gardener37("gardener","TEST", "10.100.142.69"),
	gardener38("gardener","QUASIPRODUCT", "10.100.142.71"),
	gardener39("gardener","PRODUCT", "10.100.142.72"),
	
	test("test", "test", "127.0.0.1"),
	;
	
	IPEnum(String projectCode, String profile, String ip){
		this.projectCode = projectCode;
		this.profile = profile;
		this.ip = ip;
	}
			
	private String projectCode;
	
	private String profile;
	
	private String ip;

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public static String getIPbyProjectcodeAndProfile(String projectCode, String profile) {
		for(IPEnum ipEnum: IPEnum.values()) {
			if(ipEnum.getProjectCode().equals(projectCode)&&ipEnum.getProfile().equals(profile)) {
				return ipEnum.getIp();
			}
		}
		throw new RpcException("NO IP found with projectcode:" + projectCode +" , profile:" + profile);
	}
	
}
