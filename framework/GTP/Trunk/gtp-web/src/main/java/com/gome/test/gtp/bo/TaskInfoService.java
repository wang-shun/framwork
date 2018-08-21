/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.*;
import com.gome.test.gtp.model.AgentInfo;
import com.gome.test.gtp.model.TaskInfo;
import com.gome.test.gtp.schedule.TaskInfoListBo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author user
 */
@Service
public class TaskInfoService extends BaseService<TaskInfo> {
    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private AgentInfoDao agentInfoDao;
    @Autowired
    private RunRegularInfoDao runRegularInfoDao;
    @Autowired
    private LoadConfDao loadConfDao;
    @Autowired
    private TaskListDao taskListDao;
    @Autowired
    private ConfigureDictionaryService cdService;
    @Autowired
    private ObjectService objectService;
    @Autowired
    private LoadConfigureDictionaryService loadConfigureDictionaryService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private TaskInfoListBo taskInfoListBo;

    public List getTaskList() {
        List taskList = taskInfoDao.getTaskList();
        if (taskList == null || taskList.isEmpty()) {
            return taskList;
        } else {
            Map<Integer, String> replaceRule = new HashMap<Integer, String>();
            replaceRule.put(2, Constant.ENV);
            replaceRule.put(3, Constant.GROUP);
//            replaceRule.put(4, Constant.BROWSER);
            replaceRule.put(6, Constant.REGULAR_START_TYPE);
            replaceRule.put(10, Constant.DIC_TASK_TYPE);
            replaceRule.put(11, Constant.TASK_STATUS);
            replaceRule.put(12, Constant.RUN_TYPE);
            loadConfigureDictionaryService.replaceValue(taskList, replaceRule);
        }
        return taskList;
    }

    public List getGuiOpenUrlTaskList()
    {
        List taskList = taskInfoDao.getGuiOpenUrlTaskList();
        if (taskList == null || taskList.isEmpty()) {
            return taskList;
        } else {
            Map<Integer, String> replaceRule = new HashMap<Integer, String>();
            replaceRule.put(2, Constant.ENV);
            replaceRule.put(3, Constant.GROUP);
//            replaceRule.put(4, Constant.BROWSER);
            replaceRule.put(6, Constant.REGULAR_START_TYPE);
            replaceRule.put(10, Constant.DIC_TASK_TYPE);
            replaceRule.put(11, Constant.TASK_STATUS);
            replaceRule.put(12, Constant.RUN_TYPE);
            loadConfigureDictionaryService.replaceValue(taskList, replaceRule);
        }
        return taskList;
    }



    public List<TaskInfo> getAllTask() {
        return taskInfoDao.getAll();
    }

    public Object getTaskInfoById(int id) {
        return taskInfoDao.getTaskInfoById(id).get(0);
    }

    public Object getTaskDetailsById(int id) {
        return taskInfoDao.getTaskDetailsById(id).get(0);
    }

    public List getRegularList() {
        return runRegularInfoDao.getRegularList();
    }

    public int getRegularIdById(int id) {
        return (Integer) taskInfoDao.getProById(id, "").get(0);
    }

    public String getRegularNameByTaskId(int id) {
        return taskInfoDao.getRegularNameByTaskId(id);
    }

    public String getBranchNameById(int id) {
        return taskInfoDao.getBranchNameById(id);
    }

    public int getBranchIdByName(String name) {
        return taskInfoDao.getBranchIdByName(name);
    }

    public int getRegularIdByName(String name) {
        if ("None".equals(name)) {
            return 0;
        } else {
            return taskInfoDao.getRegularIdByName(name);
        }
    }

//    public List getBranchList() {
//        return taskInfoDao.getBranchList();
//    }

    public List getBranchListByTaskType(int taskType) {
        return taskInfoDao.getBranchListByTaskType(taskType);
    }


    public List getMachineList() {
        return agentInfoDao.getMachineList();
    }

    public List getMachineListByType(String type) {
        return agentInfoDao.getMachineListByType(type);
    }

    public List getAgentNameBrowserListByType(String type) {
        List<Object> agentNameBrowserList = agentInfoDao.getAgentNameBrowserListByType(type);
        List<List<String>> agentBrowserList = new ArrayList<List<String>>();
        for (Object nameBrowser : agentNameBrowserList) {
            List<String> agentBrowser = new ArrayList<String>();
            agentBrowser.add(((Object[])nameBrowser)[0].toString());
            int browserValue = Integer.valueOf(((Object[])nameBrowser)[1].toString());
            List<String> browserList = taskInfoListBo.getBrowserListByValue(browserValue);
            agentBrowser.add(Util.join(browserList));
            agentBrowserList.add(agentBrowser);
        }
        return agentBrowserList;
    }

//    public List getMachineListById(int id) {
//        TaskInfo taskInfo = taskInfoDao.get(id);
//        if (taskInfo.getTaskType() == Constant.TASK_TYPE_GUI) {
//
//        } else {
//            return getMachineListByType(loadConfigureDictionaryService.getNameByValue(Constant.DIC_TASK_TYPE, taskInfo.getTaskType()));
//        }
//    }

