package cn.godzilla.dao;

import java.util.Map;

import cn.godzilla.model.ProjPrivate;

public interface ProjPrivateMapper {
  
    int insert(ProjPrivate record);

    int insertSelective(ProjPrivate record);
    
    int update(ProjPrivate record);
    
    ProjPrivate queryDetail(Map<String, String> map);
}