package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="DailyActiveCase")
@DynamicUpdate(true)
public class DailyActiveCase implements Serializable{
	
	private Integer caseID;
	private String caseName;
	private Timestamp createTime;
	private Timestamp lastRunTime;
	private String owner;
	private Integer bussinessGroup;
	private Integer taskType;
	private Boolean isOrderList;
	private Integer priotrity;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CaseID")
	public Integer getCaseID() {
		return caseID;
	}
	public void setCaseID(Integer caseID) {
		this.caseID = caseID;
	}
	@Column(name="CaseName",nullable = false)
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	@Column(name="CreateTime",nullable = false)
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Column(name="LastRunTime",nullable=true)
	public Timestamp getLastRunTime() {
		return lastRunTime;
	}
	public void setLastRunTime(Timestamp lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
	@Column(name="Owner",nullable=true)
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	@Column(name="BussinessGroup",nullable = false)
	public Integer getBussinessGroup() {
		return bussinessGroup;
	}
	public void setBussinessGroup(Integer bussinessGroup) {
		this.bussinessGroup = bussinessGroup;
	}
	@Column(name="TaskType",nullable = false)
	public Integer getTaskType() {
		return taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	@Column(name="isOrderList",nullable = false)
	public Boolean isOrderList() {
		return isOrderList;
	}
	public void setOrderList(Boolean isOrderList) {
		this.isOrderList = isOrderList;
	}
	@Column(name="Priotrity",nullable = false)
	public Integer getPriotrity() {
		return priotrity;
	}
	public void setPriotrity(Integer priotrity) {
		this.priotrity = priotrity;
	}
	
	
}
