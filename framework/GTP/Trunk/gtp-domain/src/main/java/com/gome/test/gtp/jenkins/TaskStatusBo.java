package com.gome.test.gtp.jenkins;


import com.gome.test.gtp.dao.AgentInfoDao;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.dao.TaskListDao;
import com.gome.test.gtp.model.AgentInfo;
import com.gome.test.gtp.model.TaskInfo;
import com.gome.test.gtp.model.TaskList;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class TaskStatusBo {

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private TaskListDao taskListDao;

    @Autowired
    private AgentInfoDao agentInfoDao;

    public void setTaskInfoDao(TaskInfoDao taskInfoDao) {
        this.taskInfoDao = taskInfoDao;
    }

    /**
     * 用于Start End EndLoad 插件 更新状态
     * @param taskListId
     * @param origianlStatus
     * @param goalStatus
     * @param editor
     * @param log
     * @throws Exception
     */
    public void updateTaskStatus(int taskListId, int origianlStatus, int goalStatus, String editor, String log) throws Exception {
        /**
         * Update TaskList
         */
        TaskList list = getTaskListByIdAndCheck(taskListId, origianlStatus);
        if (origianlStatus == Constant.JOB_SENT) {
            /**
             * Start Mojo
             */
            list.setStartTime(new Timestamp(System.currentTimeMillis()));
        } else if (origianlStatus == Constant.JOB_RUNNING) {
            /**
             * End Mojo
             */
            list.setEndTime(new Timestamp(System.currentTimeMillis()));
        }
        String newLog = Util.appendTaskLog(list.getLogs(),
                new String[]{log, String.format("TaskListId:%d, TaskId:%d 状态%d->%d", taskListId, list.getTaskID(), origianlStatus, goalStatus)});

//        System.out.println(newLog);
        Logger.info(newLog);
        list.setLogs(newLog);
        list.setTaskState(goalStatus);

        taskListDao.update(list);

        /**
         * Update TaskInfo
         */
        TaskInfo info = taskInfoDao.get(list.getTaskID());


        if (info == null) {
            throw new Exception(String.format("id 为 %d 的TaskInfo 不存在！！", list.getTaskID()));
        } else {
            /**
             * Start Mojo
             */
            if (origianlStatus == Constant.JOB_SENT) {
                info.setTaskStatus(goalStatus);
                info.setLastRunTime(Util.nowTimestamp());
            }
            /**
             * End Mojo
             */
            else if (origianlStatus == Constant.JOB_RUNNING) {
                /**
                 * TODO 是否存在多线程方法的漏洞(多个拆分的任务同时请求）
                 */
                List<TaskList> taskLists = taskListDao.getTaskListByTaskIdGuid(list.getTaskID(), list.getGuid());
                Set<Integer> statusSet = new HashSet<Integer>();
                for (TaskList taskList : taskLists) {
                    statusSet.add(taskList.getTaskState());
                }
                if (isAllEndStatus(statusSet)) {
                    /**
                     * 修改Agent状态
                     */
                    List<AgentInfo> agentList = agentInfoDao.getAgentByLabel(list.getAgentIP());
                    AgentInfo agent = null;
                    if (agentList != null && !agentList.isEmpty()) {
                        agent = agentList.get(0);
                        agent = agentInfoDao.get(agent.getAgentID());
                    }
                    if (agent == null) {
                        Logger.warn("未找到需要更新的Agent ！！");
                    } else if (agent.getTaskId().equals(info.getTaskID())) {
                        agent.setAgentStatus(Constant.AGENT_STATUS_IDLE);
                        agentInfoDao.update(agent);
                    } else {
                        Logger.warn("任务结束时 Agent.TaskID 不等于 TaskInfo.TaskID");
                    }
                    /**
                     * 设置TaskInfo状态
                     */
                    info.setTaskStatus(getEndStatus(statusSet));
                }
            }
            taskInfoDao.update(info);
        }
    }

    public TaskList getTaskListByIdAndCheck(int taskListId, int origianlStatus) throws Exception {
        TaskList list = taskListDao.get(taskListId);
        if (list == null) {
            throw new Exception(String.format("id 为 %d 的 taskList 不存在！！", taskListId));
        }

        if (origianlStatus != list.getTaskState()) {
            throw new Exception(String.format("id 为 %d 的 taskList 状态不是 %d 而是 %d ！！",
                    taskListId, origianlStatus, list.getTaskState()));
        }

        return list;
    }

    public TaskList getTaskListById(int taskListId) throws Exception {
        TaskList list = taskListDao.get(taskListId);
        if (list == null) {
            throw new Exception(String.format("id 为 %d 的 taskList 不存在！！", taskListId));
        }

        return list;
    }

    public boolean isAllEndStatus(Set<Integer> statusSet) {
        boolean endStatus = true;
        for(Integer status : statusSet) {
            if (!Constant.END_STATUS_SET.contains(status)) {
                endStatus = false;
                break;
            }
        }
        return endStatus;
    }

    public boolean isEndStatus(int status) {
        boolean endStatus = true;
        if (!Constant.END_STATUS_SET.contains(status)) {
            endStatus = false;
        }
        return endStatus;
    }

    public int getEndStatus(Set<Integer> statusSet) {
        int endStatus;
        if(statusSet.contains(Constant.JOB_ERROR)) {
            endStatus = Constant.JOB_ERROR;
        } else if(statusSet.contains(Constant.JOB_ABORTED)) {
            endStatus = Constant.JOB_ABORTED;
        } else if(statusSet.contains(Constant.JOB_STOPPED)){
            endStatus = Constant.JOB_STOPPED;
        } else {
            endStatus = Constant.JOB_COMPLETED;
        }

        return endStatus;
    }

    public void checkAndIdelAgent(int taskId, String agentIp) {
        List<AgentInfo> agentInfos = agentInfoDao.getMachineByTaskIdIp(taskId, agentIp);
        if (agentInfos != null && agentInfos.size() == 1) {
            agentInfoDao.amendAgentStatus(agentInfos.get(0).getAgentID());
        }
    }
}
