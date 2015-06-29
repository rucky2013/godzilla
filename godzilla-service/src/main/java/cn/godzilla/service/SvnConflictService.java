package cn.godzilla.service;

import java.util.List;
import java.util.Map;

import cn.godzilla.model.SvnConflict;

public interface SvnConflictService{
	
	public int insert(SvnConflict record);

    public int insertSelective(SvnConflict record);
    
    public List<SvnConflict> queryList(Map<String, String> map);
    
    public int update(SvnConflict record);

}
