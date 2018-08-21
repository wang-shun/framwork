package com.gome.test.gtp.schedule;

import com.gome.test.gtp.dao.AgentInfoDao;
import com.gome.test.gtp.dao.ConfigureDictionaryDao;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.dao.TaskListDao;
import com.gome.test.gtp.jenkins.JenkinsJobBo;
import com.gome.test.gtp.jenkins.SynInfo;
import com.gome.test.gtp.jenkins.TaskStatusBo;
import com.gome.test.gtp.model.AgentInfo;
import com.gome.test.gtp.model.TaskInfo;
import com.gome.test.gtp.model.TaskList;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by lizonglin on 2015/8/4/0004.
 */
@Component
public class TaskInfoListBo {
    @Autowired
    private TaskListDao taskListDao;

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private TaskStatusBo taskStatusBo;

    @Autowired
    private AgentInfoDao agentInfoDao;

    @Autowired
    private JenkinsJobBo jenkinsJobBo;

    @Autowired
    private Environment env;

    @Autowired
    private ConfigureDictionaryDao cdDao;

    @Transactional
    public void amendTaskInfoStatus() {
        /**
         * TaskInfo中状态为非结束状态的所有TaskInfo
         */
        List<TaskInfo> taskInfoList = taskInfoDao.getNonendTaskInfo();
        /**
         * TaskList中所有最后一次执行拆分后处于结束状态的Task的最终状态
         */
        Map<Integer, Integer> taskIdStatusMap = getTaskIdEndStatus();
        /**
         * 所有处于非运行状态的Agent
         */
        List<AgentInfo> runningAgentInfoList = agentInfoDao.getRunningAgentList();

        /**
         * 先Amend TaskInfo的状态
         */
        Map<Integer, Integer> amendMap = new HashMap<Integer, Integer>();
        int taskInfoId;
        int taskInfoStatus;
        if(taskInfoList != null && !taskInfoList.isEmpty() && !taskIdStatusMap.isEmpty() && taskIdStatusMap != null) {
            for (TaskInfo taskInfo : taskInfoList) {
                taskInfoId = taskInfo.getTaskID();
                taskInfoStatus = taskInfo.getTaskStatus();
                if (taskIdStatusMap.containsKey(taskInfoId) && taskInfoStatus != (taskIdStatusMap.get(taskInfoId))) {
                    amendMap.put(taskInfoId, taskIdStatusMap.get(taskInfoId));
                }
            }
        }

        if (!amendMap.isEmpty()) {
            taskInfoDao.amendTaskInfoStatus(amendMap);
        }

        /**
         * 再Amend Agent的状态
         */
        if(taskIdStatusMap != null && !taskIdStatusMap.isEmpty() &&
                runningAgentInfoList != null && !runningAgentInfoList.isEmpty()) {
            Set<Integer> endTaskIdSet = taskIdStatusMap.keySet();
            List<Integer> agentIdList = new ArrayList<Integer>();
            for (AgentInfo agentInfo : runningAgentInfoList) {
                if (endTaskIdSet.contains(agentInfo.getTaskId())) {
                    agentIdList.add(agentInfo.getAgentID());
                }
            }
            if (!agentIdList.isEmpty()) {
                agentInfoDao.amendAgentStatus((Integer[]) agentIdList.toArray());
            }
        }
    }

