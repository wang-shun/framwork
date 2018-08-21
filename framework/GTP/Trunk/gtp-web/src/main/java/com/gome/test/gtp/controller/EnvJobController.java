/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.EnvJobsService;
import com.gome.test.gtp.bo.ObjectService;
import com.gome.test.gtp.bo.ToUtfCoding;
import com.gome.test.gtp.model.ScheduleJob;
import com.gome.test.gtp.model.TableResult;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Dangdang.Cao
 */
@Controller
@RequestMapping(value = "/envJobs")
public class EnvJobController {

    @Autowired
    private EnvJobsService evnJobService;
    @Autowired
    private ToUtfCoding toUtfCoding;

    @Autowired
    private ObjectService objService;

    @RequestMapping(value = "")
    ModelAndView goEnvJobsPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("envJobs");
        return mv;
    }

    @RequestMapping(value = "/allInfo")
    @ResponseBody
    TableResult getAllJobs() throws ParseException {
        try {
            List<ScheduleJob> allJobs = evnJobService.getAll();
            if (allJobs != null) {
                return new TableResult(false, 1, allJobs.size(), allJobs.size(), allJobs.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception ex) {
            return new TableResult(true, ex.toString());
        }
    }

    @RequestMapping(value = "/edit/{id}")
    ModelAndView envJobsEdit(@PathVariable("id") int id) {
        ModelAndView mv = new ModelAndView();
        ScheduleJob job = evnJobService.get(id);
        Date now = new Date();
        now = job.getScheduleTime();
        mv.setViewName("envJobs_edit");
        mv.addObject("job", job);
        return mv;
    }

    @RequestMapping(value = "/save/{id}")
    ModelAndView envEditSave(@PathVariable("id") int id) throws ParseException {
        ModelAndView mv = new ModelAndView();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ScheduleJob job = evnJobService.get(id);
        job.setScheduleWeek(request.getParameter("scheduleWeek"));
        job.setCMD(toUtfCoding.toUtfCoding(request.getParameter("CMD")));
        job.setEnable(Boolean.parseBoolean(request.getParameter("enable")));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp createtime = Timestamp.valueOf(df.format(new Date()));
        job.setModifyTime(createtime);

        String time = request.getParameter("scheduleTime");
        String modifyTime = Util.timestamp2String(createtime);
        String date = modifyTime.split(" ")[0];
        String ScheTime = date + " " +time + ":00";
        Timestamp scheduleTime = Util.StringToTimestamp(ScheTime);
        job.setScheduleTime(scheduleTime);
        evnJobService.update(job);
        mv.setViewName("envJobs");
        return mv;

    }

    @RequestMapping(value = "/detail/{id}")
    ModelAndView envJobDetail(@PathVariable("id") int id) {
        ModelAndView mv = new ModelAndView();
        ScheduleJob job = evnJobService.get(id);
        mv.addObject("job", job);
        mv.setViewName("envJobs_detail");
        return mv;
    }

}
