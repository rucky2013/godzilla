package cn.godzilla.dao;

import java.util.Map;

import cn.godzilla.model.ClientConfig;


public interface ClientConfigMapper {
	
	ClientConfig queryDetail(Map<String, String> map);

	int updateDeployversion(Map<String, String> parameterMap);
}
