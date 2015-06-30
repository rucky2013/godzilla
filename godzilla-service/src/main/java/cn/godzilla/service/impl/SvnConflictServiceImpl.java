package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.SvnConflictMapper;
import cn.godzilla.model.SvnConflict;
import cn.godzilla.service.SvnConflictService;

@Service
public class SvnConflictServiceImpl implements SvnConflictService {

	@Autowired
	private SvnConflictMapper dao;
	
	@Override
	public int insert(SvnConflict record) {

		return dao.insert(record);
	}

	@Override
	public int insertSelective(SvnConflict record) {

		return dao.insertSelective(record);
	}

	@Override
	public List<SvnConflict> queryList(String projectCode,String branchUrl) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("projectCode", projectCode);
		map.put("branchUrl", branchUrl);
		
		return dao.queryList(map);
	}

	@Override
	public int update(SvnConflict record) {

		return dao.update(record);
	}

}
