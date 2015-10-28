package cn.godzilla.service;

import java.util.List;

import cn.godzilla.common.Constant;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.model.OperateLog;

public interface OperateLogService extends Constant {

	public List<OperateLog> queryList(String projectCode,String profile);
	
	public List<OperateLog> queryAll(Long id);

	public ResponseBodyJson logThenReturn(ResponseBodyJson response);
}
