package cn.godzilla.service;

import cn.godzilla.common.ConstantValue;
import cn.godzilla.common.ReturnCodeEnum;


public interface UserService extends ConstantValue{
	
	public ReturnCodeEnum login(String username, String password);

}
