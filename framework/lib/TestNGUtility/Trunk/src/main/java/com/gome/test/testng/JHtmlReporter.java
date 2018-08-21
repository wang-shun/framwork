package com.gome.test.testng;

import com.gome.test.Constant;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.uncommons.reportng.HTMLReporter;
import org.uncommons.reportng.ReportNGUtils;

import java.io.*;
import java.util.*;

public class JHtmlReporter extends HTMLReporter {

    private Set<String> reRunSuites;
    private static final String TEMPLATES_PATH = "org/uncommons/reportng/templates/html/";
    private static final String OUTPUT_KEY = "org.uncommons.reportng.escape-output";

    public JHtmlReporter() {
        this(null);
    }

    public JHtmlReporter(Set<String> reRunSuites) {
        if (null == reRunSuites) {
            this.reRunSuites = new HashSet<String>();
        } else {
            this.reRunSuites = reRunSuites;
        }
    }

    @Override
    protected void generateFile(File file,
                                String templateName,
                                VelocityContext context) throws Exception {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), Constant.UTF_8);
        System.setProperty(OUTPUT_KEY, "false");
        ReportNGUtils utils = (ReportNGUtils) context.get("utils");

        List<ISuite> suites = (List<ISuite>) context.get("suites");
        for (int i = 0; i < utils.getAllOutput().size(); i++) {
            if (null != utils.getAllOutput().get(i) && utils.getAllOutput().get(i).trim().isEmpty() == false
                    && utils.getAllOutput().get(i).endsWith("<br/>") == false) {
                utils.getAllOutput().set(i, String.format("%s<br/>", utils.getAllOutput().get(i)));

                formatKeyword(utils, i, "执行 [Given", "执行 [<span style=\"color:green\">Given</span>");
                formatKeyword(utils, i, "执行 [When", "执行 [<span style=\"color:green\">When</span>");
                formatKeyword(utils, i, "执行 [Then", "执行 [<span style=\"color:green\">Then</span>");
                formatKeyword(utils, i, "[ INFO]", "<span style=\"color:green\">[ INFO]</span>");
                formatKeyword(utils, i, "[ERROR]", "<strong style=\"color:red\">[ERROR]</strong>");
                formatKeyword(utils, i, "[ WARN]", "<span style=\"color:#AE8F00\">[ WARN]</span>");
            }
        }

        try {
            Velocity.mergeTemplate(TEMPLATES_PATH + templateName,
                    Constant.UTF_8,
                    context,
                    writer);
            writer.flush();
        } finally {
            writer.close();
        }
//        printLog("", templateName);
        if (templateName.equals("suites.html.vm")) {
            List<String> repassSuiteTestNames = getRepassSuiteTestName(suites);

            List<Integer> replaceLineNum = getReplaceLineNum(file, repassSuiteTestNames);

            Map<String, String> repMap = new HashMap<String, String>();
            repMap.put("failureIndicator", "successIndicator");
            repMap.put("&#x2718;", "&#x2714;");
            replaceFile(file, replaceLineNum, repMap);

        }

    }

    private void formatKeyword(ReportNGUtils utils, int i, String oldWord, String newWord) {
        utils.getAllOutput().set(i, utils.getAllOutput().get(i).replace(oldWord, newWord));
    }

    private List<String> getRepassSuiteTestName(List<ISuite> suites) throws IOException {
        List<String> repassSuiteTestNames = new ArrayList<String>();
        for (int i = 0; i < suites.size(); i++) {
            List<ISuiteResult> results = new ArrayList<ISuiteResult>(suites.get(i).getResults().values());
            for (int j = 0; j < results.size(); j++) {
                if (results.get(j).getTestContext().getPassedTests().size() > 0 && results.get(j).getTestContext().getFailedTests().size() > 0) {
                    repassSuiteTestNames.add(String.format("suite%d_test%d_results.html", i+1, j+1));
//                    printLog(String.format("suite%d_test%d_results.html", i+1, j+1), "suites");
                }
            }
        }
        return repassSuiteTestNames;
    }

    private void replaceFile(File file, List<Integer> replaceLineNum, Map<String, String> repMap) throws IOException {
        InputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        StringBuffer stringBuffer = new StringBuffer();

//        printLog("lineNum ", replaceLineNum.toString());

        int lineIndex = 1;
        String line;
        while ((line = br.readLine()) != null) {
            if (replaceLineNum.contains(lineIndex+1)) {
//                printLog(String.valueOf(lineIndex+1), line);
                for (String repKey : repMap.keySet()) {
                    line = line.replaceAll(repKey, repMap.get(repKey));
                }
            }
            stringBuffer.append(line + "\r\n");
            lineIndex++;
        }
        br.close();
        isr.close();
        fis.close();

        PrintWriter printWriter = new PrintWriter(file, "UTF-8");
        printWriter.write(stringBuffer.toString().toCharArray());
        printWriter.flush();
        printWriter.close();
    }

    private List<Integer> getReplaceLineNum(File file, List<String> repassSuiteTestNames) throws IOException {
//        printLog("getLineNum ", file.getAbsolutePath());
        InputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        List<Integer> replaceLineNum = new ArrayList<Integer>();
        int lineIndex = 1;
        String line;
        while ((line = br.readLine()) != null) {
//            line = line.replaceAll("Test", "TestTest");
            if (line.contains("\"")) {
                if (repassSuiteTestNames.contains(line.split("\"")[1])) {
                    replaceLineNum.add(lineIndex);
                }
            }
            lineIndex++;
        }
        br.close();
        isr.close();
        fis.close();

        return replaceLineNum;
    }


    private void printLog(String log, String templateName) throws IOException {
        FileWriter fileWriter = new FileWriter("D:\\log.txt", true);
        fileWriter.write(log + "------" + templateName + "\n");
        fileWriter.flush();
        fileWriter.close();
    }
}
