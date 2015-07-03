package cn.godzilla.service;


import cn.godzilla.model.ProjStatus;

public interface ProjStatusService{
	
	public int insert(ProjStatus record);

    public int insertSelective(ProjStatus record);
    
    public ProjStatus queryDetail(String projectCode,String operateStaff);
    
    public boolean update(ProjStatus record);

    public ProjStatus save(ProjStatus record);

}
