package cn.godzilla.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.PropConfigMapper;
import cn.godzilla.model.PropConfig;
import cn.godzilla.service.PropConfigService;

@Service
public class PropConfigServiceImpl implements PropConfigService {

	@Autowired
	private PropConfigMapper dao;
	
	@Override
	public int insert(PropConfig record) {

		return dao.insert(record);
	}

	@Override
	public int insertSelective(PropConfig record) {

		return dao.insertSelective(record);
	}

	@Override
	public int update(PropConfig record) {

		return dao.update(record);
	}

	@Override
	public PropConfig queryDetailById(long id) {

		return dao.queryDetailById(id);
	}

	@Override
	public PropConfig queryDetailByKey(Map<String, String> map) {

		return dao.queryDetailByKey(map);
	}

	@Override
	public List<PropConfig> queryList(Map<String, String> map) {
		
		return dao.queryList(map);
	}

}
