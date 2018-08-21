package com.gome.test.gtp.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by lizonglin on 2016/1/13/0013.
 */
@Entity
@Table(name = "LoadScene")
public class LoadScene {
    private int id;
    private String name;
    private String onError;
    private int threadNum;
    private int initDelay;
    private int startCount;
    private int startCountBurst;
    private int startPeriod;
    private int stopCount;
    private int stopPeriod;
    private int flightTime;
    private int rampUp;
    private Timestamp lastUpdateTime;
    private boolean isTemplate;
    private boolean isTest;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnError() {
        return onError;
    }

    public void setOnError(String onError) {
        this.onError = onError;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getInitDelay() {
        return initDelay;
    }

    public void setInitDelay(int initDelay) {
        this.initDelay = initDelay;
    }

    public int getStartCount() {
        return startCount;
    }

    public void setStartCount(int startCount) {
        this.startCount = startCount;
    }

    public int getStartCountBurst() {
        return startCountBurst;
    }

    public void setStartCountBurst(int startCountBurst) {
        this.startCountBurst = startCountBurst;
    }

    public int getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(int startPeriod) {
        this.startPeriod = startPeriod;
    }

    public int getStopCount() {
        return stopCount;
    }

    public void setStopCount(int stopCount) {
        this.stopCount = stopCount;
    }

    public int getStopPeriod() {
        return stopPeriod;
    }

    public void setStopPeriod(int stopPeriod) {
        this.stopPeriod = stopPeriod;
    }

    public int getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
    }

    public int getRampUp() {
        return rampUp;
    }

    public void setRampUp(int rampUp) {
        this.rampUp = rampUp;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Column(name = "isTemplate")
    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean isTemplate) {
        this.isTemplate = isTemplate;
    }
    @Column(name = "isTest")
    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean isTest) {
        this.isTest = isTest;
    }

}
