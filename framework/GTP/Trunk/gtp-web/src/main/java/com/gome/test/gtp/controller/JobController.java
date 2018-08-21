/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.JobService;
import com.gome.test.gtp.model.Result;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Zonglin.Li
 */
@Controller
public class JobController {
    @Autowired JobService jobService;
    @RequestMapping(value = "/list/jobs", method = RequestMethod.GET)
    @ResponseBody
    public Result envJobRetrive(){
        try{
            List list = jobService.getAllJobs();
            return new Result(false, "success", list);
        }catch(Exception ex){
            return new Result(true, "", ex.getMessage());
        }     
    }
}