    private Map<Integer, Integer> getTaskIdEndStatus() {
        /**
         * 分隔时间最新的TaskList，可能有重复多余条目（in max(SplitTime) 产生）
         */
        List<TaskList> taskLists = taskListDao.getGroupedList();
        /**
         * 取得每个TaskID对应的最新SplitTime
         */
        Map<Integer, Timestamp> taskIdMaxSplitTimeMap = new HashMap<Integer, Timestamp>();
        if (taskLists != null && !taskLists.isEmpty()) {
            for(TaskList taskList : taskLists) {
                if (taskIdMaxSplitTimeMap.containsKey(taskList.getTaskID())) {
                    if (taskIdMaxSplitTimeMap.get(taskList.getTaskID()).getTime() < taskList.getSplitTime().getTime()) {
                        taskIdMaxSplitTimeMap.put(taskList.getTaskID(), taskList.getSplitTime());
                    }
                } else {
                    taskIdMaxSplitTimeMap.put(taskList.getTaskID(), taskList.getSplitTime());
                }
            }
        }

        /**
         * 根据TaskID对应的最新SplitTime删除重复的多余条目
         */
        List<TaskList> removeTaskList = new ArrayList<TaskList>();
        if (taskLists != null && !taskLists.isEmpty() && taskIdMaxSplitTimeMap != null && !taskIdMaxSplitTimeMap.isEmpty()) {
            for(TaskList taskList : taskLists) {
                if (taskIdMaxSplitTimeMap.get(taskList.getTaskID()) != taskList.getSplitTime()) {
                    removeTaskList.add(taskList);
                }
            }
            taskLists.removeAll(removeTaskList);
        }

        /**
         * 将taskLists组装为<TaskID,List<Status>>
         */
        Map<Integer, List<Integer>> taskIdTaskStateMap = new HashMap<Integer, List<Integer>>();
        if (taskLists != null && !taskLists.isEmpty())
        for (TaskList taskList : taskLists) {
            if (taskIdTaskStateMap.containsKey(taskList.getTaskID())) {
                taskIdTaskStateMap.get(taskList.getTaskID()).add(taskList.getTaskState());
            } else {
                List<Integer> taskStateList = new ArrayList<Integer>();
                taskStateList.add(taskList.getTaskState());
                taskIdTaskStateMap.put(taskList.getTaskID(), taskStateList);
            }
        }
        /**
         * 获取最终的<TaskID,EndStatus>
         */
        Map<Integer, Integer> taskIdEndStatusMap = new HashMap<Integer, Integer>();
        if (!taskIdTaskStateMap.isEmpty()) {
            Set<Integer> statusSet;
            for (Map.Entry<Integer,List<Integer>> idStatusMap : taskIdTaskStateMap.entrySet()) {
                statusSet = new HashSet<Integer>(idStatusMap.getValue());
                if (taskStatusBo.isAllEndStatus(statusSet)) {
                    taskIdEndStatusMap.put(idStatusMap.getKey(), taskStatusBo.getEndStatus(statusSet));
                }
            }
        }

        return taskIdEndStatusMap;
    }

    public Boolean hasTaskbyTaskType(String businessGroupid,String typeid)
    {
        List taskList=taskInfoDao.getTaskCountbyTaskType(businessGroupid, typeid );
        return  Integer.parseInt(taskList.get(0).toString()) >0 ;
    }

    public Boolean hasTaskbyEnv(String businessGroupid,String typeid,String envid)
    {
        List taskList=taskInfoDao.getTaskCountbyEnv(businessGroupid,typeid,envid);
        return   Integer.parseInt(taskList.get(0).toString()) >0 ;
    }

    public Boolean hasTaskbyGroupid(String businessGroupid)
    {
        List taskList=taskInfoDao.getTaskCountbyGroupid(businessGroupid);
        return   Integer.parseInt(taskList.get(0).toString()) >0 ;
    }

    public List getTaskInfoTree(String businessGroupid,String typeid,String envid)
    {
        return taskInfoDao.getTaskInfoTree(businessGroupid,typeid,envid);
    }

    public List getTaskIdbyGroupid(String groupid)
    {
        return  taskInfoDao.getTaskIdbyGroupid(groupid);
    }
    /**
     * 统一数据库jenkins状态相关
     */
    public void synNonEndTask() {
        int sentDelaySeconds = Integer.valueOf(env.getProperty("expire.duration.sentdelayseconds"));
        List<TaskList> sentAndRunningTaskList = taskListDao.getSentAndRunningTaskList(sentDelaySeconds);
        for (TaskList taskList : sentAndRunningTaskList) {//尝试同步所有的sent Running状态的TaskList
            synJenkinsDBStatus(taskList);
        }
    }

