package com.gome.test.gtp.service.schedulerutil;

import com.gome.test.utils.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lizonglin on 2016/2/1/0001.
 */
@Component
public abstract class AbstractExecutor implements Job {
    @Autowired
    protected Environment env;
    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }

    public abstract String getCronExp();

    public abstract List<Class> getAutowiredClassList();

    protected void logInfo() {
        Logger.info("<" + this.getClass().getSimpleName() + ">");
    }

    protected Object getAutowiredClass(JobExecutionContext jobExecutionContext, Class c) {
        return jobExecutionContext.getJobDetail().getJobDataMap().get(c.getSimpleName());
    }
}
