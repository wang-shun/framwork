package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "TaskList")
@DynamicUpdate(true)
public class TaskList implements Serializable {

    private Integer id;
    private Integer taskID;
    private String guid;
    private String action;
    private String taskFrom;
    private Integer reRunReportID;
    private String caseLists;
    private String agentIP;
    private Timestamp createTime;
    private String jobName;
    private Timestamp sentToAgentTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer taskState;
    private String logs;
    private Integer priority;
    private Integer splitCount;
    private Integer splitIndex;
    private Timestamp splitTime;
    private String browser;

    @Column(name = "SplitCount", nullable = false)
    public Integer getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(Integer splitCount) {
        this.splitCount = splitCount;
    }

    @Column(name = "SplitIndex", nullable = false)
    public Integer getSplitIndex() {
        return splitIndex;
    }

    public void setSplitIndex(Integer splitIndex) {
        this.splitIndex = splitIndex;
    }

    @Column(name ="SplitTime", nullable = false)
    public Timestamp getSplitTime() {
        return splitTime;
    }

    public void setSplitTime(Timestamp splitTime) {
        this.splitTime = splitTime;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "TaskID", nullable = false)
    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    @Column(name = "Guid", nullable = true)
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Column(name = "Action", nullable = true)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column(name = "TaskFrom", nullable = true)
    public String getTaskFrom() {
        return taskFrom;
    }

    public void setTaskFrom(String taskFrom) {
        this.taskFrom = taskFrom;
    }

    @Column(name = "ReRunReportID", nullable = true)
    public Integer getReRunReportID() {
        return reRunReportID;
    }

    public void setReRunReportID(Integer reRunReportID) {
        this.reRunReportID = reRunReportID;
    }

    @Column(name = "CaseLists", nullable = true)
    public String getCaseLists() {
        return caseLists;
    }

    public void setCaseLists(String caseLists) {
        this.caseLists = caseLists;
    }

    @Column(name = "AgentIP", nullable = true)
    public String getAgentIP() {
        return agentIP;
    }

    public void setAgentIP(String agentIP) {
        this.agentIP = agentIP;
    }

    @Column(name = "CreateTime", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "JobName", nullable = true)
    public String getJobName() { return jobName; }

    public void setJobName(String jobName) { this.jobName = jobName; }

    @Column(name = "SentToAgentTime", nullable = true)
    public Timestamp getSentToAgentTime() {
        return sentToAgentTime;
    }

    public void setSentToAgentTime(Timestamp sentToAgentTime) {
        this.sentToAgentTime = sentToAgentTime;
    }

    @Column(name = "StartTime", nullable = true)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Column(name = "EndTime", nullable = true)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Column(name = "TaskState", nullable = true)
    public Integer getTaskState() {
        return taskState;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
    }

    @Column(name = "Logs", nullable = true)
    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    @Column(name = "Priority", nullable = true)
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name = "Browser", nullable = true)
    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }
}
