package cn.godzilla.common.serialize;

import java.nio.charset.Charset;

public class StringSerializer implements Serializer<String> {
	
	private final Charset charset;
	
	public StringSerializer() {
		this(Charset.forName("UTF-8"));
	}
	
	public StringSerializer(String name) {
		this(Charset.forName(name));
	}
	
	public StringSerializer(Charset charset) {
		this.charset = charset;
	}
	@Override
	public byte[] serialize(String t) throws SerializationException {
		return t==null?null:t.getBytes(charset);
	}

	@Override
	public String deserialize(byte[] bytes) throws SerializationException {
		return bytes==null?null:new String(bytes, charset);
	}
	
}
