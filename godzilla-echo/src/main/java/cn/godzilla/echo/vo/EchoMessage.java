package cn.godzilla.echo.vo;

import java.io.Serializable;

public class EchoMessage implements Serializable{
	
	public String username;
	public String area;
	public String info;
	
	private EchoMessage() {
		this("","","");
	}
	private EchoMessage(String username, String area, String info) {
		this.username = username;
		this.area = area;
		this.info =info;
	}
	
	public static EchoMessage getInstance(String username, String area, String info) {
		return new EchoMessage(username, area, info);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
