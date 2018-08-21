/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.LoadConfService;
import com.gome.test.gtp.bo.TaskInfoService;
import com.gome.test.gtp.bo.TaskStatusService;
import com.gome.test.gtp.dao.AgentInfoDao;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.QueueDao;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.dao.mongodb.MongoDBDao;
import com.gome.test.gtp.jenkins.JenkinsBo;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.report.TestResultService;
import com.gome.test.gtp.schedule.TaskInfoListBo;
import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zonglin.Li
 */
@Controller
@RequestMapping(value = "/task")
public class TaskInfoController {

    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private ConfigureDictionaryService cdService;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private TestResultService testResultService;
    @Autowired
    private LoadConfService loadConfService;
    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private MongoDBDao  mongoDBDao;
    @Autowired
    private TaskScheduleJob taskScheduleJob;
    @Autowired
    private QueueDao queueDao;
    @Autowired
    private TaskStatusService taskStatusService;
    @Autowired
    private JenkinsBo jenkinsBo;

    @Autowired
    private TaskInfoListBo taskInfoListBo;

    @Autowired
    private AgentInfoDao agentInfoDao;

    //=========================================显示所有=========================================
    @RequestMapping("")
    ModelAndView goTaskPage() {
        ModelAndView mv = new ModelAndView("task");
        mv.addObject("browserTypes", cdService.getBrowsers());
        mv.addObject("envTypes", cdService.getEnvs());
        mv.addObject("taskStatus", cdService.getTaskStatus());
        mv.addObject("taskTypes", cdService.getTaskTypes());
        mv.addObject("groups", cdService.getGroups());
        mv.addObject("params","");
        return mv;
    }

    @RequestMapping("/search/{params}")
    ModelAndView goBranchPage2( @PathVariable(value = "params") String params){
        ModelAndView mv = new ModelAndView("task");
        mv.addObject("browserTypes", cdService.getBrowsers());
        mv.addObject("envTypes", cdService.getEnvs());
        mv.addObject("taskStatus", cdService.getTaskStatus());
        mv.addObject("taskTypes", cdService.getTaskTypes());
        mv.addObject("groups", cdService.getGroups());
        mv.addObject("params",params);
        return mv;
    }


