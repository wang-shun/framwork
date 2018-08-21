package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zhangjiadi on 16/7/11.
 */
@Entity
@Table(name = "response_queue")
@DynamicUpdate(true)
public class ResponseQueue {
    private int id;
    @Column(name = "jobName")
    private String jobName;
    @Column(name = "appInfoId")
    private int appInfoId;
    @Column(name = "objectTime")
    private String objectTime;
//    @Column(name = "ip")
    private String ip;
    @Column(name = "port")
    private String port;
    @Column(name = "level")
    private String level;
    @Column(name = "env")
    private String env;
    @Column(name = "status")
    private int status;
    @Column(name = "replacehost")
    private boolean replaceHost;
//    @Column(name = "logs")
    private String logs;
    @Column(name = "responseinfoId")
    private int responseinfoId;
    @Column(name = "StartTime")
    private Timestamp startTime;
    @Column(name = "EndTime")
    private Timestamp endTime;
    @Column(name = "appName")
    private String appName;
    @Column(name = "version")
    private String version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" )
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(int appInfoId) {
        this.appInfoId = appInfoId;
    }

    public String getObjectTime() {
        return objectTime;
    }

    public void setObjectTime(String objectTime) {
        this.objectTime = objectTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isReplaceHost() {
        return replaceHost;
    }

    public void setReplaceHost(boolean replaceHost) {
        this.replaceHost = replaceHost;
    }

    public int getResponseinfoId() {
        return responseinfoId;
    }

    public void setResponseinfoId(int responseinfoId) {
        this.responseinfoId = responseinfoId;
    }

    public String getJobName(int id)
    {
        return String.format("Job_%d_%d",System.currentTimeMillis(),id);
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }
    @Column(name = "EndTime" )
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Column(name = "StartTime" )
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
