package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zhangjiadi on 16/4/6.
 */
@Entity
@Table(name="ReplaceInfo")
@DynamicUpdate(true)
public class ReplaceInfo implements Serializable {

    private Integer replaceId;
    private Integer taskId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ReplaceID")
    public Integer getReplaceId() {
        return replaceId;
    }

    public void setReplaceId(Integer replaceId) {
        this.replaceId = replaceId;
    }
    @Column(name="TaskID")
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
    @Column(name="FileName",nullable=true)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name="ReplaceKey",nullable=true)
    public String getReplacekey() {
        return replacekey;
    }

    public void setReplacekey(String replacekey) {
        this.replacekey = replacekey;
    }

    @Column(name="ReplaceValue",nullable=true)
    public String getReplaceValue() {
        return replaceValue;
    }

    public void setReplaceValue(String replaceValue) {
        this.replaceValue = replaceValue;
    }

    private String fileName;
    private String replacekey;
    private String replaceValue;

    @Column(name="createTime",nullable=true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    private Timestamp createTime;




}
