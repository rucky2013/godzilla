package cn.godzilla.service;

import javax.servlet.http.HttpServletResponse;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.User;

public interface MvnService extends Constant{

	/**
	 * 部署项目
	 *-1.svn合并提交主干
	 * 1.替换pom文件 配置变量
	 * 2.mvn deploy
	 * 3.将sh命令>queue
	 * @param projectCode
	 * @param profile
	 * @param parentVersion
	 * @param pencentkey
	 * @return
	 */
	public ReturnCodeEnum doDeploy(String projectCode, String profile, String parentVersion, String pencentkey);
	
	/**
	 * 部署项目进行进度百分比
	 * @param pencentkey
	 * @return
	 */
	public ReturnCodeEnum getProcessPercent(String pencentkey);
	
	/**
	 * 下载war包
	 * @param response
	 * @param projectCode
	 * @param profile
	 * @return
	 */
	public ReturnCodeEnum downLoadWar(HttpServletResponse response, String projectCode, String profile);
	/**
	 * 显示部署日志
	 * @param response
	 * @param projectCode
	 * @param profile
	 * @param logid
	 * @return
	 */
	public ReturnCodeEnum showdeployLog(HttpServletResponse response, String projectCode, String profile, String logid);
	/**
	 * 显示war包lib信息
	 * @param response
	 * @param projectCode
	 * @param profile
	 * @param logid
	 * @return
	 */
	public ReturnCodeEnum showwarInfo(HttpServletResponse response, String projectCode, String profile, String logid);
	
	/**
	 * 重启tomcat
	 * @param projectCode
	 * @param profile
	 * @return
	 */
	public ReturnCodeEnum restartTomcat(String projectCode, String profile) ;

	
}
