package cn.godzilla.dao;

import java.util.List;

import cn.godzilla.model.SvnCmdLog;

public interface SvnCmdLogMapper {

	int insert(SvnCmdLog record);

    int insertSelective(SvnCmdLog record);
    
    List<SvnCmdLog> queryList();
}