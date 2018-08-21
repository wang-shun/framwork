//package com.gome.test.gtp;
//
//import com.gome.test.gtp.dao.TaskInfoDao;
//import com.gome.test.gtp.dao.mongodb.MongoDBDao;
//import com.gome.test.gtp.dao.mongodb.ReportDao;
//import com.gome.test.gtp.model.TaskReport;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.annotations.Test;
//
//import javax.annotation.Resource;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.List;
//import java.util.Map;
//
//
//
//public class MongoDBTest  {
//    @Resource
//    MongoDBDao  reportDB;
//
//    @Test
//    public void testGetJobReport(){
//        String tid = "Job_1468400768415_2";
//        List<TaskReport> list  = reportDB.getJobList(tid);
//        System.out.println("get list size is : " + list.size());
//    }
//
////    public static String readFileByLines(String fileName) {
////        File file = new File(fileName);
////        BufferedReader reader = null;
////        StringBuffer strB = new StringBuffer();
////        try {
////            System.out.println("以行为单位读取文件内容，一次读一整行：");
////            reader = new BufferedReader(new FileReader(file));
////            String tempString = null;
////            int line = 1;
////            // 一次读入一行，直到读入null为文件结束
////            while ((tempString = reader.readLine()) != null) {
////                // 显示行号
////                // System.out.println("line " + line + ": " + tempString);
////                strB.append(tempString);
////                line++;
////            }
////            reader.close();
////        } catch (IOException e) {
////            e.printStackTrace();
////        } finally {
////            if (reader != null) {
////                try {
////                    reader.close();
////                } catch (IOException e1) {
////                }
////            }
////        }
////
////        return strB.toString();
////    }
//
//
////    public static void main(String[] args) throws ParseException {
////        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
////
////        context.scan("com.gome.test.gtp.dao");
////        context.refresh();
////
////        //更新报表
////        int taskType = 1;
////
////
////        Map<Integer, Integer> taskAndGroup = ((TaskInfoDao) context.getBean("taskInfoDao")).getTaskAndGroup();
//////        ((ReportDao) context.getBean("reportDao")).updateReportOwner(20150609, 20160609, taskType);
//////        ((ReportDao) context.getBean("reportDao")).updateReportGroup(20150609, 20150609, taskType, taskAndGroup);
//////
////        ((ReportDao) context.getBean("reportDao")).updateAGReport(20150301, 20160501, taskType, taskAndGroup);
////
////
////
////        String abc="";
////
//////
//////        ((ReportDao) context.getBean("reportDao")).updateReportOwner(20150609, 20150609, taskType);
//////            ((ReportDao) context.getBean("reportDao")).updateReportGroup(20150609, 20150609, taskType, taskAndGroup);
////
//////        int dates[] = new int[]{20150504, 20150505, 20150506, 20150507, 20150508, 20150509, 20150510};
//////        Integer groups[] = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8};
//////
//////        int[][] data = ((ReportDao) context.getBean("reportDao")).getReport(dates, new Integer[]{1, 2, 3, 4, 5},
//////                Constant.REPORT_GROUP, Constant.GROUP_ID, groups, Constant.REPORT_PASS_RATE);
//////
//////        for (int[] d : data) {
//////            System.out.println();
//////            for (int i : d)
//////                System.out.print(i + ",");
////
////
//////        Query query = new Query();
//////        Criteria criteria = new Criteria();
//////        criteria.and("taskId").is(1031);
//////        query.addCriteria(criteria);
//////
//////        List<TaskReport> taskReport = ((ReportDao) context.getBean("reportDao")).getByCondition(TaskReport.class, query);
////
////    }
//}
