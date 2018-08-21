package com.gome.test.gtp.utils;

import java.sql.Timestamp;
import java.util.*;

public class Constant {
    public final static String MONGODB_NAME = "gtp";
    public final static String TASK_TYPE = "taskType";
    public final static String TASK_ID = "taskId";
    public final static String DAO_PACKAGE_NAME = "com.gome.test.gtp";
    /**
     * configure dictionary parent name
     */
    public static final String ENV = "Environment";
    public static final String GROUP = "BusinessGroup";
    public static final String GROUP_ID = "businessGroupId";
    public static final String TASK_STATUS = "TaskStatus";
    public static final String BROWSER = "Browser";
    public static final String DATE_RANGE = "DateRange";
    public static final String EXCUTE_TYPE = "ExcuteType";
    public static final String RUN_TYPE = "TaskRunType";
    public static final String REGULAR_START_TYPE = "RunRegularStartType";
    public static final String AGENT_STATUS = "AgentStatus";
    public static final String REPORT_TYPE = "ReportType";
    public final static String DIC_TASK_TYPE = "TaskType";
    public final static String ADVICE_TYPE = "AdviceType";
    public final static String ADVICE_STATUS = "AdviceStatus";

    /**
     * gtp-domain properties file
     */
    public static final String APP_PROP_FILE = "/application.properties";

    /**
     * task report
     */
    public static final String REPORT_GROUP = "ReportGroup";
    public static final String REPORT_OWNER = "ReportOwner";
    public static final String TASK_REPORT = "TaskReport";
    public static final String JOB_REPORT="JobReport";
    public static final String TESTJMT_REPORT="TestJMTReport_Test";
    public static final String JMT_REPORT="JMTReport";
    public static final String JMTAGG_REPORT="JMTAGGReport";
    public static final String TESTJMTAGG_REPORT="JMTAGGReport_Test";
    public static final String CASE_RUNTIME = "CaseRunTime";
    public static final String AG_REPORT ="AGReport";

    public static final String _ID = "_id";
    public static final String DATE = "date";
    public static final String OWNER = "owner";
    public static final String GROUPID = "groupId";
    public static final String TEST_RESULT = "testResult";
    public static final String CASE_NUM = "caseNum";
    public static final String PASS_NUM = "passNum";

    public static final String PASS = "pass";
    public static final String FAIL = "fail";

    public static final int REPORT_CASE_NUMBER = 1;
    public static final int REPORT_PASS_RATE = 2;
    public static final int REPORT_PASS_NUMBER = 3;

    public static final String JSON_REPORT = "/TestProject/target/surefire-reports/japi4gtp.json";
    public static final String TESTNG_XML = "/TestProject/src/test/resources/testng/testng.xml";
    public static final String TESTNG_XML_DIR = "/TestProject/src/test/resources/testng/";
    public static final String POM_FILE = "/TestProject/pom.xml";
    public static final String JSON_PicturePath="/TestProject/target/test-classes/screenCapture";

    /**
     * Response 初始化状态
     */
    public static final int JOB_Initialization=1;


    /**
     * Task/Job状态
     */
    public static final int JOB_CREATED = 10;
    /**
     * 进入队列
     */
    public static final int JOB_WAINTING = 20;
    /**
     * 已拆分未分配
     */
    public static final int JOB_SPLITTED = 30;
    /**
     * 已分配未下发
     */
    public static final int JOB_ASSIGNED = 40;
    /**
     * 已下发未执行
     */
    public static final int JOB_SENT = 50;
    /**
     * 已执行未结束
     */
    public static final int JOB_RUNNING = 60;
    /**
     * 已执行未结束
     */
    public static final int JOB_TIMEOUT = 69;

    /**
     * 正常结束
     */
    public static final int JOB_COMPLETED = 70;
    /**
     * 超时结束
     */
    public static final int JOB_ABORTED = 80;
    /**
     * 出错
     */
    public static final int JOB_ERROR = 90;

    /**
     * TaskInfo已删除
     */
    public static final int JOB_DELETED = 100;

    /**
     * WEB上 手动 Stop Task
     */
    public static final int JOB_STOPPED = 110;

