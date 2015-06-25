package cn.godzilla.dao;

import cn.godzilla.model.SvnCmdLog;

public interface SvnCmdLogMapper {

	int insert(SvnCmdLog record);

    int insertSelective(SvnCmdLog record);
}