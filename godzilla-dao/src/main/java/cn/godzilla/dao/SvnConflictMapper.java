package cn.godzilla.dao;

import java.util.List;
import java.util.Map;

import cn.godzilla.model.SvnConflict;

public interface SvnConflictMapper {
   
    int insert(SvnConflict record);

    int insertSelective(SvnConflict record);
    
    List<SvnConflict> queryList(Map<String, String> map);
    
    int update(SvnConflict record);
}