    public static final Set<Integer> END_STATUS_SET = new HashSet<Integer>();
    static {
        END_STATUS_SET.add(JOB_CREATED);
        END_STATUS_SET.add(JOB_COMPLETED);
        END_STATUS_SET.add(JOB_ABORTED);
        END_STATUS_SET.add(JOB_ERROR);
        END_STATUS_SET.add(JOB_DELETED);
        END_STATUS_SET.add(JOB_STOPPED);
    }

    public static final Set<Integer> NONEND_STATUS_SET = new HashSet<Integer>();
    static {
        NONEND_STATUS_SET.add(JOB_WAINTING);
        NONEND_STATUS_SET.add(JOB_SPLITTED);
        NONEND_STATUS_SET.add(JOB_ASSIGNED);
        NONEND_STATUS_SET.add(JOB_SENT);
        NONEND_STATUS_SET.add(JOB_RUNNING);
    }

    public static final int NON_END_STATUS = -1;

    /**
     * Queue的状态
     */
    public static final int QUEUE_NEW = 10;
    public static final int QUEUE_DELETED = 20;
    public static final int QUEUE_FAIL = 30;

    /**
     * 分类为Task的Queue
     */
    public static final int QUEUE_TYPE_TASK = 1;

    /**
     * 进入TaskList的方式：定时、手动
     */
    public static final String ENQUEUE_BY_SCHEDULER = "ByScheduler";
    public static final String ENQUEUE_BY_START = "ByManualStart";
    public static final String ENQUEUE_BY_Test="ByTest";

    /**
     * TriggerKey GroupName
     */
    public static final String TRIGGER_GROUP_EXECUTOR = "EXECUTOR";
    public static final String TRIGGER_GROUP_TASKJOB = "TASKJOB";

    /**
     * regular start type
     */
    public static final int REGULAR_ONCE = 1;
    public static final int REGULAR_DAILY = 2;
    public static final int REGULAR_WEEKLY = 3;
    /**
     * task types
     */
    public static final int TASK_TYPE_API = 1;
    public static final int TASK_TYPE_GUI = 2;
    public static final int TASK_TYPE_LOAD = 3;
    public static final int LEFT_TREE_LOAD = 4;
    public static final int TASK_TYPE_JOB = 5 ;

    /**
     * config file name for jenkins job
     */
    public static final String OS_TYPE_LINUX_API = "linux-1";
    public static final String OS_TYPE_WIN_GUI = "win-2";
    public static final String OS_TYPE_MAC_GUI = "mac-2";
    public static final String OS_TYPE_LINUX_LOAD = "linux-3";
    public static final String OS_TYPE_LINUX_Job = "linux-5";

    public static final String CFG_FILE_NAME_LINUX_API = "config-linux-api.xml";
    public static final String CFG_FILE_NAME_WIN_GUI = "config-win-gui.xml";
    public static final String CFG_FILE_NAME_MAC_GUI = "config-mac-gui.xml";
    public static final String CFG_FILE_NAME_LINUX_LOAD = "config-linux-load.xml";
    public static final String CFG_FILE_NAME_LINUX_JOB = "config-linux-job.xml";

    public static final String CFG_FILE_NAME_API = "config-api.xml";
    public static final String CFG_FILE_NAME_GUI = "config-gui.xml";
    public static final String CFG_FILE_NAME_LOAD = "config-load.xml";
    public static final String CFG_FILE_NAME_JOB = "config-job.xml";
    public static final String TREENODE_LOAD="treeNode.xml";

    public static final Map<String, String> CFG_MAP = new HashMap<String, String>();

    static {
        CFG_MAP.put(String.valueOf(TASK_TYPE_API), CFG_FILE_NAME_API);
        CFG_MAP.put(String.valueOf(TASK_TYPE_GUI), CFG_FILE_NAME_GUI);
        CFG_MAP.put(String.valueOf(TASK_TYPE_LOAD), CFG_FILE_NAME_LOAD);
        CFG_MAP.put(String.valueOf(TASK_TYPE_JOB), CFG_FILE_NAME_JOB);
        CFG_MAP.put(String.valueOf(LEFT_TREE_LOAD),TREENODE_LOAD);

        CFG_MAP.put(OS_TYPE_LINUX_API,CFG_FILE_NAME_LINUX_API);
        CFG_MAP.put(OS_TYPE_WIN_GUI,CFG_FILE_NAME_WIN_GUI);
        CFG_MAP.put(OS_TYPE_MAC_GUI,CFG_FILE_NAME_MAC_GUI);
        CFG_MAP.put(OS_TYPE_LINUX_LOAD,CFG_FILE_NAME_LINUX_LOAD);
        CFG_MAP.put(OS_TYPE_LINUX_Job,CFG_FILE_NAME_LINUX_JOB);
    }

