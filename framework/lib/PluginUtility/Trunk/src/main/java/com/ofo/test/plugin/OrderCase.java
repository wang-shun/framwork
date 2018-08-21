package com.ofo.test.plugin;

import com.ofo.test.Constant;
import com.ofo.test.plugin.graph.XmlSuite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlTest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderCase {

    private final String id;
    private final String caseDesc;
    private final boolean continueAfterFailure;
    private final String owner;
    private List<String> steps;

    private String level;
    private String descLevel;

    public OrderCase(String id, String caseDesc,
                     boolean continueAfterFailure,
                     String owner, List<String> steps) {
        this.id = id;
        this.caseDesc = caseDesc;
        this.continueAfterFailure = continueAfterFailure;
        if (null == owner) {
            this.owner = Constant.UNKNOW;
        } else {
            this.owner = owner;
        }
        this.steps = steps;
        this.level = "[]";
        this.descLevel = "[]";
    }

    public List<String> getSteps() {
        return steps;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return caseDesc;
    }

    public String getId() {
        return id;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescLevel() {
        return descLevel;
    }

    public void expandSteps(Map<String, OrderCase> orderCases,
                            Map<String, String> descByMethod) {
        JSONArray arr = new JSONArray();
        JSONArray arr1 = new JSONArray();
        List<String> newSteps = new ArrayList<String>();
        for (String step : steps) {
            if (orderCases.containsKey(step)) {
                OrderCase orderCase = orderCases.get(step);
                newSteps.addAll(orderCase.getSteps());
                JSONObject obj = new JSONObject();
                obj.put(step, new JSONArray(orderCase.getLevel()));
                arr.put(obj);
                JSONObject obj1 = new JSONObject();
                obj1.put(orderCase.getDescription(), new JSONArray(
                        orderCase.getDescLevel()));
                arr1.put(obj1);
            } else if (descByMethod.containsKey(step)) {
                newSteps.add(step);
                arr.put(step);
                arr1.put(descByMethod.get(step));
            } else if(orderCases.containsKey(step.replace("_Ocase","")))
            {
                newSteps.add(step);
                arr.put(step);
                arr1.put(step);

            }else {
                throw new IllegalArgumentException(
                        String.format("顺序测试%s的步骤%s不存在", id, step));
            }
        }
        steps = newSteps;
        level = arr.toString();
        descLevel = arr1.toString();
    }

    public XmlSuite toXmlSuite(String testngPath,
                               Map<String, String> classByMethod,Map<String,String> descByMethod) {
        String suiteName = id;
        String suitePath = String.format("%s%stestng-%s.xml",
                testngPath, File.separator, suiteName);

        XmlSuite suite = new XmlSuite();
        suite.setName(suiteName);
        suite.setFileName(suitePath);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constant.OWNER , owner);
        parameters.put(Constant.DESCRIPTION, caseDesc);
        if (!continueAfterFailure) {
            parameters.put(Constant.SKIP_ONCE_FAIL, "true");
        } else {
            parameters.put(Constant.SKIP_ONCE_FAIL, "false");
        }
        suite.setParameters(parameters);
        List<XmlTest> tests = new ArrayList<XmlTest>();
        for (int k = 0; k < steps.size(); ++k) {
            String oCase = steps.get(k).toString().trim();


//            String testName = String.format("step_%03d_%s",k + 1 , descByMethod.get(steps.get(k)));
            String testName = String.format("step_%03d_%s",new Object[] { Integer.valueOf(k + 1), descByMethod.get(this.steps.get(k))});
            String methodName = steps.get(k);
            String xmlClassName = classByMethod.get(methodName);
            if (null == xmlClassName) {
                xmlClassName = "default";
            }

            XmlTest test = new XmlTest();

            test.setName(testName);
            if (oCase.lastIndexOf("_Ocase") == (oCase.length() - "_Ocase".length())) {

                suite.getFileMaps().put(testName, oCase.substring(0, oCase.length() - "_Ocase".length()));
            } else {
                List<XmlClass> classes = new ArrayList<XmlClass>();
//            classes.add(new XmlClass(BASE_CONFIG_CLASS, false));
                XmlClass xmlClass = new XmlClass(xmlClassName, false);
                List<XmlInclude> includedMethods = new ArrayList<XmlInclude>();
                includedMethods.add(new XmlInclude(methodName));
                xmlClass.setIncludedMethods(includedMethods);
                classes.add(xmlClass);
                test.setClasses(classes);

            }
            tests.add(test);
        }


        suite.setTests(tests);
        return suite;
    }
}
