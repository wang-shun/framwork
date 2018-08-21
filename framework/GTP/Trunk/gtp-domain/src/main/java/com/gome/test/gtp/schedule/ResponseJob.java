package com.gome.test.gtp.schedule;

import com.gome.test.gtp.dao.AppInfoDao;
import com.gome.test.gtp.dao.EnvInfoDao;
import com.gome.test.gtp.dao.ResponseDao;
import com.gome.test.gtp.dao.TaskListDao;
import com.gome.test.gtp.dao.mongodb.MongoDBDao;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.report.JobMailService;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;
import sun.rmi.runtime.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangjiadi on 16/7/11.
 */
@Component
public class ResponseJob {
    @Autowired
    private ResponseDao responseDao;

    @Autowired
    private AppInfoDao appInfoDao;

    @Autowired
    private EnvInfoDao envInfoDao;

    @Autowired
    private JobServiceBo jobServiceBo;

    @Autowired
    private TaskListDao taskListDao;

    @Autowired
    private MongoDBDao mogoDBDao;

    @Autowired
    private JobMailService jobMailService;


    //set job execute time out limit.
    private final static Long MAX_JOB_RUN_TIME = 1000*60*10L;
    /**
     * 执行向拆分ResponseInfo
     * 由于每5秒执行一次
     *
     *
     */
//    @Transactional
    public synchronized void executeCreateQueue() {
        Logger.info("******************** ResponseJob execute.");
        List<ResponseInfo> splitTaskList = getAllResponseInfoByStatus(Constant.JOB_Initialization);
        for (ResponseInfo responseInfo : splitTaskList) {
            jobServiceBo.SendQueue(responseInfo);
            responseInfo.setStatus(Constant.JOB_WAINTING);
            responseDao.update(responseInfo);//修改状态
        }
        Logger.info("******************** ResponseJob end.");
    }

    /**
     * 每5秒执行一次
     *
     *
     */
    public synchronized void executeUpdateResponseStatue()
    {
        Logger.info("******************************do  UpdateJob Executor------------------");
        List<ResponseInfo> splitTaskList = getAllResponseInfoByStatus(Constant.JOB_WAINTING);
        for (ResponseInfo responseInfo : splitTaskList) {
            List<ResponseQueue> queueList=getAllResponseQueueByResponseId(responseInfo.getId());
            boolean isAllEnd=true;
            boolean isTimeout = false;
            boolean isPassTest = false , isAllComplet = true;
            for(ResponseQueue queue : queueList)
            {
                if(queue.getStatus() == Constant.JOB_RUNNING){
                    Long begintime = queue.getEndTime().getTime();
                    Long diff = System.currentTimeMillis()-begintime;
//                    System.out.println("==============================> get diff time is : " + diff);
                    if(diff>MAX_JOB_RUN_TIME){
                        //job over time or fail. then update db ;
                        Logger.error("Response queue ID " + queue.getId() + " has over max execution time.");
//                        responseInfo.setStatus(80);
//                        responseDao.update(responseInfo);//修改状态
                        // update response queue set status is timeout.
                        taskListDao.updateTaskJobStuatus(queue.getJobName(), "jenkins execution time out.", Constant.JOB_TIMEOUT);
                        isTimeout= true;
                        break;
//                        continue;
                    }

                }
                isAllEnd = (queue.getStatus()==Constant.JOB_COMPLETED || queue.getStatus()==Constant.JOB_ABORTED
                           || queue.getStatus()==Constant.JOB_ERROR )? isAllEnd : false;

                isAllComplet = (queue.getStatus()==Constant.JOB_COMPLETED )? isAllComplet : false;
            }
            if(isAllEnd || isTimeout)
            {

                if(isAllComplet){
                    // get all split app end result.
                    isPassTest = mogoDBDao.getAllIsPassTest(queueList);
                    if(isPassTest){
                        responseInfo.setStatus(90);
                    }else{
                        responseInfo.setStatus(80);
                    }

                }else{
                    //test fail.
                    responseInfo.setStatus(80);

                }
                responseDao.update(responseInfo);//修改状态
                // 发送邮件
                Logger.info("----------begin send mail.----------------- response id is : " + responseInfo.getId());
                sendMailByResponseID(responseInfo.getId());
            }
        }

    }


    /**
     * send mail
     * @param appName
     * @param taskId
     */
    private synchronized void sendMail(String appName , String taskId){
        List<String> mailtoList = getMailListByAppName(appName);
        for(String to : mailtoList){
            Logger.info("======> mail to " + to);
        }
        List<JobReport> reports = mogoDBDao.getJobList(taskId);
        if(reports != null && reports.size()>0){
            jobMailService.sendJobReport(reports.get(0) , mailtoList , appName);
        }else{
            JobReport errReport = new JobReport();
            Long current = System.currentTimeMillis();
            errReport.setStartTime(current-MAX_JOB_RUN_TIME);
            errReport.setEndTime(current+MAX_JOB_RUN_TIME);
            errReport.setPass(0);
            errReport.setDuration(MAX_JOB_RUN_TIME);
            errReport.setTaskName(appName);
            errReport.setTotalCases(1);
            errReport.setErrorMessage("Job execute time out or end plugin execute fail.");
            CaseResult  result = new CaseResult();
            CaseResult results[] = new CaseResult[1];

            result.setTestCaseName("All");
            result.setStackTrace("All case executed time out.");

            results[0] = result;
            errReport.setDetails(results);

            jobMailService.sendJobReport(errReport, mailtoList , appName);
            Logger.error("Get job report by task id fail.");
        }

    }


