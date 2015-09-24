package cn.godzilla.common.response;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;

public class ResponseBodyJson implements ResponseBody {
	
	public final String returncode;
	public final String returnmsg;
	public final String returnmemo;
	public final Object data;
	
	private ResponseBodyJson() {
		returncode = "";
		returnmsg = "";
		returnmemo = "";
		data = null;
	}
	
	private ResponseBodyJson(String returncode, String returnmsg, String returnmemo, Object data) {
		this.returncode = returncode;
		this.returnmsg = returnmsg;
		this.returnmemo = returnmemo;
		this.data = data;
	}
	
	public static ResponseBodyJson.Builder custom() {
		return new Builder();
	}
	
	public static class Builder {
		private String returncode;
		private String returnmsg;
		private String returnmemo;
		private Object data;
		
		Builder() {
			super();
			this.returncode = "200000";
			this.returnmsg = "SUCCESS";
			this.returnmemo = "";
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
		
		public ResponseBodyJson.Builder setAll(BusinessException e) {
			this.returncode = e.getErrorCode();
			this.returnmsg = e.getStatus();
			this.returnmemo = e.getErrorMsg();
			this.data = null;
			return this;
			
		}
		
		public ResponseBodyJson.Builder setAll(ReturnCodeEnum returnEnum) {
			this.returncode = returnEnum.getReturnCode();
			this.returnmsg = returnEnum.getStatus();
			this.returnmemo = returnEnum.getReturnMsg();
			this.data = null;
			return this;
		}
		
		public ResponseBodyJson.Builder setAll(ReturnCodeEnum returnEnum, Object data) {
			this.returncode = returnEnum.getReturnCode();
			this.returnmsg = returnEnum.getStatus();
			this.returnmemo = returnEnum.getReturnMsg();
			this.data = data;
			return this;
		}
		
		public ResponseBodyJson.Builder setAll(String returncode, String returnmsg, String returnmemo){
			this.returncode = returncode;
			this.returnmsg = returnmsg;
			this.returnmemo = returnmemo;
			this.data = null;
			return this;
		}

		public ResponseBodyJson build() {
			return new ResponseBodyJson(returncode, returnmsg, returnmemo, data);
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
	
	public Object getData() {
		return data;
	}
	
}
