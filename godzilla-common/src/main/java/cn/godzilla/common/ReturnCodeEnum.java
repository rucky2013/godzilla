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
	//mvn
	NO_MVNDEPLOY("100014","mvn部署失败"),
	NO_CHANGEPOM("100015","mvn部署失败:替换pom文件失败"),
	NO_RPCFACTORY("100016", "mvn部署失败:rpc初始化错误"),
	//authorization
	NO_AUTHORIZATION("100017","没有项目权限"),
	NO_ADDUSER("100018","添加用户失败"),
	NO_SAMEPASSWORD("100019","密码不一样"),
	NO_UPDATEFUNRIGHT("100020","更新用户权限失败"),
	//svn model
	NO_CHANGECOMMIT("100021", "没有更改可以提交"),
	NO_SVNCOMMIT("100022", "svn提交失败"),
	NO_FOUNDCONFLICT("100023", "合并出现冲突，请先解决冲突"),
	NO_CLIENTPARAM("100024", "client.sh缺少参数"),
	NO_SERVERPARAM("100025", "server.sh缺少参数"),
	NO_JAVASHELLCALL("100026", "java调用shell执行失败"),
	NO_SVNVERSION("100027","svn version获取失败"),
	NO_GODZILLA("100029","godzilla 客户端操作失败"),
	NO_DELETEBRANCH("100030", "删除分支失败"),
	NO_SVNMERGE("100031", "合并分支失败"),
	NO_MVNBUILD("100032", "mvn build 失败"),
	NO_COMMON("100033", "公共异常:后台错误(异常处日志未处理)"),
	NO_CONCURRENCEDEPLOY("100034", "其他人正在发布，请等待一会重试"),
	NO_MVNSETPROPS("100035", "mvn build 失败:xml,properties中含有${xx}未设置配置项"),
	NO_HASKEYDEPLOY("100036", "没有设置 公平锁为此项目"),
	NO_EXISTUSER("100037", "用户名已存在"),
	
	OK_SUCCESS("200000", "SUCCESS"),
	//user模块
	OK_CHECKUSER("200001","验证用户成功"),
	OK_LOGIN("200002","用户登录成功"),
	//prop模块
	OK_ADDUPDATEPROP("200011","添加更新配置成功"),
	OK_VERIFYPROP("200012","审核配置成功"),
	//mvn
	OK_MVNDEPLOY("200014","mvn部署成功"),
	//authorization
	OK_AUTHORIZATION("200017","验证项目权限成功"),
	OK_ADDUSER("200018","添加用户成功"),
	OK_UPDATEFUNRIGHT("200020","更新用户权限成功"),
	//svn model
	OK_SVNCOMMIT("200021", "提交合并成功"),
	OK_SVNVERSION("200027","svn version获取成功"),
	OK_SORTPROP("200028","prop sort成功"),
	OK_GODZILLA("200029","godzilla 客户端操作成功"),
	OK_DELETEBRANCH("200030", "删除分支成功"),
	OK_SVNMERGE("200031", "合并分支成功"),
	;
	
	public String returnCode;
	public String returnMsg;
	
	ReturnCodeEnum() {
		this.returnCode = "";
		this.returnMsg = "";
	}
	ReturnCodeEnum(String returnCode, String returnMsg) {
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
	}
	
	public String getReturnCode(){
		return returnCode;
	}
	
	public String getReturnMsg() {
		return returnCode+":"+returnMsg;
	}
	
	public String getStatus() {
		if(this.getReturnCode().indexOf("2")==0) {
			return "SUCCESS";
		} else if(this.getReturnCode().indexOf("1")==0) {
			return "FAILURE";
		} else {
			return "FAILURE";
		}
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
