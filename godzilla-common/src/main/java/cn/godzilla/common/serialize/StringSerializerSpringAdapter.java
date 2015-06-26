package cn.godzilla.common.serialize;

import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class StringSerializerSpringAdapter extends StringRedisSerializer{

	private StringSerializer stringSerializer;
	
	public StringSerializerSpringAdapter() {
		stringSerializer = new StringSerializer("UTF-8");
	}
	@Override
	public byte[] serialize(String t) throws SerializationException {
		return stringSerializer.serialize(t);
	}

	@Override
	public String deserialize(byte[] bytes) throws SerializationException {
		return stringSerializer.deserialize(bytes);
	}

}