    public List getMachineListByTypeBrowser(int type, String browsers) {
        List<AgentInfo> agentInfoList = agentInfoDao.getMachineListByType(loadConfigureDictionaryService.getNameByValue(Constant.DIC_TASK_TYPE, type));
        for (int i = agentInfoList.size(); i >=0; i--) {
            if (!checkAgentBrowser(agentInfoList.get(i).getBrower(), browsers)) {
                agentInfoList.remove(i);
            }
        }
        return agentInfoList;
    }

    public boolean checkAgentBrowser(int agentBrowser, String browsers) {
        List<String> agentBrowserList = taskInfoListBo.getBrowserListByValue(agentBrowser);
        List<String> browserList = Arrays.asList(browsers.split(","));
        return agentBrowserList.containsAll(browserList);
     }

    public int getAgentId(String machine) {
        if ("None".equals(machine)) {
            return 0;
        } else {
            return agentInfoDao.getMachineIdByName(machine);
        }
    }

    public List getLoadConfList() {
        return loadConfDao.getLoadConfNameList();
    }

    public void deleteById(int id) {
        taskInfoDao.executeSql(String.format("update TaskInfo set TaskStatus=%d where TaskID=%d",
                Constant.JOB_DELETED,
                id));
    }

    public List getTaskReportList(int id) {
        return taskInfoDao.getTaskReportById(id);
    }

    public void copyTaskById(int id, HttpServletRequest request) throws Exception {
        sessionService.setRequest(request);
        TaskInfo srcTaskInfo = taskInfoDao.get(id);
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setAgent(srcTaskInfo.getAgent());
        taskInfo.setBranch(srcTaskInfo.getBranch());
        taskInfo.setBrowser(srcTaskInfo.getBrowser());
        taskInfo.setCreator(sessionService.getSessionUsername());
        taskInfo.setCreator(srcTaskInfo.getCreator());
        taskInfo.setEmailList(sessionService.getSessionEmail());
        taskInfo.setEnv(srcTaskInfo.getEnv());
        taskInfo.setExcuteInfo(srcTaskInfo.getExcuteInfo());
        taskInfo.setExcuteType(srcTaskInfo.getExcuteType());
        taskInfo.setLastEditor(sessionService.getSessionUsername());
        taskInfo.setMonitored(srcTaskInfo.getMonitored());
        taskInfo.setRunRegularInfoID(srcTaskInfo.getRunRegularInfoID());
        taskInfo.setRunType(srcTaskInfo.getRunType());
        taskInfo.setSplit(srcTaskInfo.getSplit());
        taskInfo.setTaskStatus(Constant.JOB_CREATED);
        taskInfo.setTaskType(srcTaskInfo.getTaskType());
        taskInfo.setTaskName(String.format("Copy%d_%s", System.currentTimeMillis(), srcTaskInfo.getTaskName()));
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        taskInfo.setCreateTime(ts);
        taskInfo.setLastEditorTime(ts);
        taskInfo.setStartDate(ts);
        taskInfo.setLastRunTime(Constant.UNDEFINED_TIMESTAMP);
        taskInfoDao.save(taskInfo);
    }

    public Map<Integer, Object[]> getTaskInfoIdStatus() {
        Map<Integer, Object[]> idStatusLastTimeMap = new HashMap<Integer, Object[]>();
        List idStatusList = taskInfoDao.getTaskIdStatusLastTime();//taskId taskStatus lastRunTime agentStatus agentTaskId
        Map<Integer, String> replaceRule = new HashMap<Integer, String>();
        replaceRule.put(1, Constant.TASK_STATUS);
        loadConfigureDictionaryService.replaceValue(idStatusList, replaceRule);
        for(int i = 0; i < idStatusList.size(); i++) {
            Object[] idStatus = (Object[])idStatusList.get(i);
            idStatusLastTimeMap.put(Integer.valueOf(idStatus[0].toString()),idStatus);
        }

        return idStatusLastTimeMap;
    }

    public List getBranchListbyGroupId(int groupId)
    {
        return taskInfoDao.getBranchListByGroupId(groupId);
    }

//    public List getBranchListbyParams(String params)
//    {
//        List reslut=null;
//        if (params.length()>0 && params.split("[_]").length> 1) {
//            String groupName=params.split("[_]")[1];
//            int groupId=0;
//            List<Object[]> groupList=cdService.getGroups();
//            for(int i=0;i<groupList.size();i++)
//            {
//                if(groupName.equals(groupList.get(i)[0].toString()))
//                {
//                    groupId=Integer.valueOf(groupList.get(i)[1].toString());
//                    break;
//                }
//            }
//
//            reslut=getBranchListbyGroupId(groupId);
//        } else {
//            reslut=getBranchList();
//        }
//        return reslut;
//    }

    public List getJenkinsReportByTaskId(int taskId) {
        return taskListDao.getJenkinsReportByTaskId(taskId);
    }
}
