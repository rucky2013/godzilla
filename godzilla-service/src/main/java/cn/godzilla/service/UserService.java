package cn.godzilla.service;

import java.util.List;
import java.util.Map;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.Project;
import cn.godzilla.model.User;

public interface UserService extends Constant{
	
	public ReturnCodeEnum login(String project, String profile, String username, String password, String newsid);

	public void logout(String project, String profile, String sid);

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
	public ReturnCodeEnum checkUserStatusBySid(String project, String profile, String sid) ;

	/**
	 * 根据sid 查询 缓存 或者数据库 
	 * 的当前用户对象
	 * @param sid
	 * @return
	 */
	public User getUserBySid(String project, String profile, String sid);

	/**
	 * 查询所有 用户
	 * @return
	 */
	public List<User> queryAllUser(String project, String profile);

	/**
	 * 查询 username的所有项目权限
	 * @param userName
	 * @return
	 */
	public List<Project> queryProjectsByUsername(String project, String profile, String userName);

	/**
	 * 查询 所有用户的所有项目权限
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getUserAuthList(String project, String profile);

	/**
	 * 添加  用户 
	 * @param username
	 * @param password
	 * @param confirm 
	 * @param departname 
	 * @return
	 */
	public ReturnCodeEnum addUser(String project, String profile, String username, String password, String confirm, String departname);

	/**
	 * 获得用户的  所有权限项目 
	 * 1.有权限
	 * 0.没权限
	 * @param editUsername
	 * @return
	 */
	public List<Map<String, Object>> getUserProjects(String project, String profile, String editUsername);
	
	/**
	 * 编辑用户 工作台  项目权限
	 * 1.设置当前用户所有项目权限为失效
	 * 2.insert 新项目权限
	 * 注：需要事务支持
	 * @param editUsername
	 * @param selectProjects
	 * @return
	 */
	public ReturnCodeEnum updateUserProjects(String project, String profile, String editUsername, String selectProjects);

	public User getUserById(String project, String profile, String sid);

	public ReturnCodeEnum changePassword(String project, String profile, User user, String oldpassword, String password);

}
