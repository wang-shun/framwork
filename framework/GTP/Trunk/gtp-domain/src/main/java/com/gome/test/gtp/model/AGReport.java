package com.gome.test.gtp.model;

/**
 * Created by lizonglin on 2016/4/25/0025.
 */
public class AGReport {
    private String owner;
    private int groupId;
    private Long date;
    private int caseNum;
    private int passNum;
    private String failCase;
    private int taskType;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(int caseNum) {
        this.caseNum = caseNum;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public String getFailCase() {
        return failCase;
    }

    public void setFailCase(String failCase) {
        this.failCase = failCase;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
}
