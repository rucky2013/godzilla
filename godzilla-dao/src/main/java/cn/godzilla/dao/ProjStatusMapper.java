package cn.godzilla.dao;

import java.util.Map;

import cn.godzilla.model.ProjStatus;

public interface ProjStatusMapper {
    
    int insert(ProjStatus record);

    int insertSelective(ProjStatus record);
    
    ProjStatus queryDetail(Map<String, String> map);
    
    boolean update(ProjStatus record);

}