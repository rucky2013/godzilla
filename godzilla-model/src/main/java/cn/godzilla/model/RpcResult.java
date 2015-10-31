package cn.godzilla.model;

import java.io.Serializable;

public class RpcResult implements Serializable{
	
	public String rpcCode;
	public String rpcMsg;
	public int logid;
	
	private RpcResult() {
		this("0", "success", 0);
	}
	public RpcResult(String rpcCode, String rpcMsg, int logid) {
		this.rpcCode = rpcCode;
		this.rpcMsg = rpcMsg;
		this.logid = logid;
	}
	
	public int getLogid() {
		return logid;
	}
	public void setLogid(int logid) {
		this.logid = logid;
	}
	public String getRpcCode() {
		return rpcCode;
	}
	public void setRpcCode(String rpcCode) {
		this.rpcCode = rpcCode;
	}
	public String getRpcMsg() {
		return rpcMsg;
	}
	public void setRpcMsg(String rpcMsg) {
		this.rpcMsg = rpcMsg;
	}
	
	public static RpcResult create(String sort, int logid) {
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
		default:
			rpcResult = new RpcResult(CREATEFAIL, "RpcResult create failed", logid);
		}
		return rpcResult;
	}
	
	private final static String SUCCESS = "0";
	private final static String FAILURE = "1";
	private final static String CREATEFAIL = "2";
	private final static String BUILDFAILURE = "3";
	private final static String NOSETPROPS = "4";
}
