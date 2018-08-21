/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.ScheduleJob;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author shan.tan
 */
@Repository
public class ScheduleJobDao extends BaseDao<ScheduleJob> {

    public List getJobs() {
        String sql = "select id,JobName,CMD,ModifyTime,ModifyUser,scheduleWeek,CONVERT(CHAR(8), scheduleTime, 24) as scheduleTime,enable from ScheduleJob";
        return sqlQuery(sql);
    }

}
