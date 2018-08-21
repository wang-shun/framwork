package com.gome.test.gtp.jenkins;

import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.TaskListDao;
import com.gome.test.gtp.model.TaskList;
import com.gome.test.gtp.schedule.AgentBo;
import com.gome.test.gtp.schedule.TaskScheduleJob;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.utils.Logger;
import com.gome.test.utils.XmlUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizonglin on 2015/5/6.
 */
@Service
public class JenkinsJobBo {
    @Autowired
    private TaskListDao taskListDao;
    @Autowired
    private TaskScheduleJob taskScheduleJob;
    @Autowired
    private JenkinsBo jenkinsBo;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private AgentBo agentBo;

    /**
     * 根据taskId 创建 jenkins Job & 更新TaskList（jobName，time，status等）
     * Http Status Code：200
     *
     * @param taskList
     * @throws IOException
     */
    public void createJob(TaskList taskList)  {
        /**
         * 获取TaskType
         */
        Object[] taskInfos = (Object[]) taskListDao.getTaskInfoByTaskListId(taskList.getId()).get(0);

        /**
         * 获得机器名，指定机器或不指定机器；
         * 获取后都会更改机器的状态为Running
         */
        String agentLabel =
                agentBo.getAgentLabel(String.valueOf(taskInfos[5]),
                        Integer.valueOf(String.valueOf(taskInfos[7])),
                        Integer.valueOf(String.valueOf(taskInfos[1])),
                        taskList.getBrowser());

        /**
         * 根据是否获取到可用机器决定是否创建Job
         */
        if (!agentLabel.equals(Constant.CONFIG_AGENT_NULL_LABEL)) {//成功获取到可用机器
            /**
             * Job名为:TaskID_SplitCount_SplitIndex_时间戳
             */
            String jobName = String.format("%d_%d_%d_%d",
                    taskList.getTaskID(),
                    taskList.getSplitCount(),
                    taskList.getSplitIndex(),
                    System.currentTimeMillis());

            int createStatus = 0;
            int buildStatus = 0;
            try {
                String replacedCfgStr = jenkinsBo.replaceConfigFile(taskList.getId(), jobName, agentLabel, taskInfos);
                createStatus = jenkinsBo.postAttachJenkinsMethod(replacedCfgStr,
                        jobName,
                        lcdService.getNameByValue(Constant.DIC_TASK_TYPE, Integer.valueOf(taskInfos[1].toString())));
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
                    taskScheduleJob.afterSent(taskList, jobName, agentLabel);
                } else {
                    /**
                     * 如果发送或启动不成功，TaskList改为Aborted，
                     * 判断所有拆分的TaskList决定是否更新TaskInfo以及释放机器
                     */
                    taskScheduleJob.afterFailBuild(taskList, agentLabel);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (createStatus != HttpStatus.SC_OK || buildStatus != HttpStatus.SC_CREATED)
                    taskScheduleJob.afterFailBuild(taskList, agentLabel);
            }

        }
    }

    public int findJob(String jobName) {
        return jenkinsBo.simpleGetJenkinsMethod("/job/" + jobName);
    }

    public int buildJob(String jobName) {
        return jenkinsBo.postJenkinsMethod("/job/" + jobName + "/build?deplay=0sec");
    }

    public int deleteJob(String jobName) {
        return jenkinsBo.postJenkinsMethod("/job/" + jobName + "/doDelete");
    }

    public String getCurrentJobNum(String jobName) throws XPathExpressionException, DocumentException {
        String response = jenkinsBo.getJenkinsMethod("/job/" + jobName + "/api/xml").getResponse();
        String xpathStr = "//build[1]/number[1]";
        NodeList buildNum = XmlUtils.getXMLNode(response, xpathStr);
        if (buildNum.getLength() == 1) {
            return buildNum.item(0).getTextContent();
        } else {
            return "0";
        }
    }

    /**
     * Stop成功：返回200
     *
     * @param jobName
     */
    public int stopJob(String jobName) {
        int httpStatus = 0;
        try {
            httpStatus = jenkinsBo.postJenkinsMethod("/job/" + jobName + "/" + Constant.JOB_DEFAULT_NUM + "/stop");
        } catch (Exception ex) {
            Logger.error("Stop Jenkins Job 失败 " + ex.getMessage());
        }
        return httpStatus;
    }

