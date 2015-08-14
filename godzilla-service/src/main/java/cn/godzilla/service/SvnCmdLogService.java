package cn.godzilla.service;

import java.util.List;

import cn.godzilla.model.SvnCmdLog;

public interface SvnCmdLogService{
	
	public int insert(SvnCmdLog record);

    public int insertSelective(SvnCmdLog record);
    
    public List<SvnCmdLog> queryList();

	int addSvnCommandLog(String username, String repositoryUrl, String command, String realName);


}
