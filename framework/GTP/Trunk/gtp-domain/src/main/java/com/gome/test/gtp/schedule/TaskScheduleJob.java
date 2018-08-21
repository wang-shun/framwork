package com.gome.test.gtp.schedule;

import com.gome.test.gtp.dao.*;
import com.gome.test.gtp.dao.mongodb.ReportDao;
import com.gome.test.gtp.jenkins.JenkinsJobBo;
import com.gome.test.gtp.jenkins.TaskStatusBo;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.CsvUtils;
import com.gome.test.utils.Logger;
import com.gome.test.utils.SvnUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNURL;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

@Component
public class TaskScheduleJob {
    @Autowired
    private TaskListDao taskListDao;

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private AgentInfoDao agentInfoDao;

    @Autowired
    private JenkinsJobBo jenkinsJobBo;

    @Autowired
    private Environment env;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private QueueDao queueDao;

    @Autowired
    private LoadConfigureDictionaryService lcdService;

    @Autowired
    private TaskStatusBo taskStatusBo;

    @Autowired
    private AgentBo agentBo;

    @Autowired
    private TaskInfoListBo taskInfoListBo;

    /**
     * 执行拆分
     * 由于每5秒执行一次（新线程），如果5秒内没有拆分并更新完所有的TaskList，下一个线程可能会重复拆分
     * 需要对数据库加锁，并同步执行
     * （可以每次只选一个TaskList，但效率较低）
     */
//    @Transactional
    public synchronized void executeSplitTask() {
        List<TaskList> taskList = getAllTaskInfoIdByStatus(Constant.JOB_WAINTING);//SQL中已按SplitTime GUID 两次排序
        if (taskList != null) {
            Map<String, Long> taskRunTimes = getLastRunTime(taskList);
            //保证同一批进行拆分的TaskList（无论是否需要拆分）的SplitTime相同
            Timestamp splitTime = Util.nowTimestamp();
            for (TaskList task : taskList) {
                TaskInfo taskInfo = getTaskInfoById(task.getTaskID());
                if (taskInfo != null) {
                    if (taskInfo.getSplit() && taskInfo.getTaskType() != Constant.TASK_TYPE_LOAD) {//标记为split的非load task
                        //同一条TaskList拆分的子TaskList，SplitTIme相同
                        saveSplittedTaskList(task.getId(), getSplittedTaskList(task, taskRunTimes, splitTime), taskInfo);
                    } else {//标记为非split或load的task
                        //相同的SplitTime，如GUI任务多浏览器情况需要相同的SplitTime进行标记
                        updateUnsplittedTaskList(task.getId(), taskInfo, splitTime);
                    }
                }
            }
        }
    }

    /**
     * 执行向Jenkins发送Job
     * 由于每5秒执行一次（新线程），如果5秒内没有发送并更新完所有的TaskList，下一个线程可能会重复发送
     * 需要对数据库加锁，并同步执行
     * （可以每次只选一个TaskList，但效率较低）
     */
//    @Transactional
    public synchronized void executeCreateJenkins() {
        List<TaskList> splitTaskList = getAllTaskInfoIdByStatus(Constant.JOB_SPLITTED);//SQL中已按SplitTime GUID 两次排序
        for (TaskList tasklist : splitTaskList) {
            jenkinsJobBo.createJob(tasklist);//需要判断拆分后的同TaskId的TaskList是否都已发送到Jenkins
        }
    }

    public synchronized void executeAbortExpire() {
        /**
         * 超时的TaskList TaskInfo Agent
         */
        updateExpireTask(Long.valueOf(env.getProperty("expire.duration.longstuck")), false);
        updateExpireTask(Long.valueOf(env.getProperty("expire.duration.longstuckload")), true);

    }

