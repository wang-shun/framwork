package com.gome.test.gtp.schedule;

import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.ResponseQueueDao;
import com.gome.test.gtp.jenkins.JenkinsBo;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.Logger;
import com.gome.test.utils.StringUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhangjiadi on 16/7/11.
 */
@Component
public class JobServiceBo {
    @Autowired
    private ResponseQueueDao queueDao;
    @Autowired
    private AgentBo agentBo;
    @Autowired
    private JenkinsBo jenkinsBo;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private TaskScheduleJob taskScheduleJob;



    public void SendQueue(ResponseInfo responseInfo) {
        String[] ipList = responseInfo.getIp().split("[,]");
        String[] portList = responseInfo.getPort().split("[,]");


        for (int i = 0; i < ipList.length; i++) {
            String ip = ipList[i];
            String port = portList[i];
            if (ipList.length==1 || !StringUtils.isEmpty(ip)) {
                ResponseQueue queue = new ResponseQueue();
                queue.setAppInfoId(responseInfo.getAppInfoId());
                queue.setEnv(responseInfo.getEnv());
                queue.setIp(ip);
                queue.setJobName(queue.getJobName(responseInfo.getId()));
                queue.setLevel(responseInfo.getLevel());
                queue.setObjectTime(responseInfo.getObjectTime());
                queue.setStatus(Constant.JOB_ASSIGNED);
                queue.setPort(port);
                queue.setResponseinfoId(responseInfo.getId());
                queue.setReplaceHost(responseInfo.isReplaceHost());
                queue.setVersion(responseInfo.getVersion());
                queue.setAppName(responseInfo.getAppName());

                saveQueue(queue);
            }
        }
    }

    private void saveQueue(ResponseQueue queue) {
        try {
            queueDao.save(queue);//插入job队列
        } catch (Exception ex) {
            Logger.info("sava queue is error!");
        }
    }



    public void createJob(ResponseQueue queue,AppInfo appInfo, EnvInfo envInfo)  {

        /**
         * 获得机器名，指定机器或不指定机器；
         * 获取后都会更改机器的状态为Running
         */
        String agentLabel = agentBo.getJobAgentLabel(queue.getId());
        Logger.info("++++++++++++ get agent label is : " + agentLabel);
        /**
         * 根据是否获取到可用机器决定是否创建Job
         */
        if (!agentLabel.equals(Constant.CONFIG_AGENT_NULL_LABEL)) {//成功获取到可用机器
            /**
             * Job名为:TaskID_SplitCount_SplitIndex_时间戳
             */
            String jobName = queue.getJobName();
            Logger.info("++++++++++++ get job name is : " + jobName);
            int createStatus = 0;
            int buildStatus = 0;
            try {
                //取envid
                int  envId= queue.isReplaceHost()? lcdService.getValueByName(Constant.ENV,queue.getEnv().toUpperCase()) : 0 ;

//                Logger.info("++++++++++++ get env id is : " + envId);
                String replacedCfgStr = jenkinsBo.replaceConfigFile(jobName, agentLabel,String.valueOf(Constant.TASK_TYPE_JOB) ,appInfo.getSvn(),envId,envInfo);
//                Logger.info("+++++++++++get replace config file is : " + replacedCfgStr);
                createStatus = jenkinsBo.postAttachJenkinsMethod(replacedCfgStr, jobName, "Job");
                Logger.info("++++++++++++++ create status is : " + createStatus);

                if (createStatus == HttpStatus.SC_OK) {
                    buildStatus = buildJob(jobName);
                    if (buildStatus != HttpStatus.SC_CREATED) {
                        buildStatus = buildJob(jobName);//未启动成功会尝试再次启动一次该Job，仍不成功就更新状态为Aborted
                    }
                }

                if (createStatus == HttpStatus.SC_OK && buildStatus == HttpStatus.SC_CREATED) {
                    /**
                     *发送成功后更新TaskList状态为：SENT,并将分配的机器IP写入TaskList
                     */
                    Logger.info("+++++++++++++++ begin execute afterSent");
                    afterSent(queue, jobName, agentLabel);
                    Logger.info("+++++++++++++++ end execute afterSent");
                } else {
                    /**
                     * 如果发送或启动不成功，TaskList改为Aborted，
                     * 判断所有拆分的TaskList决定是否更新TaskInfo以及释放机器
                     */
                    Logger.info("+++++++++++++++ begin execute afterFailBuild");
                    afterFailBuild(queue, jobName, agentLabel);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (createStatus != HttpStatus.SC_OK || buildStatus != HttpStatus.SC_CREATED)
                    afterFailBuild(queue, jobName, agentLabel);
                Logger.info("++++++++ end  create job.");
            }

        }else{
            Logger.info("++++++++++++ agent label : " + agentLabel + " , can not use.");
        }
    }


    public static String getResponseStatus(int status)
    {
        String result="";
        switch (status)
        {
            case Constant.JOB_Initialization:
                result="数据已保存，等待进入执行队列";
                break;
            case Constant.JOB_WAINTING:
                result="已经进入队列，排队执行";
                break;
            case Constant.JOB_ASSIGNED:
                result="已经开始执行，执行尚未结束";
                break;
            case Constant.JOB_COMPLETED:
                result="正常执行结束";
                break;
//            case Constant.JOB_ABORTED:
//                result="超时执行结束,or运行失败";
//                break;
//            case Constant.JOB_ERROR:
//                result="执行发生异常";
//                break;
            case 80:
                result = "测试失败";
                break;
            case 90:
                result = "测试通过";
                break;
            default:
                result="状态未知，请等待";
                break;

        }

        return result;
    }

    public int buildJob(String jobName) {
        return jenkinsBo.postJenkinsMethod("/job/" + jobName + "/build?deplay=0sec");
    }

    public void afterFailBuild(ResponseQueue queue,String jobName,String agentLabel)
    {
        queue = queueDao.get(queue.getId());
        queue.setJobName(jobName);
        queue.setLogs(queue.getLogs() + "\n 向Jenkins发送失败，继续等待执行 ");
        queue.setEndTime(Util.nowTimestamp());
        queueDao.update(queue);

        agentBo.updateStatusByLabel(agentLabel, queue.getId(), Constant.AGENT_STATUS_IDLE);

    }

    public void afterSent(ResponseQueue queue,String jobName,String agentLabel)
    {
            queue = queueDao.get(queue.getId());
            queue.setStatus(Constant.JOB_RUNNING);
            queue.setJobName(jobName);
            queue.setLogs((StringUtils.isEmpty(queue.getLogs())?"":queue.getLogs())  + "\n 向Jenkins发送 ");
            queue.setEndTime(Util.nowTimestamp());
            queueDao.update(queue);
            //因为插件没有做好，所以先注释掉该句
           //agentBo.updateStatusByLabel(agentLabel, queue.getId(), Constant.AGENT_STATUS_RUNNING);
        //插件没有做好，直接在这里改机器的状态为未使用
            agentBo.updateStatusByLabel(agentLabel, queue.getId(), Constant.AGENT_STATUS_IDLE);

    }
}