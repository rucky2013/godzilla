package cn.godzilla.service;

import java.util.List;

import cn.godzilla.model.SvnConflict;

public interface SvnConflictService{
	
	public int insert(SvnConflict record);

    public int insertSelective(SvnConflict record);
    
    public List<SvnConflict> queryList(String projectCode,String branchUrl);
    
    public int update(SvnConflict record);

}