    @Test
    public void testInitReport(){
        JobReport errReport = new JobReport();
        errReport.setPass(0);
        errReport.setDuration(MAX_JOB_RUN_TIME);
        errReport.setTaskName("test");
        errReport.setTotalCases(1);
        errReport.setErrorMessage("Job execute time out or end plugin execute fail.");
        CaseResult  result = new CaseResult();
        CaseResult results[] = new CaseResult[1];

        result.setTestCaseName("All");
        result.setStackTrace("All case executed time out.");

        results[0] = result;
        errReport.setDetails(results);

        CaseResult test[] = errReport.getDetails();
        if(results!=null && results.length>0) {
            for (CaseResult tmp : test) {

                System.out.println(tmp.getTestCaseName());
                System.out.println(tmp.getStackTrace());
            }
        }
        System.out.println("get size is : "+ errReport.getDetails().length);

    }
    public synchronized  void sendMailByResponseID(int responseID){

        List<ResponseQueue> queues = getAllResponseQueueByResponseId(responseID);

//        System.out.println("-------------get response queue size is : " + queues.size());
        for(ResponseQueue queue : queues){
            String appName = queue.getAppName();
            String taskId = queue.getJobName();
            Logger.info("------get response app name is :  " + appName);
            Logger.info("------get response task id is :  " + taskId);
            sendMail(appName , taskId);
        }
    }


    /**
     * get mail list by appInfo
     * @param appName
     * @return
     */
    private List<String> getMailListByAppName(String appName){
        List<AppInfo> appInfos =getAppinfoByName(appName);
        List<String> mailList = new ArrayList<String>();
        if(appInfos !=null && appInfos.size()>0){
            AppInfo info = appInfos.get(0);
            String mails = info.getMailList();
            if(!mails.isEmpty()){
                for(String mail : mails.split(",")){
                    mailList.add(mail);
                }

            }

        }
        return mailList;
    }


    public synchronized void SendJob()
    {
//        Logger.info("*****************send job begin.------------------");
        List<ResponseQueue> queueList= getAllResponseQueueByStatus(Constant.JOB_ASSIGNED);
//        Logger.info("------------send job " + queueList.size());
        for(ResponseQueue queue:queueList)
        {
            AppInfo appInfoList=appInfoDao.get(queue.getAppInfoId());
//            Logger.info("------------get app id is: " + appInfoList.getId());
            EnvInfo envInfo=getEnvInfo(appInfoList.getId(),queue.getEnv());
//            Logger.info("------------get env zk id is: " + envInfo.getZkAddress());
            jobServiceBo.createJob(queue,appInfoList,envInfo);
        }

//        Logger.info("*************send job end.------------------");
    }

    private  EnvInfo getEnvInfo(int appInfoId,String env)
    {
        String sql = String.format(" select * from envinfo where appInfoId=%d and env='%s' ", appInfoId,env);
        //fix bug weijx
        List<EnvInfo> envInfos = responseDao.sqlQuery(sql, EnvInfo.class);
        if(envInfos != null && envInfos.size()>0) {
            return (EnvInfo) responseDao.sqlQuery(sql, EnvInfo.class).get(0);
        }else{
            Logger.info("== ==== == sql => " + sql);
           Logger.info("ENV get is null.");
            return null;
        }
    }

    private  List<ResponseQueue> getAllResponseQueueByStatus(int status)
    {

//        String sql = String.format("select id , responseinfoId , jobName , appInfoId , appName ,  version ,objectTime ,ip, port , level ,env , status , replacehost, StartTime ,EndTime  from response_queue where status=%d ", status);
        String sql = String.format("select *from response_queue where status=%d for update ", status);

        return responseDao.sqlQuery(sql, ResponseQueue.class);

    }

//    private  List<ResponseQueue> getAllResponseQueueByStatus2(int status)
//    {
//        String sql = String.format("select * from response_queue where status=%d  for update", status);
//        return responseDao.sqlQuery(sql, ResponseQueue.class);
//
//    }

    private  List<ResponseQueue> getAllResponseQueueByResponseId(int id)
    {
       String sql = String.format(" select * from response_queue where responseinfoId=%d for update ", id);
//        String sql = String.format("select id , responseinfoId , jobName , appInfoId , appName ,  version ,objectTime ,ip, port , level ,env , status , replacehost, StartTime ,EndTime  from response_queue where responseinfoId=%d ", id);

        return responseDao.sqlQuery(sql, ResponseQueue.class);
    }

    private synchronized List<ResponseInfo> getAllResponseInfoByStatus(int status) {
        String sql = String.format("select * from responseInfo where status=%d for update", status);
        return responseDao.sqlQuery(sql, ResponseInfo.class);
    }

//    public synchronized void updateJobStatus(String objectId , String logs , int status ){
//        taskListDao.updateTaskJobStuatus(objectId , logs ,status);
//    }
private synchronized List<AppInfo> getAppinfoByName(String appName)
{
    String sql = String.format(" select * from appInfo where appName='%s'  for update", appName);
    return responseDao.sqlQuery(sql, AppInfo.class);
}

}
