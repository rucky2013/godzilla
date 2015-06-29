package cn.godzilla.dao;

import java.util.List;

import cn.godzilla.model.OperateLog;

public interface OperateLogMapper {
    
    int insert(OperateLog record);

    int insertSelective(OperateLog record);
    
    List<OperateLog> queryList();
}