    /**
     * TaskList 状态超时时间 改写在 application.xml 中
     */
//    public static final long STATE_EXPIRE_DURATION = 6 * 3600 * 1000;
//    public static final long STATE_LONG_STOP_DURATION = 30 * 60 * 1000;
//    public static final long STATE_JENKINS_JOB_DURATION = 5 * 24 * 3600 * 1000;

    /**
     * 删除过期的Jenkins Job 后TaskList中JobName名称
     */
    public static final String DELETED_JENKINS_JOB_NAME = "";

    /**
     * Executor CronExpression
     */

    /**
     * TaskList表的各种时间列名
     */
    public static final String TASK_LIST_SENT_TIME = "SentToAgentTime";
    public static final String TASK_LIST_CREATE_TIME = "CreateTime";
    public static final String TASK_LIST_START_TIME = "StartTime";
    public static final String TASK_LIST_END_TIME = "EndTime";
    public static final String CASECATEGORY_CSV = "CaseCategroy.csv";

    /**
     * config.xml参数替换
     */
    public static final String CONFIG_TASKLIST_ID = "TASKLIST_ID";
    public static final String CONFIG_TASK_TYPE = "TASK_TYPE";
    public static final String CONFIG_SVN_URL = "SVN_URL";
    public static final String CONFIG_PROJECT_NAME = "PROJECT_NAME";
    public static final String CONFIG_EMAIL_LIST = "EMAIL_LIST";
    public static final String CONFIG_SVN_CODE_DIR = "SVN_CODE_DIR";
    public static final String CONFIG_SVN_CODE_DIR2 = "SVN_CODE_DIR2";
    public static final String CONFIG_SVN_DIR = "SVN_DIR";
    public static final String CONFIG_JOB_DIR = "JOB_DIR";
    public static final String CONFIG_JOB_DIR2 = "JOB_DIR2";
    public static final String CONFIG_JOB_NAME = "JOB_NAME";
    public static final String CONFIG_AGENT_LABEL = "AGENT_LABEL";
    public static final String CONFIG_JOB_HOSTS = "JOB_HOSTS";
    public static final String CONFIG_LOAD_VERIFY = "LOAD_VERIFY";
    public static final String CONFIG_AGENT_NULL_LABEL = "AGENT_NULL__LABEL";
    public static final String CONFIG_LOAD_WORKDIR = "WORKDIR";
    public static final String CONFIG_LOAD_TMPLIBDIR = "TMPLIBDIR";
    public static final String CONFIG_LOAD_LIBDIR = "LIBDIR";

    /**
     * GUI FTP 参数替换
     */
    public static final String GUI_FTP_REPORT_SUBDIR = "GUI_FTP_REPORT_SUBDIR";
    public static final String GUI_FTP_REPORT_ROOTDIR = "GUI_FTP_REPORT_ROOTDIR";
    public static final String GUI_FTP_REPORT_HOST = "GUI_FTP_REPORT_HOST";
    public static final String GUI_FTP_REPORT_USERNAME = "GUI_FTP_REPORT_USERNAME";
    public static final String GUI_FTP_REPORT_PASSWORD = "GUI_FTP_REPORT_PASSWORD";

    /**
     * SVN 账号参数替换
     */
    public static final String CONFIG_SVN_USERNAME = "SVN_USERNAME";
    public static final String CONFIG_SVN_PASSWORD = "SVN_PASSWORD";

    /**
     * LOAD FTP 参数替换
     */
    public static final String LOAD_FTP_REPORT_SUBDIR = "LOAD_FTP_LOG_SUBDIR";

