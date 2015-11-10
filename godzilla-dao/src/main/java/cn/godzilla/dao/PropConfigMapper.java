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

	List<PropConfig> queryByProjectcodeAndCreatebyAndProfileGroupBy(Map<String, String> parameterMap);

	int changeStatusByNewverify(Map<String, String> parameterMap);

	void updatePropLastValue(Map<String, Object> parameterMap1);

	int verifyOKProp(Map<String, String> parameterMap);

	int updatePropIndex(Map<String, Integer> parameterMap);

	List<PropConfig> queryByProjectcodeAndCreatebyAndProfileAndBillid(Map<String, Object> parameterMap);

	void updatePropStatusById(Map<String, Object> parameterMap);

	void updatePropLastValueAndStatusById(Map<String, Object> parameterMap1);

	int verifyNOProp(Map<String, Object> parameterMap2);
}