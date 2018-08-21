package com.gome.test.gtp.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangjiadi on 16/1/26.
 */
public class TaskLoadReport implements Serializable {
    @JsonIgnore
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private int taskId;

    public int getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(int taskListId) {
        this.taskListId = taskListId;
    }

    private int taskListId;
    @JsonIgnore
    private String taskName;
    private Long startTime;
    private Long endTime;
    private int total;
    private int fail;
    private int pass;
    private Long duration;
    private Long createTime;
    private String errorMessage;
    private String mojoLogUrl;

    public boolean getIsTest() {
        return isTest;
    }

    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }

    private boolean isTest;


    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }



    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskListId) {
        this.taskId = taskListId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMojoLogUrl() {
        return mojoLogUrl;
    }

    public void setMojoLogUrl(String mojoLogUrl) {
        this.mojoLogUrl = mojoLogUrl;
    }







    public List<LoadLogDetail> getLoadLogDetails() {
        return loadLogDetails;
    }

    public void setLoadLogDetails(List<LoadLogDetail> loadLogDetails) {
        this.loadLogDetails = loadLogDetails;
    }

    private List<LoadLogDetail> loadLogDetails;

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
