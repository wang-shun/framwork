package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.ConfigureDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfigureDictionaryDao extends BaseDao<ConfigureDictionary> {
    @Autowired
    private AppConfiguration env;

    public Integer getTypeId (String type) {
        String hql = "select cd.id from ConfigureDictionary as cd where cd.itemName='" + type+"'";
        return (Integer)find(hql).get(0);
    }
    
    public List getConfigTypes (String type) {
        String hql = "select cd.itemName, cd.itemValue from ConfigureDictionary as cd where cd.itemParentID=" + getTypeId(type) + "order by cd.itemValue";
        return find(hql);
    }
    
    public int getConfigIdByTypeName (String type, String name) {
        String hql = "select cd.itemValue from ConfigureDictionary as cd where cd.itemParentID=" + getTypeId(type) + " and cd.itemName = '" + name + "'";
        return (Integer) sqlQuery(hql).get(0);
    }
    
    public String getItemNameByItemValue(String type, int value){
        String hql = "select cd.itemName from ConfigureDictionary as cd where cd.itemParentID=" + getTypeId(type) + " and cd.itemValue = '" + value + "'";
        return (String)find(hql).get(0);
    }
    
    public List getItemNameValue(String type){
        String hql = "select cd.itemName,cd.itemValue from ConfigureDictionary as cd where cd.itemParentID=" + getTypeId(type);
        return sqlQuery(hql);
    }

    public List getInitialNameId() {
        String rootId = env.getProperty("cd.root.nodeId");
        String hql = "select cd.itemName,cd.id from ConfigureDictionary as cd where cd.itemParentID = " + rootId;
        return sqlQuery(hql);
    }

    public List<ConfigureDictionary> getRawConfigureDictionary() {
        String sql = "select * from ConfigureDictionary";
        return sqlQuery(sql);
    }

    public List<String> getSortedListByParentName(String parentName) {
        String sql = String.format("SELECT ConfigureDictionary.ItemName\n" +
                "FROM ConfigureDictionary\n" +
                "WHERE ConfigureDictionary.ItemParentID IN (\n" +
                "SELECT ConfigureDictionary.ID\n" +
                "FROM ConfigureDictionary\n" +
                "WHERE ConfigureDictionary.ItemName = '%s') order by ConfigureDictionary.ItemValue", parentName);
        return (List<String>) sqlQuery(sql);
    }
}
