package com.gome.test.gtp.schedule;

import com.gome.test.gtp.dao.AgentInfoDao;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.dao.TaskListDao;
import com.gome.test.gtp.model.AgentInfo;
import com.gome.test.gtp.model.TaskInfo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lizonglin on 2015/8/3/0003.
 */
@Component
public class AgentBo {
    @Autowired
    private AgentInfoDao agentInfoDao;
    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private TaskListDao taskListDao;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private TaskInfoListBo taskInfoListBo;

    public String getJobAgentLabel(int queueId) {
        String agentLabel = null;
        String type = "Job";

        //没有已占用机器的情况下找一台空闲机器
        List<AgentInfo> agentInfoList = agentInfoDao.getAll();
        Collections.sort(agentInfoList);
        for (AgentInfo agentInfo : agentInfoList) {
            if (agentInfo.getAgentStatus() == Constant.AGENT_STATUS_IDLE && agentInfo.getTaskType().toUpperCase().equals(type.toUpperCase())) {//空闲且类型匹配
                agentLabel = agentInfo.getAgentLabel();
                runJobAgentStatus(agentInfo.getAgentID(), queueId);
                break;
            }
        }


        if (agentLabel == null) {
            agentLabel = Constant.CONFIG_AGENT_NULL_LABEL;
        }

        return agentLabel;
    }


    public String getAgentLabel(String agentLabel, int taskId, int taskType, String browser) {
        //不指定机器的情况下
        if(agentLabel.equals(Constant.NONE_SPECIFIC_AGENT_LABEL)){//得到机器label或CONFIG_AGENT_NULL_LABEL
            agentLabel = getIdleAgent(taskId, taskType, browser);
            if (!agentLabel.equals(Constant.CONFIG_AGENT_NULL_LABEL))
                occupyByLabel(agentLabel, taskId);
            else
                Logger.info(taskId + " 暂时没有可用机器");
        }
        //指定机器的情况下
        else {//得到传入的机器label或CONFIG_AGENT_NULL_LABEL
            if(isAvailableByLabel(agentLabel, taskId)) {//GUI浏览器问题前台保存时已检验过；获取可用的机器（只有API允许一台机器多个子任务）
                List<AgentInfo> agentInfos = agentInfoDao.getAgentByLabel(agentLabel);
                if (agentInfos != null && agentInfos.size() > 0) {
                    if (taskType == Constant.TASK_TYPE_GUI) {
                        if (agentContainsBrowser(agentInfos.get(0).getBrower(), browser)) {//再次检查浏览器
                            occupyByLabel(agentLabel, taskId);
                        } else {
                            Logger.error(taskId + " 指定的机器 " + agentLabel + " 未配置所选浏览器 " + browser);
                            agentLabel = Constant.CONFIG_AGENT_NULL_LABEL;
                        }
                    } else {
                        occupyByLabel(agentLabel, taskId);
                    }

                } else {
                    Logger.error(taskId + "指定的机器 " + agentLabel + " 不存在");
                    agentLabel = Constant.CONFIG_AGENT_NULL_LABEL;
                }
            } else {
                Logger.info(taskId + " 指定的机器 " + agentLabel + " 已被占用");
                agentLabel = Constant.CONFIG_AGENT_NULL_LABEL;
            }
        }
        return agentLabel;
    }

