package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.ProjPrivateMapper;
import cn.godzilla.model.ProjPrivate;
import cn.godzilla.service.ProjPrivateService;

@Service("projPrivateService")
public class ProjPrivateServiceImpl implements ProjPrivateService {
	
	@Autowired
	private ProjPrivateMapper dao;

	@Override
	public int insert(ProjPrivate record) {
		
		return dao.insert(record);
	}

	@Override
	public int insertSelective(ProjPrivate record) {
		
		return dao.insertSelective(record);
	}

	@Override
	public int update(ProjPrivate record) {

		return dao.update(record);
	}

	@Override
	public ProjPrivate queryDetail(String projectCode,String userName) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("projectCode", projectCode);
		map.put("userName", userName);
		
		return dao.queryDetail(map);
	}

	@Override
	public ProjPrivate save(ProjPrivate projPrivate) {
		
		ProjPrivate result = this.queryDetail(projPrivate.getProjectCode(), projPrivate.getUserName());
		
		Long id = result.getId();
		if(id > 0){ //存在即更新
			projPrivate.setId(id);
			this.update(projPrivate);
		}else{ //不存在就新增
			
			this.insertSelective(projPrivate);
			
		}
		return queryDetail(projPrivate.getProjectCode(), projPrivate.getUserName());
	}

}
