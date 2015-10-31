package cn.godzilla.service;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.RpcResult;
import cn.godzilla.model.User;

public interface MvnProviderService extends Constant{

	/**
	 * 部署项目
	 * 3.将sh命令>queue
	 * 
	 * @param srcUrl
	 * @param projectCode
	 * @param profile
	 */
	public RpcResult mvnDeploy(String str, String PROJECT_NAME, String projectEnv, String USER_NAME, String realname, String profile);

}
