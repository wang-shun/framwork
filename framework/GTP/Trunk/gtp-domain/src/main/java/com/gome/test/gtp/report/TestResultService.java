/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.report;

import com.gome.test.gtp.dao.mongodb.MongoDBDao;
import com.gome.test.gtp.model.CaseResult;
import com.gome.test.gtp.model.TaskLoadReport;
import com.gome.test.gtp.model.TaskReport;
import com.gome.test.gtp.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestResultService {

    @Autowired
    private MongoDBDao mongoDBDao;

    public List getTestList() {
        return mongoDBDao.getAll(TaskReport.class);
    }


    public TaskLoadReport getLoadLogInfoById(String id){
        TaskLoadReport report=new TaskLoadReport();
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").is(id);
        query.addCriteria(criteria);
        List<TaskLoadReport>  taskLoadReportList = mongoDBDao.getByCondition(TaskLoadReport.class, query);

        if(taskLoadReportList!=null && taskLoadReportList.size()>0)
            report=taskLoadReportList.get(0);



        return report;

    }

    public List<TaskReport> getById(String id) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").is(id);
        query.addCriteria(criteria);
        List<TaskReport> taskReportList = mongoDBDao.getByCondition(TaskReport.class, query);
        if (taskReportList != null && taskReportList.isEmpty() == false) {
            taskReportList.get(0).setDetails(mergeCaseResult(taskReportList.get(0)));
        }
        return taskReportList;
    }

    public CaseResult[] mergeCaseResult(TaskReport taskReport) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("taskId").is(taskReport.getTaskId());
        criteria.and("splitTime").is(taskReport.getSplitTime());
        query.addCriteria(criteria);
        List<TaskReport> taskReportList = mongoDBDao.getByCondition(TaskReport.class, query);
        int length = 0;
        if (taskReportList != null && taskReportList.isEmpty() == false) {
            for (TaskReport report : taskReportList) {
                length = length + report.getDetails().length;
            }
        }
        CaseResult[] caseResults = new CaseResult[length];
        if(taskReportList != null && taskReportList.isEmpty() == false) {
            caseResults = new CaseResult[length];
            int index = 0;
            for(TaskReport report : taskReportList) {
                for(CaseResult caseResult : report.getDetails())
                    caseResults[index++] = caseResult;
            }
        }
        return caseResults;
    }

    public List<TaskReport> getByTaskId(int taskId) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("taskId").is(taskId);
        query.addCriteria(criteria).limit(Constant.API_REPORT_LIMIT);

        Criteria cols = new Criteria();

        cols.and("_id").is(1);
        cols.and("taskId").is(1);
        cols.and("generateTime").is(1);
        cols.and("taskType").is(1);
        cols.and("endTime").is(1);
        cols.and("date").is(1);
        cols.and("pass").is(1);
        cols.and("startTime").is(1);
        cols.and("duration").is(1);
        cols.and("totalCases").is(1);
        cols.and("splitTime").is(1);
        cols.and("resultFilePath").is(1);

        List<TaskReport> taskReportList = mongoDBDao.getByCondition(TaskReport.class, query);

        Map<Long, TaskReport> splitTimeTaskReportMap = new HashMap<Long, TaskReport>();
        for(TaskReport taskReport : taskReportList) {
            if(splitTimeTaskReportMap.containsKey(taskReport.getSplitTime())) {
                splitTimeTaskReportMap.put(taskReport.getSplitTime(),mergeTaskReport(splitTimeTaskReportMap.get(taskReport.getSplitTime()),taskReport));
            } else {
                splitTimeTaskReportMap.put(taskReport.getSplitTime(), taskReport);
            }
        }

        return new ArrayList<TaskReport>(splitTimeTaskReportMap.values()) ;
    }


    public TaskLoadReport getLoadReportByTaskListId(int taskListId)
    {
        TaskLoadReport report=new TaskLoadReport();
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("taskListId").is(taskListId);
        query.addCriteria(criteria);
        List<TaskLoadReport> taskReportList =  mongoDBDao.getByCondition(TaskLoadReport.class, query);
        if(taskReportList.size()>0)
            report=taskReportList.get(0);
        return report;
    }

    public TaskReport getMergedReportByTaskIdSplitTime(int taskId, long splitTime) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("taskId").is(taskId);
        criteria.and("splitTime").is(splitTime);
        query.addCriteria(criteria);
        List<TaskReport> taskReportList = mongoDBDao.getByCondition(TaskReport.class, query);
        TaskReport taskReport = null;
        if (taskReportList.size() > 1) {
            taskReport = taskReportList.get(0);
            for (int i = 1; i < taskReportList.size(); i++) {
                taskReport = mergeTaskReport(taskReport, taskReportList.get(i));
            }
        } else if (taskReportList.size() == 1) {
            taskReport = taskReportList.get(0);
        }

        return taskReport;
    }

    //合并拆分的CaseResult Report,同一Task拆分出的所有Case为一份
    private TaskReport mergeTaskReport (TaskReport taskReport1, TaskReport taskReport2) {
        if(taskReport1.getSplitTime() == taskReport2.getSplitTime() && taskReport1.getTaskId() == taskReport2.getTaskId()) {

            taskReport1.setTotalCases(taskReport1.getTotalCases() + taskReport2.getTotalCases());
            taskReport1.setPass(taskReport1.getPass() + taskReport2.getPass());
            taskReport1.setFail(taskReport1.getFail() + taskReport2.getFail());
            taskReport1.setAborted(taskReport1.getAborted() + taskReport2.getAborted());
            
            if(taskReport1.getStartTime() > taskReport2.getStartTime()) {
                taskReport1.setStartTime(taskReport2.getStartTime());
            }

            if(taskReport1.getEndTime() < taskReport2.getEndTime()) {
                taskReport1.setEndTime(taskReport2.getEndTime());
            }

            taskReport1.setDuration(taskReport1.getEndTime() - taskReport1.getStartTime());
        }

        return taskReport1;
    }
}
