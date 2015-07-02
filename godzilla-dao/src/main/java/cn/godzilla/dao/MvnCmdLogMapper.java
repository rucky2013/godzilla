package cn.godzilla.dao;

import java.util.List;

import cn.godzilla.model.MvnCmdLog;

public interface MvnCmdLogMapper {

	int insert(MvnCmdLog record);

    int insertSelective(MvnCmdLog record);
    
    List<MvnCmdLog> queryList();
    
    
}