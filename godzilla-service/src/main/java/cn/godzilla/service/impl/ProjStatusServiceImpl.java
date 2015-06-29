package cn.godzilla.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.dao.ProjStatusMapper;
import cn.godzilla.model.ProjStatus;
import cn.godzilla.service.ProjStatusService;

public class ProjStatusServiceImpl implements ProjStatusService {

	@Autowired
	private ProjStatusMapper dao;
	
	@Override
	public int insert(ProjStatus record) {

		return dao.insert(record);
	}

	@Override
	public int insertSelective(ProjStatus record) {

		return dao.insertSelective(record);
	}

	@Override
	public ProjStatus queryDetail(Map<String, String> map) {
		
		return dao.queryDetail(map);
	}

	@Override
	public boolean update(ProjStatus record) {
		
		return dao.update(record);
	}

}
