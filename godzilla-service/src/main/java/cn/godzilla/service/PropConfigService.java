package cn.godzilla.service;

import java.util.List;
import java.util.Map;

import cn.godzilla.model.PropConfig;

public interface PropConfigService{
	
	 	public int insert(PropConfig record);

	    public int insertSelective(PropConfig record);
	    
	    public int update(PropConfig record);
	    
	    public PropConfig queryDetailById(long id);
	    
	    public PropConfig queryDetailByKey(Map<String, String> map);
	    
	    public List<PropConfig> queryList(Map<String, String> map);

}
