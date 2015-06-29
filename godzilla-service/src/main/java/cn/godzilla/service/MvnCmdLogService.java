package cn.godzilla.service;

import java.util.List;

import cn.godzilla.model.MvnCmdLog;

public interface MvnCmdLogService{
	
	public int insert(MvnCmdLog record);

    public int insertSelective(MvnCmdLog record);
    
    public List<MvnCmdLog> queryList();
    

}
