package cn.godzilla.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.dao.SvnConflictMapper;
import cn.godzilla.model.SvnConflict;
import cn.godzilla.service.SvnConflictService;

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
	public List<SvnConflict> queryList(Map<String, String> map) {

		return dao.queryList(map);
	}

	@Override
	public int update(SvnConflict record) {

		return dao.update(record);
	}

}
