package cn.godzilla.common;

public class BusinessException extends RuntimeException implements Constant {
	
	final String status = "FAILURE";
	String errorCode = "";
	String errorMsg = "";
	
	public BusinessException(String msg) {
		super(msg);
		this.errorCode = NO_COMMON;
		this.errorMsg = msg;
	}
	
	public BusinessException(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	public String getStatus() {
		return status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	
}
