package cn.godzilla.model;

import java.io.Serializable;

public class ProjPrivate implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String userName;

    private String projectCode;

    private String virtualTruckUrl;
    
    private Integer ifVirtual;

    private String currentBranchUrl;
    
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

    public String getVirtualTruckUrl() {
        return virtualTruckUrl;
    }

    public void setVirtualTruckUrl(String virtualTruckUrl) {
        this.virtualTruckUrl = virtualTruckUrl;
    }

	public Integer getIfVirtual() {
		return ifVirtual;
	}

	public void setIfVirtual(Integer ifVirtual) {
		this.ifVirtual = ifVirtual;
	}

	public String getCurrentBranchUrl() {
		return currentBranchUrl;
	}

	public void setCurrentBranchUrl(String currentBranchUrl) {
		this.currentBranchUrl = currentBranchUrl;
	}
	
	
    
}