    public TaskInfoForSchedule getTaskInfoForScheduleById(int taskId) {
        List list = taskInfoDao.getTaskMainInfoById(taskId);
        TaskInfoForSchedule task = new TaskInfoForSchedule();

        if (list.size() > 0) {
            Object[] row = (Object[]) list.get(0);
            task.setTaskID(Integer.valueOf(row[0].toString()));
            task.setTaskName(row[1].toString());
            task.setExcuteInfo(row[2].toString());
            task.setBranchUrl(row[3].toString());
            task.setAgentIP(row[4].toString());
        }
        return task;
    }

    public synchronized void insertQueue(String caseList, String byType, String browser, String uuid, TaskInfoForSchedule... taskInfoForSchedules) {
        //都只有一个TaskInfoForSchedule
        for (TaskInfoForSchedule task : taskInfoForSchedules) {
            TaskList taskList = new TaskList();
            taskList.setTaskID(task.getTaskID());
            taskList.setGuid(uuid);
            taskList.setAction("Start");
            taskList.setTaskFrom(byType);
            taskList.setReRunReportID(0);
            taskList.setCaseLists(caseList);
            taskList.setAgentIP(task.getAgentIP());
            taskList.setJobName("");
            taskList.setSentToAgentTime(Constant.UNDEFINED_TIMESTAMP);
            taskList.setCreateTime(new Timestamp(System.currentTimeMillis()));
            taskList.setStartTime(Constant.UNDEFINED_TIMESTAMP);
            taskList.setEndTime(Constant.UNDEFINED_TIMESTAMP);
            taskList.setTaskState(Constant.JOB_WAINTING);
            taskList.setLogs(Util.appendTaskLog(taskList.getLogs(), "[CreateTaskList] insertQueue " + byType));
            taskList.setPriority(0);
            taskList.setSplitCount(0);
            taskList.setSplitIndex(0);
            taskList.setSplitTime(Constant.UNDEFINED_TIMESTAMP);
            taskList.setBrowser(browser);
            this.insertList(taskList);

            TaskInfo taskInfo = taskInfoDao.get(task.getTaskID());
            taskInfo.setTaskStatus(Constant.JOB_WAINTING);
            taskInfo.setStartDate(new Timestamp(System.currentTimeMillis()));
            this.updateTaskInfo(taskInfo);
        }
    }

    public void insertList(TaskList taskList) {
        taskListDao.save(taskList);
    }

