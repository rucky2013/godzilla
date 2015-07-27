package cn.godzilla.common;

public enum ReturnCodeEnum {
	
	/**
	 * 错误码  6位
	 * 其中第一位标识错误还是正确，1开头为错误，2开头为正确 ，低5位为 模块号
	 * 错误码定义从末位开始累加，随时修改更新，以免重复定义
	 *
	 */
	//user模块
	NULL_NAMEPASSWORD("100001", "用户名或密码为空！"),
	NOTEXIST_USER("100002","用户名不存在！"),
	WRONG_PASSWORD("100003","密码错误！"),
	UNKNOW_ERROR("100004","未知错误！"),
	NO_LOGIN("100005", "还未登录或sid失效"),
	//prop模块
	NO_ADDUPDATEPROP("100011","添加更新配置失败"),
	NO_VERIFYPROP("100012","审核配置失败"),
	NO_AUTHORITY("100013","没有审核权限"),
	
	//user模块
	OK_CHECKUSER("200001","验证用户成功"),
	OK_LOGIN("200002","用户登录成功"),
	//prop模块
	OK_ADDUPDATEPROP("200011","添加更新配置成功"),
	OK_VERIFYPROP("200012","审核配置成功"),
	;
	
	public String returnCode;
	public String returnMsg;
	
	private ReturnCodeEnum(String returnCode, String returnMsg) {
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
	}
	
	public String getReturnCode(){
		return returnCode;
	}
	
	public String getReturnMsg() {
		return returnMsg;
	}
	
	public static ReturnCodeEnum getByReturnCode(String returnCode) {
		if(StringUtil.isEmpty(returnCode)) return null;  //null for empty returncode
		for(ReturnCodeEnum r:ReturnCodeEnum.values()) {
			if(r.getReturnCode().equals(returnCode)){
				return r;
			}
		}
		return null;
	}
}
