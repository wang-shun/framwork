/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.Hosts;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 * @author Zonglin.Li
 */
@Repository
@Transactional
public class HostsDao extends BaseDao<Hosts>{
    public Object[] getHostById (int id) {
        String sql = String.format("select * from Hosts where id = %d", id);
        List hosts = sqlQuery(sql);
        if (!hosts.isEmpty()) {
            return (Object[])hosts.get(0);
        } else {
            return null;
        }
    }
}