    /**
     * 不指定机器的情况下选择可用机器
     * @param taskId
     * @param taskType
     * @param browser 需要的浏览器
     * @return
     */
    public String getIdleAgent(int taskId, int taskType, String browser) {

        String agentLabel = null;
        String type = lcdService.getNameByValue(Constant.DIC_TASK_TYPE, taskType);
//先清理对应task未占用
// //以及占用超过6个小时的机器为空闲
        for(AgentInfo agentInfo : agentInfoDao.getAll()) {
            if (agentInfo.getTaskId() == taskId && isEndTaskStatus(taskId)
//                    || agentInfo.getLastRunTime().getTime() - Util.nowTimestamp().getTime() > Constant.AGENT_STATUS_DURATION && agentInfo.getAgentStatus() != 0
                    ) {
                initAgentStatus(agentInfo.getAgentID());
            }
        }

//优先找已占用的机器，避免一个拆分后的任务占有所有机器(仅出现在拆分后的任务)；这种情况只允许API任务
        for(AgentInfo agentInfo : agentInfoDao.getAll()) {
            if (agentInfo.getTaskId() == taskId && agentInfo.getAgentStatus() == Constant.AGENT_STATUS_RUNNING && taskType == Constant.TASK_TYPE_API) {
                agentLabel = agentInfo.getAgentLabel();
                runAgentStatus(agentInfo.getAgentID(), taskId);
                break;
            }
        }

//没有已占用机器的情况下找一台空闲机器
        if (agentLabel == null) {
            List<AgentInfo> agentInfoList = agentInfoDao.getAll();
            Collections.sort(agentInfoList);
            for(AgentInfo agentInfo : agentInfoList) {
                if (agentInfo.getAgentStatus() == Constant.AGENT_STATUS_IDLE && agentInfo.getTaskType().equals(type)) {//空闲且类型匹配
                    if (taskType != Constant.TASK_TYPE_GUI) {//非GUI机器不用考虑浏览器
                        agentLabel = agentInfo.getAgentLabel();
                        runAgentStatus(agentInfo.getAgentID(), taskId);
                        break;
                    } else {//GUI机器需要考虑浏览器
                        if (agentContainsBrowser(agentInfo.getBrower(), browser)) {//实际上每个TaskList只会包含一种浏览器
                            agentLabel = agentInfo.getAgentLabel();
                            runAgentStatus(agentInfo.getAgentID(), taskId);
                            break;
                        }
                    }

                }
            }
        }

        if(agentLabel == null) {
            agentLabel = Constant.CONFIG_AGENT_NULL_LABEL;
        }

        return agentLabel;
    }

