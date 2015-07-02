package cn.godzilla.model;

import java.io.Serializable;
import java.util.Date;

public class PropConfig implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String projectCode;

    private String profile;

    private String proKey;

    private String proValue;

    private String remark;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String lastValue;

    private Integer status;

    private String auditor;

    private String auditorText;

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

    public String getProKey() {
        return proKey;
    }

    public void setProKey(String proKey) {
        this.proKey = proKey;
    }

    public String getProValue() {
        return proValue;
    }

    public void setProValue(String proValue) {
        this.proValue = proValue;
    }

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        this.lastValue = lastValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditorText() {
        return auditorText;
    }

    public void setAuditorText(String auditorText) {
        this.auditorText = auditorText;
    }
}