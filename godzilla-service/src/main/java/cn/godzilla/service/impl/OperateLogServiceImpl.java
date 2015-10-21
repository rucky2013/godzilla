package cn.godzilla.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.OperateLogMapper;
import cn.godzilla.model.OperateLog;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.web.GodzillaApplication;

@Service("operateLogService")
public class OperateLogServiceImpl extends GodzillaApplication implements OperateLogService {

	@Autowired
	private OperateLogMapper dao ;
	
	@Override
	public int insert(OperateLog record) {
		
		return dao.insert(record);
	}

	@Override
	public int insertSelective(OperateLog record) {
		
		return dao.insertSelective(record);
	}
	
	@Override
	public int addOperateLog(String username, String projectCode, String profile, String operation, String executeResult, String resultInfo) {
		OperateLog record = new OperateLog();
		record.setUserName(username);
		record.setProjectCode(projectCode);
		record.setProfile(profile);
		record.setOperation(operation);
		try {
			record.setExecuteResult(Integer.parseInt(executeResult));
		} catch (NumberFormatException e) {
			if("SUCCESS".equals(executeResult)) {
				record.setExecuteResult(SUCC);
			} else if("FAILURE".equals(executeResult)) {
				record.setExecuteResult(FAIL);
			} else {
				record.setExecuteResult(-3);
			}
		}
		
		record.setResultInfo(resultInfo);
		record.setExecuteTime(new Date());
		return dao.insertSelective(record);
	}
	@Override
	public int addOperateLog(String username, String projectCode, String profile, String operation) {
		return addOperateLog(username, projectCode, profile, operation, "1", "执行成功");
	}

	@Override
	public List<OperateLog> queryList(String projectCode,String profile) {
		String username = super.getUser().getUserName();
		Map<String, String> map = new HashMap<String, String>();
		map.put("projectCode", projectCode);
		map.put("profile", profile);
		map.put("username", username) ;
		return dao.queryList(map);
	}

	@Override
	public List<OperateLog> queryAll(Long id) {
		
		String username = super.getUser().getUserName();
		if(id <= 0 ){
			id = Long.MAX_VALUE ;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", id) ;
		//map.put("username", username) ;
		return dao.queryAll(map) ;
	}

}
