package cn.godzilla.common.serialize;

public interface Serializer<T> {
	
	byte[] serialize(T t) throws SerializationException ;
	
	T deserialize(byte[] bytes) throws SerializationException;
}
