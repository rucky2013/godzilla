package cn.godzilla.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.godzilla.common.RedisUtil;


enum CACHE_ENUM {
	USERID, USER, PROJECTS
}
@Component
public class RedisCache extends AbstractCache<String, String, CACHE_ENUM> {

	@Autowired
	private RedisUtil redisDB;
	
	public final static String USERID_PREFIX = "userid:";
	public final static String USER_PREFIX = "user:";
	public final static String PROJECTS_PREFIX = "projects:";
	
	@Override
	public void save(cn.godzilla.common.cache.Cache.Entry<String, String> entry) {
		
		
	}

	@Override
	public void delete(String key) {
		
		
	}

	@Override
	public String get(String key) {
		
		return null;
	}

	@Override
	public cn.godzilla.common.cache.Cache.Entry<String, String> createEntry(CACHE_ENUM type) {
		
		return null;
	}
	
	
	class Entry implements Cache.Entry<String, String> {

		@Override
		public String getKey() {
			
			return null;
		}

		@Override
		public void setKey(String key) {
			
			
		}

		@Override
		public String getValue() {
			
			return null;
		}

		@Override
		public void setValue(String value) {
			
			
		}

		
	}

}
