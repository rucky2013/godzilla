package cn.creditease.godzilla.dao;

import cn.creditease.godzilla.model.SvnCmdLog;

public interface SvnCmdLogMapper {

	int insert(SvnCmdLog record);

    int insertSelective(SvnCmdLog record);
}