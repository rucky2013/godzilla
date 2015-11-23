package cn.godzilla.service;

import cn.godzilla.model.ClientConfig;

public interface ClientConfigService {
	
	ClientConfig queryDetail(String projectCode,String profile);

}
