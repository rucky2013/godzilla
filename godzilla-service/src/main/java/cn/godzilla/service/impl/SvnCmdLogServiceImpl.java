package cn.godzilla.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.SvnCmdLogMapper;
import cn.godzilla.model.SvnCmdLog;
import cn.godzilla.service.SvnCmdLogService;

@Service
public class SvnCmdLogServiceImpl implements SvnCmdLogService{
	
	@Autowired
	private SvnCmdLogMapper dao ;

	@Override
	public int insert(SvnCmdLog record) {

		return dao.insert(record);
	}

	@Override
	public int insertSelective(SvnCmdLog record) {

		return dao.insertSelective(record);
	}

	@Override
	public List<SvnCmdLog> queryList() {

		return dao.queryList();
	}
	

	

}
