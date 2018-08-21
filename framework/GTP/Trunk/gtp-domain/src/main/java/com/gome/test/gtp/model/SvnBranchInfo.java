package com.gome.test.gtp.model;

import com.gome.test.gtp.utils.Constant;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "SvnBranchInfo")
@DynamicUpdate(true)
@DictionaryMap
public class SvnBranchInfo implements Serializable {
    private Integer branchID;
    private String branchName;
    private String branchUrl;
    private Timestamp expireDate;
    private Integer businessGroup;
    private Integer type;
    @Transient
    @DictionaryMap(parent = Constant.ENV, keyColName = "environment")
    private String environmentName;

    @Transient
    @DictionaryMap(parent = Constant.GROUP, keyColName = "businessGroup")
    private String bussinessGroupName;

    @Column(name = "BusinessGroup", nullable = false)
    public Integer getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(Integer businessGroup) {
        this.businessGroup = businessGroup;
    }

    @Transient
    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    @Transient
    public String getBussinessGroupName() {
        return bussinessGroupName;
    }

    public void setBussinessGroupName(String bussinessGroupName) {
        this.bussinessGroupName = bussinessGroupName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BranchID")
    public Integer getBranchID() {
        return branchID;
    }

    public void setBranchID(Integer branchID) {
        this.branchID = branchID;
    }

    @Column(name = "BranchName", nullable = false)
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Column(name = "BranchUrl", nullable = false)
    public String getBranchUrl() {
        return branchUrl;
    }

    public void setBranchUrl(String branchUrl) {
        this.branchUrl = branchUrl;
    }

    @Column(name = "ExpireDate", nullable = false)
    public Timestamp getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }

    @Column(name = "Type", nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
