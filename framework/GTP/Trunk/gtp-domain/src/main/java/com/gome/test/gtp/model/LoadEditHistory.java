package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhangjiadi on 16/2/1.
 */
@Entity
@Table(name = "LoadEditHistory")
@DynamicUpdate(true)
public class LoadEditHistory {

    public LoadEditHistory()
    {

    }

    public  LoadEditHistory(String user_Name,String user_IP,String sceneName,String sceneName_Old,String environment,String template,String version,String timeVersion,String smallSceneName,String dataCreateTime,JMTEditType editType)
    {
        this.user_Name=user_Name;
        this.user_IP= user_IP;
        this.sceneName=sceneName;
        this.sceneName_Old=sceneName_Old;
        this.environment=environment;
        this.template=template;
        this.version=version;
        this.smallSceneName=smallSceneName;
        this.dataCreateTime=dataCreateTime;
        this.editTime=new Date();
        this.editType=editType.getEditTypekey();
        this.timeVersion=timeVersion;
    }

    private int id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public String getUser_IP() {
        return user_IP;
    }

    public void setUser_IP(String user_IP) {
        this.user_IP = user_IP;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneName_Old() {
        return sceneName_Old;
    }

    public void setSceneName_Old(String sceneName_Old) {
        this.sceneName_Old = sceneName_Old;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    private String user_Name;
    private String user_IP;
    private String sceneName;
    private String sceneName_Old;
    private Date editTime;
    private String smallSceneName;

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

    private String environment;
    private String template;
    private String version;

    public String getTimeVersion() {
        return timeVersion;
    }

    public void setTimeVersion(String timeVersion) {
        this.timeVersion = timeVersion;
    }

    private String timeVersion;

    public String getDataCreateTime() {
        return dataCreateTime;
    }

    public void setDataCreateTime(String dataCreateTime) {
        this.dataCreateTime = dataCreateTime;
    }

    private String dataCreateTime;

    public int getEditType() {
        return editType;
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }

    private int editType;

    @Transient
    public String getEditTypeValue() {
        return JMTEditType.getValue(editType).getEditTypeValue();
    }

    public void setEditTypeValue(String editTypeValue) {
        this.editTypeValue = editTypeValue;
    }

    private String editTypeValue;


}
