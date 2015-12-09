package com.rpcf.serialize;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Serializer {
	private static ThreadLocal<Kryo> kryoThreadLocal;
	
	static {
		kryoThreadLocal = new ThreadLocal<Kryo>() {
			public Kryo initialValue() {
				return new Kryo();
			}
		};
	}
	
	public static byte[] serialize(Object object) throws SerializeException {
		if(object ==null) {
			throw new SerializeException ("指向object对象指针为空");
		}
		
		Kryo kryo = kryoThreadLocal.get();
		ByteArrayOutputStream stream = new ByteArrayOutputStream(20000);
		Output output = new Output(stream);
		kryo.writeObject(output, object);
		output.close();
		
		return stream.toByteArray();
	}
	
	public static <T>T deserializer(byte[] data, Class<T> clazz) throws SerializeException {
		if(data ==null || data.length==0) {
			throw new SerializeException("数据为空");
		}
		
		InputStream stream = new BufferedInputStream(new ByteArrayInputStream(data));
		Input input = new Input(stream);
		return kryoThreadLocal.get().readObject(input, clazz);
	}
}
