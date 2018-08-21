/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Zonglin.Li
 */
@Repository
public class UserInfoDao extends BaseDao<UserInfo>{
    public List getUserListByName(String userName) {
        String hql = "select UserName, LastGroup, LastLogin, IsAdmin, UserID from UserInfo where UserName = '" + userName + "'";
        return sqlQuery(hql);
    }

    public boolean uniqueUsername (String username) {
        String sql = String.format("select count(username) from UserInfo where username = '%s'",username);
        String countStr = sqlQuery(sql).get(0).toString();
        if ("0" == countStr) {
            return true;
        } else {
            return false;
        }
    }

    public String getEncryptPwByUsername (String username) {
        String sql = String.format("select UserInfo.password from UserInfo where username = '%s'",username);
        return sqlQuery(sql).get(0).toString();
    }

    public UserInfo getUserInfoByUsername (String username) {
        String sql = String.format("select * from UserInfo where username = '%s'", username);
        List userInfoList = sqlQuery(sql, UserInfo.class);
        if (userInfoList.size() == 0 || userInfoList.isEmpty()) {
            return null;
        } else {
            return (UserInfo) userInfoList.get(0);
        }
    }
}
