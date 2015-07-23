package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.OperateLogMapper;
import cn.godzilla.model.OperateLog;
import cn.godzilla.service.OperateLogService;

@Service("operateLogService")
public class OperateLogServiceImpl implements OperateLogService {

	@Autowired
	private OperateLogMapper dao ;
	
	@Override
	public int insert(OperateLog record) {
		
		return dao.insert(record);
	}

	@Override
	public int insertSelective(OperateLog record) {
		
		return dao.insertSelective(record);
	}

	@Override
	public List<OperateLog> queryList(String projectCode,String profile) {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("projectCode", projectCode);
		map.put("profile", profile);
		
		return dao.queryList(map);
	}

	@Override
	public List<OperateLog> queryAll(Long id) {
		
		if(id <= 0 ){
			
			id = Long.MAX_VALUE ;
		}
		
		Map<String, Long> map = new HashMap<String, Long>();
		
		map.put("id", id) ;
		
		return dao.queryAll(map) ;
	}

}
