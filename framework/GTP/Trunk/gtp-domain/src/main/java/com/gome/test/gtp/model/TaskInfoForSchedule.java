package com.gome.test.gtp.model;


public class TaskInfoForSchedule {

    int taskID;
    String taskName;
    String excuteInfo;
    String branchUrl;
    String agentIP;
    String agentLabel;

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getExcuteInfo() {
        return excuteInfo;
    }

    public void setExcuteInfo(String excuteInfo) {
        this.excuteInfo = excuteInfo;
    }

    public String getBranchUrl() {
        return branchUrl;
    }

    public void setBranchUrl(String branchUrl) {
        this.branchUrl = branchUrl;
    }

    public String getAgentIP() {
        return agentIP;
    }

    public void setAgentIP(String agentIP) {
        this.agentIP = agentIP;
    }

    public String getAgentLabel() {
        return agentLabel;
    }

    public void setAgentLabel(String agentLabel) {
        this.agentLabel = agentLabel;
    }
}
