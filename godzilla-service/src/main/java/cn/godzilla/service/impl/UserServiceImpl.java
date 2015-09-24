package cn.godzilla.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.godzilla.common.RedisUtil;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.cache.CACHE_ENUM;
import cn.godzilla.common.cache.RedisCache;
import cn.godzilla.dao.FunRightMapper;
import cn.godzilla.dao.ProjectMapper;
import cn.godzilla.dao.UserMapper;
import cn.godzilla.model.FunRight;
import cn.godzilla.model.Project;
import cn.godzilla.model.User;
import cn.godzilla.service.UserService;
import cn.godzilla.web.GodzillaApplication;

import com.alibaba.fastjson.JSON;

@Service("userService")
public class UserServiceImpl extends GodzillaApplication implements UserService{
	
	private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private FunRightMapper funRightMapper;
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
		
		//更新user login lasttime
		userMapper.updateLogintimeByUsername(username);
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
	
	
	@Override
	public List<User> queryAllUser() {
		List<User> users = userMapper.queryAllUser();
		return users;
	}

	@Override
	public List<Project> queryProjectsByUsername(String userName) {
		List<Project> projects = projectMapper.queryProjectsByUsername(userName);
		return projects;
	}

	@Override
	public List<Map<String, Object>> getUserAuthList() {
		List<User> userlist = this.queryAllUser();
		List<Map<String, Object>> userAuthList = new ArrayList<Map<String, Object>>();
		int index = 1;
		for(User u: userlist) {
			Map<String, Object> userAuthMap = new HashMap<String, Object>();
			List<Project> projects = this.queryProjectsByUsername(u.getUserName());
			userAuthMap.put("index", index++);
			userAuthMap.put("id", u.getId());
			userAuthMap.put("username", u.getUserName());
			userAuthMap.put("projects", projects);
			
			userAuthList.add(userAuthMap);
		}
		return userAuthList;
	}

	@Override
	public ReturnCodeEnum addUser(String username, String password, String confirm, String departname) {
		
		if(StringUtil.isEmpty(username)||StringUtil.isEmpty(password)||StringUtil.isEmpty(confirm)) {
			return ReturnCodeEnum.getByReturnCode(NULL_NAMEPASSWORD);
		}
		if(!password.equals(confirm)){
			return ReturnCodeEnum.getByReturnCode(NULL_NAMEPASSWORD);
		}
		User user = new User();
		user.setUserName(username);
		user.setRealName(username);
		user.setPassword(password);
		user.setIsAdmin(0);
		user.setCreateBy(super.getUser().getUserName());
		user.setStatus(1);
		user.setDepartName(departname);
		int insert = userMapper.insertUser(user);
		return ReturnCodeEnum.getByReturnCode(OK_ADDUSER);
	}

	@Override
	public List<Map<String, Object>> getUserProjects(String editUsername) {
		List<Project> userprojects = this.queryProjectsByUsername(editUsername);
		List<Project> allprojects = projectMapper.queryAll();
		
		List<Map<String, Object>> userprojectsList = new ArrayList<Map<String, Object>>();
		for(Project pro: allprojects){
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("projectCode",pro.getProjectCode());
			if(userprojects.contains(pro)) {
				tempMap.put("auth", "1");
			} else {
				tempMap.put("auth", "0");
			}
			userprojectsList.add(tempMap);
		}
		return userprojectsList;
	}

	@Override
	@Transactional
	public ReturnCodeEnum updateUserProjects(String editUsername, String selectProjects) {
		
		String[] projects = selectProjects.split(",");
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("editUsername"	,editUsername );
		int delete = funRightMapper.deleteRightsByusername(parameterMap);
		for(String projectCode: projects) {
			if(StringUtil.isEmpty(projectCode)) {
				continue;
			}
			
			FunRight funRight = new FunRight();
			funRight.setUserName(editUsername);
			funRight.setProjectCode(projectCode);
			funRight.setProjectName(projectCode);
			funRight.setStatus(1);
			funRight.setCreateBy(super.getUser().getUserName());
			
			int insert = funRightMapper.insertSelective(funRight);
			
		}
		return ReturnCodeEnum.getByReturnCode(OK_UPDATEFUNRIGHT);
	}

	@Override
	public User getUserById(String id) {
		User user = userMapper.queryUserByUserId(id);
		return user;
	}

	
}
