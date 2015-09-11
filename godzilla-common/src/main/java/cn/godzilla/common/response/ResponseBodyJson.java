package cn.godzilla.common.response;

import cn.godzilla.common.ReturnCodeEnum;

public class ResponseBodyJson implements ResponseBody {
	
	public final String returncode;
	public final String returnmsg;
	public final String returnmemo;
	
	private ResponseBodyJson() {
		returncode = "";
		returnmsg = "";
		returnmemo = "";
	}
	
	private ResponseBodyJson(String returncode, String returnmsg, String returnmemo) {
		this.returncode = returncode;
		this.returnmsg = returnmsg;
		this.returnmemo = returnmemo;
	}
	
	public static ResponseBodyJson.Builder custom() {
		return new Builder();
	}
	
	public static class Builder {
		private String returncode;
		private String returnmsg;
		private String returnmemo;
		
		Builder() {
			super();
			this.returncode = "200000";
			this.returnmsg = "SUCCESS";
			this.returnmemo = "";
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
		
		public ResponseBodyJson.Builder setAll(ReturnCodeEnum returnEnum) {
			this.returncode = returnEnum.getReturnCode();
			this.returnmsg = returnEnum.getStatus();
			this.returnmemo = returnEnum.getReturnMsg();
			return this;
		}
		
		public ResponseBodyJson.Builder setAll(String returncode, String returnmsg, String returnmemo){
			this.returncode = returncode;
			this.returnmsg = returnmsg;
			this.returnmemo = returnmemo;
			return this;
		}

		public ResponseBodyJson build() {
			return new ResponseBodyJson(returncode, returnmsg, returnmemo);
		}
		
	}

	private String getReturncode() {
		return returncode;
	}

	private String getReturnmsg() {
		return returnmsg;
	}

	private String getReturnmemo() {
		return returnmemo;
	}
	
}
