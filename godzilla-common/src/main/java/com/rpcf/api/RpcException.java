package com.rpcf.api;

public class RpcException extends RuntimeException {
	
	private String message = "";
	
	public RpcException () {
		super();
	}
	
	public RpcException (String message) {
		this.message = message;
	}
	
	public RpcException(String message, Throwable cause){
		super(cause);
		this.message = message;
	}
	
	public RpcException (Throwable cause) {
		super(cause);
	}
	
	public String getMessage() {
		return this.message;
	}
}
