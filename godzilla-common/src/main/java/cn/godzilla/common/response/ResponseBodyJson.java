package cn.godzilla.common.response;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;

public class ResponseBodyJson implements ResponseBody {
	
	public final String returncode; //1XXXXXX 2XXXXXX
	public final String returnmsg; //SUCCESS FAILURE 
	public final String returnmemo;//错误信息
	public final Object data;
	public final String operator;
	
	private ResponseBodyJson() {
		returncode = "";
		returnmsg = "";
		returnmemo = "";
		operator = "";
		data = null;
	}
	
	private ResponseBodyJson(String returncode, String returnmsg, String returnmemo, Object data, String operator) {
		this.returncode = returncode;
		this.returnmsg = returnmsg;
		this.returnmemo = returnmemo;
		this.operator = operator;
		this.data = data;
	}
	
	public static ResponseBodyJson.Builder custom() {
		return new Builder();
	}
	
	public static class Builder {
		private String returncode;
		private String returnmsg;
		private String returnmemo;
		private String operator;
		private Object data;
		
		Builder() {
			super();
			this.returncode = "200000";
			this.returnmsg = "SUCCESS";
			this.returnmemo = "";
			this.operator = "";
			this.data = null;
		}
		
		public ResponseBodyJson.Builder setReturncode(String returncode) {
			this.returncode = returncode;
			return this;
		}

		public ResponseBodyJson.Builder setReturnmsg(String returnmsg) {
			this.returnmsg = returnmsg;
			return this;
		}

		public ResponseBodyJson.Builder setReturnmemo(String returnmemo) {
			this.returnmemo = returnmemo;
			return this;
		}
		
		public ResponseBodyJson.Builder setOperator(String operator) {
			this.operator = operator;
			return this;
		}
		
		public ResponseBodyJson.Builder setAll(BusinessException e, String operator) {
			this.returncode = e.getErrorCode();
			this.returnmsg = e.getStatus();
			this.returnmemo = e.getErrorMsg();
			this.operator = operator;
			this.data = null;
			return this;
			
		}
		
		public ResponseBodyJson.Builder setAll(ReturnCodeEnum returnEnum, String operator) {
			this.returncode = returnEnum.getReturnCode();
			this.returnmsg = returnEnum.getStatus();
			this.returnmemo = returnEnum.getReturnMsg();
			this.operator = operator;
			this.data = null;
			return this;
		}
		
		public ResponseBodyJson.Builder setAll(ReturnCodeEnum returnEnum, Object data, String operator) {
			this.returncode = returnEnum.getReturnCode();
			this.returnmsg = returnEnum.getStatus();
			this.returnmemo = returnEnum.getReturnMsg();
			this.operator = operator;
			this.data = data;
			return this;
		}
		
		public ResponseBodyJson.Builder setAll(String returncode, String returnmsg, String returnmemo, String operator){
			this.returncode = returncode;
			this.returnmsg = returnmsg;
			this.returnmemo = returnmemo;
			this.operator = operator;
			this.data = null;
			return this;
		}

		public ResponseBodyJson build() {
			return new ResponseBodyJson(returncode, returnmsg, returnmemo, data, operator);
		}
		
	}

	public String getReturncode() {
		return returncode;
	}

	public String getReturnmsg() {
		return returnmsg;
	}

	public String getReturnmemo() {
		return returnmemo;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public Object getData() {
		return data;
	}
	
}