    /**
     * 获取在当前构建队列中排队的 List<jobName>
     *
     * @return
     * @throws XPathExpressionException
     * @throws DocumentException
     */
    public List getBuildQueue() throws XPathExpressionException, DocumentException {
        List<String> buildList = new ArrayList<String>();
        String response = jenkinsBo.getJenkinsMethod("/queue/api/xml").getResponse();
        String xpathExp = "//task//name";
        NodeList jobInQueue = jenkinsBo.getXMLNode(response, xpathExp);
//        System.out.println("list size: " + jobInQueue.getLength());
        Logger.info("list size: " + jobInQueue.getLength());
        for (int i = 0; i < jobInQueue.getLength(); i++) {
            buildList.add(jobInQueue.item(i).getTextContent());
//            System.out.println(i + "th Node Content: " + jobInQueue.item(i).getTextContent());
            Logger.info(i + "th Node Content: " + jobInQueue.item(i).getTextContent());
        }
        return buildList;
    }

    public void checkAndDelLongWaitJob(List<TaskList> longWaitTaskList) throws XPathExpressionException, DocumentException {
        List<String> jobNameList = getBuildQueue();

        String jobName;
        for (TaskList taskList : longWaitTaskList) {
            jobName = taskList.getJobName();
            if (!jobNameList.contains(taskList.getJobName())) {
                deleteJob(jobName);
            }
        }
    }

    public SynInfo getSynInfo(String jobName) {
        ResponseInfo responseInfo = jenkinsBo.getJenkinsMethod(String.format("/job/%s/1/api/xml", jobName));
        String xpathExpBuilding = "//building";
        String xpathExpResult = "//result";
        String xpathExpQueueItem = "//queueItem";
        NodeList buildingNode;
        SynInfo synInfo = new SynInfo();
        try {
            if (responseInfo.getHttpCode() == HttpStatus.SC_OK) {
                buildingNode = jenkinsBo.getXMLNode(responseInfo.getResponse(), xpathExpBuilding);
                boolean isBuilding = Boolean.valueOf(buildingNode.item(0).getTextContent());
                synInfo.setJobName(jobName);
                synInfo.setBuilding(isBuilding);
                if (!isBuilding) {
                    synInfo.setResult(jenkinsBo.getXMLNode(responseInfo.getResponse(), xpathExpResult).item(0).getTextContent());
                }
                synInfo.setQueueItem(jenkinsBo.getXMLNode(responseInfo.getResponse(), xpathExpQueueItem).getLength());
            } else {
                synInfo.setJobName(jobName);
                synInfo.setBuilding(false);
                synInfo.setResult(Constant.JOB_RESULT_FAILURE);
                synInfo.setQueueItem(0);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return synInfo;
    }

//    public static void main(String[] args) throws IOException, XPathExpressionException, DocumentException {
//        JenkinsJobBo cjj = new JenkinsJobBo();

//        long timestamp = System.currentTimeMillis();

//        TaskList taskList = new TaskList();
//        taskList.setTaskID(999);
//        taskList.setId(2220);
//        taskList.setSplitCount(3);
//        taskList.setSplitIndex(2);
//        cjj.createJob(taskList);

//        cjj.buildJob("api");
//        cjj.buildJob("test_create_job_01");
//        cjj.buildJob("Testproject");
//        cjj.buildJob("client_post_create_job_01");


//        cjj.checkBuildQueue();

//        String temp = "test/#test#/test/#testtest#/test";
//        String temp = "https://repo.ds.gome.com.cn:8443/svn/test/SourceCode/sample/Branches/APITestV2.0.0/TestCase/CaseCategory/";
//        Map<String, String> repMap = new HashMap<String,String>();
//        repMap.put("test","TEST");
//        repMap.put("testtest","TESTTEST");
//        while (temp.indexOf("/#") != -1 || temp.indexOf("#/") != -1) {
//            int indexBegin = temp.indexOf("/#");
//            int indexEnd = temp.indexOf("#/");
//            String key = temp.substring(indexBegin + 2, indexEnd);
//            System.out.println(indexBegin + ":" + indexEnd + ":" + key);
//            String repStr = temp.substring(indexBegin, indexEnd + 2);
//            temp = temp.replaceAll(repStr, repMap.get(key));
//        }

//        System.out.println(temp.substring(temp.indexOf("/", 10),temp.length()));
////        String out = temp.replaceAll(temp)
//    }

}
