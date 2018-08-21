package com.gome.test.gtp.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangjiadi on 16/1/22.
 */
@Document
public class LoadLogDetail implements Serializable    {

    public LoadLogDetail ()
    {
        this.duration= 0L;
        this.total=0L;
        this.error=0L;
        this.success=0L;
    }

    private String sceneName;
    private String labelName;
    private String resultVersion;

    public String getTimeVersion() {
        return timeVersion;
    }

    public void setTimeVersion(String timeVersion) {
        this.timeVersion = timeVersion;
    }

    private String timeVersion;
    private String smallSceneName;
    private Long startTime;
    private Long endTime;
    private Long total;
    private Long error;
    private Long success;
    private Long duration;

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getResultVersion() {
        return resultVersion;
    }

    public void setResultVersion(String resultVersion) {
        this.resultVersion = resultVersion;
    }

    public String getSmallSceneName() {
        return smallSceneName;
    }

    public void setSmallSceneName(String smallSceneName) {
        this.smallSceneName = smallSceneName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @JsonIgnore
    public String getStartTimeStr(){
        if(this.startTime==null)
            return "";
        Date date=new Date(this.startTime);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    @JsonIgnore
    public String getEndTimeStr(){
        if(this.endTime==null)
            return "";
        Date date=new Date(this.endTime);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }



}
