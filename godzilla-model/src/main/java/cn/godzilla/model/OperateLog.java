package cn.godzilla.model;

import java.io.Serializable;
import java.util.Date;

public class OperateLog implements Serializable{
	
    /**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	private Long id;

    private String userName;
    
    private String realName;

    private String projectCode;

    private String profile;
    
    private String clientIp;
    
    private String sort;
    
    private String commands;
    
    private String operation;

    private Integer operateCode;

    private Date executeTime;

    private Integer executeResult;

    private String resultInfo;
    
    private String deployLog;
    
    private String warInfo;
    
    private String catalinaLog;
    
    public String getCatalinaLog() {
		return catalinaLog;
	}

	public void setCatalinaLog(String catalinaLog) {
		this.catalinaLog = catalinaLog;
	}
	
    public String getDeployLog() {
		return deployLog;
	}

	public void setDeployLog(String deployLog) {
		this.deployLog = deployLog;
	}

	public String getWarInfo() {
		return warInfo;
	}

	public void setWarInfo(String warInfo) {
		this.warInfo = warInfo;
	}

	public String getClientIp() {
		return clientIp;
	}

    public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCommands() {
		return commands;
	}

	public void setCommands(String commands) {
		this.commands = commands;
	}
	
    public String getRealName() {
		return realName;
	}

    public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(Integer operateCode) {
        this.operateCode = operateCode;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public Integer getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(Integer executeResult) {
        this.executeResult = executeResult;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }
}