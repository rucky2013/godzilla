package cn.godzilla.service;

import cn.godzilla.common.ReturnCodeEnum;

public interface SvnService {

	/**
	 * 提交主干
	 * @param projectCode
	 * @param profile
	 * @return ReturnCodeEnum
	 * 			OK_SVNCOMMIT, NO_SVNCOMMIT, NO_FOUNDCONFLICT, NO_CLIENTPARAM,
	 * 			NO_SERVERPARAM,  NO_CHANGECOMMIT, NO_JAVASHELLCALL, 
	 */
	ReturnCodeEnum svnCommit(String projectCode, String profile);

	boolean svnMerge(String projectCode, String profile);
	/**
	 * 获取 资源地址 svn 版本号 存储在 Application.svnVersionThreadLocal 里
	 * @param repositoryUrl
	 * @param projectCode
	 * @param profile
	 * @return 
	 * 			
	 */
	ReturnCodeEnum getVersion(String repositoryUrl, String projectCode);

}
