package com.gome.test.gtp.model;

/**
 * Created by zhangjiadi on 16/7/15.
 */
public class Health {
    private String appName;
    private String version;
    private String objectTime;
    private String ip;
    private String port;
    private String env;
    private String level;
    private String remark;
    private String sign;
    private String replaceHost;

    public String getReplaceHost() {
        return replaceHost;
    }

    public void setReplaceHost(String replaceHost) {
        this.replaceHost = replaceHost;
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

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
