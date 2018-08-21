/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.AgentInfoDao;
import com.gome.test.gtp.model.AgentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author user
 */
@Service
public class AgentInfoService extends BaseService<AgentInfo>{
    @Autowired
    private AgentInfoDao agentInfoDao;
    //获取所有机器名称列表
    public List getMachineList () {
        return agentInfoDao.getAllMachine();
    }
    public Object getMachineById(int id) {return agentInfoDao.getMachineById(id).get(0);}
    public int getAgentIdByName (String name) {
        return agentInfoDao.getMachineIdByName(name);
    }
}
