package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import com.gome.test.gtp.service.schedulerutil.SchedulerUtils;
import com.gome.test.gtp.service.taskschedule.CreateTaskListJob;
import com.gome.test.gtp.utils.Constant;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Map;

/**
 * 将所有的ExecutorClass 在工程启动时 加入Scheduler
 */
@Component
public class LoadExecutorSchedule {
    @Autowired
    private SpringContextHelper springContextHelper;
    @Autowired
    private SchedulerUtils schedulerUtils;

    @PostConstruct
    private void init() throws SchedulerException, ParseException {
        Map<String, AbstractExecutor> jobs = springContextHelper.getBeansOfType(AbstractExecutor.class);

        for (AbstractExecutor abstractExecutor : jobs.values()) {
            if (abstractExecutor.getClass() != CreateTaskListJob.class) {
                String className = abstractExecutor.getClass().getSimpleName();
                schedulerUtils.loadSchedule(abstractExecutor, className, Constant.TRIGGER_GROUP_EXECUTOR, abstractExecutor.getCronExp());
            }
        }
    }
}