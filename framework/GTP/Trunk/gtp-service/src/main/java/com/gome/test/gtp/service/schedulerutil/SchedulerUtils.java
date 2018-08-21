package com.gome.test.gtp.service.schedulerutil;

import com.gome.test.gtp.service.executor.SpringContextHelper;
import com.gome.test.utils.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by lizonglin on 2016/2/2/0002.
 */
@Component
public class SchedulerUtils {
    @Autowired
    private SpringContextHelper springContextHelper;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public void loadSchedule(AbstractExecutor abstractExecutor, String schedulerKey, String triggerGroup, String expression) throws SchedulerException, ParseException {
        Logger.info("Load ExecutorJob: " + schedulerKey);
        CronExpression cronExpression = new CronExpression(expression);

        if (cronExpression.getNextValidTimeAfter(new Date()) != null) {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(schedulerKey, triggerGroup);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);


            if (null == trigger) {
                JobDetail jobDetail = JobBuilder
                        .newJob(abstractExecutor.getClass())
                        .setJobData(loadJobDataMap(abstractExecutor.getAutowiredClassList()))
                        .withIdentity(schedulerKey)
                        .build();//创建job

                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);//创建Cron

                TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder);
                trigger = triggerBuilder.build();

                scheduler.scheduleJob(jobDetail, trigger);//将job和trigger加入schedule

            } else {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

                trigger = trigger
                        .getTriggerBuilder()
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .build();

                scheduler.rescheduleJob(triggerKey, trigger);
            }
        }
    }

    //每个JobClass所需要的bean
    private JobDataMap loadJobDataMap(List<Class> classList) {
        JobDataMap jobDataMap = new JobDataMap();
        for (Class c : classList) {
            jobDataMap.put(c.getSimpleName(), springContextHelper.getBean(c));
        }
        return jobDataMap;
    }

    //暂停某个job
    public void pauseScheduler(String key) throws SchedulerException {
        Logger.info("暂停Job: " + key);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(key);
        scheduler.pauseJob(jobKey);
    }

    //删除job
    public void delScheduler(String key) throws SchedulerException {
        Logger.info("删除Job: " + key);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(key);
        scheduler.deleteJob(jobKey);
    }

    //更新job
    public void updateScheduler(String key, String group, CronExpression cronExpression) throws SchedulerException {
        Logger.info("尝试更新Job: " + key);
        if (cronExpression.getNextValidTimeAfter(new Date()) != null) {
            Logger.info("更新Job: " + key);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(key, group);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger != null) {
                trigger = trigger
                        .getTriggerBuilder()
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .build();

                scheduler.rescheduleJob(triggerKey, trigger);
            }
        }
    }

    //插入新job
    public void insertScheduler(String taskId, AbstractExecutor abstractExecutor, CronExpression cronExpression, String triggerKeyGroup) throws SchedulerException {
        Logger.info("尝试插入Job: " + taskId);
        if (cronExpression.getNextValidTimeAfter(new Date()) != null) {
            Logger.info("插入Job: " + taskId);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(taskId, triggerKeyGroup);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            if (null == trigger) {
                JobDetail jobDetail = JobBuilder
                        .newJob(abstractExecutor.getClass())
                        .setJobData(loadJobDataMap(abstractExecutor.getAutowiredClassList()))
                        .withIdentity(taskId)
                        .build();//创建job

                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);//创建Cron

                trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .build();//创建带Cron的trigger

                scheduler.scheduleJob(jobDetail, trigger);//将job和trigger加入schedule
            }
        }
    }

    //Once类型的sec min hr day mon ? 转换为一个过期时间
    public Date getEndDate(String cronExpression) {
        String[] dateArr = cronExpression.split(" ");
        int sec;
        int min;
        int hr;
        int day;
        int mon;
        int year;
        if (dateArr.length != 6) {
            return null;
        } else if (!dateArr[5].equals("?")){
            return null;
        } else {
            sec = 1;

            return new Date();
        }

    }

    @Test
    public void test() throws ParseException {
        String cron = "0 0 0 4 2 ? 2017";
        CronExpression cronExpression = new CronExpression(cron);
        Date date1 = cronExpression.getNextValidTimeAfter(new Date());
        Date date2 = cronExpression.getNextValidTimeAfter(date1);
        System.out.println(date1);
        System.out.println(date2);

    }
}
