package cn.godzilla.service;

import cn.godzilla.common.ReturnCodeEnum;

public interface SvnService {

	
	ReturnCodeEnum svnCommit(String projectCode, String profile);

	boolean svnMerge(String projectCode, String profile);
	/**
	 * 获取 资源地址 svn 版本号 存储在 Application.svnVersionThreadLocal 里
	 * @param repositoryUrl
	 * @param projectCode
	 * @param profile
	 * @return
	 */
	ReturnCodeEnum getVersion(String repositoryUrl, String projectCode, String profile);

}
