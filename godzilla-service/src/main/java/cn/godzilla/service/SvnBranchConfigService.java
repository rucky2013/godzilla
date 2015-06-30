package cn.godzilla.service;

import java.util.List;

import cn.godzilla.model.SvnBranchConfig;

public interface SvnBranchConfigService {
	
	int insert(SvnBranchConfig svnBranchConfig);
	
	int update(SvnBranchConfig svnBranchConfig);
	
	List<SvnBranchConfig> queryListByProjectCode(String projectCode);

}