    public Map<Integer, Integer> getTaskIdNumMap(List<TaskList> taskLists) {
        Map<Integer, Integer> taskIdNumMap = new HashMap<Integer, Integer>();
        for (TaskList taskList : taskLists) {
            int taskId = taskList.getTaskID();
            if (taskIdNumMap.get(taskId) == null) {
                taskIdNumMap.put(taskId, 1);
            } else {
                taskIdNumMap.put(taskId, taskIdNumMap.get(taskId) + 1);
            }
        }

        return taskIdNumMap;
    }

    public int getNonEndSplitTaskListNum(int taskListId) {
        int count = 0;
        List<TaskList> taskLists = taskListDao.getSplitedTaskListById(taskListId);
        for (TaskList taskList : taskLists) {
            if (!taskStatusBo.isEndStatus(taskList.getTaskState())) {
                count ++;
            }
        }
        return count;
    }



    /**
     * 数据库中job状态为未结束状态时，去询问Jenkins，如果找不到或找到了已结束的job则修改TaskList的状态
     * @param sentOrRunningTaskList
     */
    private void synJenkinsDBStatus(TaskList sentOrRunningTaskList) {
        SynInfo synInfo = jenkinsJobBo.getSynInfo(sentOrRunningTaskList.getJobName());//获取Jenkins上该job的状态
        if (!synInfo.isBuilding() && synInfo.getQueueItem() == 0) {//非运行中并且没有在构建队列中排队
            String op = "[SynExecutor]";
            if (synInfo.getResult() != null && synInfo.getResult().equals(Constant.JOB_RESULT_SUCCESS)) {
                amendListInfoAgent(sentOrRunningTaskList, Constant.JOB_COMPLETED, op, Constant.JOB_COMPLETED);
            } else if (synInfo.getResult() != null && synInfo.getResult().equals(Constant.JOB_RESULT_FAILURE)) {
                amendListInfoAgent(sentOrRunningTaskList, Constant.JOB_ERROR, op, Constant.JOB_ERROR);
            } else {
                amendListInfoAgent(sentOrRunningTaskList, Constant.JOB_ABORTED, op, Constant.JOB_ABORTED);
            }
        }
    }

    //修正TaskList TaskInfo Agent的状态，根据split拆分后的多个TaskList是否都已结束决定是否修改TaskInfo和Agent
    public void amendListInfoAgent(TaskList taskList, int listState, String op, int infoState) {
        if (getNonEndSplitTaskListNum(taskList.getId()) == 1) {
            taskListDao.updateTaskListState(listState, op, taskList);
            Logger.info(op + " TaskList(" + taskList.getId() + ") -> " + listState);
            taskInfoDao.updateTaskInfoState(taskList.getTaskID(), infoState);//更新TaskInfo
            Logger.info(op + " TaskInfo(" + taskList.getTaskID() + ") -> " + infoState);
            taskStatusBo.checkAndIdelAgent(taskList.getTaskID(), taskList.getAgentIP());
            Logger.info(op + " Agent(" + taskList.getAgentIP() + ") -> 10");
        } else if (getNonEndSplitTaskListNum(taskList.getId()) > 1) {
            taskListDao.updateTaskListState(listState, op, taskList);
            Logger.info(op + " TaskList(" + taskList.getId() + ") -> " + listState);
        }
    }

    //根据TaskId获取浏览器列表，GUI任务使用
    public List<String> getBrowserListByTaskId(int taskId) {
        TaskInfo taskInfo = taskInfoDao.get(taskId);
        return getBrowserListByValue(taskInfo.getBrowser());
    }

    public List<String> getBrowserListByValue(int browser) {
        List<String> allBrowser = cdDao.getSortedListByParentName(Constant.BROWSER);
        List<String> browserList = new ArrayList<String>();
        if (browser > 0) {
            for (int i = 0; i < allBrowser.size(); i++) {
                if (1 == browser % 2) {
                    browserList.add(allBrowser.get(i));
                }
                browser = browser >> 1;
            }
            if (0 == browserList.size()) {
                browserList.add("");
            }
        }
        return browserList;
    }
}
