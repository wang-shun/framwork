package com.gome.test.gtp.model;

public class CaseRunTime {
    private String caseName;
    private long duration;
    private int lastrunDate;

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getLastrunDate() {
        return lastrunDate;
    }

    public void setLastrunDate(int lastrunDate) {
        this.lastrunDate = lastrunDate;
    }
}