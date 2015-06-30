package cn.godzilla.service;

import java.util.List;

import cn.godzilla.model.OperateLog;

public interface OperateLogService {

	public int insert(OperateLog record);

	public int insertSelective(OperateLog record);

	public List<OperateLog> queryList(String projectCode,String profile);

}
