/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.Dao;
import com.gome.test.gtp.model.DictionaryMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author Dangdang.Cao
 */
@Service
public class ObjectService extends BaseService<Object> {

    @Autowired
    private Dao dao;

    public List findCaseReportAll(int reportId) {
        String hql = "select cr.reportId,cr.testCaseName,cr.reRun,cr.computerName,cr.duration,cr.testResult,cfrc.errorMessage,cfrc.advice,cr.stackTrace from CaseResult cr , CaseFailReasonCategory cfrc where cfrc.id = cr.failReasonCategoryID and cr.reportId=" + reportId + " and cfrc.errorMessage is not null and cfrc.advice is not null";
        return dao.find(hql);
    }

    public List findAllUsers() {
        String hql = "select M.userName,M.email,AspnetRoles.roleName,M.comment,M.createDate,M.lastLoginDate,M.lastPasswordChangedDate from (select T.*,AspnetUsersInRoles.roleId from (SELECT a.userName,b.*  FROM AspnetUsers AS a,AspnetMembership AS b WHERE a.userId=b.userId) as T left join AspnetUsersInRoles on T.userId=AspnetUsersInRoles.userId) as M left join AspnetRoles on M.roleId = AspnetRoles.roleId";
        return dao.find(hql);
    }

    public Object copyEntity(Object object) throws Exception
    {
        Class<?> classType = object.getClass();
        Object objectCopy = classType.getConstructor(new Class[]{}).newInstance(new Object[]{});
        Field[] fields = classType.getDeclaredFields();
        for(Field field : fields)
        {
            field.setAccessible(true);
            Boolean isCopy = true;
            for (Annotation annotation :field.getDeclaredAnnotations()) {
                if (annotation.annotationType() == DictionaryMap.class) {
                    isCopy = false;
                }
            }
            if (isCopy) {
                String name = field.getName();
                String firstLetter = name.substring(0, 1).toUpperCase();
                String getMethodName = "get" + firstLetter + name.substring(1);
                String setMethodName = "set" + firstLetter + name.substring(1);
                Method getMethod = classType.getMethod(getMethodName, new Class[]{});
                Method setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});
                Object value = getMethod.invoke(object, new Object[]{});
                setMethod.invoke(objectCopy, new Object[]{value});
            }
        }
        return objectCopy;

    }
}
