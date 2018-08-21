/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.ScheduleJobDao;
import com.gome.test.gtp.model.ScheduleJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dangdang.Cao
 */
@Service
public class EnvJobsService extends BaseService<ScheduleJob>{
     @Autowired
     private ScheduleJobDao scheduleJobDao;

     
}
