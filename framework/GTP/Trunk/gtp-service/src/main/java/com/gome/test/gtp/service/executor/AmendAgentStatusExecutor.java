package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.schedule.AgentBo;
import com.gome.test.gtp.schedule.TaskInfoListBo;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizonglin on 2015/8/7/0007.
 * 修正Agent状态
 * 修正
 */
@Component
public class AmendAgentStatusExecutor extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(TaskInfoListBo.class);
        autowiredList.add(AgentBo.class);
        autowiredList.add(Environment.class);
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        AgentBo agentBo = (AgentBo) jobExecutionContext.getJobDetail().getJobDataMap().get(AgentBo.class.getSimpleName());
        Environment env = (Environment) jobExecutionContext.getJobDetail().getJobDataMap().get(Environment.class.getSimpleName());

        /**
         * 修正某占用机器的Task，但该Task没有处于50和60状态（正常占用机器的两个状态）的TaskList
         */
        agentBo.amendAgentStatus(Long.valueOf(env.getProperty("expire.duration.difftime")));
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.amendstatus");
    }


}
