package com.ofo.test.plugin;

import com.ofo.test.plugin.graph.Digraph;
import com.ofo.test.plugin.graph.KahnTopological;
import com.ofo.test.plugin.graph.XmlSuite;
import com.gome.test.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.xml.XmlTest;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class OrderCases {

    private List<XmlSuite> xmlSuites;
    private List<OrderCase> orderCaseList;
    private int testCount;



    public List<XmlSuite> getXmlSuites() {
        return xmlSuites;
    }

    /*
    * 取得该文件的orderCase
    * */
    public List<OrderCase> getOrderCaseList() {
        return orderCaseList;
    }

    public OrderCases(String orderCaseExcelPath, String testngPath,
                      Map<String, String> classByMethod,
                      Map<String, String> descByMethod,
                      Map<String, List<String>> testSuitesByGroup) throws IOException {
        this(new File(orderCaseExcelPath), testngPath, classByMethod,
                descByMethod, testSuitesByGroup);
    }

    public OrderCases(File orderCaseExcelFile, String testngPath,
                      Map<String, String> classByMethod,
                      Map<String, String> descByMethod,
                      Map<String, List<String>> testSuitesByGroup) throws IOException {
        this(new FileInputStream(orderCaseExcelFile), testngPath,
                classByMethod, descByMethod, testSuitesByGroup);
    }

    public OrderCases(InputStream is, String testngPath,
                      Map<String, String> classByMethod,
                      Map<String, String> descByMethod,
                      Map<String, List<String>> testSuitesByGroup) throws IOException {
        xmlSuites = new ArrayList<XmlSuite>();
        orderCaseList = new ArrayList<OrderCase>();
        try {
            parse(is, testngPath, classByMethod, descByMethod,
                    testSuitesByGroup);
            for(XmlSuite suite:xmlSuites)
            {

                TreeMap<String,String> files=suite.getFileMaps();
                if(files.size()>0)
                {

                    for(String key:files.keySet())
                    {
                        XmlSuite s= getXmlSuitebyName(files.get(key).toString());
                        String index=getTestIndex(key,suite);
                        for(int i=0;i<s.getTests().size();i++)
                        {
                               int indiex=Integer.valueOf(index);
                               XmlTest test=s.getTests().get(i);
                                XmlTest nt=new XmlTest();
                                String suiteName=s.getName();
                                String testName= test.getName() ;
                                int k=getTestCountByName(String.format("%s_%s", suiteName, testName),suite);
                                testName=String.format("%s_%s_%03d", suiteName, testName,k+1) ;
                                nt.setName(testName);
                                nt.setClasses(test.getClasses());
                               if(i==0) {
                                   suite.getTests().set(indiex, nt);
                               }else
                                   suite.getTests().add(indiex + i, nt);

                        }
                    }
                }
                suite.getFileMaps().clear();
                cmdSuite(suite);
            }

        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    private void cmdSuite(XmlSuite suite)
    {
        testCount = Integer.MAX_VALUE;

        int thisindexCount=0;

        for(int i=0;i<suite.getTests().size();i++)
        {
            if(i >= thisindexCount || i==0 )
            {
                thisindexCount = testCount * (suite.getM_testListArray().size()+1);
                suite.getM_testListArray().add(new ArrayList<XmlTest>());
            }
            suite.getM_testListArray().get(suite.getM_testListArray().size()-1).add(suite.getTests().get(i));
        }
    }

    public String getTestIndex(String testname,XmlSuite suite)
    {
        List<XmlTest> list= suite.getTests();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getName().equals(testname))
            {
                return String.valueOf(i);
            }
        }
        return list.size()==0?"-1": null;
    }

    public XmlSuite getXmlSuitebyName(String name)
    {
        XmlSuite result=null;
        for(XmlSuite suite:xmlSuites)
        {
            if(suite.getName().equals(name))
                result=suite;
        }
        return result;
    }

    public int getTestCountByName(String testname,XmlSuite suite)
    {
        List<XmlTest> list= suite.getTests();
        int reslut=0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getName().indexOf(testname)==0)
            {
                reslut++;
            }
        }
        return reslut;
    }


    private void parse(InputStream is, String testngPath,
                       Map<String, String> classByMethod,
                       Map<String, String> descByMethod,
                       Map<String, List<String>> testSuitesByGroup) throws IOException {
        Map<String, OrderCase> orderCases = new HashMap<String, OrderCase>();
        Digraph graph = new Digraph();
        Set<String> s1 = new HashSet<String>();
        Set<String> s2 = new HashSet<String>();
        Set<String> ocaseIds=new HashSet<String>();

        Workbook workbook = new XSSFWorkbook(is);
        int sheetNum = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNum; ++i) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            // skip non-OrderList sheet
            if (!sheetName.startsWith("OrderList")) {
                continue;
            }

            Iterator<Row> rowIter = sheet.iterator();
            // skip headers
            if (!rowIter.hasNext()) {
                continue;
            }
            Row header = rowIter.next();
            List<String> headers = ExcelUtils.getHeadersFrom(header);
            int caseNameId = headers.indexOf("用例名称");

            // Traversal each row
            while (rowIter.hasNext()) {
                Row row = rowIter.next();
                if (row.getLastCellNum() < 4) {
                    continue;
                }

                int t = 0;
                // get case id
                Cell cell = row.getCell(t++);
                if (null == cell) {
                    continue;
                }
                String id = ExcelUtils.getCellValue(cell).toString();
                if ("".equals(id)) {
                    continue;
                }else {
                    ocaseIds.add(id+"_Ocase");
                }

                // get case name
                String caseName = "Unknown";
                if (caseNameId == t) {
                    cell = row.getCell(t++);
                    if (null == cell) {
                        continue;
                    }
                    caseName = ExcelUtils.getCellValue(cell).toString();
                    if ("".equals(caseName)) {
                        caseName = "UnKnown";
                    }
                }

                // continue or skip after failure
                cell = row.getCell(t++);
                boolean continueAfterFailure = true;
                if (null != cell) {
                    continueAfterFailure = Boolean.valueOf(ExcelUtils
                            .getCellValue(cell).toString());
                }

                // get owner
                cell = row.getCell(t++);
                String owner = "Unknown";
                if (null != cell) {
                    owner = ExcelUtils.getCellValue(cell).toString();
                }

                if (classByMethod.containsKey(id)) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Duplicated case '%s' found in class %s and order cases",
                                    id, classByMethod.get(id)));
                }

                graph.addNode(id);
                if (s1.contains(id)) {
                    throw new IllegalArgumentException(String.format(
                            "Duplicated case '%s' found in order cases", id));
                }
                s1.add(id);

                // get groups
                Set<String> groups = new HashSet<String>();
                groups.add("ALL");
                groups.add("OrderCase");
                Utils.addGroup(testSuitesByGroup, groups, id);

                // get test steps
                List<String> steps = new ArrayList<String>();
                for (int j = t; j < row.getLastCellNum(); ++j) {
                    cell = row.getCell(j);
                    if (null == cell) {
                        continue;
                    }
                    String step = ExcelUtils.getCellValue(cell).toString();
                    if (null == step) {
                        continue;
                    }
                    step = step.trim();
                    if (step.isEmpty()) {
                        continue;
                    }
                    if (!classByMethod.containsKey(step)) {
                        //graph.addEdge(step, id);
                        s2.add(step);
                    }
                    steps.add(step);
                }

                OrderCase orderCase = new OrderCase(id, caseName,
                        continueAfterFailure, owner, steps);
                orderCases.put(id, orderCase);
                this.orderCaseList.add(orderCase);
            }
        }

        for (String s : s2) {
            if (!s1.contains(s)&& !ocaseIds.contains(s)) {
                throw new IllegalArgumentException(String.format(
                        "Step '%s' isn't a test case in order case", s));
            }
        }


        List<String> ids = KahnTopological.sort(graph);
        for (String id : ids) {
            OrderCase orderCase = orderCases.get(id);
            orderCase.expandSteps(orderCases, descByMethod);
        }

        for (String id : orderCases.keySet()) {
            OrderCase orderCase = orderCases.get(id);
            XmlSuite suite = orderCase.toXmlSuite(testngPath, classByMethod);
            xmlSuites.add(suite);
        }

    }

    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }
}
