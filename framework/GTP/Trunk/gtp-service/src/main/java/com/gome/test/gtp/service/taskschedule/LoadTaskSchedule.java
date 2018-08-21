package com.gome.test.gtp.service.taskschedule;

import com.gome.test.gtp.schedule.ScheduleBo;
import com.gome.test.gtp.service.schedulerutil.SchedulerUtils;
import com.gome.test.gtp.utils.Constant;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by lizonglin on 2016/1/28/0028.
 *
 * 将所有的Task 在工程启动时 加入Scheduler
 *
 */
@Component
public class LoadTaskSchedule {
    @Autowired
    private ScheduleBo scheduleBo;
    @Autowired
    private SchedulerUtils schedulerUtils;

    @PostConstruct
    public void initTask() throws Exception {
        Map<String, CronExpression> taskIdCronExpMap = scheduleBo.loadTaskIdCronExpMap();

        if(taskIdCronExpMap == null){
            return;
        }
        for (String taskId : taskIdCronExpMap.keySet()) {
            String cronExpression = taskIdCronExpMap.get(taskId).getCronExpression();
//            if (cronExpression.split(" ")[cronExpression.split(" ").length].equals("?")){
//                Date date = new Date();
//                schedulerUtils.loadSchedule(CreateTaskListJob.class.newInstance(), taskId, Constant.TRIGGER_GROUP_TASKJOB, cronExpression);
//            }
            schedulerUtils.loadSchedule(CreateTaskListJob.class.newInstance(), taskId, Constant.TRIGGER_GROUP_TASKJOB, cronExpression);

        }
    }
}
