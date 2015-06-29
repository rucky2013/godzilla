package cn.godzilla.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.dao.SvnChangeLogMapper;
import cn.godzilla.model.SvnChangeLog;
import cn.godzilla.service.SvnChangeLogService;

public class SvnChangeLogServiceImpl implements SvnChangeLogService{

	@Autowired
	private SvnChangeLogMapper dao;
	
	@Override
	public int insert(SvnChangeLog record) {

		return dao.insert(record);
	}

	@Override
	public int insertSelective(SvnChangeLog record) {

		return dao.insertSelective(record);
	}

	@Override
	public List<SvnChangeLog> queryList() {

		return dao.queryList();
	}

}
