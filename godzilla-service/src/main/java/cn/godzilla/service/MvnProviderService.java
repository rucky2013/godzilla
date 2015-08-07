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
	public RpcResult deployProject(String username, String srUrl, String projectCode, String profile, String IP) ;
	
}
