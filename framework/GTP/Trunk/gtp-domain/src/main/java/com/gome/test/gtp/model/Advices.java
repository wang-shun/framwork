 package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


 @Entity
 @Table(name="Advices")
 @DynamicUpdate(true)
 public class Advices implements Serializable{
 private Integer advicesId;
 private String name;
 private String advice;
 private String owner;
 private Integer status;
 private Timestamp createTime;
 private Integer advicetype;
 private String assignTo;
 @Id
 @GeneratedValue(strategy=GenerationType.IDENTITY)
 @Column(name="AdvicesID")
 public Integer getAdvicesId() {
     return advicesId;
 }
 public void setAdvicesId(Integer advicesId) {
     this.advicesId = advicesId;
 }
 @Column(name="Name",nullable = false)
 public String getName() {
     return name;
 }
 public void setName(String name) {
     this.name = name;
 }
 @Column(name="Advice",nullable = false)
 public String getAdvice() {
     return advice;
 }
 public void setAdvice(String advice) {
     this.advice = advice;
 }
 @Column(name="Owner",nullable=true)
 public String getOwner() {
     return owner;
 }
 public void setOwner(String owner) {
     this.owner = owner;
 }
 @Column(name="AdviceStatus",nullable=true)
 public Integer getStatus() {
     return status;
 }
 public void setStatus(Integer status) {
     this.status = status;
 }
 @Column(name="CreateTime",nullable=true)
 public Timestamp getCreateTime() {
     return createTime;
 }
 public void setCreateTime(Timestamp createTime) {
     this.createTime = createTime;
 }
 @Column(name="AdviceType",nullable=true)
 public Integer getAdvicetype() {
     return advicetype;
 }

 public void setAdvicetype(Integer advicetype) {
     this.advicetype = advicetype;
 }
 @Column(name="AssignTo",nullable=true)
 public String getAssignTo() {
     return assignTo;
 }
 public void setAssignTo(String assignTo) {
     this.assignTo = assignTo;
 }

 }
