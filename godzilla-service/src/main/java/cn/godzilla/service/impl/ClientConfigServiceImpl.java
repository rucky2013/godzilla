package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.ClientConfigMapper;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.service.ClientConfigService;

@Service("clientConfigService")
public class ClientConfigServiceImpl implements ClientConfigService {

	@Autowired
	private ClientConfigMapper clientConfigMapper;
	
	@Override
	public ClientConfig queryDetail(String projectCode, String profile) {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("projectCode", projectCode);
		map.put("profile", profile);
		
		return clientConfigMapper.queryDetail(map);
	}

}
