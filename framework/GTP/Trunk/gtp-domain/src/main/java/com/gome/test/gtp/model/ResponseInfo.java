package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by zhangjiadi on 16/7/11.
 */
@Entity
@Table(name = "responseInfo")
@DynamicUpdate(true)
public class ResponseInfo {

    private int id;
    private String jobName;
    private int appInfoId;
    private String appName;
    private String objectTime;
    private String ip;
    private String port;
    private String level;
    private String env;
    private int status;
    private boolean replaceHost;
    private String remark;
    private String version;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @javax.persistence.Id
    public int getId() {
        return id;
    }

    public boolean isReplaceHost() {
        return replaceHost;
    }

    public void setReplaceHost(boolean replaceHost) {
        this.replaceHost = replaceHost;
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
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}