    public boolean stopJobByTaskId(int taskId) {
        List<TaskList> runningTaskList = taskListDao.getRunningTaskListByTaskId(taskId);
        boolean allStopped = true;
        String jobName;
        int statusCode;
        List<String> agentIpList = new ArrayList<String>();
        for (TaskList taskList : runningTaskList) {
            jobName = taskList.getJobName();
            statusCode = jenkinsJobBo.stopJob(jobName);
            if (HttpStatus.SC_MOVED_TEMPORARILY != statusCode) {
                statusCode = jenkinsJobBo.stopJob(jobName);
            }
            if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode) {
                String newLog = Util.appendTaskLog(taskList.getLogs(), String.format("[Stop] TaskListId:%d, TaskId:%d 状态%d->%d",taskList.getId(),taskList.getTaskID(),taskList.getTaskState(), Constant.JOB_STOPPED));
                taskList.setLogs(newLog);
                taskList.setTaskState(Constant.JOB_STOPPED);
                taskListDao.update(taskList);
                agentIpList.add(taskList.getAgentIP());
            } else {
                allStopped = false;
            }

        }
        if (allStopped) {
            taskInfoDao.updateTaskInfoState(taskId, Constant.JOB_STOPPED);
            List<AgentInfo> runningAgentList = agentInfoDao.getRunningAgentList();
            List<Integer> agentIdList = new ArrayList<Integer>();
            for (AgentInfo agentInfo : runningAgentList) {
                if (agentIpList.contains(agentInfo.getAgentIp())) {
                    agentIdList.add(agentInfo.getAgentID());
                }
            }
            agentInfoDao.amendAgentStatus((Integer[]) agentIdList.toArray(new Integer[agentIdList.size()]));
        }
        return allStopped;
    }


    public List<TaskList> getAllTaskInfoIdByStatus(int status) {
        String sql = String.format("select * from TaskList where taskState=%d order by SplitTime, GUID for update", status);
        return taskListDao.sqlQuery(sql, TaskList.class);
    }

    public TaskInfo getTaskInfoById(int taskId) {
        return taskInfoDao.get(taskId);
    }

    public TaskInfo updateTaskInfo(TaskInfo taskInfo) {
        String sql = String.format("update TaskInfo set TaskInfo.StartDate = '%s',TaskInfo.TaskStatus = %d where TaskInfo.TaskID = %d", taskInfo.getStartDate(), taskInfo.getTaskStatus(), taskInfo.getTaskID() );
//        return taskInfoDao.update(taskInfo);
        taskInfoDao.executeSql(sql);
        return taskInfo;
    }

    public int deleteTaskListById(int taskListId) {
        return taskListDao.executeSql(String.format("delete from TaskList where ID=%d", taskListId));
    }

    @Transactional
    public void saveSplittedTaskList(int originalTaskListId, List<TaskList> splittedTasklist, TaskInfo taskInfo) {
        if (splittedTasklist.isEmpty()) {
            TaskList taskList = taskListDao.get(originalTaskListId);
            if (taskList != null) {
                taskList.setTaskState(Constant.JOB_COMPLETED);
                taskList.setLogs(taskList.getLogs() + "\n No Case to Execute! ");
                taskList.setEndTime(new Timestamp(System.currentTimeMillis()));
                taskListDao.update(taskList);

                taskInfo.setTaskStatus(Constant.JOB_COMPLETED);
            }
        } else {
            for (TaskList t : splittedTasklist) {
                insertList(t);
            }
            deleteTaskListById(originalTaskListId);
            taskInfo.setTaskStatus(Constant.JOB_SPLITTED);
        }

        updateTaskInfo(taskInfo);
    }

    /**
     * 不拆分CaseList的情况下，更新TaskList和TaskInfo
     * @param taskListId
     * @param taskInfo
     */
    @Transactional
    public void updateUnsplittedTaskList(int taskListId, TaskInfo taskInfo, Timestamp splitTime) {
        TaskList taskList = taskListDao.get(taskListId);
        if (taskList.getCaseLists() == null || taskList.getCaseLists().isEmpty() && taskInfo.getTaskType() != Constant.TASK_TYPE_LOAD) {
            taskList.setTaskState(Constant.JOB_COMPLETED);
            taskList.setSplitTime(splitTime);
            taskList.setLogs(taskList.getLogs() + "\n No Case to Execute! ");
            taskList.setEndTime(Util.nowTimestamp());

            taskInfo.setTaskStatus(Constant.JOB_COMPLETED);
        } else {
            taskList.setLogs(Util.appendTaskLog(taskList.getLogs(), "[SplitExecutor] Need not split"));
            taskList.setStartTime(Util.nowTimestamp());
            taskList.setTaskState(Constant.JOB_SPLITTED);
            taskList.setSplitTime(splitTime);

            taskInfo.setTaskStatus(Constant.JOB_SPLITTED);
        }
        taskListDao.update(taskList);
        updateTaskInfo(taskInfo);
    }

    /**
     * CreateTime创建后超过millis毫秒后 设置为Abort
     * 除Abort之外的任何一种状态都可能因超时转为Abort
     *
     * @param millis
     */
    public synchronized void updateExpireTask(long millis, boolean isLoad) {
        String taskTypeCondition;
        String agentTaskTypeCondition;
        if (isLoad) {
            taskTypeCondition = String.format(" = %d", Constant.TASK_TYPE_LOAD);
            agentTaskTypeCondition = String.format(" = '%s'", lcdService.getNameByValue(Constant.DIC_TASK_TYPE, Constant.TASK_TYPE_LOAD));
        } else {
            taskTypeCondition = String.format(" <> %d", Constant.TASK_TYPE_LOAD);
            agentTaskTypeCondition = String.format(" <> '%s'", lcdService.getNameByValue(Constant.DIC_TASK_TYPE, Constant.TASK_TYPE_LOAD));
        }
        String expireTime = Util.dateToString(new java.util.Date(System.currentTimeMillis() - millis));

        /**
         * 查找Expire TaskList
         */
        List<TaskList> expireTaskList = taskListDao.getExpireTaskList(expireTime, taskTypeCondition);
        /**
         * Stop 超时Jenkins Job
         */
        for (TaskList taskList : expireTaskList) {
            jenkinsJobBo.stopJob(taskList.getJobName());
        }
        /**
         * 更新TaskList
         */
        if (expireTaskList != null && expireTaskList.size() > 0) {
            try {
                taskListDao.updateExpireTaskList(expireTime, taskTypeCondition);
            }catch (Exception e){
//                System.out.println("-----------------catch Exception.--------------------------");
                Logger.error("-----------------catch Exception.--------------------------");
                Logger.error(e.getMessage());
                e.printStackTrace();
            }

        }
        /**
         * 更新TaskInfo
         */
        List expireTaskInfoIdList = taskInfoDao.getExpireTaskInfoId(expireTime, taskTypeCondition);
        if (expireTaskInfoIdList != null && expireTaskInfoIdList.size() > 0) {
            taskInfoDao.updateExpireTaskInfo(expireTime, taskTypeCondition);
        }
        /**
         * 更新Agent
         */
        List expireAgentList = agentInfoDao.getExpireAgent(expireTime, agentTaskTypeCondition);
        if (expireAgentList != null && expireAgentList.size() > 0) {
            agentInfoDao.updateExpireAgent(expireTime, agentTaskTypeCondition);
        }
    }

    /**
     * 指定状态的超时时间millis，会根据waitStatus 更新到RevertToStatus
     *
     * @param waitStatusAndRevertToStatus
     */
    public void revertLongStopTaskExecutor(HashMap<Integer, Integer> waitStatusAndRevertToStatus, long millis) {
        for (Map.Entry<Integer, Integer> status : waitStatusAndRevertToStatus.entrySet()) {
            taskListDao.executeSql(
                    String.format("update TaskList set TaskState=%d where StartTime < '%s' and TaskState =%d",
                            status.getValue(),
                            Util.dateToString(new java.util.Date(System.currentTimeMillis() - millis)),
                            status.getKey()));
        }
    }

    @Transactional
    public void updateStatus(HashMap<Integer, Integer> waitStatusAndRevertToStatus, List<TaskList> tasks) {
        if (tasks.isEmpty() == false) {
            List<Integer> taskId = new ArrayList<Integer>();
            for (TaskList taskList : tasks)
                taskId.add(taskList.getId());

            StringBuffer stringBuffer = new StringBuffer();
            for (Integer id : taskId) {
                if (stringBuffer.length() > 0)
                    stringBuffer.append(",");

                stringBuffer.append(id);
            }
            for (Map.Entry<Integer, Integer> status : waitStatusAndRevertToStatus.entrySet()) {
                taskListDao.executeSql(
                        String.format("update TaskList set TaskState=%d where ID in (%s) and TaskState =%d",
                                status.getValue(),
                                stringBuffer,
                                status.getKey()));
            }
        }
    }

    /**
     * 获取超过停留指定状态status时间millis的TaskList列表
     *
     * @param status
     * @param millis
     * @return
     */

    public List<TaskList> getLongStopTask(int status, long millis, String timeType) {
        return taskInfoDao.sqlQuery(
                String.format("select * from TaskList where %s < '%s' and TaskState = %d",
                        timeType,
                        Util.dateToString(new java.util.Date(System.currentTimeMillis() - millis)),
                        status), TaskList.class);
    }

    public List<TaskList> getExpireJenkinsJob(String timeType, long millis) {
        return taskInfoDao.sqlQuery(
                String.format("select * from TaskList where %s < '%s' AND JobName <> ''",
                        timeType,
                        Util.dateToString(new java.util.Date(System.currentTimeMillis() - millis))), TaskList.class);
    }

    public void updateTaskListAfterDelJob(int taskListId, String jobName) {
        taskListDao.executeSql(
                String.format("update TaskList FORCE INDEX (INDEX_CREATETIME) set JobName='%s' where ID=%d",
                        jobName,
                        taskListId)
        );
    }

    @Transactional
    public void afterSent(TaskList taskList, String jobName, String agentLabel) {
        /**
         * 发送到Jenkins成功后，更新该条TaskList
         */
        if (taskList.getTaskState() == Constant.JOB_SPLITTED) {
            taskList = taskListDao.get(taskList.getId());
            taskList.setTaskState(Constant.JOB_SENT);
            taskList.setJobName(jobName);
            taskList.setSentToAgentTime(Util.nowTimestamp());
            taskList.setAgentIP(agentLabel);
            taskListDao.update(taskList);
        }
        /**
         * 发送一条TaskList到Jenkins后，
         * 判断TaskInfo是否仍为Split状态，
         * 如果是，改为Sent
         *
         * 用于有拆分情况的状态更新
         * 保证拆分的第一条发送成功后更新TaskInfo为sent
         */
        TaskInfo taskInfo = taskInfoDao.get(taskList.getTaskID());
        if (taskInfo.getTaskStatus() == Constant.JOB_SPLITTED) {
            taskInfo.setTaskStatus(Constant.JOB_SENT);
        }
    }

    public void afterFailBuild(TaskList taskList, String agentLabel) {
        /**
         * 发送失败，将该条TaskList改为Aborted
         */
        if (taskList.getTaskState() == Constant.JOB_SENT) {
            taskList = taskListDao.get(taskList.getId());
            taskList.setTaskState(Constant.JOB_ABORTED);
            taskList.setLogs(taskList.getLogs() + "\n 两次向Jenkins发送Job都失败 ");
            taskList.setEndTime(Util.nowTimestamp());
            taskListDao.update(taskList);
        }

        /**
         * 发送失败后判断同时拆分出来的TaskList是否都已停止，如果是就将TaskInfo改为Aborted
         */
        TaskInfo taskInfo = taskInfoDao.get(taskList.getTaskID());
        List<TaskList> taskLists = taskListDao.getTaskListByTaskIdGuid(taskList.getTaskID(), taskList.getGuid());
        Set<Integer> statusSet = new HashSet<Integer>();
        for (TaskList list : taskLists) {
            statusSet.add(list.getTaskState());
        }

        if (taskStatusBo.isAllEndStatus(statusSet)) {
            taskInfo.setTaskStatus(Constant.JOB_ABORTED);
            taskInfoDao.update(taskInfo);
            agentBo.revertByLabel(agentLabel, taskList.getTaskID());
        }
    }

    public String getCaseList(String csvFile, String express) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        ResultSet caseList = CsvUtils.executeQuery(csvFile, express);
        if (caseList != null) {
            while (caseList.next()) {
                if (stringBuffer.length() > 0)
                    stringBuffer.append(",");

                stringBuffer.append(caseList.getString(1));
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 删除文件 重新下载
     *
     * @param svnUrl  相对路径
     * @param svnPath 相对路径
     * @return csv文件绝对路径
     * @throws Exception
     */
    public String updateCaseCategory(String svnUrl, String svnPath) throws Exception {
//        System.out.println("casecategory: " + env.getProperty("casecategory.path") + File.separator + svnPath);//GTP-Service 服务器的路径
        Logger.info("casecategory: " + env.getProperty("casecategory.path") + File.separator + svnPath);
        File caseCategoryDir = new File(env.getProperty("casecategory.path"), svnPath);

        Util.deleteAllFilesOfDir(caseCategoryDir);
        caseCategoryDir.mkdirs();
        URL url = new URL(svnUrl);

        SvnUtils.checkout(SVNURL.create(url.getProtocol(), "", url.getHost(), url.getPort(), url.getPath(), true), caseCategoryDir.getAbsolutePath(),
                env.getProperty("casecategory.svn_username"), env.getProperty("casecategory.svn_password"));

        return new File(caseCategoryDir.getAbsolutePath(), Constant.CASECATEGORY_CSV).getAbsolutePath();
    }

    /**
     * --------------------拆分Case相关------------------
     */
    public List<TaskList> getSplittedTaskList(TaskList taskList, Map<String, Long> taskRunTimes, Timestamp splitTime) {
        List<TaskList> result = new ArrayList<TaskList>();
        if (taskList.getCaseLists() != null && !taskList.getCaseLists().isEmpty()) {
            List<StringBuffer> cases = splitCase(taskList.getCaseLists(), taskRunTimes);
            //同一组拆分后的TaskList用splitTime标记
//            Timestamp splitTime = Util.nowTimestamp();
            for (int i = 1; i <= cases.size(); i++) {
                TaskList task = new TaskList();
                task.setTaskID(taskList.getTaskID());
                task.setAction(taskList.getAction());
                task.setTaskFrom(taskList.getTaskFrom());
                task.setReRunReportID(taskList.getReRunReportID());
                task.setCaseLists(cases.get(i - 1).toString());

                task.setAgentIP(taskList.getAgentIP());
                task.setCreateTime(taskList.getCreateTime());
                task.setSentToAgentTime(taskList.getSentToAgentTime());
                task.setStartTime(new Timestamp(System.currentTimeMillis()));
                task.setEndTime(taskList.getEndTime());

                task.setTaskState(Constant.JOB_SPLITTED);
                task.setLogs(Util.appendTaskLog(taskList.getLogs(), String.format("分割生成 %s", cases.get(i - 1).toString())));
                task.setPriority(taskList.getPriority());
                task.setGuid(taskList.getGuid());//拆分的子TaskList GUID相同
                task.setSplitCount(cases.size());
                task.setSplitIndex(i);
                task.setSplitTime(splitTime);
                task.setJobName(taskList.getJobName());
                task.setBrowser(taskList.getBrowser());//拆分的子TaskList Browser相同

                result.add(task);
            }
        }
        return result;
    }


    /**
     * @return 拆分后的结果
     */
    private List<StringBuffer> splitCase(String cases, final Map<String, Long> taskRunTimes) {
        long totalDuration = 0;

        String[] caseList = cases.split(",");
        //计算总时间 看是否需要拆分
        for (String c : caseList) {
            if (taskRunTimes.containsKey(c)) {
                totalDuration += taskRunTimes.get(c);
            } else {
                totalDuration += getCaseRunDurationDefault();
            }
        }

        List<StringBuffer> result = new ArrayList<StringBuffer>();
        //不需拆分
        if (totalDuration < getTaskMaxDuration()) {
            result.add(new StringBuffer(cases));
            return result;
        }

        //需要拆分 耗时从打到小排列
        List<Map.Entry<String, Long>> caseAndDuration = createSortedCaseAndDuration(caseList, taskRunTimes);
        for (Map.Entry<String, Long> duration : caseAndDuration)
//            System.out.println(String.format("%s : %s", duration.getKey(), duration.getValue()));
            Logger.info(String.format("%s : %s", duration.getKey(), duration.getValue()));
        Set<String> assignedCase = new HashSet<String>();
        long durationSum = 0;
        for (Map.Entry<String, Long> currentCaseDuration : caseAndDuration) {
            if (assignedCase.contains(currentCaseDuration.getKey()))
                continue;

            if (currentCaseDuration.getValue() >= getTaskMaxDuration()) {//单个已超
                result.add(new StringBuffer(currentCaseDuration.getKey()));
                assignedCase.add(currentCaseDuration.getKey());
            } else {
                StringBuffer stringBuffer = new StringBuffer(currentCaseDuration.getKey());
                assignedCase.add(currentCaseDuration.getKey());

                durationSum = currentCaseDuration.getValue();
                //从最小的开始寻找 逐渐追加
                for (int i = caseAndDuration.size() - 1; i >= 0; i--) {
                    if (assignedCase.contains(caseAndDuration.get(i).getKey()))
                        continue;
                    //不超过 则继续追加
                    if (durationSum + caseAndDuration.get(i).getValue() <= getTaskMaxDuration()) {
                        stringBuffer.append(",");
                        stringBuffer.append(caseAndDuration.get(i).getKey());
                        assignedCase.add(caseAndDuration.get(i).getKey());
                        durationSum += caseAndDuration.get(i).getValue();
                    } else {      //超过了 不需再追加
                        break;
                    }
                }
                result.add(stringBuffer);
            }
        }

        return result;
    }

    /**
     * 耗时由大到小排列
     *
     * @param caseList
     * @param taskRunTimes
     * @return ket:caseName value:duration
     */
    private List<Map.Entry<String, Long>> createSortedCaseAndDuration(String[] caseList, final Map<String, Long> taskRunTimes) {
        List<Map.Entry<String, Long>> caseAndDuration = new ArrayList<Map.Entry<String, Long>>();
        for (final String caseName : caseList) {
            caseAndDuration.add(new Map.Entry<String, Long>() {
                @Override
                public String getKey() {
                    return caseName;
                }

                @Override
                public Long getValue() {
                    return taskRunTimes.containsKey(caseName) ? taskRunTimes.get(caseName) : getCaseRunDurationDefault();
                }

                @Override
                public Long setValue(Long value) {
                    return taskRunTimes.containsKey(caseName) ? taskRunTimes.get(caseName) : getCaseRunDurationDefault();
                }
            });
        }

        //耗时反排序
        Collections.sort(caseAndDuration, new Comparator<Map.Entry<String, Long>>() {
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return (o2.getValue() - o1.getValue()) > 0 ? 1 : -1;
            }
        });

        return caseAndDuration;
    }

    /**
     * 最后一次各Case执行的耗时，如果不存在就默认为 CASE_RUN_DURATION_DEFAULT
     *
     * @param taskLists
     * @return
     */
    public Map<String, Long> getLastRunTime(List<TaskList> taskLists) {
        Map<String, Long> result = new HashMap<String, Long>();

        for (TaskList taskList : taskLists) {
            String[] cases = taskList.getCaseLists().split(",");
            for (String c : cases) {
                if (!result.containsKey(c))
                    result.put(c, getCaseRunDurationDefault());
            }
        }

        //从mongodb获取最后一次执行时长
        Query query = new Query();
        query.addCriteria(Criteria.where("caseName").in(result.keySet()));
        List<CaseRunTime> caseRunTimes = reportDao.getByCondition(CaseRunTime.class, query);

        for (CaseRunTime caseRunTime : caseRunTimes) {
            if (result.containsKey(caseRunTime.getCaseName()))
                result.put(caseRunTime.getCaseName(), caseRunTime.getDuration());
        }
        return result;
    }

    /**
     * -------------------CreateTask相关----------------------
     */

    public void createTask(Integer taskId, String byType) throws Exception {
        TaskInfoForSchedule taskInfoForSchedule = getTaskInfoForScheduleById(taskId);
        int taskType = taskInfoDao.getTaskTypeById(taskId);

        //根据TaskType选择对应创建任务的方法，可以扩展
        switch (taskType) {
            case Constant.TASK_TYPE_API:
                createAPITask(taskInfoForSchedule, byType);
                break;
            case Constant.TASK_TYPE_GUI:
                createGUITask(taskInfoForSchedule, byType);
                break;
            case Constant.TASK_TYPE_LOAD:
                createLOADTask(taskInfoForSchedule, byType);
                break;
            default:
                throw new Exception(String.format("没有找到与TaskType(%d)对应的方法来创建Task",taskType));
        }
    }

    private void createAPITask(TaskInfoForSchedule taskInfoForSchedule, String byType) throws Exception {
        String csvFile;
        csvFile = updateCaseCategory(taskInfoForSchedule.getBranchUrl() + "/TestCase/CaseCategory/", String.valueOf(taskInfoForSchedule.getTaskID()));
        String caseList = getCaseList(csvFile, taskInfoForSchedule.getExcuteInfo());
        String uuid = UUID.randomUUID().toString();
        insertQueue(caseList, byType, "", uuid, taskInfoForSchedule);
    }

    private void createGUITask(TaskInfoForSchedule taskInfoForSchedule, String byType) throws Exception {
        String csvFile;
        csvFile = updateCaseCategory(taskInfoForSchedule.getBranchUrl() + "/TestCase/case/CaseCategory/", String.valueOf(taskInfoForSchedule.getTaskID()));
        String caseList = getCaseList(csvFile, taskInfoForSchedule.getExcuteInfo());
        List<String> browserList = taskInfoListBo.getBrowserListByTaskId(taskInfoForSchedule.getTaskID());
        String uuid = UUID.randomUUID().toString();
        for (String browser : browserList) {//每种浏览器创建一个TaskList
            insertQueue(caseList, byType, browser, uuid, taskInfoForSchedule);
        }
    }

    private void createLOADTask(TaskInfoForSchedule taskInfoForSchedule, String byType) throws Exception {
        String uuid = UUID.randomUUID().toString();
        insertQueue("", byType, "", uuid, taskInfoForSchedule);
    }

    /**
     * @param taskIds
     * @return 已经消耗的<TaskId,QueueId>
     */
    public Map<Integer, Integer> appendQueueTask(List<Integer> taskIds) {
        Map<Integer, Integer> queueIds = new HashMap<Integer, Integer>();
        List queues = queueDao.getByTypeAndStatus(Constant.QUEUE_TYPE_TASK, Constant.QUEUE_NEW);
        if (queues != null) {
            for (Object q : queues) {
                Integer taskId = Integer.valueOf(((Object[]) q)[1].toString());
                if (!taskIds.contains(taskId)) {
                    taskIds.add(taskId);
                }
                queueIds.put(taskId, Integer.valueOf(((Object[]) q)[0].toString()));
            }

        }
        return queueIds;
    }

    private Long getCaseRunDurationDefault() {
        return Long.valueOf(lcdService.getValueByName(Constant.GTP_CONFIG, Constant.DEFAULT_CASE_DURATION));
    }

    private Long getTaskMaxDuration() {
        return Long.valueOf(lcdService.getValueByName(Constant.GTP_CONFIG, Constant.MAX_CASE_DURATION));
    }

    public static void main(String[] args) throws Exception {
//        TaskScheduleJob taskScheduleJob = new TaskScheduleJob();
//        String caseList = taskScheduleJob.getCaseList("D:/caseCat/1060/CaseCategroy.csv", "C1='a'");
//        System.out.println("CaseList: "+caseList);


        System.out.println("casecategory: " + "D:\\caseCat\\1058");
        File caseCategoryDir = new File("D:\\caseCat\\", "1058");

        Util.deleteAllFilesOfDir(caseCategoryDir);
        caseCategoryDir.mkdirs();
        URL url = new URL("https://@repo.ds.gome.com.cn:8443/svn/test/SourceCode/sample/Trunk/APITest/TestCase/CaseCategory/");

        SvnUtils.checkout(SVNURL.create(url.getProtocol(), "", url.getHost(), url.getPort(), url.getPath(), true), caseCategoryDir.getAbsolutePath(),
                "lizonglin", "lin456");
    }
}