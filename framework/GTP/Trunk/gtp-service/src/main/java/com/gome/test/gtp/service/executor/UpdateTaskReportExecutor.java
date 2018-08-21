package com.gome.test.gtp.service.executor;


import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.dao.mongodb.ReportDao;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UpdateTaskReportExecutor extends AbstractExecutor {


    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(ReportDao.class);
        autowiredList.add(TaskInfoDao.class);
        autowiredList.add(LoadConfigureDictionaryService.class);
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        TaskInfoDao taskInfoDao = (TaskInfoDao) jobExecutionContext.getJobDetail().getJobDataMap().get(TaskInfoDao.class.getSimpleName());
        LoadConfigureDictionaryService lcdService = (LoadConfigureDictionaryService) jobExecutionContext.getJobDetail().getJobDataMap().get(LoadConfigureDictionaryService.class.getSimpleName());
        ReportDao reportDao = (ReportDao) jobExecutionContext.getJobDetail().getJobDataMap().get(ReportDao.class.getSimpleName());

        Map<Integer, Integer> taskAndGroup = taskInfoDao.getTaskAndGroup();

        int day = Integer.valueOf(Util.dateToString(new java.util.Date(System.currentTimeMillis()), "yyyyMMdd"));
        List taskTypes = lcdService.getValueNameList("TaskType");
        for (Object taskType : taskTypes) {
            int taskTypeInt = Integer.valueOf(((Object[])taskType)[1].toString());
            reportDao.updateReportOwner(day-3, day, taskTypeInt);
            reportDao.updateReportGroup(day-3, day, taskTypeInt, taskAndGroup);
        }
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.updatetaskreport");
    }


}
