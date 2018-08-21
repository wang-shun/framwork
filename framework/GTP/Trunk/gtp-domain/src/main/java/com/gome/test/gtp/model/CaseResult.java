package com.gome.test.gtp.model;

import java.io.Serializable;

public class CaseResult implements Serializable {
    private int taskId;
    private Boolean rerun;
    private int rerunCount;
    private String testCaseName;
    private String computerName;
    private long startTime;
    private long endTime;
    private long duration;
    private String testResult;
    private String errorMessage;
    private String stackTrace;
    private String owner;
    private String bugId;
    private String failReason;
    private int issueType;
    private String orderListName;
    private String failReasonCategoryId;
    private String caseDesc;
    private CaseResult[] children;


    private String picturePath;
    private String casePicturePath;

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    private String Post;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    private String browser;



    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getCasePicturePath() {
        return casePicturePath;
    }

    public void setCasePicturePath(String casePicturePath) {
        this.casePicturePath = casePicturePath;
    }



    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Boolean getRerun() {
        return rerun;
    }

    public void setRerun(Boolean rerun) {
        this.rerun = rerun;
    }

    public int getRerunCount() {
        return rerunCount;
    }

    public void setRerunCount(int rerunCount) {
        this.rerunCount = rerunCount;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBugId() {
        return bugId;
    }

    public void setBugId(String bugId) {
        this.bugId = bugId;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public int getIssueType() {
        return issueType;
    }

    public void setIssueType(int issueType) {
        this.issueType = issueType;
    }

    public String getOrderListName() {
        return orderListName;
    }

    public void setOrderListName(String orderListName) {
        this.orderListName = orderListName;
    }

    public String getFailReasonCategoryId() {
        return failReasonCategoryId;
    }

    public void setFailReasonCategoryId(String failReasonCategoryId) {
        this.failReasonCategoryId = failReasonCategoryId;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public CaseResult[] getChildren() {
        return children;
    }

    public void setChildren(CaseResult[] children) {
        this.children = children;
    }
}