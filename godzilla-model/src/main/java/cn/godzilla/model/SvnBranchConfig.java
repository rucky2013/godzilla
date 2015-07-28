package cn.godzilla.model;

import java.io.Serializable;
import java.util.Date;

public class SvnBranchConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String projectCode;
	/**
	 * 例如
	 * XX/branch/test-0508
	 */
	private String branchUrl;
	
	private String branchName;
	
	private String createBy;
	
	private Date createTime ;
	
	private String createVersion;
	
	private String currentVersion ;
	
	private Integer status ;

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

	public String getBranchUrl() {
		return branchUrl;
	}

	public void setBranchUrl(String branchUrl) {
		this.branchUrl = branchUrl;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateVersion() {
		return createVersion;
	}

	public void setCreateVersion(String createVersion) {
		this.createVersion = createVersion;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
