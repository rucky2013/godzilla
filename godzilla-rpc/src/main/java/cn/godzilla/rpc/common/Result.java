package cn.godzilla.rpc.common;

public class Result {
	private String id;
	private boolean isSuccess;
	private String message;
	private Object result;
	
	public Result() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
	
	public static Result success(Object val, String id) {
		Result result = new Result();
		result.setResult(val);
		result.setSuccess(true);
		result.setId(id);
		return result;
	}
	
	public static Result fail(String msg, String id) {
		Result result = new Result();
		result.setMessage(msg);
		result.setResult(null);
		result.setSuccess(false);
		result.setId(id);
		return result;
	}
	                         
}
