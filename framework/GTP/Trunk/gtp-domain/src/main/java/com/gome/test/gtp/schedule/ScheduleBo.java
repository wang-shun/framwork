package com.gome.test.gtp.schedule;

import com.gome.test.gtp.dao.RunRegularInfoDao;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.model.RunRegularInfo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.utils.Logger;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by lizonglin on 2015/5/7.
 */
@Service
public class ScheduleBo {
    @Autowired
    private RunRegularInfoDao runRegularInfoDao;
    @Autowired
    private TaskInfoDao taskInfoDao;

    /**
     * 将runRegularInfo转为cron expression
     *
     * @param runRegularInfo
     * @return
     */
    private CronExpression transTime2Cron(RunRegularInfo runRegularInfo) throws ParseException {
        int startType = runRegularInfo.getStartType();
        Timestamp runTime = runRegularInfo.getRunTime();
        String weekday = runRegularInfo.getWeekDay();

        String hour = String.valueOf(runTime.getHours());
        String minute = String.valueOf(runTime.getMinutes());
        String second = String.valueOf(runTime.getSeconds());

        String year = String.valueOf(runTime.getYear() + 1900);//date 的 getter函数 不是直观含义
        String month = String.valueOf(runTime.getMonth() + 1);
        String day = String.valueOf(runTime.getDate());
        String express;
        switch (startType) {
            case Constant.REGULAR_DAILY:
                express = String.format("%s %s %s * * ? *", second, minute, hour);
                break;
            case Constant.REGULAR_WEEKLY:
                express = String.format("%s %s %s ? * %s", second, minute, hour, weekday);
                break;
            case Constant.REGULAR_ONCE:
                express = String.format("%s %s %s %s %s ? %s", second, minute, hour, day, month, year);
                break;
            default:
                express = String.format("%s %s %s %s %s ?", second, minute, hour, day, month);
                break;
        }

        return new CronExpression(express);
    }


    /**
     * HashMap<regularId, cronExp>
     * 不同regular定时相同时，可能有多个regularId对应一个cronExp的情况
     *
     * @return
     */
    private HashMap<Integer, CronExpression> loadRegularIdCronMap() throws ParseException {
        List<RunRegularInfo> runRegularInfoList = runRegularInfoDao.getAll();
        HashMap<Integer, CronExpression> regularIdCronMap = new HashMap<Integer, CronExpression>();
        for (RunRegularInfo runRegularInfo : runRegularInfoList) {
            regularIdCronMap.put(runRegularInfo.getId(), transTime2Cron(runRegularInfo));
        }
        return regularIdCronMap;
    }

    /**
     * HashMap<taskId, regularId> 忽略DELETED状态的TaskInfo和当天已加入过TaskList的TaskInfo
     * 存在多个task对应一个regular的情况
     *
     * @return
     */
    private HashMap<Integer, Integer> loadTaskIdRegularIdMap() {
        HashMap<Integer, Integer> rtMap = new HashMap<Integer, Integer>();
        List taskIdRegularIdList = taskInfoDao.getTaskIdRegularId();
        if (taskIdRegularIdList != null) {
            for (int i = 0; i < taskIdRegularIdList.size(); i++) {
                Object[] rtArray = (Object[]) taskIdRegularIdList.get(i);
                rtMap.put(Integer.valueOf(rtArray[0].toString()), Integer.valueOf(rtArray[1].toString()));
            }
        }
        return rtMap;
    }

    /**
     * HashMap<cronExp,List<taskId>>
     *
     * @return
     */

    public Map<String, CronExpression> loadTaskIdCronExpMap() throws ParseException {
        Map<String, CronExpression> taskIdCronExpMap = new HashMap<String, CronExpression>();
        HashMap<Integer, Integer> taskRegularMap = loadTaskIdRegularIdMap();//HashMap<taskId, regularId>
        HashMap<Integer, CronExpression> regularCronMap = loadRegularIdCronMap();//HashMap<regularId, CronExp>
        for (Map.Entry entry : taskRegularMap.entrySet()) {
            CronExpression cronExpression = regularCronMap.get(entry.getValue());
            if (cronExpression!=null &&cronExpression.getNextValidTimeAfter(new Date()) != null) {//过滤掉不再执行的表达式
                taskIdCronExpMap.put(String.valueOf(entry.getKey()), cronExpression);
            }
        }
        return taskIdCronExpMap;
    }

    public void insertTaskIdList2TaskList(Integer taskIdList) {
//        System.out.println(taskIdList);
        Logger.info(taskIdList.toString());
    }
}
