package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.LoadScene;
import com.gome.test.gtp.utils.Constant;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lizonglin on 2015/7/23/0023.
 */
@Component
public class LoadSceneDao extends BaseDao<LoadScene> {
    public List<String> getAllSceneName() {
        String sql = "select name from LoadScene";
        return sqlQuery(sql);
    }

    public List getAllScene() {
        String sql = String.format("SELECT LoadScene.id,LoadScene.name AS lname,LoadScene.onError,LoadScene.threadNum,LoadScene.initDelay,LoadScene.startCount,LoadScene.startCountBurst,LoadScene.startPeriod,LoadScene.stopCount,LoadScene.stopPeriod,LoadScene.flightTime,LoadScene.rampUp,LoadScene.lastUpdateTime,LoadScene.isTemplate,LoadScene.isTest, CASE WHEN EXISTS (\n" +
                "SELECT DISTINCT LoadConf.sceneName\n" +
                "FROM LoadConf\n" +
                "WHERE LoadConf.name IN (\n" +
                "SELECT DISTINCT TaskInfo.LoadConfName\n" +
                "FROM TaskInfo\n" +
                "WHERE TaskInfo.TaskStatus <> %d) AND LoadConf.sceneName = lname) THEN true ELSE false END\n" +
                "FROM LoadScene;", Constant.JOB_DELETED);
        return sqlQuery(sql);
    }

    public boolean isUniqueByNameId(String name, int id) {
        boolean isUnique = false;
        String sql = String.format("select count(name) from LoadScene where name = '%s' and id != %d",name, id);
        if (Integer.valueOf(sqlQuery(sql).get(0).toString()) == 0) {
            isUnique = true;
        }
        return isUnique;
    }

    public List getLoadSceneByTaskListId(int taskListId) {
        String sql = String.format("SELECT LoadScene.*\n" +
                "FROM LoadScene\n" +
                "LEFT JOIN LoadConf ON LoadScene.name = LoadConf.sceneName \n" +
                "LEFT JOIN TaskInfo ON TaskInfo.LoadConfName = LoadConf.name\n" +
                "LEFT JOIN TaskList ON TaskInfo.TaskID = TaskList.TaskID\n" +
                "WHERE TaskList.ID = %d", taskListId);
        return sqlQuery(sql, LoadScene.class);
    }

    public List getFirstTestLoadScene() {
        String sql = "select * from LoadScene where LoadScene.isTest = 1 limit 1";
        return sqlQuery(sql, LoadScene.class);
    }

    public List<LoadScene> getLoadSceneByIsTemplate(boolean isTemplate)
    {
        String sql = String.format("SELECT * FROM LoadScene where isTemplate=%s",isTemplate);
        return sqlQuery(sql, LoadScene.class);
    }

    public boolean isUsed(String sceneName) {
        boolean isUsed = true;
        String sql = String.format("SELECT TaskInfo.LoadConfName\n" +
                "FROM TaskInfo\n" +
                "WHERE TaskInfo.LoadConfName IN (\n" +
                "SELECT LoadConf.name\n" +
                "FROM LoadConf\n" +
                "WHERE LoadConf.sceneName = '%s') AND TaskInfo.TaskStatus <> 100", sceneName);
        List usedSceneList = sqlQuery(sql);
        if (null == usedSceneList || 0 == usedSceneList.size()) {
            isUsed = false;
        }
        return isUsed;
    }
}
