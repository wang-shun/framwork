package com.gome.test.gtp.jenkins;

import com.gome.test.gtp.dao.AppInfoDao;
import com.gome.test.gtp.dao.TaskListDao;
import com.gome.test.gtp.model.EnvInfo;
import com.gome.test.gtp.schedule.AgentBo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.utils.Logger;
import com.gome.test.utils.StringUtils;
import com.mongodb.util.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizonglin on 2015/5/12.
 */
@Service
public class JenkinsBo {
    @Autowired
    private Environment env;

    @Autowired
    private TaskListDao taskListDao;

    @Autowired
    private AgentBo agentBo;

    @Autowired
    private AppInfoDao appInfoDao;

    /**
     * 向Jenkins发送带请求体的restful post请求
     *
     * @param cfgFileStr
     * @param jobName
     * @param taskType
     * @return
     * @throws FileNotFoundException
     */
    public int postAttachJenkinsMethod(String cfgFileStr, String jobName, String taskType) throws FileNotFoundException {
        HttpClient client = new HttpClient();
//        client.getHostConfiguration().setProxy("127.0.0.1", 8888);
        String viewName = env.getProperty("jenkins.view." + taskType.toLowerCase());
//        String jenkinsBaseURL = "http://" + agentIp + ":" + port;
        String jenkinsBaseURL = env.getProperty("jenkins.baseurl");
//        System.out.println("jenkins uri is : " + jenkinsBaseURL);
        PostMethod postMethod = new PostMethod(jenkinsBaseURL + "/view/" + viewName + "/createItem?name=" + jobName);
/**
 * Jenkins认证相关已注释掉
 */
//        String jenkinsAdmin = env.getProperty("jenkins.admin");
//        String jenkinsPassword = env.getProperty("jenkins.password");
//        client.getParams().setAuthenticationPreemptive(true);
//        Credentials defaultcreds = new UsernamePasswordCredentials(jenkinsAdmin, jenkinsPassword);
//        client.getState().setCredentials(new AuthScope(baseUrl, port), defaultcreds);
        postMethod.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
        postMethod.setRequestBody(cfgFileStr);
//        postMethod.setDoAuthentication(true);
        int createStatus = 0;
        try {
            createStatus = client.executeMethod(postMethod);
            Logger.info(createStatus + "\n" + postMethod.getResponseBodyAsString());
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return createStatus;
    }

    /**
     * 向Jenkins发送restful post请求
     *
     * @param url
     * @return
     */
    public int postJenkinsMethod(String url) {
        HttpClient client = new HttpClient();

        String jenkinsBaseURL = env.getProperty("jenkins.baseurl");//分view下的Job
        PostMethod postMethod = new PostMethod(jenkinsBaseURL + url);
        int status = 0;
        try {
            status = client.executeMethod(postMethod);
            Logger.info(status + "\n" + postMethod.getResponseBodyAsString());
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return status;
    }

    public int simpleGetJenkinsMethod(String url) {
        HttpClient client = new HttpClient();
        String jenkinsBaseURL = env.getProperty("jenkins.baseurl");
        GetMethod getMethod = new GetMethod(jenkinsBaseURL + url);
        int httpStatus = 0;
        try {
            httpStatus = client.executeMethod(getMethod);
        } catch (Exception ex) {
            Logger.error("获取Jenkins job 失败 " + ex.getMessage());
        }
        return httpStatus;
    }

    /**
     * 向Jenkins发送restful get请求
     *
     * @param url
     * @return
     */
    public ResponseInfo getJenkinsMethod(String url) {
        HttpClient client = new HttpClient();
        ResponseInfo responseInfo = new ResponseInfo();

        String jenkinsBaseURL = env.getProperty("jenkins.baseurl");//分view下的Job，view分法待定
        GetMethod getMethod = new GetMethod(jenkinsBaseURL + url);
        try {
            responseInfo.setHttpCode(client.executeMethod(getMethod));
            responseInfo.setResponse(getMethod.getResponseBodyAsString());
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return responseInfo;
    }

    /**
     * 根据XPath表达式xpathExp,取xmlStr 中的节点
     *
     * @param xmlStr
     * @param xpathExp
     * @return
     * @throws DocumentException
     * @throws XPathExpressionException
     */
    public NodeList getXMLNode(String xmlStr, String xpathExp) throws DocumentException, XPathExpressionException {
        ByteArrayInputStream stream = new ByteArrayInputStream(xmlStr.getBytes());
        InputSource doc = new InputSource(new InputStreamReader(stream));
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile(xpathExp);
        return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    }

    /**
     * 获取postAttach请求所需的config文件字符串
     *
     * @param taskListId
     * @param jobName
     * @param agentLabel
     * @param taskInfos
     * @return
     * @throws IOException
     */
    public String replaceConfigFile(int taskListId, String jobName, String agentLabel, Object[] taskInfos) throws IOException {
        /**
         * 根据机器 OS 和 TaskType 获取 config.xml 文件
         */
        String os = agentBo.getAgentOSByLabel(agentLabel);
        String taskType = String.valueOf(taskInfos[1]);
        String osType = String.format("%s-%s", os.toLowerCase(), taskType);

        return repConfigFile(loadConfigMapByType(taskListId, jobName, agentLabel, taskInfos), osType);
    }



    public String replaceConfigFile(String jobName,String agentLabel,String taskType,String svnUrl,int envId, EnvInfo envInfo) throws IOException
    {
        /**
         * 根据机器 OS 和 TaskType 获取 config.xml 文件
         */
        String os = agentBo.getAgentOSByLabel(agentLabel);
//        System.out.println("%%%%%%%%% get os is : " + os);
        String osType = String.format("%s-%s", os.toLowerCase(), taskType);

        String envString=envInfo.getZkAddress();
        if(!StringUtils.isEmpty(envString))
        {
            //目前没有获取地方，暂定写死
            //todo need get from db or SOA.
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("version","userBase-facade:1.0.0UAT-SNAPSHOT,userBase-common:1.0.0UAT-SNAPSHOT");
            jsonObject.put("URIString",envString);
            envString= jsonObject.toString();
            envString=envString.replace("\"","\\\"");
        }

//        System.out.println(" ---get env string is : " + envString);

        return repConfigFile(loadConfigMapByType(jobName, agentLabel,svnUrl,taskType,envId,envString), osType);

    }




    /**
     * 装载替换文件的键值映射表
     *
     * @param listId
     * @param jobName
     * @param agentLabel
     * @param taskInfos
     * @return
     */
    public Map<String, String> loadConfigMapByType(int listId, String jobName, String agentLabel, Object[] taskInfos) {
        //TaskListId
        String taskListId = String.valueOf(listId);
        //TaskType int，决定了config文件的命令
        String taskType = String.valueOf(taskInfos[1]);
        //Email List
        String emailList = String.valueOf(taskInfos[2]);
        //Svn URL
        String svnUrl = String.valueOf(taskInfos[3]);
        //Svn URL 最后一个 / 之后的文件夹名
        String projectName = svnUrl.substring(svnUrl.lastIndexOf("/") + 1, svnUrl.length());
        //jobName
        String jobHosts = taskListDao.getHostsByTaskListId(listId).get(0).toString();
        //机器OS, 决定了config文件中的路径、命令
        String agentOS = agentBo.getAgentOSByLabel(agentLabel);
        //API LOAD 暂时认为只在Linux机器执行
        String svnCodeDir = env.getProperty("jenkins.linux.svn_code_dir");
        String jobDir = env.getProperty("jenkins.linux.job_dir");
        //URL端口号之后的路径
        String svnDir = svnUrl.substring(svnUrl.indexOf("/", 10));

        /**
         * GUI（Windows/Mac+Python） 需特殊处理, 在Win或Mac系统执行
         */
        String svnCodeDir2 = "";
        String jobDir2 = "";
        String guiFtpSubDir = "";
        String guiFtpRootDir = "";
        String guiFtpHost = "";
        String guiFtpUsername = "";
        String guiFtpPassword = "";
        String loadFtpSubdir = "";
        String svnUserName = "";
        String svnPassword = "";

        guiFtpSubDir = env.getProperty("gtp.gui.ftp.subdir");
        guiFtpHost = env.getProperty("gtp.gui.ftp.host");
        guiFtpUsername = env.getProperty("gtp.gui.ftp.username");
        guiFtpPassword = env.getProperty("gtp.gui.ftp.password");

        svnUserName = env.getProperty("casecategory.svn_username");
        svnPassword = env.getProperty("casecategory.svn_password");

        if (String.valueOf(Constant.TASK_TYPE_GUI).equals(taskType)) {
            if (agentOS.equals(Constant.AGENT_OS_WIN)) {//Win系统
                guiFtpRootDir = env.getProperty("gtp.gui.ftp.rootdir").replace("\\", "\\\\\\\\");
                jobHosts = jobHosts.replace("\n", "\\\\n");
                svnCodeDir = env.getProperty("jenkins.win.svn_code_dir").replace("\\", "\\\\\\\\");
                svnCodeDir2 = env.getProperty("jenkins.win.svn_code_dir2").replace("\\", "\\\\\\\\");
                svnDir = svnDir.replace("/", "\\\\\\\\");
                jobDir = env.getProperty("jenkins.win.job_dir").replace("\\", "\\\\\\\\");
                jobDir2 = env.getProperty("jenkins.win.job_dir2").replace("\\", "\\\\\\\\");
            } else {//Mac系统
                guiFtpRootDir = env.getProperty("gtp.gui.ftp.rootdir");
                jobHosts = jobHosts.replace("\n", "\\\\n");
                svnCodeDir = env.getProperty("jenkins.mac.svn_code_dir");
                svnCodeDir2 = env.getProperty("jenkins.mac.svn_code_dir2");
                jobDir = env.getProperty("jenkins.mac.job_dir");
                jobDir2 = env.getProperty("jenkins.mac.job_dir2");
            }


        }

        /**
         * LOAD 需要特殊处理
         */
        String loadVerify = "";
        if (String.valueOf(Constant.TASK_TYPE_LOAD).equals(taskType)) {
            if (!String.valueOf(taskInfos[6]).trim().equals("")) {
                loadVerify = String.format(" -P %s", String.valueOf(taskInfos[6]));
            }
            loadFtpSubdir = env.getProperty("gtp.load.ftp.subdir");
        }

        /**
         * 需要替换的字段map：repMap，包含5个字段
         *
         * 暂时未分类
         */
        Map<String, String> repMap = new HashMap<String, String>();
        repMap.put(Constant.CONFIG_TASKLIST_ID, taskListId);
        repMap.put(Constant.CONFIG_TASK_TYPE, taskType);
        repMap.put(Constant.CONFIG_EMAIL_LIST, emailList);
        repMap.put(Constant.CONFIG_SVN_URL, svnUrl);
        repMap.put(Constant.CONFIG_AGENT_LABEL, agentLabel);
        repMap.put(Constant.CONFIG_PROJECT_NAME, projectName);
        repMap.put(Constant.CONFIG_SVN_CODE_DIR, svnCodeDir);
        repMap.put(Constant.CONFIG_SVN_CODE_DIR2, svnCodeDir2);
        repMap.put(Constant.CONFIG_SVN_DIR, svnDir);
        repMap.put(Constant.CONFIG_JOB_DIR, jobDir);
        repMap.put(Constant.CONFIG_JOB_DIR2, jobDir2);
        repMap.put(Constant.CONFIG_JOB_NAME, jobName);
        repMap.put(Constant.CONFIG_JOB_HOSTS, jobHosts);
        repMap.put(Constant.CONFIG_LOAD_VERIFY, loadVerify);
        repMap.put(Constant.CONFIG_LOAD_WORKDIR, jobDir + jobName + "/" + projectName);
        repMap.put(Constant.CONFIG_LOAD_TMPLIBDIR, jobDir + jobName + "/" + projectName + "/ExecuteTest/tmplib");
        repMap.put(Constant.CONFIG_LOAD_LIBDIR, jobDir + jobName + "/" + projectName + "/ExecuteTest/lib");

        repMap.put(Constant.GUI_FTP_REPORT_SUBDIR, guiFtpSubDir);
        repMap.put(Constant.GUI_FTP_REPORT_ROOTDIR, guiFtpRootDir);
        repMap.put(Constant.GUI_FTP_REPORT_HOST, guiFtpHost);
        repMap.put(Constant.GUI_FTP_REPORT_USERNAME, guiFtpUsername);
        repMap.put(Constant.GUI_FTP_REPORT_PASSWORD, guiFtpPassword);

        repMap.put(Constant.CONFIG_SVN_USERNAME, svnUserName);
        repMap.put(Constant.CONFIG_SVN_PASSWORD, svnPassword);

        repMap.put(Constant.LOAD_FTP_REPORT_SUBDIR, loadFtpSubdir);

        return repMap;
    }

    /**
     * 根据任务类型和键值映射表选择相应的配置文件模板进行替换
     *
     * @param repMap
     * @param osType
     * @return
     * @throws IOException
     */
    private String repConfigFile(Map<String, String> repMap, String osType) throws IOException {
        return replaceFileFromResourceName(repMap, Constant.CFG_MAP.get(osType));
    }

    public String replaceFileFromInputStream(Map<String, String> repMap, InputStream fis) throws IOException {
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
//        System.out.println("-------get map size is : " + repMap.size());
        String line;
        int indexBegin;
        int indexEnd;
        String key;
        String repStr;
        String resultCfgStr = "";
        while ((line = br.readLine()) != null) {
            while (line.indexOf("/#") != -1 && line.indexOf("#/") != -1) {
                indexBegin = line.indexOf("/#");
                indexEnd = line.indexOf("#/");
                key = line.substring(indexBegin + 2, indexEnd);
                repStr = line.substring(indexBegin, indexEnd + 2);
                if (repMap.get(key) != null) {
//                    System.out.println("----------: "+repStr + " ; replace "+  repMap.get(key));
                    line = line.replaceAll(repStr, repMap.get(key));
                }
            }
            resultCfgStr = resultCfgStr + line + "\r\n";
        }
        br.close();
        isr.close();
        fis.close();
//        System.out.println("------------send to job body is : "+ resultCfgStr);
        return resultCfgStr;
    }

    /**
     * 替换配置文件模板，/#XXX#/形式的字段均为占位符
     *
     * @param repMap
     * @param fileName
     * @return
     * @throws IOException
     */
    public String replaceFileFromResourceName(Map<String, String> repMap, String fileName) throws IOException {
        InputStream fis = this.getClass().getResourceAsStream(String.format("/%s", fileName));
//        System.out.println("$$$$$$$$$$$$$$ get file name is: " + fileName);
        return replaceFileFromInputStream(repMap, fis);
    }

    public String getJenkinsIpPort() {
        return env.getProperty("jenkins.baseurl");
    }

    public static void main(String[] args) throws IOException, XPathExpressionException {


        String envString="";
        //目前没有获取地方，暂定写死
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("version","userBase-facade:1.0.0UAT-SNAPSHOT,userBase-common:1.0.0UAT-SNAPSHOT");
        jsonObject.put("URIString",envString);
        envString= jsonObject.toString();
        envString=envString.replace("\"","\\\"");
//        System.out.println(envString);
        Logger.info(envString);
//        HttpClient client = new HttpClient();
//        String jenkinsIp = "10.126.59.2";
//        String port = "12345";
//        String jenkinsBaseURL = "http://" + jenkinsIp + ":" + port;//分view下的Job，view分法待定
//        String path = "/job/1055_0_0_1453534274679";
//
//        GetMethod getMethod = new GetMethod(jenkinsBaseURL + path);
//        PostMethod postMethod = new PostMethod(jenkinsBaseURL + "/job/SecondDemo/5/stop");
//        String responseBody;
//        int httpCode = client.executeMethod(postMethod);
//        responseBody = postMethod.getResponseBodyAsString();
//        int httpCode = client.executeMethod(getMethod);
//        responseBody = getMethod.getResponseBodyAsString();
//System.out.println(httpCode);
//        System.out.println(responseBody);
//
//
//        String xpathExp = "//build";
//        ByteArrayInputStream stream = new ByteArrayInputStream(responseBody.getBytes());
//        InputSource doc = new InputSource(new InputStreamReader(stream));
//        XPathFactory xPathfactory = XPathFactory.newInstance();
//        XPath xpath = xPathfactory.newXPath();
//        XPathExpression expr = xpath.compile(xpathExp);
//        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//
//
//        System.out.println(nodeList.getLength());
//        System.out.println(System.getProperty("file.separator"));
        Logger.info(System.getProperty("file.separator"));


//        String s1 = "Test the simple";
//        System.out.println(s1.replaceAll("the", "&"));

        Process p = Runtime.getRuntime().exec("tasklist");
//        System.out.println(p.toString());
        Logger.info(p.toString());


        String svnUrl = "https://repo.ds.gome.com.cn:8443/svn/test/SourceCode/sample/Trunk/openURL";
        String svnDir = svnUrl.substring(svnUrl.indexOf("/", 10));
//        System.out.println("svnDir " + svnDir);
    }



    public Map<String, String> loadConfigMapByType(String jobName, String agentLabel,String svnUrl,String taskType,int envId,String urlString) {


        //Email List
        String emailList = "";

        //Svn URL 最后一个 / 之后的文件夹名
        String projectName = svnUrl.substring(svnUrl.lastIndexOf("/") + 1, svnUrl.length());
        //jobName
        String jobHosts = envId>0? taskListDao.getHostsByEnv(envId).get(0).toString():"";
//        System.out.println("$$$$$$$$$$$$$$$$$ job host name is : " + jobHosts);
        //机器OS, 决定了config文件中的路径、命令
        String agentOS = agentBo.getAgentOSByLabel(agentLabel);
//        System.out.println("$$$$$$$$$$$$$$$$$ agent os is : " + agentOS);
        //API LOAD 暂时认为只在Linux机器执行
        String svnCodeDir = env.getProperty("jenkins.linux.svn_code_dir");
        String jobDir = env.getProperty("jenkins.linux.job_dir");
        //URL端口号之后的路径
        String svnDir = svnUrl.substring(svnUrl.indexOf("/", 10));

        /**
         * GUI（Windows/Mac+Python） 需特殊处理, 在Win或Mac系统执行
         */
        String svnCodeDir2 = "";
        String jobDir2 = "";
        String guiFtpSubDir = "";
        String guiFtpRootDir = "";
        String guiFtpHost = "";
        String guiFtpUsername = "";
        String guiFtpPassword = "";
        String loadFtpSubdir = "";
        String svnUserName = "";
        String svnPassword = "";

        guiFtpSubDir = env.getProperty("gtp.gui.ftp.subdir");
        guiFtpHost = env.getProperty("gtp.gui.ftp.host");
        guiFtpUsername = env.getProperty("gtp.gui.ftp.username");
        guiFtpPassword = env.getProperty("gtp.gui.ftp.password");

        svnUserName = env.getProperty("casecategory.svn_username");
        svnPassword = env.getProperty("casecategory.svn_password");

        if (String.valueOf(Constant.TASK_TYPE_GUI).equals(taskType)) {
            if (agentOS.equals(Constant.AGENT_OS_WIN)) {//Win系统
                guiFtpRootDir = env.getProperty("gtp.gui.ftp.rootdir").replace("\\", "\\\\\\\\");
                jobHosts = jobHosts.replace("\n", "\\\\n");
                svnCodeDir = env.getProperty("jenkins.win.svn_code_dir").replace("\\", "\\\\\\\\");
                svnCodeDir2 = env.getProperty("jenkins.win.svn_code_dir2").replace("\\", "\\\\\\\\");
                svnDir = svnDir.replace("/", "\\\\\\\\");
                jobDir = env.getProperty("jenkins.win.job_dir").replace("\\", "\\\\\\\\");
                jobDir2 = env.getProperty("jenkins.win.job_dir2").replace("\\", "\\\\\\\\");
            } else {//Mac系统
                guiFtpRootDir = env.getProperty("gtp.gui.ftp.rootdir");
                jobHosts = jobHosts.replace("\n", "\\\\n");
                svnCodeDir = env.getProperty("jenkins.mac.svn_code_dir");
                svnCodeDir2 = env.getProperty("jenkins.mac.svn_code_dir2");
                jobDir = env.getProperty("jenkins.mac.job_dir");
                jobDir2 = env.getProperty("jenkins.mac.job_dir2");
            }


        }

        /**
         * LOAD 需要特殊处理
         */
        String loadVerify = "";

        /**
         * 需要替换的字段map：repMap，包含5个字段
         *
         * 暂时未分类
         */
        Map<String, String> repMap = new HashMap<String, String>();
        repMap.put(Constant.CONFIG_TASKLIST_ID, "");
        repMap.put(Constant.CONFIG_TASK_TYPE, taskType);
        repMap.put(Constant.CONFIG_EMAIL_LIST, emailList);
        repMap.put(Constant.CONFIG_SVN_URL, svnUrl);
        repMap.put(Constant.CONFIG_AGENT_LABEL, agentLabel);
        repMap.put(Constant.CONFIG_PROJECT_NAME, projectName);
        repMap.put(Constant.CONFIG_SVN_CODE_DIR, svnCodeDir);
        repMap.put(Constant.CONFIG_SVN_CODE_DIR2, svnCodeDir2);
        repMap.put(Constant.CONFIG_SVN_DIR, svnDir);
        repMap.put(Constant.CONFIG_JOB_DIR, jobDir);
        repMap.put(Constant.CONFIG_JOB_DIR2, jobDir2);
        repMap.put(Constant.CONFIG_JOB_NAME, jobName);
        repMap.put(Constant.CONFIG_JOB_HOSTS, jobHosts);
        repMap.put(Constant.CONFIG_LOAD_VERIFY, loadVerify);
        repMap.put(Constant.CONFIG_LOAD_WORKDIR, jobDir + jobName + "/" + projectName);
        repMap.put(Constant.CONFIG_LOAD_TMPLIBDIR, jobDir + jobName + "/" + projectName + "/ExecuteTest/tmplib");
        repMap.put(Constant.CONFIG_LOAD_LIBDIR, jobDir + jobName + "/" + projectName + "/ExecuteTest/lib");

        repMap.put(Constant.GUI_FTP_REPORT_SUBDIR, guiFtpSubDir);
        repMap.put(Constant.GUI_FTP_REPORT_ROOTDIR, guiFtpRootDir);
        repMap.put(Constant.GUI_FTP_REPORT_HOST, guiFtpHost);
        repMap.put(Constant.GUI_FTP_REPORT_USERNAME, guiFtpUsername);
        repMap.put(Constant.GUI_FTP_REPORT_PASSWORD, guiFtpPassword);

        repMap.put(Constant.CONFIG_SVN_USERNAME, svnUserName);
        repMap.put(Constant.CONFIG_SVN_PASSWORD, svnPassword);

        repMap.put(Constant.LOAD_FTP_REPORT_SUBDIR, loadFtpSubdir);

        if(!StringUtils.isEmpty(urlString)) {
            repMap.put(Constant.URLSTRING, urlString);
        }

        return repMap;
    }



}
