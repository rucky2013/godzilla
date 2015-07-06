package cn.godzilla.echo.vo;

import java.io.Serializable;

public class EchoMessage implements Serializable{
	
	public String sid;
	public String area;
	public String info;
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
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
