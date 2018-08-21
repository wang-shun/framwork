package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.jenkins.JenkinsJobBo;
import com.gome.test.gtp.model.TaskList;
import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import com.gome.test.gtp.utils.Constant;
import org.apache.commons.httpclient.HttpStatus;
import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteJenkinsExecutor extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(TaskScheduleJob.class);
        autowiredList.add(JenkinsJobBo.class);
        autowiredList.add(Environment.class);
        return autowiredList;
    }
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
/**
 * 定期清理Jenkins Job（Jenkins没有该功能，且删文件不起作用）
 */
        TaskScheduleJob taskScheduleJob = (TaskScheduleJob) jobExecutionContext.getJobDetail().getJobDataMap().get(TaskScheduleJob.class.getSimpleName());
        JenkinsJobBo jenkinsJobBo = (JenkinsJobBo) jobExecutionContext.getJobDetail().getJobDataMap().get(JenkinsJobBo.class.getSimpleName());
        Environment env = (Environment) jobExecutionContext.getJobDetail().getJobDataMap().get(Environment.class.getSimpleName());

        List<TaskList> expireJobList = taskScheduleJob.getExpireJenkinsJob(Constant.TASK_LIST_SENT_TIME, Long.valueOf(env.getProperty("expire.duration.jenkinsjob")));
        for (TaskList taskList : expireJobList) {
            int getStatus = jenkinsJobBo.findJob(taskList.getJobName());
            if (getStatus == HttpStatus.SC_OK) {
                if (jenkinsJobBo.deleteJob(taskList.getJobName()) == HttpStatus.SC_OK) {
                    taskScheduleJob.updateTaskListAfterDelJob(taskList.getId(), Constant.DELETED_JENKINS_JOB_NAME);
                }
            } else if (getStatus == HttpStatus.SC_NOT_FOUND){
                taskScheduleJob.updateTaskListAfterDelJob(taskList.getId(), Constant.DELETED_JENKINS_JOB_NAME);
            }
        }
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.deleteexpirejob");
    }
}
