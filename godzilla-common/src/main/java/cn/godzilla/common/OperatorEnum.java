package cn.godzilla.common;

import org.springframework.util.StringUtils;

public enum OperatorEnum {
	
	DEPLOY("301", "DEPLOY", "部署"),
	SRCEDIT("302", "SRCEDIT", "源代码编辑"),
	UPDATEPROP("303", "UPDATEPROP", "更新配置项"),
	VERIFYPROP("304", "VERIFYPROP", "审核配置项"),
	SORTPROP("305", "SORTPROP", "排序配置项"),
	BRANCHADD("306", "BRANCHADD", "分支添加"),
	BRANCHEDIT("307", "BRANCHEDIT", "分支修改"),
	BRANCHDELETE("308", "BRANCHDELETE", "分支删除"),
	SVNSTATUS("309", "SVNSTATUS", "主干状态"),
	SVNMERGE("310", "SVNMERGE", "合并代码"),
	SVNCOMMIT("311", "SVNCOMMIT", "提交主干"),
	TOMCATRESTART("312", "TOMCATRESTART", "重新启动"),
	SSHCOPYWAR("313", "SSHCOPYWAR", "下载部署包"),
	WARDOWNLOAD("313", "WARDOWNLOAD", "下载部署包"),
	GODZILLAEX("314", "GODZILLAEX", "内部异常"),
	ADMINOPERATOR("315", "ADMINOPERATOR", "管理员操作"),
	EDITWORKDESK("316", "EDITWORKDESK", "分配项目权"),
	ADDUSER("317", "ADDUSER", "添加用户"),
	LOGIN("318", "LOGIN", "用户登录"),
	SHOWDEPLOYLOG("319", "SHOWDEPLOYLOG", "查看部署日志"),
	SHOWWARINFO("320", "SHOWWARINFO", "查看部署包"),
	CONFLICTRESOLVED("321", "CONFLICTRESOLVED", "标记冲突解决"),
	CHANGEPASSWD("322", "CHANGEPASSWD", "修改密码"),
	;
	public String operatorCode;
	public String operatorEn;
	public String operatorCn;
	
	OperatorEnum(String operatorCode, String operatorEn, String operatorCn) {
		this.operatorCode = operatorCode;
		this.operatorEn = operatorEn;
		this.operatorCn = operatorCn;
	}
	
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getOperatorEn() {
		return operatorEn;
	}
	public void setOperatorEn(String operatorEn) {
		this.operatorEn = operatorEn;
	}
	public String getOperatorCn() {
		return operatorCn;
	}
	public void setOperatorCn(String operatorCn) {
		this.operatorCn = operatorCn;
	}
	//根据操作码转换为中文
	public static String getOperatorCnbyCode(String operatorCode) {
		if(StringUtil.isEmpty(operatorCode)) return null;
		for(OperatorEnum o : OperatorEnum.values()) {
			if(o.getOperatorCode().equals(operatorCode)) {
				return o.getOperatorCn();
			}
		}
		return "";
	}
	//根据英文转换为中文
	public static String getOperatorCnByEn(String operatorEn) {
		if(StringUtils.isEmpty(operatorEn)) return null;
		for(OperatorEnum o: OperatorEnum.values()) {
			if(o.getOperatorEn().equals(operatorEn)) {
				return o.getOperatorCn();
			}
		}
		return "";
	}
}
