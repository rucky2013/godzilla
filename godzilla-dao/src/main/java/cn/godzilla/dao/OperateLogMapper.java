package cn.godzilla.dao;

import java.util.List;
import java.util.Map;

import cn.godzilla.model.OperateLog;

public interface OperateLogMapper {
    
    int insert(OperateLog record);

    int insertSelective(OperateLog record);
    
    List<OperateLog> queryList(Map<String, String> map);
    
    List<OperateLog> queryAll(Map<String, Long> map);
}