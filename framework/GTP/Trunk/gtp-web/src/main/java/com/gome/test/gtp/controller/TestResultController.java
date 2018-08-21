/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.*;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.report.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Dangdang.Cao
 */
@Controller
@RequestMapping(value = "/testResult")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;
    @Autowired
    private ObjectService objService;
    @Autowired
    private CaseResultService caseResultService;
    @Autowired
    private EnvJobsService envjobService;
    @Autowired
    private TaskInfoDao taskInfoDao;

    public static int repId;

    @RequestMapping("")
    public ModelAndView goReportPage() {
        ModelAndView mv = new ModelAndView("testResult");
        return mv;
    }

    @RequestMapping(value = "/allInfo")
    @ResponseBody
    TableResult getAllTestResult() {
        try {
            List result = testResultService.getTestList();
            return new TableResult(false, 1, result.size(), result.size(), result.toArray());
        } catch (Exception ex) {
            return new TableResult(true, ex.toString());
        }
    }

    @RequestMapping(value = "/detail/{reportID}")
    ModelAndView TestReportDetail(@PathVariable("reportID") String reportID) {
        ModelAndView mv = new ModelAndView("testResultDetail");
        List<TaskReport> taskReports = testResultService.getById(reportID);
        TaskReport taskReport = null;
        if (taskReports != null && taskReports.isEmpty() == false) {
            taskReport = taskReports.get(0);
            if (taskReport.getTaskName() == null) {
                TaskInfo taskInfo = taskInfoDao.get(taskReport.getTaskId());
                if (taskInfo != null)
                    taskReport.setTaskName(taskInfo.getTaskName());
            }
        }

        mv.addObject("taskReport", taskReport);
        return mv;
    }

    @Autowired
    private Environment env;

    @RequestMapping(value = "/loaddetail/{reportID}")
    ModelAndView TestReportLoadDetail(@PathVariable("reportID") String reportID) {
        ModelAndView mv = new ModelAndView("testResultLoadDetail");
        TaskLoadReport taskReport = testResultService.getLoadLogInfoById(reportID);

        if (taskReport != null ) {
            TaskInfo taskInfo = taskInfoDao.get(taskReport.getTaskId());
            if (taskInfo != null)
                taskReport.setTaskName(taskInfo.getTaskName());
        }
        taskReport.setMojoLogUrl(env.getProperty("gtp.load.ftp.url")+taskReport.getMojoLogUrl());
        mv.addObject("taskReport", taskReport);
        return mv;
    }

    @RequestMapping(value = "/detailInfo")
    @ResponseBody
    TableResult DetailInfo() {
        Integer reportID = getRepId();
        try {
            List reportDetail = objService.findCaseReportAll(reportID);
            if (reportDetail != null) {
                return new TableResult(false, 1, reportDetail.size(), reportDetail.size(), reportDetail.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception ex) {
            return new TableResult(true, ex.toString());
        }
    }

    @RequestMapping(value = "/report/{reportId}")
    @ResponseBody
    void GenerateExcel(@PathVariable("reportId") int reportId, HttpServletResponse response) {
        ExcelModel excel = new ExcelModel();
        excel.setSheetName(String.valueOf(reportId));
        List<CaseResult> results = caseResultService.getResultById(reportId);   //getResultById(reportId)
        try {
            AgentInfoExcelDownLoad down = new AgentInfoExcelDownLoad();
            ExcelModel model = down.createDownLoadExcel(results, excel);
            String fileName = "testReport" + String.valueOf(reportId) + ".xls";
            down.downLoad(fileName, model, response);
        } catch (Exception e) {
        }
    }

    /**
     * @return the repId
     */
    public Integer getRepId() {
        return repId;
    }

    /**
     * @param repId the repId to set
     */
    public void setRepId(Integer repId) {
        this.repId = repId;
    }

}
