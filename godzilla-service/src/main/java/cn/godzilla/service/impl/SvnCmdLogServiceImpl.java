package cn.godzilla.service.impl;

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
	public void insert(SvnCmdLog svnCmdLog) {
		
		dao.insert(svnCmdLog);
		
		
	}

}
