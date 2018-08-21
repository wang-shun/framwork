package com.gome.test.gtp.model;

/**
 * Created by zhangjiadi on 15/11/10.
 */
public class JMTReportTimeStamp implements Comparable<JMTReportTimeStamp> {

    public JMTReportTimeStamp(Long timeStamp,JMTReport jmtReport)
    {
        this.timeStamp=timeStamp;
        this.jmtReport=jmtReport;
    }

    public JMTReportTimeStamp()
    {

    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public JMTReport getJmtReport() {
        return jmtReport;
    }

    public void setJmtReport(JMTReport jmtReport) {
        this.jmtReport = jmtReport;
    }

    private Long timeStamp;
    private JMTReport jmtReport;




    @Override
    public int compareTo(JMTReportTimeStamp o) {
        return  (int) (timeStamp - o.timeStamp) ;
    }
}
