package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.schedule.ResponseJob;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiadi on 16/7/12.
 */
@Component
public class SendJobService  extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(ResponseJob.class);
        return autowiredList;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        ResponseJob responseJob = (ResponseJob) jobExecutionContext.getJobDetail().getJobDataMap().get(ResponseJob.class.getSimpleName());
        responseJob.SendJob();
    }

    @Override
    public String getCronExp() {

        return env.getProperty("executor.cronexp.sendJobService");

    }
}
