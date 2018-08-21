package com.gome.test.gtp.service.taskschedule;

import com.gome.test.gtp.schedule.ScheduleBo;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import com.gome.test.gtp.service.schedulerutil.SchedulerUtils;
import com.gome.test.gtp.utils.Constant;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

/**
 * Created by lizonglin on 2016/1/28/0028.
 */
@Component
public class UpdateTaskScheduleExecutor extends AbstractExecutor {
    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(ScheduleBo.class);
        autowiredList.add(SchedulerFactoryBean.class);
        autowiredList.add(SchedulerUtils.class);
        return autowiredList;
    }
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        ScheduleBo scheduleBo = (ScheduleBo) jobExecutionContext.getJobDetail().getJobDataMap().get(ScheduleBo.class.getSimpleName());
        SchedulerFactoryBean schedulerFactoryBean = (SchedulerFactoryBean) jobExecutionContext.getJobDetail().getJobDataMap().get(SchedulerFactoryBean.class.getSimpleName());
        SchedulerUtils schedulerUtils = (SchedulerUtils) jobExecutionContext.getJobDetail().getJobDataMap().get(SchedulerUtils.class.getSimpleName());
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            //DB当前实际的<TaskID, CronExpression>,已过滤掉永远不会执行的Cron
            Map<String, CronExpression> dbTaskIdCronExpMap = scheduleBo.loadTaskIdCronExpMap();

            //获取 Scheduler 中 jobClass 为 CreateTaskListJob 的 <SchedulerKey, CronExpression>
            Map<String, CronExpression> currentTaskIdCronExpMap = new HashMap<String, CronExpression>();

            GroupMatcher<JobKey> matcher = GroupMatcher.anyGroup();
            Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
//            Logger.info("Current JobKeySet: " + jobKeySet.toString());
            Set<JobDetail> jobDetailSet = new HashSet<JobDetail>();
            for (JobKey jobKey : jobKeySet) {
                jobDetailSet.add(scheduler.getJobDetail(jobKey));
            }
//            Logger.info("==========TaskScheduler==========");
            for (JobDetail jobDetail : jobDetailSet) {
                if (jobDetail.getJobClass().getName().equals(CreateTaskListJob.class.getName())) {
                    TriggerKey triggerKey = TriggerKey.triggerKey(jobDetail.getKey().getName(), Constant.TRIGGER_GROUP_TASKJOB);
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    CronExpression cronExpression = new CronExpression(trigger.getCronExpression());

//                    Logger.info(jobDetail.getKey().getName() + " : " + cronExpression);

                    currentTaskIdCronExpMap.put(jobDetail.getKey().getName(), cronExpression);
                }
            }

            //遍历DB当前实际的<TaskID, CronExpression>
            for (String taskId : dbTaskIdCronExpMap.keySet()) {
                if (currentTaskIdCronExpMap.containsKey(taskId)) {//查看当前Scheduler中是否已存在该Scheduler
                    if (!dbTaskIdCronExpMap.get(taskId).getCronExpression().equals(currentTaskIdCronExpMap.get(taskId).getCronExpression())){//CronExpression不同的更新
                        schedulerUtils.updateScheduler(taskId, Constant.TRIGGER_GROUP_TASKJOB, dbTaskIdCronExpMap.get(taskId));
                    }
                } else {
                    schedulerUtils.insertScheduler(taskId, CreateTaskListJob.class.newInstance(), dbTaskIdCronExpMap.get(taskId), Constant.TRIGGER_GROUP_TASKJOB);//不存在的插入
                }
            }

            //遍历Current<SchedulerKey, CronExpression>
            for (String schedulerKey : currentTaskIdCronExpMap.keySet()) {
                if (!dbTaskIdCronExpMap.containsKey(schedulerKey)
                        || currentTaskIdCronExpMap.get(schedulerKey).getNextValidTimeAfter(new Date()) == null) {//当前数据库中没有的删除,CronExpression表示的时间再也不会执行的删除
                    schedulerUtils.delScheduler(schedulerKey);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.updatescheduler");
    }


}
