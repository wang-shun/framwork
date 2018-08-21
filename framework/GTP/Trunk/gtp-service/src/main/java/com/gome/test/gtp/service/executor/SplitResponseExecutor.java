package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.schedule.ResponseJob;
import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import com.gome.test.utils.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiadi on 16/7/11.
 */
@Component
public class SplitResponseExecutor extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(ResponseJob.class);
        return autowiredList;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        Logger.info("---------------enter SplitResponseExecutor");
        ResponseJob responseJob = (ResponseJob) jobExecutionContext.getJobDetail().getJobDataMap().get(ResponseJob.class.getSimpleName());
        Logger.info("---------------after get instance.");
        responseJob.executeCreateQueue();
        Logger.info("---------------end execute job.");
    }

    @Override
    public String getCronExp() {

        return env.getProperty("executor.cronexp.splitJobService");

    }
}
