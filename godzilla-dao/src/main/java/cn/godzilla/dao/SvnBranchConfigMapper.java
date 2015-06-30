package cn.godzilla.dao;

import java.util.List;

import cn.godzilla.model.SvnBranchConfig;

public interface SvnBranchConfigMapper {
	
	int insert(SvnBranchConfig svnBranchConfig);
	
	int update(SvnBranchConfig svnBranchConfig);
	
	List<SvnBranchConfig> queryListByProjectCode(String projectCode);

}
