package com.gome.test.gtp.service.executor;


import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除6小时依然未完成的task
 * 同时判断机器是否已被这些任务占用超时
 */
@Component
public class AbortExpireTaskExecutor extends AbstractExecutor {

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
        /**
         * 更新TaskList，将Create时间超过xx S的TaskList状态改为Aborted
         */
        taskScheduleJob.executeAbortExpire();
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.abortexpiretask");
    }


}
