/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.HostsDao;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Zonglin.Li
 */
@Service
public class HostsService {
    @Autowired
    private HostsDao hostsDao;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    
    public List getAllHosts () {
        List hostsInfoList = hostsDao.find("select id,env,lastUpdateUser,lastUpdateTime,enable from Hosts");
        Map<Integer, String> replaceRule = new HashMap<Integer, String>();
        replaceRule.put(1, Constant.ENV);
        lcdService.replaceValue(hostsInfoList, replaceRule);
        return hostsInfoList;
    }
}
