package com.gome.test.gtp.model;

public class ReportGroup  {

    private int date;

    private int taskType;

    private int caseNum;

    private int passNum;

    private int businessGroupId;

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(int caseNum) {
        this.caseNum = caseNum;
    }

    public int getBusinessGroupId() {
        return businessGroupId;
    }

    public void setBusinessGroupId(int businessGroupId) {
        this.businessGroupId = businessGroupId;
    }
}
