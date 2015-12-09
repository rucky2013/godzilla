package com.rpcf.common;

import java.io.Serializable;

public class Response implements Serializable{
	
	private Long id;
	
	private boolean isSuccess;
	
	private String message;
	
	private Object result;
	
	private Object attach;
	
	public Response() {
		
	}

	public Long getId() {
		return id;
	}

	public Object getAttach() {
		return attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	public static Response success(Object val, Long id) {
		Response result = new Response();
		result.setResult(val);
		result.setSuccess(true);
		result.setId(id);
		return result;
	}
	
	public static Response fail(String msg, Long id) {
		Response result = new Response();
		result.setMessage(msg);
		result.setResult(null);
		result.setSuccess(false);
		result.setId(id);
		return result;
	}
	                         
}
