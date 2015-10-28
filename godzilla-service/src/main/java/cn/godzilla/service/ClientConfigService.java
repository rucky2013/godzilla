package cn.godzilla.service;

import java.util.Map;

import cn.godzilla.model.ClientConfig;

public interface ClientConfigService {
	
	ClientConfig queryDetail(String projectCode,String profile);

}
