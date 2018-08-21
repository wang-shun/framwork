package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ConfigureDictionary")
@DynamicUpdate(true)
public class ConfigureDictionary implements Serializable{
private Integer id;
private Integer itemLevel;
private Integer itemParentID;
private String itemName;
private Integer itemValue;
private String itemText;
private String description;


@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="id")
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}

@Column(name="ItemLevel",nullable = false)
public int getItemLevel() {
	return itemLevel;
}
public void setItemLevel(Integer itemLevel) {
	this.itemLevel = itemLevel;
}
@Column(name="ItemParentID",nullable = false)
public int getItemParentID() {
	return itemParentID;
}
public void setItemParentID(Integer itemParentID) {
	this.itemParentID = itemParentID;
}
@Column(name="ItemName",nullable = false)
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}
@Column(name="ItemValue",nullable = false)
public Integer getItemValue() {
	return itemValue;
}
public void setItemValue(Integer itemValue) {
	this.itemValue = itemValue;
}
@Column(name="ItemText",nullable=true)
public String getItemText() {
	return itemText;
}
public void setItemText(String itemText) {
	this.itemText = itemText;
}
@Column(name="Description",nullable=true)
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}


}
