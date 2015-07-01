package cn.godzilla.service;



import cn.godzilla.model.ProjPrivate;

public interface ProjPrivateService{
	
	public int insert(ProjPrivate record);

    public int insertSelective(ProjPrivate record);
    
    public int update(ProjPrivate record);
    
    public ProjPrivate queryDetail(String projectCode,String userName);
    
    public ProjPrivate save(ProjPrivate record);

}
