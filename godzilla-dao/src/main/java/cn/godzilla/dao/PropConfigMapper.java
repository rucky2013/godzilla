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

	List<PropConfig> queryListByProjectcodeAndProfile(Map<String, String> parameters);

	int updatePropByProkey(Map<String, Object> parameterMap);
	
	List<PropConfig> queryByProjectcodeAndCreatebyAndProfile(Map<String, String> parameterMap);

	int changeStatusByIdAndProjectcode(Map<String, String> parameterMap);
}