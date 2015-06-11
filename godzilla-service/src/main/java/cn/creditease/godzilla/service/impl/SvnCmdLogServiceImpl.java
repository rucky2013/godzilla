package cn.creditease.godzilla.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.creditease.godzilla.dao.SvnCmdLogMapper;
import cn.creditease.godzilla.model.SvnCmdLog;
import cn.creditease.godzilla.service.SvnCmdLogService;

@Service
public class SvnCmdLogServiceImpl implements SvnCmdLogService{
	
	@Autowired
	private SvnCmdLogMapper dao ;
	

	@Override
	public void insert(SvnCmdLog svnCmdLog) {
		
		dao.insert(svnCmdLog);
		
		
	}

}