    public static final String URLSTRING ="EnvURI";
    public static final String ENV_VERSION ="EnvVersion";
    /**
     * LOAD Email 参数替换
     */
    public static final String LOAD_EMAIL_TASKLOADREPORT = "taskLoadReport";
    public static final String LOAD_EMAIL_HASERROR = "hasError";
    public static final String LOAD_EMAIL_UTIL = "util";
    public static final String LOAD_EMAIL_FTP_URL = "loadFtpUrl";
    public static final String LOAD_EMAIL_JENKINS_URL = "loadJenkinsUrl";
    public static final String LOAD_EMAIL_LOGDETAIL_URL = "loadLogDetailUrl";

    /**
     * 不指定机器的情况下前台标签的值
     */
    public static final String NONE_SPECIFIC_AGENT_LABEL = "None";

    /**
     * 未定义时间标识
     */
    public static final Timestamp UNDEFINED_TIMESTAMP = Timestamp.valueOf("1988-08-02 08:08:08");

    /**
     * 用户密码加密相关
     */
    /**
     * 生成SALT的数组(86)
     */
    public static final String[] SALT_ARR = {"a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
            "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "0", ".", "-", "*", "/", "'", ":", ";", ">", "<", "~", "!",
            "@", "#", "$", "%", "^", "&", "(", ")", "{", "}", "[", "]", "|"};
    /**
     * 循环加密次数
     */
    public static final int ENCODE_COUNT = 1000;
    /**
     * 加密算法类型
     */
    public static final String ENCODE_TYPE = "SHA-256";
    /**
     * SALT长度
     */
    public static final int SALT_LENGTH = 16;
    /**
     * getBytes时指定使用的字符集
     */
    public static final String CHAR_SET = "UTF-8";

    /**
     * session超时时间
     */
    public static final int SESSION_TIMEOUT = 60*60*24*7;

    /**
     * 字典表里GTP配置Parent的ValueName
     */
    public static final String GTP_CONFIG = "GTPConfig";

    /**
     * 字典表里拆分Case时间阈值的ValueName
     */
    public static final String MAX_CASE_DURATION = "MaxCaseDuration";

    /**
     * 字典表里默认Case运行时间的ValueName
     */
    public static final String DEFAULT_CASE_DURATION = "DefaultCaseDuration";

    /**
     * Load 配置类型
     */
    public static final String LOAD_CONF = "conf";
    public static final String LOAD_LIB = "lib";
    public static final String LOAD_DB = "db";
    public static final String LOAD_REPORT_SVN = "reportsvn";
    public static final String LOAD_SEL_CONF = "sel_conf";
    public static final String LOAD_SEL_LIB = "sel_lib";
    public static final String LOAD_SEL_DB = "sel_db";
    public static final String LOAD_SEL_REPORT_SVN = "sel_reportsvn";

    /**
     * Load pom文件名
     */
    public static final String LOAD_TEMP_POM = "pom.xml";
    /**
     * Load pom 待配置节点
     */
    public static final String LOAD_SEARCH_PATH = "search_paths";
    public static final String LOAD_JXM_PATH = "jmxPath";
    public static final String LOAD_USER_PATH = "userPath";
    public static final String LOAD_ARGS_PATH = "argsPath";
    public static final String LOAD_SQL_CONFIG_PATH = "sqlconfigDir";
    public static final String LOAD_ENV = "environment";

    /**
     * Agent 相关
     */
    public static final int AGENT_STATUS_IDLE = 10;
    public static final int AGENT_STATUS_RUNNING = 20;

    public static final long AGENT_STATUS_DURATION = 1000*60*60*6;

    /**
     * API Test Email 参数替换
     */
    public static final String API_EMAIL_TASKID = "TaskId";
    public static final String API_EMAIL_TASKNAME = "TaskName";
    public static final String API_EMAIL_TOTAL = "Total";
    public static final String API_EMAIL_PASSED = "Passed";
    public static final String API_EMAIL_FAILED = "Failed";
    public static final String API_EMAIL_ABORTED = "Aborted";
    public static final String API_EMAIL_STARTTIME = "StartTime";
    public static final String API_EMAIL_ENDTIME = "EndTime";
    public static final String API_EMAIL_DURATION = "Duration";
    public static final String API_EMAIL_ERRORCONTENT = "ErrorContent";
    public static final String API_EMAIL_TASKLISTGUID = "TaskListGuid";
    public static final String API_EMAIL_LINKHOST = "linkhost";

