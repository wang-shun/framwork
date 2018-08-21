/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 *
 * @author shan.tan
 */
@Entity
@Table(name="ScheduleJob")
@DynamicUpdate(true)
public class ScheduleJob implements Serializable {
    private Integer id;
    private String Jobname;
    private String CMD;
    private Timestamp ModifyTime;
    private String ModifyUser;
    private String ScheduleWeek;
    private Timestamp scheduleTime;
    private Boolean enable;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="JobName",nullable = false)
    public String getJobname() {
        return Jobname;
    }

    public void setJobname(String Jobname) {
        this.Jobname = Jobname;
    }

    @Column(name="CMD",nullable = false)
    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    @Column(name="ModifyTime",nullable = false)
    public Timestamp getModifyTime() {
        return ModifyTime;
    }

    public void setModifyTime(Timestamp ModifyTime) {
        this.ModifyTime = ModifyTime;
    }

    @Column(name="ModifyUser",nullable = false)
    public String getModifyUser() {
        return ModifyUser;
    }

    public void setModifyUser(String ModifyUser) {
        this.ModifyUser = ModifyUser;
    }

    @Column(name="scheduleWeek",nullable = false)
    public String getScheduleWeek() {
        return ScheduleWeek;
    }

    public void setScheduleWeek(String ScheduleWeek) {
        this.ScheduleWeek = ScheduleWeek;
    }

    @Column(name="scheduleTime",nullable = false)
    public Timestamp getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Timestamp scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Column(name="enable",nullable = false)
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
  
}
