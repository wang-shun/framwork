package com.gome.test.gtp.service.taskschedule;

import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import com.gome.test.gtp.utils.Constant;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizonglin on 2016/1/28/0028.
 */
@Component
public class CreateTaskListJob extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(TaskScheduleJob.class);
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logInfo();
        try {
            TaskScheduleJob taskScheduleJob = (TaskScheduleJob) context.getJobDetail().getJobDataMap().get(TaskScheduleJob.class.getSimpleName());
            String taskIdStr = context.getJobDetail().getKey().getName();
            int taskId = Integer.valueOf(taskIdStr);
            /**
             * 只是简单的将 TaskInfo<taskId> 插入 TaskList
             */
            taskScheduleJob.createTask(taskId, Constant.ENQUEUE_BY_SCHEDULER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCronExp() {
        return null;
    }


}
