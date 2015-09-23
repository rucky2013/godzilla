package cn.godzilla.model;

import java.io.Serializable;

public class Project implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String projectCode;
    private String projectName;
    private String repositoryUrl;

    private String createBy;

    private String manager;

    private Integer status;

    private String checkoutPath;
    private String webPath;
    private String warName;
    private String version;
    
    private String svnUsername;
    private String svnPassword;
    
    public String getSvnUsername() {
		return svnUsername;
	}

	public void setSvnUsername(String svnUsername) {
		this.svnUsername = svnUsername;
	}

	public String getSvnPassword() {
		return svnPassword;
	}

	public void setSvnPassword(String svnPassword) {
		this.svnPassword = svnPassword;
	}

	public String getWarName() {
		return warName;
	}

	public void setWarName(String warName) {
		this.warName = warName;
	}

	public String getWebPath() {
		return webPath;
	}

	public void setWebPath(String webPath) {
		this.webPath = webPath;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getCheckoutPath() {
		return checkoutPath;
	}

	public void setCheckoutPath(String checkoutPath) {
		this.checkoutPath = checkoutPath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Project)) {
			return false;
		}
		Project pro = (Project)obj;
		if(pro==this)
			return true;
		if(this.getProjectCode().equals(pro.getProjectCode())) {
			return true;
		} else {
			return false;
		}
	}
}