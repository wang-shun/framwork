package com.gome.test.gtp.model;

import java.util.List;

/**
 * Created by lizonglin on 2016/4/26/0026.
 */
public class AGReportEmail {
    private String reportType;
    private String groupName;
    private int totalCaseNum;
    private int totalPassNum;
    private int totalFailNum;
    private String passRate;
    private long date;
    private List<AGReport> personalReportList;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getTotalCaseNum() {
        return totalCaseNum;
    }

    public void setTotalCaseNum(int totalCaseNum) {
        this.totalCaseNum = totalCaseNum;
    }

    public int getTotalPassNum() {
        return totalPassNum;
    }

    public void setTotalPassNum(int totalPassNum) {
        this.totalPassNum = totalPassNum;
    }

    public int getTotalFailNum() {
        return totalFailNum;
    }

    public void setTotalFailNum(int totalFailNum) {
        this.totalFailNum = totalFailNum;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<AGReport> getPersonalReportList() {
        return personalReportList;
    }

    public void setPersonalReportList(List<AGReport> personalReportList) {
        this.personalReportList = personalReportList;
    }
}
