package cn.godzilla.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
import cn.godzilla.util.GodzillaServiceApplication;

import com.alibaba.fastjson.JSON;

public class UserServiceImpl extends GodzillaServiceApplication implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private FunRightMapper funRightMapper;
	@Autowired
	private RedisCache cache;

	private String checkUser(String username, String password) {

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return NULL_NAMEPASSWORD;
		}

		User user = userMapper.queryUserByUsername(username);

		if (user == null) {
			return NOTEXIST_USER;
		}
		if (!password.equals(user.getPassword())) {
			return WRONG_PASSWORD;
		}

		return OK_CHECKUSER;
	}

	@Override
	public ReturnCodeEnum login(String projectCode, String profile, String username, String password, String newsid) {
		String checkState = checkUser(username, password);
		if (OK_CHECKUSER != checkState)
			return ReturnCodeEnum.getByReturnCode(checkState); // fail check
																// name and
																// password

		// check ok main login success
		onLogin(username, password, newsid);

		return ReturnCodeEnum.getByReturnCode(OK_LOGIN);
	}

	private void onLogin(String username, String password, String newsid) {

		// 更新user login lasttime
		userMapper.updateLogintimeByUsername(username);
		User user = userMapper.queryUserByUsername(username);
		List<Project> projects = projectMapper.queryProjectsByUsername(username);

		// store in redis for security
		// rdbc.set("test", "test");
		cache.createEntry(CACHE_ENUM.USERNAME, newsid).setValue(user.getUserName() == null ? "" : user.getUserName()).save().expired(7 * 24 * 60 * 60 * 1000l);
		cache.createEntry(CACHE_ENUM.USER, user.getUserName()).setValue(JSON.toJSONString(user == null ? "" : user)).save();
		cache.createEntry(CACHE_ENUM.PROJECTS, user.getUserName()).setValue(JSON.toJSONString(projects == null ? "" : projects)).save();
	}

	@Override
	public ReturnCodeEnum checkUserStatusBySid(String projectCode, String profile, String sid) {
		String userName = cache.createEntry(CACHE_ENUM.USERNAME, sid).get();
		if (StringUtil.isEmpty(userName)) {
			return ReturnCodeEnum.getByReturnCode(NO_LOGIN);
		}
		return ReturnCodeEnum.getByReturnCode(OK_CHECKUSER);
	}

	@Override
	public void logout(String projectCode, String profile, String sid) {
		cache.createEntry(CACHE_ENUM.USERNAME, sid).delete();
	}

	private void deleteUser() {
		String username = getUser().getUserName();
		String sid = getSid();
		cache.createEntry(CACHE_ENUM.USER, username).delete();
		cache.createEntry(CACHE_ENUM.PROJECTS, username).delete();
		cache.createEntry(CACHE_ENUM.USERNAME, sid).delete();
	}

	@Override
	public User getUserBySid(String projectCode, String profile, String sid) {
		String userName = cache.createEntry(CACHE_ENUM.USERNAME, sid).get();
		if (StringUtil.isEmpty(userName)) {
			return null;
		}
		User user = JSON.parseObject(cache.createEntry(CACHE_ENUM.USER, userName).get(), User.class);
		return user;
	}

	@Override
	public List<User> queryAllUser(String projectCode, String profile) {
		List<User> users = userMapper.queryAllUser();
		return users;
	}

	@Override
	public List<Project> queryProjectsByUsername(String projectCode, String profile, String userName) {
		List<Project> projects = projectMapper.queryProjectsByUsername(userName);
		return projects;
	}

	@Override
	public List<Map<String, Object>> getUserAuthList(String projectCode, String profile) {
		List<User> userlist = this.queryAllUser(SERVER_USER, TEST_PROFILE);
		List<Map<String, Object>> userAuthList = new ArrayList<Map<String, Object>>();
		int index = 1;
		for (User u : userlist) {
			Map<String, Object> userAuthMap = new HashMap<String, Object>();
			List<Project> projects = this.queryProjectsByUsername(SERVER_USER, TEST_PROFILE, u.getUserName());
			userAuthMap.put("index", index++);
			userAuthMap.put("id", u.getId());
			userAuthMap.put("username", u.getUserName());
			userAuthMap.put("realname", u.getRealName());
			userAuthMap.put("projects", projects);

			userAuthList.add(userAuthMap);
		}
		return userAuthList;
	}

	@Override
	public ReturnCodeEnum addUser(String projectCode, String profile, String username, String password, String confirm, String departname) {

		if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password) || StringUtil.isEmpty(confirm)) {
			return ReturnCodeEnum.getByReturnCode(NULL_NAMEPASSWORD);
		}
		if (!password.equals(confirm)) {
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
		User dbuser = userMapper.queryUserByUsername(username);
		if (dbuser != null) {
			return ReturnCodeEnum.getByReturnCode(NO_EXISTUSER);
		}
		int insert = userMapper.insertUser(user);
		return ReturnCodeEnum.getByReturnCode(OK_ADDUSER);
	}

	@Override
	public List<Map<String, Object>> getUserProjects(String projectCode, String profile, String editUsername) {
		List<Project> userprojects = this.queryProjectsByUsername(SERVER_USER, TEST_PROFILE, editUsername);
		List<Project> allprojects = projectMapper.queryAll();

		List<Map<String, Object>> userprojectsList = new ArrayList<Map<String, Object>>();
		for (Project pro : allprojects) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("projectCode", pro.getProjectCode());
			if (userprojects.contains(pro)) {
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
	public ReturnCodeEnum updateUserProjects(String projectCode1, String profile, String editUsername, String selectProjects) {

		String[] projects = selectProjects.split(",");
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("editUsername", editUsername);
		int delete = funRightMapper.deleteRightsByusername(parameterMap);
		for (String projectcd : projects) {
			if (StringUtil.isEmpty(projectcd)) {
				continue;
			}

			FunRight funRight = new FunRight();
			funRight.setUserName(editUsername);
			funRight.setProjectCode(projectcd);
			funRight.setProjectName(projectcd);
			funRight.setStatus(1);
			funRight.setCreateBy(super.getUser().getUserName());

			int insert = funRightMapper.insertSelective(funRight);

		}
		return ReturnCodeEnum.getByReturnCode(OK_UPDATEFUNRIGHT);
	}

	@Override
	public User getUserById(String projectCode, String profile, String id) {
		User user = userMapper.queryUserByUserId(id);
		return user;
	}

	@Override
	public ReturnCodeEnum changePassword(String projectCode, String profile, User user, String oldpassword, String password) {
		if (!user.getPassword().equals(oldpassword)) {
			return ReturnCodeEnum.getByReturnCode(NO_WRONGOLDPWD);
		}

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("id", user.getId());
		parameterMap.put("password", password);
		int code = userMapper.updatePassword(parameterMap);
		if (code > 0) {
			deleteUser();// del redis sid-username
			return ReturnCodeEnum.getByReturnCode(OK_CHANGEPWD);
		}
		return ReturnCodeEnum.getByReturnCode(NO_CHANGEPWD);
	}

}
