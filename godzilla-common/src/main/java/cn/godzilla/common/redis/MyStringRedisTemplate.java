package cn.godzilla.common.redis;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import cn.godzilla.common.serialize.StringSerializerSpringAdapter;

public class MyStringRedisTemplate extends RedisTemplate<String, String> {

	
	public MyStringRedisTemplate() {
		RedisSerializer<String> stringSerializer = new StringSerializerSpringAdapter();
		
		setKeySerializer(stringSerializer);
		setValueSerializer(stringSerializer);
	}
	
	public MyStringRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}
	
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}
	
}
