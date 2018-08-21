package com.gome.test.gtp.model;




/**
 * Created by zhangjiadi on 15/11/17.
 */
public class SortData implements Comparable<SortData> {

    public SortData (String sortKey,Object obj,Boolean isUper)
    {
        this.sortKey=sortKey;
        this.report=obj;
        this.isUper=isUper;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public Object getReport() {
        return report;
    }

    public void setReport(Object report) {
        this.report = report;
    }

    private String sortKey;
    private Object report;
    private boolean isUper;

    @Override
    public int compareTo(SortData o) {
        //加上这句话:


        String sKey = o.getSortKey().trim();
        String tKey = this.getSortKey().trim();

        if(!isUper)
        {
            String temp=sKey;
            sKey = tKey;
            tKey= temp;
        }

        if(tKey==null || "".equals(tKey)) {
            return 1;
        } else if(sKey==null || "".equals(sKey)) {
            return -1;
        } else if( sKey.equals(tKey) || sKey == tKey )
        {
            return 0;
        }
        else {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            return tKey.compareToIgnoreCase(sKey);
        }
    }
}
