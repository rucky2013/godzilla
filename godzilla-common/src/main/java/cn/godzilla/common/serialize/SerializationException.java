package cn.godzilla.common.serialize;

public class SerializationException extends RuntimeException {
	
	public SerializationException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public SerializationException(String msg) {
		super(msg);
	}
}
