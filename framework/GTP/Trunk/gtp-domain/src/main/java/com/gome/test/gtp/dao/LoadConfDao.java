package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.LoadConf;
import com.gome.test.gtp.utils.Constant;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lizonglin on 2015/7/20/0020.
 */
@Repository
public class LoadConfDao extends BaseDao<LoadConf> {
    public List getLoadConfNameList() {
        String sql = "SELECT name FROM LoadConf";
        return sqlQuery(sql);
    }

    public boolean isUniqueByNameId(String name, int id) {
        boolean isUnique = false;
        String sql = String.format("select count(name) from LoadConf where name = '%s' and id != %d",name, id);
        if (Integer.valueOf(sqlQuery(sql).get(0).toString()) == 0) {
            isUnique = true;
        }
        return isUnique;
    }

    public List getAllLoadConf() {
        String sql = String.format("SELECT LoadConf.id, LoadConf.name as lname, LoadConf.scriptSvn, LoadConf.jmxContent, LoadConf.sceneName, LoadConf.sourceSvn, LoadConf.env, LoadConf.lastUpdateTime, CASE WHEN EXISTS (\n" +
                "SELECT LoadConf.id\n" +
                "FROM LoadConf\n" +
                "WHERE lname IN (\n" +
                "SELECT TaskInfo.LoadConfName\n" +
                "FROM TaskInfo\n" +
                "WHERE TaskInfo.TaskStatus <> %d)) THEN true ELSE false END AS Used\n" +
                "FROM LoadConf", Constant.JOB_DELETED);
        return sqlQuery(sql);
    }

    public List<LoadConf> getLoadConfByTaskListId(int taskListId) {
        String sql = String.format("SELECT LoadConf.*\n" +
                "FROM LoadConf\n" +
                "LEFT JOIN TaskInfo ON TaskInfo.LoadConfName = LoadConf.name\n" +
                "LEFT JOIN TaskList ON TaskInfo.TaskID = TaskList.TaskID\n" +
                "WHERE TaskList.ID = %d", taskListId);
        return sqlQuery(sql, LoadConf.class);

    }

    public boolean isUsed(String loadConfName) {
        boolean isUsed = true;
        String sql = String.format("SELECT TaskInfo.LoadConfName\n" +
                "FROM TaskInfo\n" +
                "WHERE TaskInfo.LoadConfName = '%s' AND TaskInfo.TaskStatus <> 100", loadConfName);
        List usedLoadConfNameList = sqlQuery(sql);
        if (null == usedLoadConfNameList || 0 == usedLoadConfNameList.size()) {
            isUsed = false;
        }
        return isUsed;
    }

    public void updateByLoadSceneName (String newName, String oldName) {
        executeSql(String.format("UPDATE LoadConf SET LoadConf.sceneName = '%s'\n" +
                "WHERE LoadConf.sceneName = '%s'",newName,oldName));
    }
}
