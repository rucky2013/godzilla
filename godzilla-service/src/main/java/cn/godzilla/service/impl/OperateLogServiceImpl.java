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
import cn.godzilla.service.OperateLogService;
import cn.godzilla.util.GodzillaServiceApplication;

public class OperateLogServiceImpl extends GodzillaServiceApplication implements OperateLogService {

	@Autowired
	private OperateLogMapper operateLogMapper ;
	
	
	//201027 所有用户都能看到 各个项目  所有人的操作
	@Override
	public List<OperateLog> queryList(String projectCode,String profile) {
		String username = getUser().getUserName();
		Map<String, String> map = new HashMap<String, String>();
		map.put("projectCode", projectCode);
		map.put("profile", profile);
		//map.put("username", username) ;
		List<OperateLog> logs = operateLogMapper.queryList(map);
		for(OperateLog l : logs) {
			l.setOperation(OperatorEnum.getOperatorCnByEn(l.getOperation()));
		}
		return logs;
	}
	
	@Override
	public List<OperateLog> queryAll(String projectCode,String profile, Long id) {
		
		String username = super.getUser().getUserName();
		if(id <= 0 ){
			id = Long.MAX_VALUE ;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", id) ;
		//map.put("username", username) ;
		List<OperateLog> logs = operateLogMapper.queryAll(map);
		
		for(OperateLog l : logs) {
			l.setOperation(OperatorEnum.getOperatorCnByEn(l.getOperation()));
		}
		return logs;
	}
	public static ResponseBodyJson logThenReturn(ResponseBodyJson response) {
		String operation = response.getOperator();
		
		GodzillaServiceApplication.operateLogService.addOperateLog(GodzillaServiceApplication.getUser().getUserName(), GodzillaServiceApplication.getUser().getRealName(), Application.projectcodeThreadLocal.get(), Application.profileThreadLocal.get(), operation, response.getReturncode(), response.getReturnmsg(), response.getReturnmemo());
		
		return response;
	}
	public static ResponseBodyJson updateLogThenReturn(ResponseBodyJson response) {
		String operation = response.getOperator();
		
		GodzillaServiceApplication.operateLogService.updateOperateLog(Application.projectcodeThreadLocal.get(), Application.profileThreadLocal.get(), Integer.parseInt(response.getData()+""), GodzillaServiceApplication.getUser().getUserName(), GodzillaServiceApplication.getUser().getRealName(), operation, response.getReturncode(), response.getReturnmsg(), response.getReturnmemo());
		
		return response;
	}
	@Override
	public int addOperateLog(String projectCode, String profile, String mvnlog, String jarlog) {
		OperateLog record = new OperateLog();
		record.setDeployLog(mvnlog);
		record.setWarInfo(jarlog);
		operateLogMapper.insertSelective(record);
		return record.getId().intValue();
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
	public OperateLog queryLogById(String projectCode, String profile, String logid) {
		OperateLog log = operateLogMapper.queryLogById(Integer.parseInt(logid));
		return log;
	}

}
