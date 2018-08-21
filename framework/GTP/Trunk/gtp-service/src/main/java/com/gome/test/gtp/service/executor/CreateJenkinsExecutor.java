package com.gome.test.gtp.service.executor;


import com.gome.test.gtp.jenkins.JenkinsJobBo;
import com.gome.test.gtp.schedule.TaskInfoListBo;
import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import org.aspectj.lang.annotation.Aspect;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫描TaskList中状态为Assigned的Task，创建Jenkins Job
 */
@Aspect
@Component
public class CreateJenkinsExecutor extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(TaskScheduleJob.class);
        autowiredList.add(JenkinsJobBo.class);
        autowiredList.add(TaskInfoListBo.class);
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        TaskScheduleJob taskScheduleJob = (TaskScheduleJob) jobExecutionContext.getJobDetail().getJobDataMap().get(TaskScheduleJob.class.getSimpleName());

        taskScheduleJob.executeCreateJenkins();
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.createjenkins");
    }


}
