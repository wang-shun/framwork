package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by zhangjiadi on 16/7/14.
 */
@Entity
@Table(name = "AppEmail")
@DynamicUpdate(true)
public class AppEmail {

    private int id;
    private String appName;
    private String address;
    private String ccaddress;


    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getCcaddress() {
        return ccaddress;
    }

    public void setCcaddress(String ccaddress) {
        this.ccaddress = ccaddress;
    }


}
