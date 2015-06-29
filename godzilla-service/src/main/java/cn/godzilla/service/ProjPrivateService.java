package cn.godzilla.service;

import java.util.Map;

import cn.godzilla.model.ProjPrivate;

public interface ProjPrivateService{
	
	public int insert(ProjPrivate record);

    public int insertSelective(ProjPrivate record);
    
    public int update(ProjPrivate record);
    
    public ProjPrivate queryDetail(Map<String, String> map);

}
