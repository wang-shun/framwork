package com.gome.test.gtp.model;

import com.gome.test.gtp.utils.Constant;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="TaskInfo")
@DynamicUpdate(true)
public class TaskInfo implements Serializable{

	private Integer taskID;
	private String taskName;
	private Integer env;
	private Integer branch;
	private Integer taskType;
	private Integer agent;
	private Integer excuteType;
	private String excuteInfo;
	private Integer browser;
	private Integer taskStatus;
	private String emailList;
	private boolean monitored;
	private String creator;
	private Timestamp createTime;
	private String lastEditor;
	private Timestamp lastEditorTime;
	private Timestamp lastRunTime;
	private Integer runRegularInfoID;
	private boolean split;
	private Timestamp startDate;
	private Integer runType;
    private String loadConfName;

    @Transient
    @DictionaryMap(parent = Constant.ENV, keyColName = "Env")
    private String envName;
    @Transient
    @DictionaryMap(parent = "TaskType", keyColName = "TaskType")
    private String taskTypeName;
    @Transient
    @DictionaryMap(parent = Constant.REGULAR_START_TYPE, keyColName = "RunRegularStartType")
    private String excuteTypeName;
    @Transient
    @DictionaryMap(parent = Constant.BROWSER, keyColName = "Browser")
    private String browserName;
    @Transient
    @DictionaryMap(parent = Constant.TASK_STATUS, keyColName = "TaskStatus")
    private String taskStatusName;
    @Transient
    @DictionaryMap(parent = Constant.RUN_TYPE, keyColName = "RunType")
    private String runTypeName;


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TaskID")
	public Integer getTaskID() {
		return taskID;
	}
	public void setTaskID(Integer taskID) {
		this.taskID = taskID;
	}
	@Column(name="TaskName",nullable = false)
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
    @Column(name="Env",nullable = false)
	public Integer getEnv() {
		return env;
	}
	public void setEnv(Integer env) {
		this.env = env;
	}
	@Column(name="Branch",nullable = false)
	public Integer getBranch() {
		return branch;
	}
	public void setBranch(Integer branch) {
		this.branch = branch;
	}
	@Column(name="TaskType",nullable = false)
	public Integer getTaskType() {
		return taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	@Column(name="Agent",nullable=true)
	public Integer getAgent() {
		return agent;
	}
	public void setAgent(Integer agent) {
		this.agent = agent;
	}
	@Column(name="ExcuteType",nullable = false)
	public Integer getExcuteType() {
		return excuteType;
	}
	public void setExcuteType(Integer excuteType) {
		this.excuteType = excuteType;
	}
	
        @Column(name="ExcuteInfo",nullable = false)
	public String getExcuteInfo() {
		return excuteInfo;
	}
        
	public void setExcuteInfo(String excuteInfo) {
		this.excuteInfo = excuteInfo;
	}
       
	@Column(name="Browser",nullable = false)
	public Integer getBrowser() {
		return browser;
	}
	public void setBrowser(Integer browser) {
		this.browser = browser;
	}
	@Column(name="TaskStatus",nullable = false)
	public Integer getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}
	@Column(name="EmailList",nullable=true)
	public String getEmailList() {
		return emailList;
	}
	public void setEmailList(String emailList) {
		this.emailList = emailList;
	}
	@Column(name="isMonitored",nullable = false)
	public boolean getMonitored() {
		return monitored;
	}
	public void setMonitored(Boolean monitored) {
		this.monitored = monitored;
	}
	@Column(name="Creator",nullable=true)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name="CreateTime",nullable=true)
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Column(name="LastEditor",nullable=true)
	public String getLastEditor() {
		return lastEditor;
	}
	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}
	@Column(name="LasEditedTime",nullable=true)
	public Timestamp getLastEditorTime() {
		return lastEditorTime;
	}
	public void setLastEditorTime(Timestamp lastEditorTime) {
		this.lastEditorTime = lastEditorTime;
	}
	@Column(name="LastRunTime",nullable=true)
	public Timestamp getLastRunTime() {
		return lastRunTime;
	}
	public void setLastRunTime(Timestamp lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
	@Column(name="RunRegularInfoID",nullable = false)
	public Integer getRunRegularInfoID() {
		return runRegularInfoID;
	}
	public void setRunRegularInfoID(Integer runRegularInfoID) {
		this.runRegularInfoID = runRegularInfoID;
	}
	@Column(name="isSplit",nullable = false)
	public boolean getSplit() {
		return split;
	}
	public void setSplit(Boolean split) {
		this.split = split;
	}
	@Column(name="StartDate",nullable = false)
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	@Column(name="RunType",nullable = false)
	public Integer getRunType() {
		return runType;
	}
	public void setRunType(Integer runType) {
		this.runType = runType;
	}
    @Column(name="LoadConfName")
    public String getLoadConfName() {
        return loadConfName;
    }

    public void setLoadConfName(String loadConfName) {
        this.loadConfName = loadConfName;
    }
}
