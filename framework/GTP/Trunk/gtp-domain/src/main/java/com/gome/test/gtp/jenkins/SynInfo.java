package com.gome.test.gtp.jenkins;

/**
 * Created by lizonglin on 2015/12/7/0007.
 */
public class SynInfo {
    String jobName;
    boolean isBuilding;
    String result;
    int queueItem;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public void setBuilding(boolean isBuilding) {
        this.isBuilding = isBuilding;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getQueueItem() {
        return queueItem;
    }

    public void setQueueItem(int queueItem) {
        this.queueItem = queueItem;
    }
}
