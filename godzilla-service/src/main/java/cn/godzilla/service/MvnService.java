package cn.godzilla.service;

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
	 * 
	 * @param srcUrl
	 * @param projectCode
	 * @param profile
	 */
	ReturnCodeEnum doDeploy(String srcUrl, String projectCode, String profile);
	
	/**
	 * 部署项目进行进度百分比
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @return
	 */
	String getProcessPercent(String sid, String projectCode, String profile);
	
}
