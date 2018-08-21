package com.gome.test.testng;

import com.gome.test.Constant;
import com.gome.test.api.annotation.CaseOwner;
import com.gome.test.context.ContextUtils;
import com.gome.test.context.IContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.*;

public class GTPReporter  implements IReporter {

    private Set<String> noExists;
    private Exception ex;
    private String outputDirectory;
    private File[] pictureList = null;
    private Boolean isGui = false;


    public void initPictureList(String outputDirectory) {
        File outputDirectoryFile = new File(outputDirectory);
        if (!outputDirectoryFile.exists())
            outputDirectoryFile.mkdir();

        String picturePath = String.format("%s/../test-classes/screenCapture", outputDirectory);
        File pictureFile = new File(picturePath);

        if(this.pictureList==null && pictureFile.exists())
        {
            this.pictureList = pictureFile.listFiles();
            Arrays.sort(pictureList, new CompratorByLastModified());
            isGui=true;
        }
    }


    public GTPReporter() {
        this(null, null, null);
    }

    public GTPReporter(Set<String> noExists, Set<String> reRunSuites) {
        this(noExists, reRunSuites, null);
    }


    public GTPReporter(Set<String> noExists, Set<String> reRunSuites, Exception ex) {
        if (null == noExists) {
            this.noExists = new HashSet<String>();
        } else {
            this.noExists = noExists;
        }

        this.ex = ex;
    }

