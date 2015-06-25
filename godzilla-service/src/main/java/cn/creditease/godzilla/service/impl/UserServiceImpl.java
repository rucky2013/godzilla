package cn.creditease.godzilla.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.creditease.godzilla.common.cache.RedisCache;
import cn.creditease.godzilla.dao.UserMapper;
import cn.creditease.godzilla.model.User;
import cn.creditease.godzilla.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisCache redisCache;

	@Override
	public int checkUser(String username, String password){
		
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
			logger.info("*****用户名或密码为空！*****");
			return -1;
		}
		
		User user = userMapper.queryUserByUsername(username);
		if(user == null) {
			logger.info("*****用户名不存在！*****");
			return -2;
		}
		if(!password.equals(user.getPassword())) {
			logger.info("*****密码错误！*****");
			return -3;
		}
		
		return 0;
	}

}
