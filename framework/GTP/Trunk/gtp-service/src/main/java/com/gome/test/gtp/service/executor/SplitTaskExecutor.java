package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizonglin on 2015/5/12.
 * <p/>
 * 定时拆分TaskList中状态为Waiting的Task，并将原Task和拆分后的Tasks都更新为Splitted
 * 拆分task
 */
@Component
public class SplitTaskExecutor extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(TaskScheduleJob.class);
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        TaskScheduleJob taskScheduleJob = (TaskScheduleJob) jobExecutionContext.getJobDetail().getJobDataMap().get(TaskScheduleJob.class.getSimpleName());
        taskScheduleJob.executeSplitTask();
    }

    @Override
    public String getCronExp() {
        String splitTaskCronExp = env.getProperty("executor.cronexp.splittask");
        return splitTaskCronExp;
    }
}