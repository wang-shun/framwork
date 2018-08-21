package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.schedule.TaskInfoListBo;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizonglin on 2015/12/7/0007.
 */
@Component
public class SynJenkinsDBStatusExecutor extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(TaskInfoListBo.class);
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        TaskInfoListBo taskInfoListBo = (TaskInfoListBo) jobExecutionContext.getJobDetail().getJobDataMap().get(TaskInfoListBo.class.getSimpleName());
        /**
         * 检查sent 或 running状态并且以发送时间超过20秒的TaskList，去询问Jenkins
         * 如果找不到或找到了已结束的job则修改TaskList、TaskInfo、AgentInfo的状态
         */
        taskInfoListBo.synNonEndTask();
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.synstatus");
    }


}
