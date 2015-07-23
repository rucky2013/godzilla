package cn.godzilla.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.SvnBranchConfigMapper;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.SvnBranchConfigService;

@Service("svnBranchConfigService")
public class SvnBranchConfigServiceImpl implements SvnBranchConfigService {

	@Autowired
	private SvnBranchConfigMapper dao;
	
	@Override
	public int insert(SvnBranchConfig svnBranchConfig) {

		return dao.insert(svnBranchConfig);
	}

	@Override
	public int update(SvnBranchConfig svnBranchConfig) {

		return dao.update(svnBranchConfig);
	}

	@Override
	public List<SvnBranchConfig> queryListByProjectCode(String projectCode) {

		return dao.queryListByProjectCode(projectCode);
	}

}
