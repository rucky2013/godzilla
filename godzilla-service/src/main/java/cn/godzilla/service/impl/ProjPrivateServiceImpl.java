package cn.godzilla.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.dao.ProjPrivateMapper;
import cn.godzilla.model.ProjPrivate;
import cn.godzilla.service.ProjPrivateService;

public class ProjPrivateServiceImpl implements ProjPrivateService {
	
	@Autowired
	private ProjPrivateMapper dao;

	@Override
	public int insert(ProjPrivate record) {
		
		return dao.insert(record);
	}

	@Override
	public int insertSelective(ProjPrivate record) {
		
		return dao.insertSelective(record);
	}

	@Override
	public int update(ProjPrivate record) {

		return dao.update(record);
	}

	@Override
	public ProjPrivate queryDetail(Map<String, String> map) {
		
		return dao.queryDetail(map);
	}

}
