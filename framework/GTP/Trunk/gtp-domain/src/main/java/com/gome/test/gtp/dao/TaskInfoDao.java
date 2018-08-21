package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.TaskInfo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.utils.StringUtils;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaskInfoDao extends BaseDao<TaskInfo> {

    public List getTaskList() {
        String hql = String.format(
                "SELECT M.TaskID AS ID,\n" +
                        " M.TaskName AS Task,\n" +
                        " M.Env,\n" +
                        " M.BusinessGroup,\n" +
                        " M.Browser, IFNULL(M.AgentName, 'None') AS Machine,\n" +
                        " StartType AS Run, FROM_UNIXTIME(UNIX_TIMESTAMP(IFNULL(M.RunTime, ''))) AS RunTime, IFNULL(M.WeekDay, 'None') AS WEEKDAY, FROM_UNIXTIME(UNIX_TIMESTAMP(IFNULL(M.LastRunTime, ''))) AS LastRunTime,\n" +
                        " M.TaskType,\n" +
                        " M.TaskStatus,\n" +
                        " M.RunType\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT N.*\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT S.*,\n" +
                        " RunRegularInfo.StartType,\n" +
                        " RunRegularInfo.RunTime,\n" +
                        " RunRegularInfo.WeekDay\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT TaskInfo.*,\n" +
                        " AgentInfo.AgentName,\n" +
                        " SvnBranchInfo.BusinessGroup\n" +
                        "FROM TaskInfo\n" +
                        "LEFT JOIN AgentInfo ON TaskInfo.Agent = AgentInfo.AgentID\n" +
                        "LEFT JOIN SvnBranchInfo ON TaskInfo.Branch = SvnBranchInfo.BranchID) AS S\n" +
                        "LEFT JOIN RunRegularInfo ON S.RunRegularInfoID = RunRegularInfo.ID) AS N) AS M\n" +
                        "WHERE M.TaskStatus != %d AND M.TaskID NOT IN (\n" +
                        "SELECT ReplaceInfo.TaskID\n" +
                        "FROM ReplaceInfo WHERE ReplaceInfo.CreateTime < (DATE_SUB(CURDATE(), INTERVAL 5 DAY)))\n" +
                        "ORDER BY M.TaskID", Constant.JOB_DELETED);
        return sqlQuery(hql);
    }

    public List getTaskInfoById(int id) {
        String hql = String.format("SELECT M.TaskID AS ID, M.TaskName AS Task, DictEnv.ItemName AS Env, M.Browser AS Browser, IFNULL(M.AgentName, 'None')AS Machine, IFNULL(M.Run, 'None')AS Run, FROM_UNIXTIME(UNIX_TIMESTAMP(IFNULL(M.RunTime, '')))AS RunTime, IFNULL(M.WeekDay, 'None')AS WEEKDAY, FROM_UNIXTIME(UNIX_TIMESTAMP(IFNULL(M.LastRunTime, '')))AS LastRunTime, DictTaskType.ItemName AS TaskType, DictTaskStatus.ItemName AS TaskStatus, DictTaskRunType.ItemName AS RunType, M.StartDate AS StartDate, \n" +
                " M.ExcuteInfo AS CaseQuery, M.IsMonitored AS isMonitored, M.IsSplit AS isSplit, M.EmailList AS emailList, M.loadConfName AS loadConfName, M.TaskType AS TYPE\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT N.*,DictRunType.ItemName AS Run\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT S.*,RunRegularInfo.StartType,RunRegularInfo.RunTime,RunRegularInfo.WeekDay\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT TaskInfo.*,AgentInfo.AgentName\n" +
                        "FROM TaskInfo\n" +
                        "LEFT JOIN AgentInfo ON TaskInfo.Agent = AgentInfo.AgentID) AS S\n" +
                        "LEFT JOIN RunRegularInfo ON S.RunRegularInfoID = RunRegularInfo.ID) AS N\n" +
                        "LEFT JOIN\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'RunRegularStartType') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictRunType ON N.StartType = DictRunType.ItemValue) AS M,\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'Environment') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictEnv,\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'TaskType') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictTaskType,\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'TaskStatus') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictTaskStatus,\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'TaskRunType') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictTaskRunType\n" +
                        "WHERE M.Env = DictEnv.ItemValue AND M.TaskType = DictTaskType.ItemValue AND M.TaskStatus = DictTaskStatus.ItemValue AND M.RunType = DictTaskRunType.ItemValue AND M.TaskID = %d",
                id);
        return sqlQuery(hql);
    }

    public List getTaskDetailsById(int id) {
        String hql = String.format("SELECT M.TaskID AS ID,M.TaskName AS Name,DictTaskType.ItemName AS TaskType,DictEnv.ItemName AS Env,M.Browser AS Browser, IFNULL(M.LoadConfName,'None') AS LoadConfName, IFNULL(M.AgentName,'None') AS Machine, FROM_UNIXTIME(UNIX_TIMESTAMP(IFNULL(M.LastRunTime,''))) AS LastRunTime,M.Creator AS Owner, M.LastEditor AS LastMender,\n" +
                        " +\n" +
                        " DictTaskRunType.ItemName AS RunType, M.BranchUrl AS branchUrl, M.EmailList AS emailList\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT N.*,DictRunType.ItemName AS Run\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT S.*,RunRegularInfo.StartType,RunRegularInfo.RunTime,RunRegularInfo.WeekDay\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT TaskInfo.*,AgentInfo.AgentName,SvnBranchInfo.BranchUrl\n" +
                        "FROM TaskInfo\n" +
                        "LEFT JOIN \n" +
                        " AgentInfo ON TaskInfo.Agent = AgentInfo.AgentID\n" +
                        "LEFT JOIN\n" +
                        " SvnBranchInfo ON TaskInfo.Branch = SvnBranchInfo.BranchID) AS S\n" +
                        "LEFT JOIN \n" +
                        " RunRegularInfo ON S.RunRegularInfoID = RunRegularInfo.ID) AS N\n" +
                        "LEFT JOIN\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'RunRegularStartType') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictRunType ON N.StartType = DictRunType.ItemValue) AS M,\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'Environment') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictEnv,\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'TaskType') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictTaskType,\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'TaskStatus') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictTaskStatus,\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName = 'TaskRunType') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictTaskRunType\n" +
                        "WHERE M.Env = DictEnv.ItemValue AND M.TaskType = DictTaskType.ItemValue AND M.TaskStatus = DictTaskStatus.ItemValue AND M.RunType = DictTaskRunType.ItemValue AND M.TaskID = %d",
                id);
        return sqlQuery(hql);
    }

    public List getProById(int id, String pro) {
        String hql = String.format("select T.%s from TaskInfo as T where T.TaskID = %d",
                pro,
                id);
        return sqlQuery(hql);
    }

    //RegularInfo
    public String getRegularNameByTaskId(int id) {
        String hql = "select RunRegularInfo.RegularName from RunRegularInfo left join TaskInfo on TaskInfo.RunRegularInfoID = RunRegularInfo.ID where TaskInfo.TaskID = " + id;
        if (null == sqlQuery(hql)) {
            return "None";
        } else {
            return (String) sqlQuery(hql).get(0);
        }
    }

    public int getRegularIdByName(String name) {
        String hql = "select ID from RunRegularInfo where RegularName = '" + name + "'";
        return (Integer) sqlQuery(hql).get(0);
    }

    //SvnBranch
    public String getBranchNameById(int id) {
        String hql = "select SvnBranchInfo.BranchName from SvnBranchInfo left join TaskInfo on TaskInfo.Branch = SvnBranchInfo.BranchID where TaskInfo.TaskID = " + id;
        if (null == sqlQuery(hql)) {
            return "None";
        } else {
            return (String) sqlQuery(hql).get(0);
        }
    }

    public int getBranchIdByName(String name) {
        String hql = "select BranchID from SvnBranchInfo where BranchName= '" + name + "'";
        return (Integer) sqlQuery(hql).get(0);
    }

//    public List getBranchList() {
//        String hql = "select distinct sbi.BranchName from SvnBranchInfo as sbi";
//        return sqlQuery(hql);
//    }

    public List getBranchListByTaskType(int taskType) {
        String hql = String.format("select distinct sbi.BranchName from SvnBranchInfo as sbi where sbi.Type = %d", taskType);
        return sqlQuery(hql);
    }

    public List getBranchListByGroupId(int groupId) {
        String hql = String.format("select distinct sbi.BranchName from SvnBranchInfo as sbi " +
                "where sbi.BusinessGroup = %d ", groupId);
        return sqlQuery(hql);
    }

    public List getTaskReportById(int id) {
        String hql = "SELECT ReportID,TaskName,ISNULL(TotalCases,0) as Total,ISNULL(Pass,0) as Pass,ISNULL(Fail,0) as Fail,ISNULL(Aborted,0) as Aborted,CONVERT(CHAR(19), ISNULL(StartTime,0), 20) as StartTime,ISNUll(Duration,0) as Duration,ISNULL(ErrorMessage,'None')as ErrorMessage FROM TaskReport as tr where TaskID = " + id;
        return sqlQuery(hql);
    }

    /**
     * @return
     */
    public List getTaskIdRegularId() {
        String sql = String.format("SELECT TaskInfo.TaskID, TaskInfo.RunRegularInfoID FROM  TaskInfo WHERE TaskStatus <> %d", Constant.JOB_DELETED);
        return sqlQuery(sql);
    }

    public Map<Integer, Integer> getTaskAndGroup() {
        String sql = "SELECT  TaskID, SvnBranchInfo.BusinessGroup FROM   TaskInfo, SvnBranchInfo WHERE  Branch = BranchID";
        List list = sqlQuery(sql);

        Map<Integer, Integer> result = new HashMap<Integer, Integer>();

        for (Object obj : list) {
            Object[] row = (Object[]) obj;
            result.put(Integer.valueOf(row[0].toString()), Integer.valueOf(row[1].toString()));
        }

        return result;
    }

    public List getTaskInfoByIds(List<Integer> taskId) {
        if (taskId.isEmpty())
            return new ArrayList();

        StringBuffer stringBuffer = new StringBuffer();

        for (Integer id : taskId) {
            if (stringBuffer.length() > 0)
                stringBuffer.append(",");

            stringBuffer.append(id);
        }

        String sql = String.format("SELECT TaskInfo.TaskID, TaskInfo.TaskName,TaskInfo.ExcuteInfo,SvnBranchInfo.BranchUrl, IFNULL(AgentInfo.AgentIP,'None'), TaskInfo.TaskType\n" +
                "FROM TaskInfo\n" +
                "LEFT JOIN AgentInfo ON TaskInfo.Agent = AgentInfo.AgentID\n" +
                "LEFT JOIN SvnBranchInfo ON TaskInfo.Branch = SvnBranchInfo.BranchID\n" +
                "WHERE TaskInfo.TaskID IN (%s)", stringBuffer);
        return sqlQuery(sql);
    }

    public List getTaskMainInfoById(int taskId) {
        String sql = String.format("SELECT TaskInfo.TaskID, TaskInfo.TaskName,TaskInfo.ExcuteInfo,SvnBranchInfo.BranchUrl, IFNULL(AgentInfo.AgentIP,'None'), TaskInfo.TaskType\n" +
                "FROM TaskInfo\n" +
                "LEFT JOIN AgentInfo ON TaskInfo.Agent = AgentInfo.AgentID\n" +
                "LEFT JOIN SvnBranchInfo ON TaskInfo.Branch = SvnBranchInfo.BranchID\n" +
                "WHERE TaskInfo.TaskID=%d", taskId);
        return sqlQuery(sql);
    }

    public int getTaskTypeById(int id) {
        String sql = String.format("SELECT TaskInfo.TaskType FROM TaskInfo WHERE TaskInfo.TaskID = %d", id);
        List taskType = sqlQuery(sql);
        if (taskType == null) {
            return -1;
        } else {
            return Integer.valueOf(taskType.get(0).toString());
        }
    }

    public List getTaskIdStatusLastTime() {
        String sql = String.format("SELECT TaskInfo.TaskID, TaskInfo.TaskStatus, TaskInfo.LastRunTime, AgentInfo.AgentStatus, AgentInfo.TaskID as at\n" +
                "FROM TaskInfo\n" +
                "LEFT JOIN AgentInfo ON TaskInfo.Agent = AgentInfo.AgentID\n" +
                "WHERE TaskInfo.TaskStatus <> %d", Constant.JOB_DELETED);
        return sqlQuery(sql);
    }

    public List<TaskInfo> getNonendTaskInfo() {
        String sql = String.format("select * from TaskInfo where TaskStatus not in (%d,%d,%d,%d,%d,%d)", Constant.JOB_DELETED, Constant.JOB_COMPLETED, Constant.JOB_ERROR, Constant.JOB_ABORTED, Constant.JOB_STOPPED, Constant.JOB_CREATED);
        List<TaskInfo> taskInfoList = sqlQuery(sql, TaskInfo.class);
        return taskInfoList;
    }

    @Transactional
    public void amendTaskInfoStatus(Map<Integer, Integer> taskIdStatusMap) {
        StringBuilder whenStr = new StringBuilder();
        StringBuilder inStr = new StringBuilder();
        for (Map.Entry<Integer, Integer> map : taskIdStatusMap.entrySet()) {
            whenStr.append(String.format("when %d then %d ", map.getKey(), map.getValue()));
            inStr.append(String.format("%d,", map.getKey()));
        }
        whenStr.setLength(whenStr.length() - 1);
        inStr.setLength(inStr.length() - 1);

        String sql = String.format(
                "update TaskInfo\n" +
                        "    set TaskInfo.TaskStatus = case TaskInfo.TaskID\n" +
                        "        %s\n" +
                        "    end\n" +
                        "where TaskInfo.TaskID in (%s)", whenStr.toString().trim(), inStr.toString());

        executeSql(sql);
    }

    /*
    * 根据组id和typeid 得到符合条件的taskInfo的数量
    * */
    public List getTaskCountbyTaskType(String businessGroupid, String typeid) {
        String sql = String.format("select count(0) from TaskInfo v  , SvnBranchInfo s where v.Branch = s.BranchID and s.BusinessGroup=%s and v.TaskType = %s and v.TaskStatus <> %d ", businessGroupid, typeid, Constant.JOB_DELETED);

        return sqlQuery(sql);
    }

    /*
    * 根据组id和typeid、envid 得到符合条件的taskInfo的数量
    * */
    public List getTaskCountbyEnv(String businessGroupid, String typeid, String envid) {
        String sql = String.format("select count(0) from TaskInfo v  , SvnBranchInfo s where v.Branch = s.BranchID and s.BusinessGroup=%s and v.TaskType = %s and v.Env=%s and v.TaskStatus <> %d ", businessGroupid, typeid, envid, Constant.JOB_DELETED);

        return sqlQuery(sql);
    }

    /*
    * 根据组id 得到符合条件的taskInfo的数量
    * */
    public List getTaskCountbyGroupid(String businessGroupid) {
        String sql = String.format("select count(0) from TaskInfo v  , SvnBranchInfo s where v.Branch = s.BranchID and s.BusinessGroup=%s and v.TaskStatus <> %d ", businessGroupid, Constant.JOB_DELETED);

        return sqlQuery(sql);
    }

    /*
    * 根据组id和typeid、envid 得到符合条件的taskInfo的id和name
    * */
    public List getTaskInfoTree(String businessGroupid, String typeid, String envid) {
        String sql = String.format("select v.TaskID, v.TaskName from TaskInfo v  , SvnBranchInfo s where v.Branch = s.BranchID and s.BusinessGroup=%s and v.TaskType = %s and v.Env=%s and v.TaskStatus <> %d ", businessGroupid, typeid, envid, Constant.JOB_DELETED);

        return sqlQuery(sql);
    }

    public List getTaskIdbyGroupid(String bussinessGroupid) {
        String sql = String.format("select TaskId from TaskInfo where runType=3 and  Branch in (select BranchId from SvnBranchInfo where BusinessGroup='%s' )", bussinessGroupid);
        return sqlQuery(sql);
    }

    public String getGroupNameByTaskid(int taskId) {
        String sql = String.format("SELECT GroupInfo.name\n" +
                "FROM GroupInfo, SvnBranchInfo, TaskInfo\n" +
                "WHERE SvnBranchInfo.BranchID = TaskInfo.Branch AND GroupInfo.itemValue = SvnBranchInfo.BusinessGroup AND TaskInfo.TaskID = %d", taskId);
        List group = sqlQuery(sql);
        if (group == null) {
            return "";
        } else {
            return group.get(0).toString();
        }
    }

    @Transactional
    public void updateTaskInfoState(int taskId, int taskState) {
        String sql = String.format("update TaskInfo set TaskStatus=%d where TaskID=%d", taskState, taskId);
        executeSql(sql);
    }

    /**
     * Abort Expire Task
     */
    public List getExpireTaskInfoId(String expireTime, String taskTypeCondition) {
        return sqlQuery(String.format("SELECT TaskInfo.TaskID FROM TaskInfo " +
                        "WHERE TaskInfo.StartDate < '%s' AND TaskInfo.TaskStatus NOT IN (%d, %d, %d, %d, %d) AND TaskInfo.TaskType %s",
                expireTime,
                Constant.JOB_DELETED,
                Constant.JOB_ABORTED,
                Constant.JOB_COMPLETED,
                Constant.JOB_ERROR,
                Constant.JOB_STOPPED,
                taskTypeCondition));
    }

    @Transactional
    public void updateExpireTaskInfo(String expireTime, String taskTypeCondition) {
        executeSql(String.format("UPDATE TaskInfo SET TaskInfo.TaskStatus = %d " +
                                "WHERE TaskInfo.StartDate < '%s' AND TaskInfo.TaskStatus NOT IN (%d, %d, %d, %d) AND TaskInfo.TaskType %s",
                        Constant.JOB_ABORTED,
                        expireTime,
                        Constant.JOB_ABORTED,
                        Constant.JOB_COMPLETED,
                        Constant.JOB_ERROR,
                        Constant.JOB_STOPPED,
                        taskTypeCondition)

        );
    }

    @Transactional
    public void updateTaskInfoByLoadConfName(String newLoadConfName, String oldLoadConfName) {
        executeSql(String.format("UPDATE TaskInfo SET TaskInfo.LoadConfName = '%s'\n" +
                "WHERE TaskInfo.LoadConfName = '%s'",newLoadConfName, oldLoadConfName));
    }


    public List getGuiOpenUrlTaskList() {
        String hql = String.format(
                "SELECT M.TaskID AS ID,\n" +
                        "       M.TaskName AS Task,\n" +
                        "       M.Env,\n" +
                        "       M.BusinessGroup,\n" +
                        "       M.Browser,\n" +
                        "       IFNULL(M.AgentName, 'None') AS Machine,\n" +
                        "       StartType AS Run,\n" +
                        "       FROM_UNIXTIME(UNIX_TIMESTAMP(IFNULL(M.RunTime, ''))) AS RunTime,\n" +
                        "       IFNULL(M.WeekDay, 'None') AS WEEKDAY,\n" +
                        "       FROM_UNIXTIME(UNIX_TIMESTAMP(IFNULL(M.LastRunTime, ''))) AS LastRunTime,\n" +
                        "       M.TaskType,\n" +
                        "       M.TaskStatus,\n" +
                        "       M.RunType ,\n" +
                        "       M.ReplaceValue AS ReplaceValue " +
                        "FROM\n" +
                        "  ( SELECT N.*\n" +
                        "   FROM\n" +
                        "     ( SELECT S.*,\n" +
                        "              RunRegularInfo.StartType,\n" +
                        "              RunRegularInfo.RunTime,\n" +
                        "              RunRegularInfo.WeekDay\n" +
                        "      FROM\n" +
                        "        ( SELECT TaskInfo.*,\n" +
                        "                 AgentInfo.AgentName,\n" +
                        "                 SvnBranchInfo.BusinessGroup,\n" +
                        " ReplaceInfo.ReplaceValue \n " +
                        "         FROM TaskInfo\n" +
                        "  Inner JOIN ReplaceInfo ON TaskInfo.TaskID = ReplaceInfo.TaskID  \n" +
                        "         LEFT JOIN AgentInfo ON TaskInfo.Agent = AgentInfo.AgentID\n" +
                        "         LEFT JOIN SvnBranchInfo ON TaskInfo.Branch = SvnBranchInfo.BranchID) AS S\n" +
                        "      LEFT JOIN RunRegularInfo ON S.RunRegularInfoID = RunRegularInfo.ID) AS N ) AS M\n" +
                        "WHERE M.TaskStatus != %d \n" +
                        "ORDER BY M.TaskID", Constant.JOB_DELETED);
        return sqlQuery(hql);
    }

    public List getTaskbyGroupid(String bussinessGroupid) {
        if(!StringUtils.isEmpty(bussinessGroupid))
        {
            bussinessGroupid =String.format(" and  Branch in (select BranchId from SvnBranchInfo where BusinessGroup='%s' )",bussinessGroupid);
        }


        String sql = String.format("select TaskId , TaskName from TaskInfo where taskType=3 %s group by TaskId , TaskName ", bussinessGroupid);
        return sqlQuery(sql);
    }

}