    /**
     * 指定机器的情况下判断机器是否可用
     * @param agentLabel
     * @param taskId
     * @return
     */
    public boolean isAvailableByLabel(String agentLabel, int taskId) {
        List<AgentInfo> agentInfos = agentInfoDao.getAgentByLabel(agentLabel);
        if(agentInfos != null && !agentInfos.isEmpty()) {
            AgentInfo agentInfo = agentInfos.get(0);
            if((agentInfo.getAgentStatus() == Constant.AGENT_STATUS_IDLE) ||//机器状态空闲
                    ((agentInfo.getTaskId() == taskId) &&
                            (agentInfo.getAgentStatus() == Constant.AGENT_STATUS_RUNNING) &&
                            (agentInfo.getTaskType().equals(lcdService.getNameByValue(Constant.DIC_TASK_TYPE, Constant.TASK_TYPE_API)))
                    )) {//允许同一机器同时接受多条TaskId相同的API任务
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Transactional
    public void occupyByLabel(String agentLabel, int taskId) {
        List<AgentInfo> agentInfos = agentInfoDao.getAgentByLabel(agentLabel);
        if(agentInfos != null && !agentInfos.isEmpty()) {
            AgentInfo agentInfo = agentInfoDao.get(agentInfos.get(0).getAgentID());
            agentInfo.setAgentStatus(Constant.AGENT_STATUS_RUNNING);
            agentInfo.setLastRunTime(Util.nowTimestamp());
            agentInfo.setTaskId(taskId);
            agentInfoDao.update(agentInfo);
        }
    }

    @Transactional
    public void revertByLabel(String agentLabel, int taskId) {
        List<AgentInfo> agentInfos = agentInfoDao.getAgentByLabel(agentLabel);
        if(agentInfos != null && !agentInfos.isEmpty()) {
            AgentInfo agentInfo = agentInfoDao.get(agentInfos.get(0).getAgentID());
            if (agentInfo.getTaskId() == taskId) {
                agentInfo.setAgentStatus(Constant.AGENT_STATUS_IDLE);
                agentInfoDao.update(agentInfo);
            }
        }
    }

    @Transactional
    public void updateByLabel(String agentLabel, int queueId) {
        List<AgentInfo> agentInfos = agentInfoDao.getAgentByLabel(agentLabel);
        if(agentInfos != null && !agentInfos.isEmpty()) {
            AgentInfo agentInfo = agentInfoDao.get(agentInfos.get(0).getAgentID());
            if (agentInfo.getQueueId() == queueId) {
                agentInfo.setAgentStatus(Constant.AGENT_STATUS_IDLE);
                agentInfo.setQueueId(0);
                agentInfoDao.update(agentInfo);
            }
        }
    }

    @Transactional
    public void updateStatusByLabel(String agentLabel, int queueId,int status) {
        List<AgentInfo> agentInfos = agentInfoDao.getAgentByLabel(agentLabel);
        if(agentInfos != null && !agentInfos.isEmpty()) {
            AgentInfo agentInfo = agentInfoDao.get(agentInfos.get(0).getAgentID());
            if (agentInfo.getQueueId() == queueId || agentInfo.getQueueId()==0 ) {
                agentInfo.setAgentStatus(status);
                agentInfo.setQueueId(queueId);
                agentInfoDao.update(agentInfo);
            }
        }
    }


    /**
     * 某Task占用机器，但该Task没有处于50和60状态（正常占用机器的两个状态）的TaskList
     * 由于占用机器和修改TaskList为50不是同时发生（中间有发送Jenkins过程），需要加一个过渡时间
     */
    public void amendAgentStatus(long sendJenkinsTime) {
        List<AgentInfo> runningAgentInfoList = agentInfoDao.getRunningAgentList();
        List occupyAgentTaskIdList = taskListDao.getOccupyAgentTaskId();

        List<Integer> occupyTaskIdList = new ArrayList<Integer>();
        if (occupyAgentTaskIdList != null && occupyAgentTaskIdList.size() > 0) {
            for (Object obj : occupyAgentTaskIdList) {
//                occupyTaskIdList.add(Integer.valueOf(String.valueOf(((Object[]) obj)[0])));
                occupyTaskIdList.add(Integer.valueOf(String.valueOf(obj)));
            }
        }

        for (AgentInfo agentInfo : runningAgentInfoList) {
            if (!occupyTaskIdList.contains(agentInfo.getTaskId())
                    && System.currentTimeMillis() - agentInfo.getLastRunTime().getTime() >  sendJenkinsTime) {//Agent->20 和 Task->50 相差发送Jenkins Job 时间
                initAgentStatus(agentInfo.getAgentID());
                Logger.info("[AmentExecutor] Agent(" + agentInfo.getAgentIp() + ") -> 10");
            }
        }
    }

    public String getAgentOSByLabel(String label) {
        List<AgentInfo> agentInfoList = agentInfoDao.getAgentByLabel(label);
        return agentInfoList.get(0).getAgentOS();
    }

    private void initAgentStatus(int agentId) {
        AgentInfo agentInfo = agentInfoDao.get(agentId);
        agentInfo.setAgentStatus(Constant.AGENT_STATUS_IDLE);
        agentInfoDao.update(agentInfo);
    }

    private void runAgentStatus(int agentId, int taskId) {
        AgentInfo agent = agentInfoDao.get(agentId);
        agent.setAgentStatus(Constant.AGENT_STATUS_RUNNING);
        agent.setTaskId(taskId);
        agent.setLastRunTime(Util.nowTimestamp());
        agentInfoDao.update(agent);
    }

    private void runJobAgentStatus(int agentId, int queueId) {
        AgentInfo agent = agentInfoDao.get(agentId);
        agent.setAgentStatus(Constant.AGENT_STATUS_RUNNING);
        agent.setQueueId(queueId);
        agent.setLastRunTime(Util.nowTimestamp());
        agentInfoDao.update(agent);
    }

    private boolean isEndTaskStatus(int taskId) {
        TaskInfo taskInfo = taskInfoDao.get(taskId);
        int taskStatus = taskInfo.getTaskStatus();
        if (taskStatus == Constant.JOB_DELETED || taskStatus == Constant.JOB_CREATED || taskStatus == Constant.JOB_COMPLETED ||
                taskStatus == Constant.JOB_ABORTED || taskStatus == Constant.JOB_ERROR || taskStatus == Constant.JOB_STOPPED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean agentContainsBrowser(int agentBrowser, String browser) {
        if (taskInfoListBo.getBrowserListByValue(agentBrowser).contains(browser)) {
            return true;
        } else {
            return false;
        }
    }
}
