package cn.godzilla.dao;

import java.util.List;
import java.util.Map;

import cn.godzilla.model.PropConfig;

public interface PropConfigMapper {
    
    int insert(PropConfig record);

    int insertSelective(PropConfig record);
    
    int update(PropConfig record);
    
    PropConfig queryDetailById(long id);
    
    PropConfig queryDetailByKey(Map<String, String> map);
    
    List<PropConfig> queryList(Map<String, String> map);
}