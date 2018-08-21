package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "RunRegularInfo")
@DynamicUpdate(true)
public class RunRegularInfo implements Serializable {
    private Integer id;
    private String regularName;
    private Integer startType;
    private Timestamp runTime;
    private String weekDay;
    private Integer runRule;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "RegularName", nullable = true)
    public String getRegularName() {
        return regularName;
    }

    public void setRegularName(String regularName) {
        this.regularName = regularName;
    }

    @Column(name = "StartType", nullable = false)
    public Integer getStartType() {
        return startType;
    }

    public void setStartType(Integer startType) {
        this.startType = startType;
    }

    @Column(name = "RunTime", nullable = false)
    public Timestamp getRunTime() {
        return runTime;
    }

    public void setRunTime(Timestamp runTime) {
        this.runTime = runTime;
    }

    @Column(name = "WeekDay", nullable = true)
    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    @Column(name = "RunRule", nullable = false)
    public Integer getRunRule() {
        return runRule;
    }

    public void setRunRule(Integer runRule) {
        this.runRule = runRule;
    }
}
