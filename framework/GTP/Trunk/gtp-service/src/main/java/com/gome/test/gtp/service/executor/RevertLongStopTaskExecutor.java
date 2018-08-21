package com.gome.test.gtp.service.executor;


import com.gome.test.gtp.jenkins.JenkinsJobBo;
import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import org.dom4j.DocumentException;
import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

/**
 * 回退在某状态停留过久的task
 */
@Component
public class RevertLongStopTaskExecutor extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(TaskScheduleJob.class);
        autowiredList.add(JenkinsJobBo.class);
        autowiredList.add(Environment.class);
        return autowiredList;
    }

    /**
     * Sent -> Splitted
     *
     * @throws XPathExpressionException
     * @throws DocumentException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
//        logInfo();
//        TaskScheduleJob taskScheduleJob = (TaskScheduleJob) jobExecutionContext.getJobDetail().getJobDataMap().get(TaskScheduleJob.class.getSimpleName());
//        JenkinsJobBo jenkinsJobBo = (JenkinsJobBo) jobExecutionContext.getJobDetail().getJobDataMap().get(JenkinsJobBo.class.getSimpleName());
//        Environment env = (Environment) jobExecutionContext.getJobDetail().getJobDataMap().get(Environment.class.getSimpleName());
//
//        /**
//         * 先获取长时间Sent状态的 List<TaskList>,并检查Jenkins构建队列，如果有就删除该Job
//         */
//        List<TaskList> longSentList = taskScheduleJob.getLongStopTask(Constant.JOB_SENT, Long.valueOf(env.getProperty("expire.duration.longsent")), Constant.TASK_LIST_SENT_TIME);
//        if (longSentList != null && longSentList.isEmpty() == false) {
//            try {
//                jenkinsJobBo.checkAndDelLongWaitJob(longSentList);
//            } catch (XPathExpressionException e) {
//                e.printStackTrace();
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            }
//            /**
//             * 状态回退为Splitted
//             */
//            HashMap<Integer, Integer> waitStatusAndRevertToStatus = new HashMap<Integer, Integer>();
//            waitStatusAndRevertToStatus.put(Constant.JOB_SENT, Constant.JOB_SPLITTED);
//            taskScheduleJob.updateStatus(waitStatusAndRevertToStatus, longSentList);
//        }
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.revertlongstoptask");
    }
}
