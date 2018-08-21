package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.ReplaceInfoService;
import com.gome.test.gtp.bo.SessionService;
import com.gome.test.gtp.bo.TaskInfoService;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.dao.TaskListDao;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.report.TestResultService;
import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangjiadi on 16/4/6.
 */
@Controller
@RequestMapping(value = "/gui-pageTest")
public class GuiOpenUrlController {

    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private ConfigureDictionaryService cdService;
    @Autowired
    private ReplaceInfoService replaceInfoService;
    @Autowired
    private TaskScheduleJob taskScheduleJob;
    @Autowired
    private TestResultService testResultService;
    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private TaskListDao taskListDao;
    @Autowired
    private Environment env;
    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "")
    ModelAndView goPageGUiPage() {
        ModelAndView mv = new ModelAndView("gui_openUrl");
        List list=cdService.getBrowsers();
        List result= new ArrayList<Object>();
        List enresult= new ArrayList<Object>();

        for(int i=0;i< list.size();i++)
        {
            Object[]  o = (Object[])list.get(i);
            String br =o[0].toString().toLowerCase().trim();
            if(br.startsWith("ie")|| br.startsWith("ed")|| br.startsWith("ip") )
                enresult.add(list.get(i));
            else
                result.add(list.get(i));
        }

        for(int i=0;i< enresult.size();i++)
        {
            result.add(enresult.get(i));
        }



        mv.addObject("browsers", result);
        return mv;
    }


    @RequestMapping(value = "/save")
    @ResponseBody
    Result createTaskSave(@RequestParam(value = "url") String url,
                          @RequestParam(value = "browser") String browser,
                          @RequestParam(value = "editor" ) String editor
                         ) {
        try {

            TaskInfo taskInfo = new TaskInfo();
            String [] browserList=browser.split(";");
            Integer browserNum=0;
            String taskName= Constant.taskName ;

            for(String br : browserList)
            {
                if(br.trim().length()>0) {
                    browserNum = browserNum + lcdService.getValueByName("Browser", br);
                    taskName += "_"+getSmallbrowser(br)  ;
                }

            }
            taskInfo.setBrowser(browserNum);

            int taskType =2 ;

            String env ="PRE";
            String caseQuery ="" ;
            boolean isMachine =false ;
            String machine ="" ;
            boolean isMonited =true ;
            boolean isSplit =false ;
            String emailList=sessionService.getSessionEmail();
            String loadConfName ="";
            taskInfo.setTaskName(taskName);
            taskInfo.setTaskType(taskType);
            taskInfo.setRunType(3);
            taskInfo.setRunRegularInfoID(13);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            taskInfo.setCreateTime(Timestamp.valueOf(df.format(new Date())));
            taskInfo.setStartDate(Timestamp.valueOf(df.format(new Date())));
            taskInfo.setEnv(lcdService.getValueByName("Environment", env));
            taskInfo.setBranch(taskInfoService.getBranchIdByName("gui_OpenURL"));
            taskInfo.setExcuteInfo(caseQuery);

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

            ReplaceInfo replaceInfo=new ReplaceInfo();
            replaceInfo.setFileName("gui.properties");
            replaceInfo.setReplaceValue(url);
            replaceInfo.setReplacekey("openUrl");
            replaceInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            replaceInfo.setTaskId(taskInfo.getTaskID());

            replaceInfoService.save(replaceInfo);

            //执行createTask
            taskScheduleJob.createTask(taskInfo.getTaskID(), Constant.ENQUEUE_BY_START);


            return new Result(false, "OK");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    private String getSmallbrowser(String browser)
    {
        browser= browser.trim().toLowerCase();
        if(browser.startsWith("ie"))
            browser= browser.substring(browser.length()-1);

        else  if(browser.startsWith("ipa"))
            browser = "pad_s";
        else
            browser = browser.substring(0,1);

        return browser.toUpperCase();

    }


    @RequestMapping(value = "/getHistory")
    @ResponseBody
    TableResult getAllTaskInfo() {
        try {
            List taskInfoList = taskInfoService.getGuiOpenUrlTaskList();
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


    @RequestMapping(value = "guiPictureReport/{reportID}")
    ModelAndView goPictureReport(@PathVariable("reportID") String reportID) {
        ModelAndView mv = new ModelAndView("gui_picture_report");
        List<TaskReport> taskReports = testResultService.getById(reportID);
        TaskReport taskReport = null;
        if (taskReports != null && taskReports.isEmpty() == false) {
            taskReport = taskReports.get(0);
            if (taskReport.getTaskName() == null) {
                TaskInfo taskInfo = taskInfoDao.get(taskReport.getTaskId());
                if (taskInfo != null) {
                    taskReport.setTaskName(taskInfo.getTaskName());
                }
            }
        }
        mv.addObject("taskReport", taskReport);
        String ip="";
        List<TaskList> taskLists = taskListDao.getTaskListBySplitTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(taskReport.getSplitTime()), taskReport.getTaskId());
        String pictureFtpUrl="";
        if(taskLists.size()>0) {
            pictureFtpUrl = String.format("ftp://%s/%s/%s/test-classes/screenCapture/", env.getProperty("gtp.gui.ftp.host"), env.getProperty("gtp.gui.ftp.subdir"), taskLists.get(0).getJobName());
            ip=taskLists.get(0).getAgentIP();
        }
        mv.addObject("pictureFtpUrl",pictureFtpUrl);
        mv.addObject("agentIP",ip);
        return mv;
    }
}