    /**
     * GUI Test Email 参数替换
     */
    public static final String GUI_EMAIL_FTP_HOST = "ftpHost";
    public static final String GUI_EMAIL_FTP_SUBDIR = "ftpSubDir";
    public static final String GUI_EMAIL_FTP_JOBNAME = "ftpJobName";

    /**
     * LOAD Test Email 参数替换
     */
    public static final String JENKINS_BASEURL = "jenkinsBaseUrl";

    /**
     * AG Report Email 参数替换
     */
    public static final String AG_REPORT_EMAIL_TEST_TYPE = "TestType";
    public static final String AG_REPORT_EMAIL_GROUP = "Group";
    public static final String AG_REPORT_EMAIL_TOTAL= "Total";
    public static final String AG_REPORT_EMAIL_PASS = "Pass";
    public static final String AG_REPORT_EMAIL_FAIL = "Fail";
    public static final String AG_REPORT_EMAIL_PASSRATE = "PassRate";
    public static final String AG_REPORT_EMAIL_PERSONALLIST = "PersonalList";

    /**
     * GUI properties文件替换关键字
     */
    public static final String GUI_SELENIUM_PROPERTIES = "selenium.properties";
    public static final String GUI_SELENIUM_BROWSER = "selenium.browser";

    /**
     * GUI Agent OS 相关
     */
    public static final String AGENT_OS = "AgentOS";

    public static final String AGENT_OS_WIN = "Win";
    public static final String AGENT_OS_LINUX = "Linux";
    public static final String AGENT_OS_MAC = "Mac";
    public static final String AGENT_OS_IOS = "IOS";
    public static final String AGENT_OS_ANDROID = "Android";

    /**
     * Email Template FileName
     */
    public static final String API_EMAIL_TEMPLATE = "API-Email-Template.html";
    public static final String GUI_EMAIL_TEMPLATE = "GUI-Email-Template.html";
    public static final String LOAD_EMAIL_TEMPLATE = "Load-Email-Template.html";
    public static final String LOAD_EMAIL_VM_TEMPLATE = "Load-Email-Template.vm";

    public static final String AG_REPORT_EMAIL_TEMPLATE = "AG-Report-Email-Template.html";

    /**
     * cc Email
     */
    public static final String API_EMAIL_CCLIST ="lizonglin@yolo24.com";

    /**
     *jenkins job 构建结果
     */
    public static final String JOB_RESULT_SUCCESS = "SUCCESS";
    public static final String JOB_RESULT_FAILURE = "FAILURE";

    /**
     *gtp 启动的 jenkins job
     */
    public static final String JOB_DEFAULT_NUM = "1";

    /**
     * API 报告条数限制
     */
    public static final int API_REPORT_LIMIT = 50;

    /**
     * LOAD 报告条数限制
     */
    public static final int LOAD_REPORT_LIMIT = 50;


    /**
     * Gui 页面测试 所使用
     *
     */
    public static final String taskName ="GUITest";

//    ===========================================Load Test 相关==============================================

    public static final String LOAD_ON_ERROR = "LoadOnSampleError";
    public static void main(String[] args) {
//        Map<Integer,Integer> taskIdStatusMap = new HashMap<Integer, Integer>();
//        taskIdStatusMap.put(100,30);
//        taskIdStatusMap.put(101,40);
//        taskIdStatusMap.put(102,50);
//        taskIdStatusMap.put(103,60);
//        StringBuilder whenStr = new StringBuilder();
//        StringBuilder inStr = new StringBuilder();
//        for (Map.Entry<Integer, Integer> map : taskIdStatusMap.entrySet()) {
//            whenStr.append(String.format("when %d then %d ", map.getKey(), map.getValue()));
//            inStr.append(String.format("%d,",map.getKey()));
//        }
//        whenStr.setLength(whenStr.length() - 1);
//        inStr.setLength(inStr.length() - 1);
//
//        String sql = String.format(
//                "update taskinfo\n" +
//                        "    set taskinfo.TaskStatus = case taskinfo.TaskID\n" +
//                        "        %s\n" +
//                        "    end\n" +
//                        "where taskinfo.TaskID in (%s)",whenStr.toString(),inStr.toString());
//        System.out.println(sql);

        String emailList = "tech-test-arch@yolo24.com,lizonglin@yolo24.com";
        List<String> mails = Arrays.asList(emailList.split(","));

        System.out.println(mails);
    }

}
