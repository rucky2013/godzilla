package cn.godzilla.service;

import java.util.List;

import cn.godzilla.common.Constant;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.model.OperateLog;

public interface OperateLogService extends Constant {

	public List<OperateLog> queryList(String projectCode,String profile);
	
	public List<OperateLog> queryAll(Long id);

	public int addOperateLog(String userName, String realName, String string,
			String string2, String operation, String returncode,
			String returnmsg, String returnmemo);

}
