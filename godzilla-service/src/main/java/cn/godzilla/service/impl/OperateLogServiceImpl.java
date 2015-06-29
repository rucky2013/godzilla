package cn.godzilla.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.dao.OperateLogMapper;
import cn.godzilla.model.OperateLog;
import cn.godzilla.service.OperateLogService;

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
	public List<OperateLog> queryList() {
		
		return dao.queryList();
	}

}
