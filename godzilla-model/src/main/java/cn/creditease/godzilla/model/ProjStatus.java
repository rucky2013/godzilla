package cn.creditease.godzilla.model;

import java.util.Date;

public class ProjStatus {
    private String id;

    private String projectCode;

    private Integer currentStatus;

    private Integer processRate;

    private String operateStaff;

    private Date createTime;

    private Date updateTime;

    private Date finishTime;

    private String resultInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getProcessRate() {
        return processRate;
    }

    public void setProcessRate(Integer processRate) {
        this.processRate = processRate;
    }

    public String getOperateStaff() {
        return operateStaff;
    }

    public void setOperateStaff(String operateStaff) {
        this.operateStaff = operateStaff;
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

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }
}