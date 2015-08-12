package cn.godzilla.dao;

import java.util.List;
import java.util.Map;

import cn.godzilla.model.SvnBranchConfig;

public interface SvnBranchConfigMapper {
	
	int insert(SvnBranchConfig svnBranchConfig);
	
	int update(SvnBranchConfig svnBranchConfig);
	
	List<SvnBranchConfig> queryListByProjectCode(String projectCode);

	int insertBranch(Map<String, String> parameterMap);

	int update(Map<String, String> parameterMap);

	int updateByProjectCode(Map<String, String> parameterMap);
}
