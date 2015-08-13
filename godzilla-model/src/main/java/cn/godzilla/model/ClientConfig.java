package cn.godzilla.model;

import java.io.Serializable;

public class ClientConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id ;
	
	private String projectCode;
	
	private String profile;
	
	private String remoteIp;
	
	private Integer status ;
	private String deployVersion;
	private String tomcatPassword;
	private String tomcatUsername;
	private String tomcatPort;
	private String tomcatNeedPlugin;
	
	public String getDeployVersion() {
		return deployVersion;
	}

	public void setDeployVersion(String deployVersion) {
		this.deployVersion = deployVersion;
	}

	public String getTomcatPassword() {
		return tomcatPassword;
	}

	public void setTomcatPassword(String tomcatPassword) {
		this.tomcatPassword = tomcatPassword;
	}

	public String getTomcatUsername() {
		return tomcatUsername;
	}

	public void setTomcatUsername(String tomcatUsername) {
		this.tomcatUsername = tomcatUsername;
	}

	public String getTomcatPort() {
		return tomcatPort;
	}

	public void setTomcatPort(String tomcatPort) {
		this.tomcatPort = tomcatPort;
	}

	public String getTomcatNeedPlugin() {
		return tomcatNeedPlugin;
	}

	public void setTomcatNeedPlugin(String tomcatNeedPlugin) {
		this.tomcatNeedPlugin = tomcatNeedPlugin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
