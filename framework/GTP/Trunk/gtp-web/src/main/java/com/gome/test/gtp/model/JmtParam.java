package com.gome.test.gtp.model;


/**
 * Created by zhangjiadi on 15/11/9.
 */
public class JmtParam {

    public JmtParam(String sceneName,String smallSceneName,String labelName,String environment,String resultVersion,String resultEndVersion,String template)
    {
        this.sceneName=sceneName;
        this.smallSceneName=smallSceneName;
        this.labelName=labelName;
        this.resultVersion=resultVersion;
        this.resultEndVersion=resultEndVersion;
        this.template=template;
        this.environment=environment;
        this.isEnable=true;
    }

    public JmtParam(String sceneName,String environment,String resultVersion,String resultEndVersion,String grpThreads,String template)
    {
        this.sceneName=sceneName;
        this.smallSceneName=null;
        this.labelName=null;
        this.resultVersion=resultVersion;
        this.resultEndVersion=resultEndVersion;
        this.template=template;
        this.environment=environment;
        this.grpThreads=grpThreads;
        this.isEnable=true;
    }


    public String gettimeVersion() {return timeVersion;}
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getResultEndVersion() {
        return resultEndVersion;
    }

    public void setResultEndVersion(String resultEndVersion) {
        this.resultEndVersion = resultEndVersion;
    }

    public String getResultVersion() {
        return resultVersion;
    }

    public void setResultVersion(String resultVersion) {
        this.resultVersion = resultVersion;
    }
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }





    private String sceneName;
    private String smallSceneName;
    private String labelName;
    private String resultVersion;
    private String resultEndVersion;
    private String template;
    private Boolean isEnable;
    private String environment;

    public void setTimeVersion(String timeVersion) {

        this.timeVersion = timeVersion.equals("NONE")?"":timeVersion;
    }

    private String timeVersion;

    public String getGrpThreads() {
        return grpThreads;
    }

    public String grpThreads ;

    public String getTimeArt() {
        return timeArt;
    }

    public void setTimeArt(String timeArt) {
        this.timeArt = timeArt;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    private String taskId;
    private String timeArt;





}
