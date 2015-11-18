package cn.godzilla.model;

import java.io.Serializable;

public class RpcResult implements Serializable{
	
	private String rpcCode;
	private String rpcMsg;
	private Long logid;
	private Object data;
	
	private RpcResult() {
		this("0", "success", 0L);
	}
	public RpcResult(String rpcCode, String rpcMsg, Long logid) {
		this.rpcCode = rpcCode;
		this.rpcMsg = rpcMsg;
		this.logid = logid;
		this.data = data;
	}
	
	public RpcResult(String rpcCode, String rpcMsg, Long logid, Object data) {
		this.rpcCode = rpcCode;
		this.rpcMsg = rpcMsg;
		this.logid = logid;
		this.data = data;
	}
	
	public Long getLogid() {
		return logid;
	}
	void setLogid(Long logid) {
		this.logid = logid;
	}
	public String getRpcCode() {
		return rpcCode;
	}
	void setRpcCode(String rpcCode) {
		this.rpcCode = rpcCode;
	}
	public String getRpcMsg() {
		return rpcMsg;
	}
	void setRpcMsg(String rpcMsg) {
		this.rpcMsg = rpcMsg;
	}
	void setData(Object data) {
		this.data = data;
	}
	public Object getData() {
		return data;
	}
	
	public static RpcResult create(String sort, Long logid) {
		RpcResult rpcResult = null;
		switch(sort){
		case "SUCCESS":
			rpcResult = new RpcResult(SUCCESS,"success", logid);
			break;
		case "FAILURE":
			rpcResult = new RpcResult(FAILURE,"failure", logid);
			break;
		case "BUILDFAILURE":
			rpcResult = new RpcResult(BUILDFAILURE,"BUILDFAILURE", logid);
			break;
		case "NOSETPROPS":
			rpcResult = new RpcResult(NOSETPROPS, "NOSETPROPS", logid);
			break;
		case "LOOSEPROP":
			rpcResult = new RpcResult(LOOSEPROP, "LOOSEPROP", logid);
			break;
		default:
			rpcResult = new RpcResult(CREATEFAIL, "RpcResult create failed", logid);
		}
		return rpcResult;
	}
	
	public static RpcResult createLooseprop(String sort, Long logid, Object data) {
		return new RpcResult(LOOSEPROP, "LOOSEPROP", logid, data);
	}
	
	public final static String SUCCESS = "0";
	public final static String FAILURE = "1";
	public final static String CREATEFAIL = "2";
	public final static String BUILDFAILURE = "3";
	public final static String NOSETPROPS = "4";
	public final static String LOOSEPROP = "5";
}