    private String getAuthors(ITestNGMethod method) {
        Annotation[] annotations = method.getConstructorOrMethod().getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == CaseOwner.class) {
                return ((CaseOwner) annotation).description();
            }
        }

        return Constant.UNKNOW;
    }


    public void generateReport(List<XmlSuite> xmlSuitesTest, List<ISuite> suitesTest, String outputDirectory) {

        File outputDirFile = new File(outputDirectory);
        if (!outputDirFile.exists())
            outputDirFile.mkdir();

        this.outputDirectory = outputDirectory;
        JSONArrayCase arr = new JSONArrayCase();

        for (ISuite suite : suitesTest) {
            String description = suite.getParameter(Constant.DESCRIPTION);
            String owner = suite.getParameter(Constant.OWNER.toLowerCase());
            if (suite.getParameter(Constant.SKIP_ONCE_FAIL) == null) {
                List<JSONObject> atomCases = parseAsJsonForAtomCaseSuite(suite);
                for (JSONObject atomCase : atomCases) {
                    arr.put(atomCase);
                }
            } else {
                JSONObject o = parseAsJsonForOrderCaseSuite(suite.getName(), suite, description, owner);
                arr.put(o);
            }
        }

        for (String caseName : noExists) {
            arr.put(getNoExistCase(caseName, arr.getMinStartDate()));
        }

        JSONObject obj = new JSONObject();
        String filePath = String.format("%s/japi4gtp.json", outputDirectory);


        appendSummary(obj, arr.getPassed(), arr.getFailed(), arr.getAborted() + noExists.size(), arr.getMinStartDate(), arr.getMaxEndDate(), filePath, new Date().getTime());
        obj.put(Constant.DETAILS, arr);
        Writer writer = null;
        try {
            try {
                File reportFile = new File(filePath);
                if (!reportFile.getParentFile().exists())
                    reportFile.getParentFile().mkdirs();
                writer = new OutputStreamWriter(new FileOutputStream(reportFile), Constant.UTF_8);
                writer.write(obj.toString(2));

            } finally {
                if (null != writer) {
                    writer.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        this.deleteFile();
    }



    private int getDate(long begin) {
        Date date = new Date(begin);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.HOUR_OF_DAY) < 8)
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return Integer.valueOf(format.format(calendar.getTime()));
    }


    private void appendSummary(JSONObject obj, int pass, int failed, int aborted, long minStartDate, long maxEndDate, String filePath, long generateTime) {
        obj.put(Constant.TOTAL_CASES, pass + failed + aborted);
        obj.put(Constant.PASSED, pass);
        obj.put(Constant.FAILED, failed);
        obj.put(Constant.ABORD, aborted);
        obj.put(Constant.START_TIME, minStartDate);
        obj.put(Constant.END_TIME, maxEndDate);
        obj.put(Constant.DURATION, getDuration(minStartDate, maxEndDate));
        obj.put(Constant.RESULT_FILE_PATH, filePath);
        obj.put(Constant.GENERATE_TIME, generateTime);
        obj.put(Constant.DATE, getDate(minStartDate));
        obj.put(Constant.TASK_ID, 0);
        obj.put(Constant.TASK_TYPE, 0);
        obj.put(Constant.SPLIT_TIME, 0);

        obj.put("browser",1222);//System.getProperty("selenium.browser")

        if (null != ex) {
            obj.put("ErrorMessage", ex.getMessage());
        }
    }

    private long getDuration(long startDate, long endDate) {
        return (endDate - startDate);
    }

    private JSONObject getNoExistCase(String caseName, long date) {
        JSONObject obj = new JSONObject();
        obj.put(Constant.TEST_CASE_NAME, caseName);
        obj.put(Constant.ERROR_MESSAGE, String.format("testng-%s.xml not exists", caseName));
        obj.put(Constant.START_TIME, date);
        obj.put(Constant.END_TIME, date);
        obj.put(Constant.TEST_RESULT, Constant.NOT_EXECUTED);
        return obj;
    }



//    private File getFile(){
//        File file;
//        file = new File(System.getProperty("user.dir")+ Constant.FILE_SEPARATOR+"//target//Context.txt");
///*        if (System.getenv().get("OS").contains("Windows")){
//            file = new File("C:\\"+Constant.FILE_SEPARATOR+"Context.txt");
//        }else {
//            file = new File(Constant.FILE_SEPARATOR+"app"+Constant.FILE_SEPARATOR+"Context.txt");
//        }*/
//        return file;
//    }
//
    /*
    private void deleteFile(){
        File file = this.getFile();
        if (file.exists()){
            file.delete();
        }
    }

    private String readFile(int lines) throws IOException{
        String value = null;
        int i =0;
        File file = this.getFile();
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(fis,"utf-8");
        BufferedReader bufread = new BufferedReader(reader);
        while (i<lines){
            value = bufread.readLine();
            i++;
        }
        bufread.close();
        reader.close();
        fis.close();
        return value;
    }
    */
    //取消使用读取文件获取post信息
    String text =null;
//    int i=1;
    private List<JSONObject> parseAsJsonForAtomCaseSuite(ISuite suite) {
        List<JSONObject> cases = new ArrayList<JSONObject>();


        for (Map.Entry<String, ISuiteResult> result : suite.getResults().entrySet()) {
//            try {
//                text = this.readFile(i);
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
            cases.add(GenerateJsonObjectForMethod(suite, result.getValue()));

//            i++;
        }

//        this.text=null;
        return cases;

    }

    private JSONObject GenerateJsonObjectForMethod(ISuite suite, ISuiteResult result) {
        ITestNGMethod method = result.getTestContext().getAllTestMethods()[0];
        return GenerateJsonObject(method.getMethodName(), suite, method.getDescription(), result);
    }

    private JSONObject GenerateJsonObject(String caseName, ISuite suite, String description, ISuiteResult result) {
        String host = suite.getHost();
        long startDate = result.getTestContext().getStartDate().getTime();
        long endDate = result.getTestContext().getEndDate().getTime();
        long duration = getDuration(startDate, endDate);
        String testResult = getRunState(result.getTestContext());

        int reRunCount = result.getTestContext().getFailedTests().size() + result.getTestContext().getPassedTests().size() - 1;

        String errorMessage = null;
        String stackTrace = null;
        if (testResult.equals(Constant.FAILED)) {
            errorMessage = getErrorMessage(result.getTestContext());
            stackTrace = getStackTrace(result.getTestContext());
        }

        String owner = getAuthors(result.getTestContext().getAllTestMethods()[0]);
        String picturePath = this.getPicture(startDate, endDate, description);

        String casePicturePath = this.getCasePicture(startDate, endDate);

        //获取存放的post请求信息
        text = this.getPost(result);

        return generateJSONObject(caseName, description, reRunCount, host, startDate,
                endDate, duration, testResult, errorMessage, stackTrace, owner, picturePath, casePicturePath, null);
    }

    private String getPost(ISuiteResult result){
        ITestContext itc = result.getTestContext();
        IResultMap passirmap = itc.getPassedTests();
        IResultMap failermap = itc.getFailedTests();
        IResultMap skipmap = itc.getSkippedTests();
        if (passirmap.size()>failermap.size()){
            try {
                Iterator<ITestResult> iterator =passirmap.getAllResults().iterator();
                if (iterator.hasNext()){
                    return iterator.next().getAttribute("post").toString();
                }else {
                    return "未发现post信息";
                }
            }catch (Exception e){
                try {
                    throw new Exception("从ITestResult中获取运行成功属性值post时发生异常");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }else if(failermap.size()>passirmap.size()){
            try {
                Iterator<ITestResult> iterator =failermap.getAllResults().iterator();
                if (iterator.hasNext()){
                    return iterator.next().getAttribute("post").toString();
                }else {
                    return "未发现post信息";
                }
            }catch (Exception e){
                try {
                    throw new Exception("从ITestResult中获取运行失败属性值post时发生异常");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }else{
            return "跳过测试未发现post信息";
        }
        return null;
    }
//取消使用读取文件获取post信息
//    int readline=1;
    private JSONObject parseAsJsonForOrderCaseSuite(String caseName, ISuite suite, String description, String owner) {
        JSONArray children = new JSONArray();
        long startDateAll = 0;
        long endDateAll = 0;
        long durationAll = 0;
        String testResultAll = Constant.NOT_EXECUTED;


        for (Map.Entry<String, ISuiteResult> result : suite.getResults().entrySet()) {
//            try {
//                text = this.readFile(readline);
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
//            readline++;
            JSONObject json = GenerateJsonObjectForMethod(suite, result.getValue());
            children.put(json);

            if (startDateAll == 0)
                startDateAll = json.getLong(Constant.START_TIME);

            endDateAll = json.getLong(Constant.END_TIME);
            durationAll += json.getDouble(Constant.DURATION);

            if (json.getString(Constant.TEST_RESULT).equals(Constant.FAILED))
                testResultAll = Constant.FAILED;
            else if (json.getString(Constant.TEST_RESULT).equals(Constant.PASSED) && testResultAll.equals(Constant.FAILED) == false)
                testResultAll = Constant.PASSED;
        }
//        this.deleteFile(); //多个ordercase生成json，完成第一个后直接删除导致后续的ordercase没有url
//        this.text=null;

        String picturePath = this.getPicture(startDateAll, endDateAll, description);

        return generateJSONObject(caseName, description, 0, suite.getHost(), startDateAll,
                endDateAll, durationAll, testResultAll, "", "", owner, picturePath, "", children);
    }

    private String getCasePicture(Long startTime, Long endTime) {
        if (!isGui)
            return null;

        StringBuilder prctureStr = new StringBuilder();

        for(int i=0;i< this.pictureList.length;i++)
        {
            if(this.pictureList[i] !=null ) {

                String[] klist = this.pictureList[i].getName().split("_");
                    //记住用户名密码登录_case_1435308917449-1-Common.异步加载-Succ_1459484743026_1.png
                    Long t = Long.valueOf(klist[klist.length - 2]);
                    if (t > startTime && t < endTime) {
                        prctureStr.append(this.pictureList[i].getName() + ";");
                        this.pictureList[i] = null;
                    }
            }

        }
        return prctureStr.length() == 0 ? prctureStr.toString() : prctureStr.toString().substring(0, prctureStr.toString().length() - 1);

    }

    private String getPicture(long startTime, long endTime, String des) {
        if (!isGui)
            return null;

        StringBuilder prctureStr = new StringBuilder();

        for(int i=0;i< this.pictureList.length;i++)
        {
            if(this.pictureList[i] !=null ) {

                String[] klist = this.pictureList[i].getName().split("_");
                if (klist[0].trim().toUpperCase().equals(des.toUpperCase().trim())) {
                    //记住用户名密码登录_case_1435308917449-1-Common.异步加载-Succ_1459484743026_1.png
                    Long t = Long.valueOf(klist[klist.length - 2]);
                    if (t > startTime && t < endTime) {
                        prctureStr.append(this.pictureList[i].getName() + ";");
                        this.pictureList[i] = null;
                    }

                }
            }

        }
        return prctureStr.length() == 0 ? prctureStr.toString() : prctureStr.toString().substring(0, prctureStr.toString().length() - 1);
    }


    private JSONObject generateJSONObject(String caseName, String description, int reRunCount, String host, long startDate,
                                          long endDate, long duration, String testResult,
                                          String errorMessage, String stackTrace, String owner, String pirctPath, String casePicturePath, JSONArray children) {
        JSONObject child = new JSONObject();
        child.put(Constant.TEST_CASE_NAME, caseName);
        child.put(Constant.CASE_DESC, description);
        child.put(Constant.RE_RUN, reRunCount > 1);
        child.put(Constant.RE_RUN_COUNT, reRunCount);
        child.put(Constant.COMPUTER_NAME, host);
        child.put(Constant.START_TIME, startDate);
        child.put(Constant.END_TIME, endDate);
        child.put(Constant.DURATION, duration);
        child.put(Constant.TEST_RESULT, testResult);
        child.put(Constant.ERROR_MESSAGE, errorMessage);
        child.put(Constant.STACK_TRACE, stackTrace);
        child.put(Constant.OWNER.toLowerCase(), owner);
        child.put("Post", text);
/*        System.out.println("------------------------------------------------");
        System.out.println("打印："+IContext.context.get("l"));
        System.out.println("------------------------------------------------");*/

        if (isGui) {
            child.put("picturePath", pirctPath);
            child.put("casePicturePath", casePicturePath);
            child.put("browser",ContextUtils.getContext().get("selenium.browser"));
        }
        child.put(Constant.CHILDREN, children == null ? new JSONArray() : children);
        this.text=null;
        return child;
    }


    private String getErrorMessage(ITestContext context) {
        StringBuilder sb = new StringBuilder();
        for (ITestResult testResult : context.getFailedTests().getAllResults()) {
            sb.append(testResult.getThrowable().getMessage());
        }
        return sb.toString();
    }

    private String getStackTrace(ITestContext context) {
        Set<ITestResult> testResults = context.getFailedTests().getAllResults();
        StringBuilder sb = new StringBuilder();
        for (ITestResult testResult : testResults) {
            Throwable tw = testResult.getThrowable();
            sb.append(tw);
            StackTraceElement[] elements = tw.getStackTrace();
            for (StackTraceElement element : elements) {
                sb.append(String.format("%s\tat ", Constant.LINE_SEPARATOR));
                sb.append(element.toString());
            }
        }
        return sb.toString();

    }

    private String getRunState(ITestContext context) {
        int passedTests = context.getPassedTests().getAllResults().size();
        int failedTests = context.getFailedTests().getAllResults().size();
        int skippedTests = context.getSkippedTests().getAllResults().size();

        String testResult = Constant.PASSED;
        if (failedTests > 0 && passedTests == 0) {
            testResult = Constant.FAILED;
        } else if (skippedTests > 0) {
            testResult = Constant.NOT_EXECUTED;
        }
        return testResult;
    }


    class JSONArrayCase extends JSONArray {
        private int passed = 0;
        private int failed = 0;
        private int aborted = 0;
        private long minStartDate = new Date().getTime();
        private long maxEndDate = -1;

        public int getPassed() {
            return passed;
        }

        public int getFailed() {
            return failed;
        }

        public int getAborted() {
            return aborted;
        }

        public long getMinStartDate() {
            return minStartDate;
        }

        public long getMaxEndDate() {
            if (-1 == maxEndDate)
                return minStartDate;

            return maxEndDate;
        }

        @Override
        public JSONArray put(Object value) {
            if (value instanceof JSONObject && ((JSONObject) value).getString(Constant.TEST_CASE_NAME).isEmpty() == false) {
                String testResult = ((JSONObject) value).getString(Constant.TEST_RESULT);
                if (testResult.equals(Constant.PASSED)) {
                    ++passed;
                } else if (testResult.equals(Constant.FAILED)) {
                    ++failed;
                } else {
                    ++aborted;
                }

                long startDate = ((JSONObject) value).getLong(Constant.START_TIME);
                long endDate = ((JSONObject) value).getLong(Constant.END_TIME);
                if (minStartDate > startDate) {
                    minStartDate = startDate;
                }
                if (maxEndDate < endDate) {
                    maxEndDate = endDate;
                }
            }
            return super.put(value);
        }
    }



class CompratorByLastModified implements Comparator<File>
    {
        public int compare(File f1, File f2) {
            long diff = f1.lastModified()-f2.lastModified();
            if(diff>0)
                return 1;
            else if(diff==0)
                return 0;
            else
                return -1;
        }
        public boolean equals(Object obj){
            return true;
        }
    }
}




