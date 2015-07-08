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
	public ProjStatus queryDetail(String projectCode,String operateStaff,String profile) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("projectCode", projectCode);
		map.put("operateStaff", operateStaff);
		map.put("profile", profile);
		
		return dao.queryDetail(map);
	}

	@Override
	public boolean update(ProjStatus record) {
		
		return dao.update(record);
	}
	@Override
	public ProjStatus save(ProjStatus record){
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("projectCode", record.getProjectCode());
		map.put("operateStaff", record.getOperateStaff());
		
		
		ProjStatus info = dao.queryDetail(map) ;
		
		if(info == null || info.getId() < 0){
			
			dao.insertSelective(record);
		}else{
			
			dao.update(record);
		}
		
		return dao.queryDetail(map) ;
		
	}

}
