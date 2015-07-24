package cn.godzilla.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

import cn.godzilla.common.RedisUtil;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.cache.CACHE_ENUM;
import cn.godzilla.common.cache.RedisCache;
import cn.godzilla.common.cache.RedisCache.Entry;
import cn.godzilla.dao.ProjectMapper;
import cn.godzilla.dao.UserMapper;
import cn.godzilla.model.Project;
import cn.godzilla.model.User;
import cn.godzilla.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private RedisCache cache;

	@Autowired
	private RedisUtil rdbc;
	
	private String checkUser(String username, String password){
		
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
			logger.info("*****用户名或密码为空！*****");
			return NULL_NAMEPASSWORD;
		}
		
		User user = userMapper.queryUserByUsername(username);
		
		if(user == null) {
			logger.info("*****用户名不存在！*****");
			return NOTEXIST_USER;
		}
		if(!password.equals(user.getPassword())) {
			logger.info("*****密码错误！*****");
			return WRONG_PASSWORD;
		}
		
		return OK_CHECKUSER;
	}

	@Override
	public ReturnCodeEnum login(String username, String password, String newsid) {
		String checkState = checkUser(username, password);
		if(OK_CHECKUSER != checkState) 
			return ReturnCodeEnum.getByReturnCode(checkState); // fail check name and password 
		
		//check ok main login success
		onLogin(username, password, newsid);
		
		return ReturnCodeEnum.getByReturnCode(OK_LOGIN);
	}
	
	private void onLogin(String username, String password, String newsid) {
		
		User user = userMapper.queryUserByUsername(username);
		List<Project> projects = projectMapper.queryProjectsByUsername(username);
		
		//store in redis for security
		rdbc.set("test", "test");
		cache.createEntry(CACHE_ENUM.USERNAME, newsid)
			.setValue(user.getUserName()==null?"":user.getUserName()).save();
		cache.createEntry(CACHE_ENUM.USER, user.getUserName())
			.setValue(JSON.toJSONString(user==null?"":user)).save();
		cache.createEntry(CACHE_ENUM.PROJECTS, user.getUserName())
			.setValue(JSON.toJSONString(projects==null?"":projects)).save();
	}
	
	public ReturnCodeEnum checkUserStatusBySid(String sid) {
		String userName = cache.createEntry(CACHE_ENUM.USERNAME, sid)
			.get();
		if(StringUtil.isEmpty(userName)) {
			return ReturnCodeEnum.getByReturnCode(NO_LOGIN);
		} 
		return ReturnCodeEnum.getByReturnCode(OK_CHECKUSER);
	}

	@Override
	public void logout(String sid) {
		cache.createEntry(CACHE_ENUM.USERNAME, sid)
			.delete();
	}

	@Override
	public User getUserBySid(String sid) {
		String userName = cache.createEntry(CACHE_ENUM.USERNAME, sid)
				.get();
		if(StringUtil.isEmpty(userName)){
			return null;
		}
		User user = JSON.parseObject(
				cache.createEntry(CACHE_ENUM.USER, userName).get()
				, User.class);
		return user;
	}
	
}
