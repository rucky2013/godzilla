package cn.godzilla.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.godzilla.common.redis.MyStringRedisTemplate;

@Component
public class RedisUtil {

	@Autowired
	private MyStringRedisTemplate template;

	public RedisUtil() {
		// template.getConnectionFactory().getConnection().select(0);
	}

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
		template.opsForValue().getOperations().expireAt(key, DateUtil.addMillis(millis));
	}
	
	
	/*public void setAesKey(String userId, String key) {
		template.opsForValue().set(KeyUtils.getAesKeyKey(userId), key);
	}

	public void setToken(String userId, String token) {
		template.opsForValue().set(KeyUtils.getTokenKey(userId), token);
		template.opsForValue().getOperations().expireAt(KeyUtils.getTokenKey(userId), DateUtil.addMillis(2592000000L));
	}

	public void setAuthCode(String phone, String face, String authCode) {
		template.opsForValue().set(KeyUtils.getAuthCodeKey(phone, face), authCode);
		template.opsForValue().getOperations().expireAt(KeyUtils.getAuthCodeKey(phone, face), DateUtil.addMillis(600000));
	}

	public String getAesKey(String userId) {
		return template.opsForValue().get(KeyUtils.getAesKeyKey(userId));
	}

	public String getToken(String userId) {
		return template.opsForValue().get(KeyUtils.getTokenKey(userId));
	}

	public String getAuthCode(String phone, String face) {
		return template.opsForValue().get(KeyUtils.getAuthCodeKey(phone, face));
	}

	public void delToken(String userId) {
		template.opsForValue().getOperations().delete(KeyUtils.getTokenKey(userId));
	}*/

}
