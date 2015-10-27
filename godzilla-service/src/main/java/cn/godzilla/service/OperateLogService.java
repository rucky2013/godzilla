package cn.godzilla.service;

import java.util.List;

import cn.godzilla.common.Constant;
import cn.godzilla.model.OperateLog;

public interface OperateLogService extends Constant {

	public int insert(OperateLog record);

	public int insertSelective(OperateLog record);

	public List<OperateLog> queryList(String projectCode,String profile);
	
	public List<OperateLog> queryAll(Long id);

	int addOperateLog(String username, String realname, String projectCode, String profile, String operation, String executeResult, String resultInfo);

	int addOperateLog(String username, String realname, String projectCode, String profile, String operation);

}
