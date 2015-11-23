package cn.godzilla.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.godzilla.common.StringUtil;
import cn.godzilla.common.redis.MyStringRedisTemplate;

@Component
public class RedisUtil {

	@Autowired
	private MyStringRedisTemplate template;

	public void set(String key, String value) {
		template.opsForValue().set(key, value);
	}
	public String get(String key) {
		String temp = template.opsForValue().get(key);
		return temp;
				
	}
	
	public void delete(String key) {
		template.opsForValue().getOperations().delete(key);
	}
	
	public void expireAt(String key, long millis) {
		template.opsForValue().getOperations().expireAt(key, StringUtil.addMillis(millis));
	}
	
}
