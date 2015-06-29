package cn.godzilla.service;

import java.util.List;

import cn.godzilla.model.SvnChangeLog;

public interface SvnChangeLogService{
	
	 	public int insert(SvnChangeLog record);

	    public int insertSelective(SvnChangeLog record);
	    
	    public List<SvnChangeLog> queryList();

}
