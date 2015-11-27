package cn.godzilla.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.common.Application;
import cn.godzilla.common.OperatorEnum;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.dao.OperateLogMapper;
import cn.godzilla.model.OperateLog;
import cn.godzilla.model.User;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.util.GodzillaServiceApplication;

public class OperateLogServiceImpl extends GodzillaServiceApplication implements OperateLogService {

	@Autowired
	private OperateLogMapper operateLogMapper ;
	
	
	//201027 所有用户都能看到 各个项目  所有人的操作
	@Override
	public List<OperateLog> queryList(String projectCode,String profile) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("projectCode", projectCode);
		map.put("profile", profile);
		List<OperateLog> logs = operateLogMapper.queryList(map);
		for(OperateLog l : logs) {
			l.setOperation(OperatorEnum.getOperatorCnByEn(l.getOperation()));
		}
		return logs;
	}
	
	@Override
	public List<OperateLog> queryAll(String projectCode,String profile, Long id) {
		
		if(id <= 0 ){
			id = Long.MAX_VALUE ;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", id) ;
		List<OperateLog> logs = operateLogMapper.queryAll(map);
		
		for(OperateLog l : logs) {
			l.setOperation(OperatorEnum.getOperatorCnByEn(l.getOperation()));
		}
		return logs;
	}
	public ResponseBodyJson logThenReturn(String projectCode, String profile, ResponseBodyJson response) {
		String operation = response.getOperator();
		User user = GodzillaServiceApplication.getUser();
		GodzillaServiceApplication.operateLogService.addOperateLog(projectCode, profile, user.getUserName(), user.getRealName(), operation, response.getReturncode(), response.getReturnmsg(), response.getReturnmemo());
		
		return response;
	}
	public ResponseBodyJson updateLogThenReturn(String projectCode, String profile, ResponseBodyJson response) {
		String operation = response.getOperator();
		User user = GodzillaServiceApplication.getUser();
		GodzillaServiceApplication.operateLogService.updateOperateLog(projectCode, profile, Integer.parseInt(response.getData()+""), user.getUserName(), user.getRealName(), operation, response.getReturncode(), response.getReturnmsg(), response.getReturnmemo());
		
		return response;
	}
	@Override
	public Long addOperateLog(String projectCode, String profile, String mvnlog, String jarlog) {
		OperateLog record = new OperateLog();
		record.setDeployLog(mvnlog);
		record.setWarInfo(jarlog);
		operateLogMapper.insertSelective(record);
		return record.getId();
	}
	@Override
	public int updateOperateLog(String projectCode, String profile, int logid, String username, String realname,
			String operation, String operateCode, String executeResult, String resultInfo) {
		OperateLog record = new OperateLog();
		record.setId(Long.valueOf(logid));
		record.setUserName(username);
		record.setRealName(realname);
		record.setProjectCode(projectCode);
		record.setProfile(profile);
		record.setSort("operate");
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
		record.setOperateCode(Integer.parseInt(operateCode));
		record.setResultInfo(resultInfo);
		record.setExecuteTime(new Date());
		return operateLogMapper.updateLogById(record);
	}
	@Override
	public int addOperateLog(String projectCode, String profile, String username, String realname, String operation, String operateCode, String executeResult, String resultInfo) {
		OperateLog record = new OperateLog();
		record.setUserName(username);
		record.setRealName(realname);
		record.setProjectCode(projectCode);
		record.setProfile(profile);
		record.setSort("operate");
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
		record.setOperateCode(Integer.parseInt(operateCode));
		record.setResultInfo(resultInfo);
		record.setExecuteTime(new Date());
		return operateLogMapper.insertSelective(record);
	}

	@Override
	public OperateLog queryLogById(String projectCode, String profile, Long logid) {
		OperateLog log = operateLogMapper.queryLogById(logid.intValue());
		return log;
	}
	
	@Override
	public int updateOperateLog(String projectCode, String profile, String catalinaLog, Long logid) {
		OperateLog record = new OperateLog();
		record.setId(logid);
		record.setCatalinaLog(catalinaLog);
		record.setExecuteTime(new Date());
		return operateLogMapper.updateLogById(record);
	}

}
