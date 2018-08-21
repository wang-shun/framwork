package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by zhangjiadi on 16/7/6.
 */
@Entity
@Table(name = "EnvInfo")
@DynamicUpdate(true)
public class EnvInfo {

    private int id;
    private int appInfoId;
    private String env;
    private String svn;
    private int dataType;
    private String zkAddress;
    private String remark;




    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(int appInfoId) {
        this.appInfoId = appInfoId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getSvn() {
        return svn;
    }

    public void setSvn(String svn) {
        this.svn = svn;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

}
