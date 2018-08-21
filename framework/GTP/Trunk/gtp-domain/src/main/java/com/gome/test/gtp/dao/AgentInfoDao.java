package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.AgentInfo;
import com.gome.test.gtp.utils.Constant;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AgentInfoDao extends BaseDao<AgentInfo> {
    public List getMachineList () {
        String hql = "select distinct ai.AgentName from AgentInfo as ai";
        return sqlQuery(hql);
    }

    public List getMachineListByType(String type) {
        String sql = String.format("select distinct AgentName from AgentInfo where TaskType='%s' order by AgentName",type);
        return sqlQuery(sql);
    }

    public List getAgentNameBrowserListByType(String type) {
        String sql = String.format("select distinct AgentName, Brower from AgentInfo where TaskType='%s' order by AgentName",type);
        return sqlQuery(sql);
    }

    public int getMachineIdByName (String name) {
        String hql = "select ai.AgentID from AgentInfo as ai where ai.AgentName = '" + name + "'";
        return (Integer) sqlQuery(hql).get(0);
    }

    public List<AgentInfo> getMachineByTaskIdIp(int taskId, String ip) {
        String sql = String.format("select * from AgentInfo where TaskID=%d and AgentIP='%s'", taskId, ip);
        return sqlQuery(sql, AgentInfo.class);
    }

    public List getAllMachine () {
        String sql = "SELECT N.AgentID AS ID,N.AgentName AS Name,N.AgentIP AS IP,N.Brower,N.ItemName AS EnvName,N.Description,N.AgentLabel, N.TaskType, N.AgentOS\n" +
                "FROM\n" +
                " (\n" +
                "SELECT S.*,DictEnv.ItemName\n" +
                "FROM\n" +
                " (\n" +
                "SELECT AgentInfo.*\n" +
                "FROM AgentInfo\n" +
                ") AS S\n" +
                "LEFT JOIN\n" +
                " (\n" +
                "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                "FROM ConfigureDictionary,\n" +
                " (\n" +
                "SELECT ID AS i,ItemName\n" +
                "FROM ConfigureDictionary\n" +
                "WHERE ItemName='Environment') AS T\n" +
                "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictEnv ON S.ENV = DictEnv.ItemValue) AS N";
        return sqlQuery(sql);
    }

    public List getMachineById(int id) {
        String sql = String.format("SELECT N.AgentID AS ID,N.AgentName AS Name,N.AgentIP AS IP,N.Brower,N.ItemName AS EnvName,N.Description,N.AgentLabel, N.TaskType, N.AgentOS\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT S.*,DictEnv.ItemName\n" +
                        "FROM\n" +
                        " (\n" +
                        "SELECT AgentInfo.*\n" +
                        "FROM AgentInfo where AgentInfo.AgentID = %d\n" +
                        ") AS S\n" +
                        "LEFT JOIN\n" +
                        " (\n" +
                        "SELECT ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue\n" +
                        "FROM ConfigureDictionary,\n" +
                        " (\n" +
                        "SELECT ID AS i,ItemName\n" +
                        "FROM ConfigureDictionary\n" +
                        "WHERE ItemName='Environment') AS T\n" +
                        "WHERE T.i=ConfigureDictionary.ItemParentID) AS DictEnv ON S.ENV = DictEnv.ItemValue) AS N",
                id);
        return sqlQuery(sql);
    }

    public List<AgentInfo> getAgentByLabel(String agentLabel) {
        String sql = String.format("select * from AgentInfo where AgentLabel = '%s'",agentLabel);
        return sqlQuery(sql, AgentInfo.class);
    }

    public List<AgentInfo> getRunningAgentList() {
        String sql = String.format("select * from AgentInfo where AgentStatus = %d", Constant.AGENT_STATUS_RUNNING);
        return sqlQuery(sql, AgentInfo.class);
    }

    @Transactional
    public void amendAgentStatus(Integer... agentIdList) {
        StringBuilder taskIdStr = new StringBuilder();
        for (int taskId : agentIdList) {
            taskIdStr.append(String.format(" %d,",taskId));
        }
        taskIdStr.setLength(taskIdStr.length() - 1);

        String sql = String.format("update AgentInfo set AgentStatus = %d where AgentID in (%s)", Constant.AGENT_STATUS_IDLE,taskIdStr.toString());
        executeSql(sql);
    }

    /**
     * Abort Expire Agent
     */
    public List getExpireAgent(String expireTime, String agentTaskTypeCondition) {
        return sqlQuery(
                String.format("SELECT AgentInfo.AgentID FROM AgentInfo " +
                                "WHERE AgentInfo.AgentStatus = %d and AgentInfo.LastRunTime < '%s' AND AgentInfo.TaskType %s",
                        Constant.AGENT_STATUS_RUNNING,
                        expireTime,
                        agentTaskTypeCondition));
    }

    public void updateExpireAgent(String expireTime, String agentTaskTypeCondition) {
        executeSql(String.format("UPDATE AgentInfo SET AgentInfo.AgentStatus = %d" +
                        "WHERE AgentInfo.AgentStatus = %d and AgentInfo.LastRunTime < '%s' AND AgentInfo.TaskType %s",
                Constant.AGENT_STATUS_IDLE,
                Constant.AGENT_STATUS_RUNNING,
                expireTime,
                agentTaskTypeCondition));
    }
}
