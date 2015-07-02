package cn.godzilla.dao;

import java.util.List;

import cn.godzilla.model.SvnChangeLog;

public interface SvnChangeLogMapper {
    
    int insert(SvnChangeLog record);

    int insertSelective(SvnChangeLog record);
    
    List<SvnChangeLog> queryList();
}