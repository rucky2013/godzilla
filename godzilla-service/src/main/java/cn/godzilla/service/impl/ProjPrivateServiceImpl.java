package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.ProjPrivateMapper;
import cn.godzilla.model.ProjPrivate;
import cn.godzilla.service.ProjPrivateService;

@Service
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
	public ProjPrivate queryDetail(String projectCode,String userName) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("projectCode", projectCode);
		map.put("userName", userName);
		
		return dao.queryDetail(map);
	}

}
