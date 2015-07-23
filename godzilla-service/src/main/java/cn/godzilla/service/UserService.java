package cn.godzilla.service;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.User;

public interface UserService extends Constant{
	
	public ReturnCodeEnum login(String username, String password, String newsid);

	public void logout(String sid);

	/**
	 * 判断当前用户 是否已经登录
	 * 
	 * 登录后会将sid 存入redis
	 * 查找redis 是否存在sid 
	 * 
	 * if not exist return NO_LOGIN
	 * else OK_CHECKUSER;
	 * 
	 * @param sid
	 * @return
	 */
	public ReturnCodeEnum checkUserStatusBySid(String sid) ;

	/**
	 * 根据sid 查询 缓存 或者数据库 
	 * 的当前用户对象
	 * @param sid
	 * @return
	 */
	public User getUserBySid(String sid);
}
