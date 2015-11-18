package cn.godzilla.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.Application;
import cn.godzilla.common.OperatorEnum;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.dao.OperateLogMapper;
import cn.godzilla.model.OperateLog;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.web.GodzillaApplication;

@Service("operateLogService")
public class OperateLogServiceImpl extends GodzillaApplication implements OperateLogService {

	@Autowired
	private OperateLogMapper operateLogMapper ;
	
	
	//201027 所有用户都能看到 各个项目  所有人的操作
	@Override
	public List<OperateLog> queryList(String projectCode,String profile) {
		String username = super.getUser().getUserName();
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
	public List<OperateLog> queryAll(Long id) {
		
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
		//20151118 因为 显示日志操作  data信息太多存储不到数据库， 而 配置pom文件与数据库 比对时候 ，将缺失配置key存入data中，数据库 无法记录
		//GodzillaApplication.operateLogService.addOperateLog(GodzillaApplication.getUser().getUserName(), GodzillaApplication.getUser().getRealName(), Application.projectcodeThreadLocal.get(), Application.profileThreadLocal.get(), operation, response.getReturncode(), response.getReturnmsg(), response.getReturnmemo()+(response.getData()==null?"":response.getData()));
		GodzillaApplication.operateLogService.addOperateLog(GodzillaApplication.getUser().getUserName(), GodzillaApplication.getUser().getRealName(), Application.projectcodeThreadLocal.get(), Application.profileThreadLocal.get(), operation, response.getReturncode(), response.getReturnmsg(), response.getReturnmemo());
		return response;
	}
	
	public static ResponseBodyJson updateLogThenReturn(ResponseBodyJson response) {
		String operation = response.getOperator();
		
		GodzillaApplication.operateLogService.updateOperateLog(Long.parseLong(response.getData()+""), GodzillaApplication.getUser().getUserName(), GodzillaApplication.getUser().getRealName(), Application.projectcodeThreadLocal.get(), Application.profileThreadLocal.get(), operation, response.getReturncode(), response.getReturnmsg(), response.getReturnmemo());
		
		return response;
	}
	@Override
	public Long addOperateLog(String mvnlog, String jarlog) {
		OperateLog record = new OperateLog();
		record.setDeployLog(mvnlog);
		record.setWarInfo(jarlog);
		operateLogMapper.insertSelective(record);
		return record.getId();
	}
	
	@Override
	public Long addOperateLog(String catalinaLog) {
		OperateLog record = new OperateLog();
		record.setCatalinaLog(catalinaLog);
		operateLogMapper.insertSelective(record);
		Long logid = record.getId();
		return logid;
	}

	@Override
	public int updateOperateLog(String catalinaLog, Long logid) {
		OperateLog record = new OperateLog();
		record.setId(Long.valueOf(logid));
		record.setCatalinaLog(catalinaLog);
		record.setExecuteTime(new Date());
		return operateLogMapper.updateLogById(record);
	}
	
	@Override
	public int updateOperateLog(Long logid, String username, String realname,
			String projectCode, String profile, String operation,
			String operateCode, String executeResult, String resultInfo) {
		OperateLog record = new OperateLog();
		record.setId(logid);
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
	
	public int addOperateLog(String username, String realname,String projectCode, String profile, String operation, String operateCode, String executeResult, String resultInfo) {
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



	private void addSvnCommandLog(String username, String trunkPath, String commands, String username2) {
		OperateLog record = new OperateLog();
		record.setUserName(username);
		record.setProjectCode("");
		record.setProfile("");
		record.setSort("svn");
		record.setOperation("");
		record.setCommands(commands);
		
		record.setResultInfo("");
		record.setExecuteTime(new Date());
		operateLogMapper.insertSelective(record);
		return ;
	}

	private void addMvnCmdLog(String username, String projectCode, String profile, String commands, String resultInfo) {
		OperateLog record = new OperateLog();
		record.setUserName(username);
		record.setProjectCode(projectCode);
		record.setProfile(profile);
		record.setSort("mvn");
		record.setOperation("");
		record.setCommands(commands);
		
		record.setResultInfo(resultInfo);
		record.setExecuteTime(new Date());
		operateLogMapper.insertSelective(record);
		return ;
	}

	@Override
	public OperateLog queryLogById(Long logid) {
		OperateLog log = operateLogMapper.queryLogById(logid.intValue());
		return log;
	}

	

}
