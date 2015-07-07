package cn.godzilla.service;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;


public interface UserService extends Constant{
	
	public ReturnCodeEnum login(String username, String password, String newsid);

	public void logout(String sid);

}