    @RequestMapping(value = "/all")
    @ResponseBody
    TableResult getAllTaskInfo() {
        try {
            List taskInfoList = taskInfoService.getTaskList();
            if (taskInfoList != null) {
                return new TableResult(false, 1, taskInfoList.size(), taskInfoList.size(), taskInfoList.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new TableResult(true, ex.toString());
        }
    }

    //=========================================新建任务=========================================

    @RequestMapping(value = "/new/{type}")
    ModelAndView createTask(@PathVariable("type") int type) {//未严格按类型获取，获取的候选数据有冗余
        ModelAndView mv = new ModelAndView();
        mv.addObject("runTypes", cdService.getTaskRunTypes());
        mv.addObject("regularInfos", taskInfoService.getRegularList());
        mv.addObject("branches", taskInfoService.getBranchListByTaskType(type));
        mv.addObject("machines", taskInfoService.getAgentNameBrowserListByType(lcdService.getNameByValue(Constant.DIC_TASK_TYPE, type)));
        mv.addObject("envs", cdService.getEnvs());
        mv.addObject("browsers", cdService.getSortedListByParentName(Constant.BROWSER));
        mv.addObject("loadConfNames", loadConfService.getLoadConfNameList());
        mv.setViewName("task_create_" + lcdService.getNameByValue(Constant.DIC_TASK_TYPE, type).toLowerCase());
        return mv;
    }

    @RequestMapping(value = "/newSave")
    @ResponseBody
    Result createTaskSave(@RequestParam(value = "taskName") String taskName,
                          @RequestParam(value = "taskType") int taskType,//
                          @RequestParam(value = "runType") String runType,//
                          @RequestParam(value = "regularInfo") String regularInfo,//
                          @RequestParam(value = "env") String env,//
                          @RequestParam(value = "branch") String branch,//
                          @RequestParam(value = "caseQuery") String caseQuery,
                          @RequestParam(value = "browser") String browser,//
                          @RequestParam(value = "isMachine") boolean isMachine,
                          @RequestParam(value = "machine") String machine,//
                          @RequestParam(value = "isMonited") boolean isMonited,
                          @RequestParam(value = "isSplit") boolean isSplit,
                          @RequestParam(value = "editor") String editor,
                          @RequestParam(value = "emailList") String emailList,
                          @RequestParam(value = "loadConfName") String loadConfName) {
        try {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskName(taskName);
            taskInfo.setTaskType(taskType);
            taskInfo.setRunType(lcdService.getValueByName("TaskRunType", runType));
            taskInfo.setRunRegularInfoID(taskInfoService.getRegularIdByName(regularInfo));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            taskInfo.setCreateTime(Timestamp.valueOf(df.format(new Date())));
            taskInfo.setStartDate(Timestamp.valueOf(df.format(new Date())));
            taskInfo.setEnv(lcdService.getValueByName("Environment", env));
            taskInfo.setBranch(taskInfoService.getBranchIdByName(branch));
            taskInfo.setExcuteInfo(caseQuery);
            taskInfo.setBrowser(cdService.calculateBrowserValue(browser));
            if (isMachine) {
                taskInfo.setAgent(taskInfoService.getAgentId(machine));
            } else {
                taskInfo.setAgent(0);
            }
            taskInfo.setMonitored(isMonited);
            taskInfo.setSplit(isSplit);
            taskInfo.setEmailList(emailList);
            taskInfo.setCreator(editor);
            taskInfo.setLastEditor(editor);
            taskInfo.setTaskStatus(Constant.JOB_CREATED);
            taskInfo.setExcuteType(lcdService.getValueByName(Constant.EXCUTE_TYPE, "categroy"));
            taskInfo.setLoadConfName(loadConfName);
            taskInfoService.save(taskInfo);
            return new Result(false, "OK");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    //=========================================编辑任务=========================================

    @ResponseBody
    @RequestMapping(value = "/browserlist/{taskId}")
    List<String> getBrowsers(@PathVariable(value = "taskId") int taskId) {
        return taskInfoListBo.getBrowserListByTaskId(taskId);
    }

    @RequestMapping(value = "/edit/{id}")
    ModelAndView getTaskEdit(@PathVariable(value = "id") int id) {
        ModelAndView mv = new ModelAndView();

        Object taskInfo = taskInfoService.getTaskInfoById(id);
        int taskType = Integer.valueOf(((Object[]) taskInfo)[18].toString());

//        mv.addObject("browsers", cdService.getBrowsers());
        mv.addObject("envs", cdService.getEnvs());
        mv.addObject("taskTypes", cdService.getTaskTypes());
        mv.addObject("runTypes", cdService.getTaskRunTypes());
        mv.addObject("regularInfos", taskInfoService.getRegularList());
        mv.addObject("branches", taskInfoService.getBranchListByTaskType(taskType));
        mv.addObject("machines", taskInfoService.getAgentNameBrowserListByType(((Object[]) taskInfo)[9].toString().toUpperCase()));
        mv.addObject("loadConfNames", loadConfService.getLoadConfNameList());

        mv.addObject("branchName", taskInfoService.getBranchNameById(id));
        mv.addObject("regularName", taskInfoService.getRegularNameByTaskId(id));
        mv.addObject("taskInfo", taskInfo);

        mv.addObject("browsers", cdService.getSortedListByParentName(Constant.BROWSER));
        mv.addObject("selectedBrowsers", taskInfoListBo.getBrowserListByTaskId(id));

        mv.setViewName("task_edit_" + ((Object[]) taskInfo)[9].toString().toLowerCase());
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/save/checkbrowser")
    Result checkBrowser(@RequestParam(value = "machine") String machine,
                        @RequestParam(value = "browsers") String browsers) {
        AgentInfo agentInfo = agentInfoDao.getAgentByLabel(machine).get(0);
        if (taskInfoService.checkAgentBrowser(agentInfo.getBrower(), browsers)) {
            return new Result(false, "true", true);
        } else {
            return new Result(false, "false", false);
        }
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    Result updateTaskInfo(
            @PathVariable(value = "id") int id,
            @RequestParam(value = "taskName") String taskName,
            @RequestParam(value = "taskType") int taskType,//
            @RequestParam(value = "runType") String runType,//
            @RequestParam(value = "regularInfo") String regularInfo,//
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "env") String env,//
            @RequestParam(value = "branch") String branch,//
            @RequestParam(value = "caseQuery") String caseQuery,
            @RequestParam(value = "browser") String browsers,//
            @RequestParam(value = "isMachine") boolean isMachine,
            @RequestParam(value = "machine") String machine,//
            @RequestParam(value = "isMonited") boolean isMonited,
            @RequestParam(value = "isSplit") boolean isSplit,
            @RequestParam(value = "editor") String editor,
            @RequestParam(value = "emailList") String emailList,
            @RequestParam(value = "loadConfName") String loadConfName) {
        try {
            TaskInfo taskInfo = taskInfoService.get(id);
            taskInfo.setTaskName(taskName);
            taskInfo.setTaskType(taskType);
            taskInfo.setRunType(cdService.getConfigIdByTypeName("TaskRunType", runType));
            taskInfo.setRunRegularInfoID(taskInfoService.getRegularIdByName(regularInfo));
            taskInfo.setEnv(cdService.getConfigIdByTypeName("Environment", env));
            taskInfo.setBranch(taskInfoService.getBranchIdByName(branch));
            taskInfo.setExcuteInfo(caseQuery);
            taskInfo.setBrowser(cdService.calculateBrowserValue(browsers));
            if (isMachine) {
                taskInfo.setAgent(taskInfoService.getAgentId(machine));
            } else {
                taskInfo.setAgent(0);
            }
            taskInfo.setMonitored(isMonited);
            taskInfo.setSplit(isSplit);
            taskInfo.setLastEditor(editor);
            taskInfo.setLastEditorTime(Util.nowTimestamp());
            taskInfo.setEmailList(emailList);
            taskInfo.setLoadConfName(loadConfName);
            return new Result(false, "OK", taskInfoService.update(taskInfo));
        } catch (Exception e) {
            String ex = e.toString();
            return new Result(true, ex);
        }
    }

    //=========================================删除任务=========================================

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    Result deleteById(@PathVariable(value = "id") int id) {
        try {
            taskInfoService.deleteById(id);
            return new Result(false, "OK");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }

    }

    //=========================================任务详情=========================================

    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    @ResponseBody
    Result getTaskDetails(@PathVariable(value = "id") int id) {
        try {
            return new Result(false, String.valueOf(id), taskInfoService.getTaskDetailsById(id));
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    //=========================================任务报告=========================================

    @RequestMapping(value = "/report/{id}/{tasktype}")
    ModelAndView goTaskReport(@PathVariable(value = "id") int id,
                              @PathVariable(value = "tasktype") String tasktype) {
        ModelAndView mv = new ModelAndView("task_report");
        mv.addObject("taskId", id);
        mv.addObject("tasktype", tasktype);
        return mv;
    }

    @RequestMapping(value = "/report/jenkinsaddr")
    Result getJenkinsAddr() {
        return new Result(false, "Jenkins Ip Port", jenkinsBo.getJenkinsIpPort());
    }

    @RequestMapping(value = "/report/list/{id}")
    @ResponseBody
    Result getTaskReportList(@PathVariable(value = "id") int id) {
        try {
            ModelAndView mv = new ModelAndView("testResultDetail");
            List<TaskReport> taskReports = testResultService.getByTaskId(id);
            TaskInfo taskInfo = taskInfoDao.get(id);

            if (taskReports != null && taskReports.isEmpty() == false) {
                for (TaskReport report : taskReports) {
                    if (report.getTaskName() == null) {
                        if (taskInfo != null)
                            report.setTaskName(taskInfo.getTaskName());
                    }
                }
            }

            return new Result(false, "OK", taskReports);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/report/loadlist/{id}")
    @ResponseBody
    Result getTaskReportloadList(@PathVariable(value = "id") int id) {
        try {
            List<TaskLoadReport> taskReports =  mongoDBDao.getLoadReportList(id);

            TaskInfo taskInfo = taskInfoDao.get(id);

            if (taskReports != null && taskReports.isEmpty() == false) {
                for (TaskLoadReport report : taskReports) {
                    if (report.getTaskName() == null) {
                        if (taskInfo != null)
                            report.setTaskName(taskInfo.getTaskName());
                    }
                }
            }

            return new Result(false, "OK", taskReports);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/report/jenkins_report/{taskId}")
    ModelAndView goJenkinsReport(@PathVariable(value = "taskId") int taskId) {
        ModelAndView mv = new ModelAndView("jenkins-report");
        mv.addObject("taskId", taskId);
        mv.addObject("jenkinsAddr", jenkinsBo.getJenkinsIpPort());
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/report/jenkins_report/list/{taskId}")
    Result getJenkinsReport(@PathVariable(value = "taskId") int taskId) {
        return new Result(false, "jenkins report", taskInfoService.getJenkinsReportByTaskId(taskId));
    }

    //=========================================复制任务=========================================

    @RequestMapping(value = "/copy/{id}")
    ModelAndView returnTaskCopy(@PathVariable(value = "id") int id) throws Exception {
        ModelAndView mv = new ModelAndView("task");
        taskInfoService.copyTaskById(id,((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        return mv;
    }

    //=========================================手动启停任务=========================================

    @RequestMapping(value = "/start/{id}")
    @ResponseBody
    Result startTaskById(
            @PathVariable(value = "id") Integer taskId
    ) {
        try {
            TaskInfo taskInfo = taskInfoDao.get(taskId);
            if(Constant.END_STATUS_SET.contains(taskInfo.getTaskStatus())) {
                taskScheduleJob.createTask(taskId, Constant.ENQUEUE_BY_START);
            } else {
                throw new Exception(" 任务正在执行或已加入队列，不允许重复执行，请等待... ");
            }
            return new Result(false, "Task " + taskId + " added to Queue !");
        } catch (Exception e) {
            return new Result(true, e.getMessage());
        }

    }

    @RequestMapping(value = "/startTest/{id}")
    @ResponseBody
    Result testTaskById(
            @PathVariable(value = "id") Integer taskId
    ) {
        try {
            TaskInfo taskInfo = taskInfoDao.get(taskId);
            if(Constant.END_STATUS_SET.contains(taskInfo.getTaskStatus())) {
                taskScheduleJob.createTask(taskId, Constant.ENQUEUE_BY_Test);
            } else {
                throw new Exception(" 测试任务正在执行或已加入队列，不允许重复执行，请等待... ");
            }
            return new Result(false, "Task " + taskId + " added to Queue !");
        } catch (Exception e) {
            return new Result(true, e.getMessage());
        }

    }

    @RequestMapping(value = "/stop/{id}")
    @ResponseBody
    Result stopTaskById(
            @PathVariable(value = "id") Integer taskId
    ) {
        boolean allStopped = false;
        try {
            allStopped = taskScheduleJob.stopJobByTaskId(taskId);
            return new Result(false, String.format("Stop Task: %s!", String.valueOf(allStopped)));
        } catch (Exception e) {
            return new Result(true, String.format("Stop Task: %s! %s", String.valueOf(allStopped), e.toString()));
        }
    }

    //=========================================任务状态刷新=========================================

    @RequestMapping(value = "/status")
    @ResponseBody
    Map getTaskInfoIdStatus() {
        return taskStatusService.getIdStatusMap();
    }
}
