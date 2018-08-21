/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Zonglin.Li
 */
@Entity
@Table(name="UserInfo")
@DynamicUpdate(true)
public class UserInfo implements Serializable {
    private int UserID;
    private String UserName;
    private String Password;
    private String LastGroup;
    private Timestamp LastLogin;
    private Timestamp LastChangeGroup;
    private Boolean IsAdmin;
    private String Email;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="UserID")
    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    @Column(name="userName")
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    @Column(name="password")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Column(name="lastGroup")
    public String getLastGroup() {
        return LastGroup;
    }

    public void setLastGroup(String LastGroup) {
        this.LastGroup = LastGroup;
    }

    @Column(name="lastLogin")
    public Timestamp getLastLogin() {
        return LastLogin;
    }

    public void setLastLogin(Timestamp LastLogin) {
        this.LastLogin = LastLogin;
    }

    @Column(name="lastChangeGroup")
    public Timestamp getLastChangeGroup() {
        return LastChangeGroup;
    }

    public void setLastChangeGroup(Timestamp LastChangeGroup) {
        this.LastChangeGroup = LastChangeGroup;
    }

    @Column(name="isAdmin")
    public Boolean getIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(Boolean IsAdmin) {
        this.IsAdmin = IsAdmin;
    }

    @Column(name="email")

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
