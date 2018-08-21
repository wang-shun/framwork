package com.gome.test.gtp.model;


import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "AgentInfo")
@DynamicUpdate(true)
public class AgentInfo implements Serializable, Comparable {
    private Integer agentID;
    private String agentName;
    private String agentLabel;
    private String agentIp;
    private Integer port;
    private Boolean reseved;
    private Integer agentStatus;
    private Integer brower;
    private Integer taskId;
    private Timestamp lastRunTime;
    private Integer ENV;
    private String description;
    private String taskType;
    private String agentOS;



    private Integer queueId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AgentID")
    public Integer getAgentID() {
        return agentID;
    }

    public void setAgentID(Integer agentID) {
        this.agentID = agentID;
    }

    @Column(name = "AgentName", nullable = false)
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Column(name = "AgentLabel", nullable = false)
    public String getAgentLabel() {
        return agentLabel;
    }

    public void setAgentLabel(String agentLabel) {
        this.agentLabel = agentLabel;
    }

    @Column(name = "AgentIP", nullable = false)
    public String getAgentIp() {
        return agentIp;
    }

    public void setAgentIp(String agentIp) {
        this.agentIp = agentIp;
    }
    @Column(name = "Port", nullable = false)
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Column(name = "isReseved", nullable = false)
    public Boolean isReseved() {
        return reseved;
    }

    public void setReseved(Boolean reseved) {
        this.reseved = reseved;
    }

    @Column(name = "AgentStatus", nullable = false)
    public Integer getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(Integer agentStatus) {
        this.agentStatus = agentStatus;
    }

    @Column(name = "Brower", nullable = false)
    public Integer getBrower() {
        return brower;
    }

    public void setBrower(Integer brower) {
        this.brower = brower;
    }

    @Column(name = "TaskID", nullable = true)
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Column(name = "LastRunTime", nullable = true)
    public Timestamp getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Timestamp lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    @Column(name = "ENV", nullable = false)
    public Integer getENV() {
        return ENV;
    }

    public void setENV(Integer eNV) {
        ENV = eNV;
    }

    @Column(name = "Description", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "TaskType", nullable = false)
    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Column(name = "AgentOS", nullable = false)
    public String getAgentOS() {
        return agentOS;
    }

    public void setAgentOS(String agentOS) {
        this.agentOS = agentOS;
    }

    @Column(name = "QueueId")
    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    public int compareTo(Object o) {
        AgentInfo agentInfo = (AgentInfo)o;
        return lastRunTime.compareTo(agentInfo.getLastRunTime());
    }
}
