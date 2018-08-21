package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by zhangjiadi on 16/2/3.
 */
@Entity
@Table(name = "LoadRemark")
@DynamicUpdate(true)
public class LoadRemark {

    private int id;
    private String sceneName;
    private String smallSceneName;
    private String environment;

    public String getDataCreateTime() {
        return dataCreateTime;
    }

    public void setDataCreateTime(String dataCreateTime) {
        this.dataCreateTime = dataCreateTime;
    }

    private String dataCreateTime;

    public LoadRemark()
    {

    }
    public  LoadRemark(String sceneName,String smallSceneName,String environment,String template,String version,String timeVersion,String dataCreateTime,String remark,String remark_History)
    {
        this.sceneName=sceneName;
        this.smallSceneName=smallSceneName;
        this.environment=environment;
        this.template=template;
        this.version=version;
        this.remark=remark;
        this.remark_History=remark_History;
        this.dataCreateTime=dataCreateTime;
        this.timeVersion = timeVersion ;

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    public int getId() {
        return id;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSmallSceneName() {
        return smallSceneName;
    }

    public void setSmallSceneName(String smallSceneName) {
        this.smallSceneName = smallSceneName;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark_History() {
        return remark_History;
    }

    public void setRemark_History(String remark_History) {
        this.remark_History = remark_History;
    }

    public void setId(int id) {
        this.id = id;

    }

    private String template;
    private String version;
    private String remark;
    private String remark_History;

    public String getTimeVersion() {
        return timeVersion;
    }

    public void setTimeVersion(String timeVersion) {
        this.timeVersion = timeVersion;
    }

    private String timeVersion;
}
