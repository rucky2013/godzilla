package cn.godzilla.service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.Project;

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

	ReturnCodeEnum svnMerge(String projectCode, String profile);
	/**
	 * 获取 资源地址 svn 版本号 存储在 Application.svnVersionThreadLocal 里
	 * @param repositoryUrl
	 * @param projectCode
	 * @param profile
	 * @return 
	 * 			
	 */
	ReturnCodeEnum getVersion(String repositoryUrl, String projectCode);
	/**
	 * 
	 * @param projectCode
	 * @param profile
	 * @return
	 */
	ReturnCodeEnum getStatus(String projectCode, String profile) ;
	
	ReturnCodeEnum svnResolved(String projectCode, String profile);

	void setConflictUrl(String projectCode, String profile, Project project);
}
