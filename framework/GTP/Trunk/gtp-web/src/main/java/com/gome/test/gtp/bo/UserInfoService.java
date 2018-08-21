/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.UserInfoDao;
import com.gome.test.gtp.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Zonglin.Li
 */
@Service
public class UserInfoService extends BaseService<UserInfo>{
    @Autowired
    private UserInfoDao userInfoDao;
    
    public Object getUserByName (String UserName) {
        List UserList = userInfoDao.getUserListByName(UserName);
        if (null == UserList) {
            return null;
        } else {
            return UserList.get(0);
        }
    }
    
    public void updateUserInfo (UserInfo userInfo) {
        System.err.println("UserID: "+userInfo.getUserID());
        if (-1 == userInfo.getUserID()) {
            UserInfo userInfoDb = new UserInfo();
            userInfoDb.setIsAdmin(false);
            userInfoDb.setLastChangeGroup(new Timestamp(new Date().getTime()));
            userInfoDb.setLastLogin(new Timestamp(new Date().getTime()));
            userInfoDb.setUserName(userInfo.getUserName());
            userInfoDb.setLastGroup(userInfo.getLastGroup());
            userInfoDao.save(userInfoDb);
        } else {
            UserInfo userInfoDb = userInfoDao.get(userInfo.getUserID());
            userInfoDb.setLastLogin(new Timestamp(new Date().getTime()));
            if (!userInfo.getLastGroup().equals(userInfoDb.getLastGroup())) {
                userInfoDb.setLastChangeGroup(new Timestamp(new Date().getTime()));
                userInfoDb.setLastGroup(userInfo.getLastGroup());
            }
        }
    }

    public boolean uniqueUsername (String username) {
        return userInfoDao.uniqueUsername(username);
    }

    public String getEncryptPwByUsername (String username) {
        return userInfoDao.getEncryptPwByUsername(username);
    }
    public UserInfo getUserInfoByUsername (String username) {
        return userInfoDao.getUserInfoByUsername(username);
    }
}
