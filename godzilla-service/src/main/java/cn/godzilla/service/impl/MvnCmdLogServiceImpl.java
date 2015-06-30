package cn.godzilla.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.MvnCmdLogMapper;
import cn.godzilla.model.MvnCmdLog;
import cn.godzilla.service.MvnCmdLogService;

@Service
public class MvnCmdLogServiceImpl implements MvnCmdLogService {
	
	@Autowired
	private MvnCmdLogMapper dao;

	@Override
	public int insert(MvnCmdLog record) {
		
		return dao.insert(record);
	}

	@Override
	public int insertSelective(MvnCmdLog record) {
		
		return dao.insertSelective(record);
	}

	@Override
	public List<MvnCmdLog> queryList() {
		
		return dao.queryList();
	}

}
