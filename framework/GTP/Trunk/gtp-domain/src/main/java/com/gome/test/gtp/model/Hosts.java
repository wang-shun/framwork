package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name="Hosts")
@DynamicUpdate(true)
public class Hosts implements Serializable{
private Integer id;
private Integer env;
private String hostContent;
private String lastUpdateUser;
private Date lastUpdateTime;
private boolean enable;

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="id")
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
@Column(name="Env",nullable = false)
public Integer getEnv() {
	return env;
}
public void setEnv(Integer env) {
	this.env = env;
}
@Column(name="HostContent",nullable = false)
public String getHostContent() {
	return hostContent;
}
public void setHostContent(String hostContent) {
	this.hostContent = hostContent;
}
@Column(name="LastUpdateUser",nullable=true)
public String getLastUpdateUser() {
	return lastUpdateUser;
}
public void setLastUpdateUser(String lastUpdateUser) {
	this.lastUpdateUser = lastUpdateUser;
}
@Column(name="LastUpdateTime",nullable=true)
public Date getLastUpdateTime() {
	return lastUpdateTime;
}
public void setLastUpdateTime(Date lastUpdateTime) {
	this.lastUpdateTime = lastUpdateTime;
}
@Column(name="Enable",nullable = false)
public boolean getEnable() {
	return enable;
}
public void setEnable(boolean enable) {
	this.enable = enable;
}


}
