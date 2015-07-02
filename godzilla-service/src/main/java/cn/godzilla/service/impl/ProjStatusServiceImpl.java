package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.ProjStatusMapper;
import cn.godzilla.model.ProjStatus;
import cn.godzilla.service.ProjStatusService;

@Service
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
	public ProjStatus queryDetail(String projectCode,String profile,String operateStaff) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("projectCode", projectCode);
		map.put("profile", profile);
		map.put("operateStaff", operateStaff);
		
		return dao.queryDetail(map);
	}

	@Override
	public boolean update(ProjStatus record) {
		
		return dao.update(record);
	}

}
