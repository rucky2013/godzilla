package cn.creditease.godzilla.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.creditease.godzilla.common.RedisUtil;
import cn.creditease.godzilla.common.StringUtil;

@Component
public class RedisCache implements Cache {

	@Autowired
	private RedisUtil redisUtil;
	
	public final static String USERID_PREFIX = "userid:";
	public final static String USER_PREFIX = "user:";
	public final static String PROJECTS_PREFIX = "projects:"; 
	
	public String getUSERIDByKey(String key) {
		UserId userId = getUSERID();
		userId.setKey(key);
		UserId findUserId = (UserId)get(userId);
		return findUserId.getValue();
	}
	
	public String getUSERIDByUuid(String uuid) {
		return getUSERIDByKey(USERID_PREFIX + uuid);
	}
	
	@Override
	public void save(KV kv) {
		redisUtil.set(kv.getKey(), kv.getValue());
	}
	
	@Override
	public KV get(KV k) {
		String value = redisUtil.get(k.getKey());
		KV kvnew = null;
		if(k instanceof UserId) {
			kvnew = getUSERID();
		} else if(k instanceof User) {
			kvnew = getUSER();
		} else if(k instanceof Projects) {
			kvnew = getPROJECTS();
		} else {
			kvnew = new KV();
		}
		kvnew.setKey(k.getKey());
		kvnew.setValue(value);
		return kvnew;
	}
	
	@Override
	public void delete(KV k) {
		redisUtil.delete(k.getKey());
	}
	
	private enum REDISENUM {
		USERID, USER, PROJECTS
	}
	
	public UserId createUSERID() {
		String uuid = StringUtil.getUUID();
		KV kv = createKV(REDISENUM.USERID, uuid);
		return (UserId)kv;
	}
	public UserId getUSERID() {
		KV kv = createKV(REDISENUM.USERID, "");
		return (UserId)kv;
	}
	public User createUSER(String userid) {
		KV kv = createKV(REDISENUM.USER, userid);
		return (User)kv;
	}
	public User getUSER() {
		KV kv = createKV(REDISENUM.USER, "");
		return (User)kv;
	}
	public Projects createPROJECTS(String userid) {
		KV kv = createKV(REDISENUM.PROJECTS, "");
		return (Projects)kv;
	}
	public Projects getPROJECTS() {
		KV kv = createKV(REDISENUM.PROJECTS, "");
		return (Projects)kv;
	}
	
	public KV createKV(REDISENUM category, String uuORuserID) {
		switch(category) {
		case USERID: 
			return new UserId(uuORuserID);
		case USER:
			return new User(uuORuserID);
		case PROJECTS:
			return new Projects(uuORuserID);
		}
		return null;
	}
	
	class UserId extends KV{
		private UserId(String uuid) {
			super(USERID_PREFIX + uuid, "");
		}
		private UserId() {
			super("", "");
		}
	}
	
	class User extends KV{
		private User(String userid) {
			super(USER_PREFIX + userid, "");
		}
		private User() {
			super("", "");
		}
	}
	
	class Projects extends KV{
		private Projects(String userid) {
			super(PROJECTS_PREFIX + userid, "");
		}
		private Projects() {
			super("", "");
		}
	}
}
