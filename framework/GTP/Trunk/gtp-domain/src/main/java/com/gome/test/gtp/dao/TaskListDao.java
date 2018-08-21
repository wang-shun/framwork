package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.TaskList;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class TaskListDao extends BaseDao<TaskList> {
    public List<TaskList> getRunningTaskListByTaskId(int taskId) {
        String sql = String.format("select * from TaskList where TaskID = %d and TaskState = %d", taskId, Constant.JOB_RUNNING);
        List<TaskList> taskList = sqlQuery(sql, TaskList.class);
        return taskList;
    }

    public synchronized List<TaskList> getTaskListByTaskIdGuid(int taskId, String guid) {
        String sql = String.format("select * from TaskList where TaskID = %d and GUID = '%s'", taskId, guid);
        List<TaskList> taskLists = sqlQuery(sql, TaskList.class);
        return taskLists;
    }

    public List getTaskInfoByTaskListId(int taskListId) {
        String sql = String.format(
                "SELECT TaskList.ID, TaskInfo.TaskType, TaskInfo.EmailList, SvnBranchInfo.BranchUrl, TaskList.AgentIp, IFNULL(AgentInfo.AgentLabel,'None'), TaskInfo.ExcuteInfo, TaskList.TaskID \n" +
                        "FROM TaskInfo \n" +
                        "LEFT join TaskList on TaskList.TaskID = TaskInfo.TaskID\n" +
                        "LEFT join SvnBranchInfo on TaskInfo.Branch = SvnBranchInfo.BranchID\n" +
                        "LEFT join AgentInfo on TaskInfo.Agent = AgentInfo.AgentID\n" +
                        "where TaskList.ID = %d", taskListId);
        return sqlQuery(sql);
    }

    public List getHostsByTaskListId(int taskListId) {
        String sql = String.format(
                "select Hosts.HostContent from Hosts, TaskList, TaskInfo where TaskList.TaskID = TaskInfo.TaskID AND TaskInfo.Env = Hosts.Env AND TaskList.ID = %d", taskListId);
        return sqlQuery(sql);
    }

    public List getHostsByEnv(int env) {
        String sql = String.format(
                "select Hosts.HostContent from Hosts where Env = %d", env);
        return sqlQuery(sql);
    }

    public List<TaskList> getGroupedList() {
        String sql = String.format("select distinct  a.* from TaskList1 a ,(select max(SplitTime) as splitTime from TaskList1 group by TaskID ) b where a.SplitTime = b.splitTime;"
                                 //"select * from TaskList where SplitTime in (select max(SplitTime) from TaskList group by TaskID)"
        );
        List<TaskList> taskList = sqlQuery(sql, TaskList.class);
        return taskList;
    }

    public List<TaskList> getSplitedTaskListById(int taskListId) {
        String sql = String.format(
                "SELECT *\n" +
                        "FROM TaskList\n" +
                        "LEFT JOIN TaskList List ON TaskList.ID = TaskList.ID\n" +
                        "WHERE TaskList.TaskID = List.TaskID AND TaskList.SplitTime = List.SplitTime AND List.ID = %d", taskListId);
        List<TaskList> taskLists = sqlQuery(sql, TaskList.class);
        return taskLists;
    }

    public List<TaskList> getNonEndTaskList() {
        String sql = String.format("select * from TaskList where TaskState not in (%d,%d,%d,%d,%d,%d)", Constant.JOB_DELETED, Constant.JOB_COMPLETED, Constant.JOB_ERROR, Constant.JOB_ABORTED, Constant.JOB_STOPPED, Constant.JOB_CREATED);
        return sqlQuery(sql, TaskList.class);
    }

    public List<TaskList> getSentAndRunningTaskList(int sentDelaySeconds) {
        String sql = String.format("select * from TaskList where TaskState in (%d,%d) and SentToAgentTime < date_sub(now(), interval %d second)", Constant.JOB_SENT, Constant.JOB_RUNNING, sentDelaySeconds);
        return sqlQuery(sql, TaskList.class);
    }

    @Transactional
    public void updateTaskListState(int state, String op, TaskList taskList) {
        String newLog = Util.appendTaskLog(taskList.getLogs(),
                String.format("[%s] TaskListId:%d, TaskId:%d 状态%d->%d", op, taskList.getId(), taskList.getTaskID(), taskList.getTaskState(), state));
        String sql = String.format("update TaskList set TaskState = %d, Logs = '%s', EndTime = '%s' where ID = %d", state, newLog, Util.nowTimestamp(), taskList.getId());
        executeSql(sql);
    }

    @Transactional
    public void endTaskListState(int state, String op, TaskList taskList) {
        String newLog = Util.appendTaskLog(taskList.getLogs(),
                String.format("[%s] 状态%d->%d", op, taskList.getTaskState(), state));
        String sql = String.format("update TaskList set TaskState = %d, EndTime='%s', Logs = '%s' where ID = %d", state, Util.nowTimestamp(), newLog, taskList.getId());
        executeSql(sql);
    }

    public List<TaskList> getTaskListByTaskId(int taskId) {
        String sql = String.format("select * from TaskList where TaskID = %d and TaskState not in (%d,%d,%d,%d,%d,%d)", taskId, Constant.JOB_DELETED, Constant.JOB_COMPLETED, Constant.JOB_ERROR, Constant.JOB_ABORTED, Constant.JOB_STOPPED, Constant.JOB_CREATED);
        return sqlQuery(sql, TaskList.class);
    }

    @Transactional
    public void lastUpdateTaskListState(int state, TaskList taskList) {
        String newLog = Util.appendTaskLog(taskList.getLogs(), String.format("[LastMojo] TaskListId:%d, TaskId:%d 状态%d->%d", taskList.getId(), taskList.getTaskID(), taskList.getTaskState(), state));
        String sql = String.format("update TaskList set TaskState = %d, Logs = '%s' where ID = %d", state, newLog, taskList.getId());
        executeSql(sql);
    }

    @Transactional
    public void updateTaskListLog(int taskListId, String appendLog) {
        String newLog = String.format("\n[%s] %s", Util.dateToString(new java.util.Date(System.currentTimeMillis())), appendLog);
        String sql = String.format("UPDATE TaskList SET TaskList.Logs = CONCAT(TaskList.`Logs`,'%s') WHERE TaskList.ID = %d", appendLog, taskListId);
        executeSql(sql);
    }


    public List<TaskList> getJenkinsReportByTaskId(int taskId) {
        String sql = String.format("select * from TaskList where TaskList.JobName <> '' and TaskList.TaskID = %d and TaskList.TaskState >=%d order by TaskList.CreateTime desc limit %d", taskId, Constant.JOB_SENT, Constant.API_REPORT_LIMIT);
        return sqlQuery(sql, TaskList.class);
    }

    public List getOccupyAgentTaskId() {
        String sql = String.format("select distinct TaskList.TaskID from TaskList where TaskList.TaskState in (%d, %d);", Constant.JOB_SENT, Constant.JOB_RUNNING);
        return sqlQuery(sql);
    }

    /**
     * Aborted Expire Task
     */
    public List<TaskList> getExpireTaskList(String expireTime, String taskTypeCondition) {
        return sqlQuery(String.format("SELECT *\n" +
                        "FROM TaskList\n" +
                        "LEFT JOIN TaskInfo ON TaskList.TaskID = TaskInfo.TaskID\n" +
                        "WHERE TaskList.CreateTime < '%s' AND TaskList.TaskState NOT IN (%d,%d,%d,%d) AND TaskInfo.TaskType %s",
                expireTime,
                Constant.JOB_ABORTED,
                Constant.JOB_COMPLETED,
                Constant.JOB_ERROR,
                Constant.JOB_STOPPED,
                taskTypeCondition), TaskList.class);
    }

    //fix bug weijx
    @Transactional
    public void updateExpireTaskList(String expireTime, String taskTypeCondition) {
        //
        String sql = String.format("update TaskList, TaskInfo set TaskList.TaskState=%d " +
                        "where TaskList.createTime < '%s' " +
                        "and TaskList.TaskState not in (%d,%d,%d,%d) " +
                        "and TaskList.TaskID = TaskInfo.TaskID " +
                        "and TaskInfo.TaskType %s",
                Constant.JOB_ABORTED,
                expireTime,
                Constant.JOB_ABORTED,
                Constant.JOB_COMPLETED,
                Constant.JOB_ERROR,
                Constant.JOB_STOPPED,
                taskTypeCondition);
//        System.out.println("--------------->updateExpireTaskList sql :=> "+ sql);
        Logger.info("--------------->updateExpireTaskList sql :=> "+ sql);
        executeSql(sql);
    }

    public List<TaskList> getTaskListBySplitTime(String splitTime,int taskId) {
        String sql = String.format("select * from TaskList where TaskList.JobName <> '' and TaskList.TaskID = %d and TaskList.TaskState >=%d and SplitTime= '%s' order by TaskList.CreateTime desc limit %d", taskId, Constant.JOB_SENT, splitTime, Constant.API_REPORT_LIMIT);
        return sqlQuery(sql, TaskList.class);
    }

    /**
     * update job status when call endJob plugin.
     * @param
     * @param appendLog
     */

    @Transactional
    public void updateTaskJobStuatus(String jobName, String appendLog , int status) {
        int result =0;
        String newLog = String.format("\n[%s] %s", Util.dateToString(new java.util.Date(System.currentTimeMillis())), appendLog);
        //update response_queue set 	status=70 and logs = 'test.' where jobName='C_1468231217669_1'
        String sql = String.format("update response_queue set status=%d ,`level`=3,logs = '%s' where jobName='%s'",status, appendLog, jobName);
//        System.out.println("---------------update job sql is : => "+ sql);
        Logger.info("---------------update job sql is : => "+ sql);
        try {
             result = executeSql(sql);
        }catch (Exception e){
            System.out.println("---------------update error message. " + e.getMessage());
        }finally {
            System.out.println("---------------update job sql status " + result);
        }
        System.out.println("---------------update job sql end ");
    }

    //==================================Load相关=========================================

}
