package cn.godzilla.model;

import java.io.Serializable;

public class SvnConflict implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String userName;

    private String projectCode;

    private String branchUrl;

    private String trunkUrl;

    private String fileName;

    private Integer status;

    private String resultInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getTrunkUrl() {
        return trunkUrl;
    }

    public void setTrunkUrl(String trunkUrl) {
        this.trunkUrl = trunkUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }
}