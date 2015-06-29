package cn.godzilla.service;

import java.util.Map;

import cn.godzilla.model.ProjStatus;

public interface ProjStatusService{
	
	public int insert(ProjStatus record);

    public int insertSelective(ProjStatus record);
    
    public ProjStatus queryDetail(Map<String, String> map);
    
    public boolean update(ProjStatus record);


}
