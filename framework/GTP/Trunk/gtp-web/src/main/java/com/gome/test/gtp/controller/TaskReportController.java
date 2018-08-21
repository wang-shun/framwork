/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.SessionService;
import com.gome.test.gtp.bo.TaskReportService;
import com.gome.test.gtp.dao.LoadEditHistroyDao;
import com.gome.test.gtp.dao.LoadRemarkDao;
import com.gome.test.gtp.dao.LoadSceneDao;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.dao.mongodb.ReportDao;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.report.GTPReportService;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zonglin.Li
 */
@Controller
@RequestMapping(value = "/gtp-report")
public class TaskReportController {
    @Autowired
    private ConfigureDictionaryService cdService;
    @Autowired
    private TaskReportService taskReportService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private LoadEditHistroyDao loadEditHistroyDao;
    @Autowired
    private LoadRemarkDao loadRemarkDao;
    @Autowired
    private LoadSceneDao loadSceneDao;
    @Autowired
    private GTPReportService gtpReportService;

    //===============================================API GUI Report 相关===============================================

    @RequestMapping(value = "")
    ModelAndView goEtpReport() {
        ModelAndView mv = new ModelAndView("gtp_report");
        mv.addObject("reportTypes", cdService.getReportTypes());
        mv.addObject("taskTypes", cdService.getTaskTypes());
        mv.addObject("groups", cdService.getGroups());
        mv.addObject("dateRange", cdService.getDateRange());
        mv.addObject("owners", reportDao.getDistinct(Constant.REPORT_OWNER, Constant.OWNER));
        return mv;
    }


    //生成图表
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    int[][] getCaseNumberByGp(
            @RequestParam(value = "dates[]") Integer dates[],
            @RequestParam(value = "group[]") Integer group[],
            @RequestParam(value = "owner[]") String owner[],
            @RequestParam(value = "personal") boolean personal,
            @RequestParam(value = "taskType[]") Integer taskType[],
            @RequestParam(value = "reportType") int reportType
    ) {
        return taskReportService.getReport(dates, group, owner, taskType, reportType, personal);
//        if (personal) {
//            return taskReportService.getOwnerReport(dates, taskType, owner, reportType);
//        } else {
//            return taskReportService.getGroupReport(dates, taskType, group, reportType);
//        }

    }

    @RequestMapping(value = "/gen_report")
    ModelAndView goGenReport() {
        ModelAndView mv = new ModelAndView("gen_report");
        mv.addObject("taskTypes", cdService.getTaskTypes());
        return mv;
    }

    //管理员手动执行计算
    @RequestMapping(value = "/generate")
    @ResponseBody
    Result genReport(
            @RequestParam(value = "taskType") int taskType,
            @RequestParam(value = "startDate") int startDate,
            @RequestParam(value = "endDate") int endDate
    ) {
        try {
            List<Integer> typeValues = cdService.getTaskTypeValues();
            if (taskType == -1) {
                gtpReportService.updateAGReport(startDate, endDate);
            } else if (typeValues.contains(taskType)) {
                List<Integer> taskTypes = new ArrayList<Integer>();
                taskTypes.add(taskType);
                gtpReportService.updateAGReport(startDate, endDate, taskTypes);
            } else {
                throw new Exception("未找到匹配的任务类型：" + taskType);
            }
            return new Result(false, "成功生成报告");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    //切换群组联动所有人
    @RequestMapping(value = "/owner/{groupId}")
    @ResponseBody
    List<String> getOwnerListByGroupId(@PathVariable(value = "groupId") int groupId) {
        return reportDao.getCaseOwnerByGroupId(groupId);
    }

    //===============================================JMT Report 相关===============================================

    @RequestMapping(value = "jmt")
    ModelAndView goJmtReport() {
        ModelAndView mv = new ModelAndView("jmt_report_new");
        List<LoadScene> list = loadSceneDao.getLoadSceneByIsTemplate(true);
        mv.addObject("loadScene", list);
        mv.addObject("groups", cdService.getGroups());
        return mv;
    }


    @RequestMapping(value = "/jmtedit/{sceneName}/{smallsceneName}/{resultverison}/{env}/{template}")
    ModelAndView goJmtEdit(@PathVariable("sceneName") String sceneName,
                           @PathVariable("smallsceneName") String smallsceneName,
                           @PathVariable("resultverison") String resultverison,
                           @PathVariable("env") String env,
                           @PathVariable("template") String template) {

        ModelAndView mv = new ModelAndView("jmt_report_edit");
        List<LoadScene> list = loadSceneDao.getLoadSceneByIsTemplate(true);
        mv.addObject("loadScene", list);
        mv.addObject("sceneName", sceneName);
        mv.addObject("smallsceneName", smallsceneName);
        mv.addObject("resultverison", resultverison);
        mv.addObject("env", env);
        mv.addObject("template", template);
        mv.addObject("groups", cdService.getGroups());
        return mv;
    }

    @RequestMapping(value = "/jmtedit/")
    ModelAndView goJmtEdit2() {
        ModelAndView mv = new ModelAndView("jmt_report_edit");
        List<LoadScene> list = loadSceneDao.getLoadSceneByIsTemplate(true);
        mv.addObject("loadScene", list);
        mv.addObject("sceneName", "");
        mv.addObject("smallsceneName", "");
        mv.addObject("resultverison", "");
        mv.addObject("env", "");
        mv.addObject("template", "");
        mv.addObject("groups", cdService.getGroups());
        return mv;
    }


    private String urlCode(String str) {
        try {
            byte bb[];
            bb = str.getBytes("ISO-8859-1"); //以"ISO-8859-1"方式解析name字符串
            str = new String(bb, "UTF-8"); //再用"utf-8"格式表示name
            return str;
        } catch (Exception ex) {
            return str;
        }
    }

    @RequestMapping(value = "newjmt")
    ModelAndView goNewJmtReport() {
        List sceneNameList = taskReportService.getJMTSceneNameList();
        sceneNameList.add(0, "");
        ModelAndView mv = new ModelAndView("jmt_report_new");
        mv.addObject("reportTypes", sceneNameList);
        List<LoadScene> list = loadSceneDao.getLoadSceneByIsTemplate(true);
        mv.addObject("loadScene", list);

        return mv;
    }

    @RequestMapping(value = "jmthistory")
    ModelAndView goJmtHistoryReport() {
        ModelAndView mv = new ModelAndView("jmt_report_history");
        List<LoadScene> list = loadSceneDao.getLoadSceneByIsTemplate(true);
        mv.addObject("loadScene", list);
        mv.addObject("groups", cdService.getGroups());

        return mv;
    }


    @RequestMapping(value = "/jmtSmallsceneName")
    @ResponseBody
    Result getSmallsceneName(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "isEnable") Boolean isEnable
    ) {
        try {
            List smallSceneNameList = taskReportService.getJMTSmallSceneNameList(sceneName, isEnable);
//            smallSceneNameList.add(0,"");

            return new Result(smallSceneNameList);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/jmtSceneName")
    @ResponseBody
    Result getSceneName(
            @RequestParam(value = "groupid") String groupid,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "taskid") String taskid,
            @RequestParam(value = "isEnable") Boolean isEnable
    ) {
        try {
            HashMap<String, List<String>> listHashMap = taskReportService.getJMTSceneNameMap(template, isEnable);

            List<String> result = new ArrayList<String>();

            if (groupid.equals("None") && StringUtils.isEmpty(taskid)) {
                for (String key : listHashMap.keySet()) {
                    result.addAll(listHashMap.get(key));
                }

            } else if (groupid.equals("None")) //选择的是
            {
                if (listHashMap.containsKey(taskid))
                    result = listHashMap.get(taskid);


            } else if (groupid.equals("Null"))
            {
                if (listHashMap.containsKey(null)) {
                    //taskid为null的数据，加入到结果中
                    List<String> nullList = listHashMap.get(null);
                    for (String str : nullList) {
                        if (!result.contains(str))
                            result.add(str);
                    }
                }

            }else {

                //取该组内全部的taskid
                List taskIdList = taskInfoDao.getTaskIdbyGroupid(groupid);
                //遍历taskid加入结果集
                if (taskIdList != null) {
                    for (Object id : taskIdList) {
                            String keyid=id.toString();
                        if (listHashMap.containsKey(keyid)) {
                            result.addAll(listHashMap.get(keyid)) ;
                        }
                    }
                }

            }


//            result.add(0,"");

            //因为按照taskid和场景名分组，所以可能有重复数据，最后去除重复

            List<String> result_Return = new ArrayList<String>();
            for (String str : result) {
                if (!result_Return.contains(str))
                    result_Return.add(str);
            }
            Collections.sort(result_Return);

            return new Result(result_Return);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/jmtLabelName")
    @ResponseBody
    Result getLabelName(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "isEnable") Boolean isEnable
    ) {
        try {
            List LabelNameList = taskReportService.getJMTLabelNameList(sceneName, isEnable);
            LabelNameList.add(0, "");
            return new Result(LabelNameList);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/jmtEnvironment")
    @ResponseBody
    Result getenvironmentList(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "isEnable") Boolean isEnable
    ) {
        try {
            List LabelNameList = taskReportService.getJMTenvironmentList(sceneName, isEnable);
//            LabelNameList.add(0,"");
            return new Result(LabelNameList);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    //
    @RequestMapping(value = "/getlabelreport")
    @ResponseBody
    Result getLabelReport(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "resultEndVersion") String resultEndVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "labelName") String labelName,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "taskId") String taskId,
            @RequestParam(value = "timeART") String timeArt
    ) {
        try {
            resultEndVersion = StringUtils.isEmpty(resultEndVersion) ? "" : getNextDate(resultEndVersion);
            JmtParam param = new JmtParam(sceneName, smallSceneName, labelName, environment, resultVersion, resultEndVersion, template);
            param.setTaskId(taskId);
            param.setTimeArt(timeArt);

            List<JMTReport> reports = taskReportService.getlabelreport(param);

            return new Result(reports);

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/getjmtreport")
    @ResponseBody
    Result getJMTReport(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "timeVersion") String timeVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "taskId") String taskId,
            @RequestParam(value = "timeavg") String timeavg,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "labelName") String labelName,
            @RequestParam(value = "template") String template
    ) {
        try {

            JmtParam param = new JmtParam(sceneName, smallSceneName, labelName, environment, resultVersion, getNextDate(resultVersion), template);
            param.setTimeVersion(timeVersion.equals("NONE") ? "" : timeVersion);
            param.setTaskId(taskId);
            param.setTimeArt(timeavg);
            List<JmtReportData> reports = taskReportService.getJMTReportList(param);

            return new Result(reports);

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/getjmthistory")
    @ResponseBody
    Result getJMTReportHistory(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "timeVersion") String timeVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "taskid") String taskid,
            @RequestParam(value = "template") String template
    ) {
        try {

            JmtParam param = new JmtParam(sceneName, smallSceneName, null, environment, resultVersion, getNextDate(resultVersion), template);
            param.setTimeVersion(timeVersion);
            param.setTaskId(taskid);
            List reports = taskReportService.getJmtHistroy(param);

            return new Result(reports);

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/getjmtedithistory")
    @ResponseBody
    TableResult getJMTEditHistory() {
        try {

            List reports = loadEditHistroyDao.getAll();

            return new TableResult(false, 1, reports.size(), reports.size(), reports.toArray());

        } catch (Exception e) {
            return new TableResult(true, e.toString());
        }
    }


    @RequestMapping(value = "/editJmtSceneName")
    @ResponseBody
    Result getJMTeditSceneName(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "timeVersion") String timeVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "oldsceneName") String oldsceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "createTime") String createTime,
            HttpServletRequest request
    ) {
        try {
            sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            JmtParam param = new JmtParam(oldsceneName, smallSceneName, null, environment, resultVersion, getNextDate(resultVersion), template);
            param.setTimeVersion(timeVersion);
            LoadEditHistory history = new LoadEditHistory(sessionService.getSessionUsername(), getIp(request), sceneName, oldsceneName, environment, template, resultVersion, param.gettimeVersion(), smallSceneName, createTime, JMTEditType.JMTEditSceneName);
            return new Result(taskReportService.updateSceneName(history, param));

        } catch (Exception e) {
            return new Result(true, e.getMessage());
        }
    }


    @RequestMapping(value = "/editJmtEnable")
    @ResponseBody
    Result getJMTeditEnable(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "timeVersion") String timeVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "createTime") String createTime,
            HttpServletRequest request
    ) {
        try {
            sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            JmtParam param = new JmtParam(sceneName, smallSceneName, null, environment, resultVersion, getNextDate(resultVersion), template);
            param.setTimeVersion(timeVersion);
            LoadEditHistory history = new LoadEditHistory(sessionService.getSessionUsername(), getIp(request), sceneName, sceneName, environment, template, resultVersion, param.gettimeVersion(), smallSceneName, createTime, JMTEditType.JMTEditEnable);
            return new Result(taskReportService.updateEnable(history, param));

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/getTpsJMTReport")
    @ResponseBody
    Result getTpsJMTReport(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "taskid") String taskid,
            @RequestParam(value = "timeavg") String timeavg,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "labelName") String labelName,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "maxdataCount") int maxdataCount

    ) {
        try {

            JmtParam param = new JmtParam(sceneName, smallSceneName, labelName, environment, resultVersion, getNextDate(resultVersion), template);
            param.setTaskId(taskid);
            param.setTimeArt(timeavg);
            return new Result(taskReportService.getTpsJMTReportList(param, maxdataCount));

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/getTpsReportLabel")
    @ResponseBody
    Result getTpsReportLabel(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "resultEndVersion") String resultEndVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "template") String template

    ) {
        try {
            resultEndVersion = getNextDate(resultEndVersion);
            JmtParam param = new JmtParam(sceneName, smallSceneName, null, environment, resultVersion, resultEndVersion, template);

            return new Result(taskReportService.getJMTReportLabel(param));

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/getHistoryTpsJMTReport")
    @ResponseBody
    Result getHistoryTpsJMTReport(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "timeVersion") String timeVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "maxdataCount") int maxdataCount

    ) {
        try {


            JmtParam param = new JmtParam(sceneName, null, null, environment, resultVersion, getNextDate(resultVersion), template);
            param.setTimeVersion(timeVersion);
            return new Result(taskReportService.getTpsJMTReportList(param, maxdataCount));

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/exportReport")
    @ResponseBody
    Result exportReport(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "template") String template,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        try {


            String[] versionList = resultVersion.split("[,]");
            String[] environmentList = environment.split("[,]");
            String[] templateList = template.split("[,]");
            String[] sceneNameList = sceneName.split("[,]");

            JmtParam[] params = new JmtParam[versionList.length];
            for (int i = 0; i < params.length; i++) {
                int index = versionList[i].indexOf("_");
                String timeVersion = index > 1 ? versionList[i].substring(index + 1) : "";
                String version = index > 1 ? versionList[i].substring(0, index) : versionList[i];
                String tem = templateList[i].equals("NONE") ? "" : templateList[i];
                params[i] = new JmtParam(sceneNameList[i], environmentList[i], version, getNextDate(version), "", tem);
                params[i].setTimeVersion(timeVersion);
            }

            String fileName = taskReportService.createReportExcel(params, request);

//            File file = new File(fileName);
//            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
//
//            FileInputStream in = new FileInputStream(file);
//
//            OutputStream out = response.getOutputStream();
//
//
//            //创建缓冲区
//            byte buffer[] = new byte[1024];
//            int len = 0;
//            //循环将输入流中的内容读取到缓冲区当中
//            while ((len = in.read(buffer)) > 0) {
//                //输出缓冲区的内容到浏览器，实现文件下载
//                out.write(buffer, 0, len);
//            }
//            //关闭文件输入流
//            in.close();
//            //关闭输出流
//            out.close();
            Result  result =new Result(fileName);
            result.setMessage(fileName.substring(fileName.lastIndexOf(File.separator)+1));

            return result;

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/getHistoryGrpJMTReport")
    @ResponseBody
    Result getHistoryGrpJMTReport(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "grpThreads") String grpThreads
    ) {
        try {


            String[] versionList = resultVersion.split("[,]");
            String[] environmentList = environment.split("[,]");
            String[] templateList = template.split("[,]");
            String[] sceneNameList = sceneName.split("[,]");

            JmtParam[] params = new JmtParam[versionList.length];
            for (int i = 0; i < params.length; i++) {
                int index = versionList[i].indexOf("_");
                String timeVersion = index > 1 ? versionList[i].substring(index + 1) : "";
                String version = index > 1 ? versionList[i].substring(0, index) : versionList[i];
                String tem = templateList[i].equals("NONE") ? "" : templateList[i];
                params[i] = new JmtParam(sceneNameList[i], environmentList[i], version, getNextDate(version), grpThreads, tem);
                params[i].setTimeVersion(timeVersion);
            }

            String[][] reports = taskReportService.getHistoryGrpJMTReport(params);

            return new Result(reports);

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/jmtgetVersion")
    @ResponseBody
    Result getVersion(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "labelName") String labelName,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "isEnable") Boolean isEnable
    ) {
        try {
            TreeMap endVersion = taskReportService.getResultVersion(sceneName, smallSceneName, labelName, environment, isEnable);
            Result result = new Result(endVersion.size() > 0 ? endVersion.get(endVersion.firstKey()) : null);
            result.setMessage(endVersion.firstKey().toString());
            return result;

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "getVersionTime")
    @ResponseBody
    Result getVersionTime(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "labelName") String labelName,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "resultdate") String resultdate,
            @RequestParam(value = "isEnable") Boolean isEnable) {
        List<String> result = taskReportService.getVersionTime(sceneName, smallSceneName, labelName, environment, resultdate, isEnable);
        return new Result(result);

    }


    @RequestMapping(value = "/getlinejmtreport")
    @ResponseBody
    Result getLineJMTReport(
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "labelName") String labelName
    ) {
        try {
            return new Result(taskReportService.getlinejmtreport(resultVersion, environment, sceneName, labelName, true));

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/gethistoryjmtreport")
    @ResponseBody
    Result getHistoryJMTReport(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "labelName") String labelName,
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "count") Integer count

    ) {
        try {
            return new Result(taskReportService.getHistoryJMTReport(sceneName, smallSceneName, labelName, resultVersion, environment, count, true));

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/getjmtRemark")
    @ResponseBody
    Result getjmtRemark(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "timeVersion") String timeVersion,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "createtime") String createtime,
            @RequestParam(value = "template") String template

    ) {
        try {

            List list = loadRemarkDao.getLoadRemark(sceneName, smallSceneName, environment, template, resultVersion, timeVersion, createtime);
            if (list.size() == 1)
                return new Result(list.get(0));
            else if (list.size() == 0)
                return new Result(new LoadRemark());
            return new Result(true, "发生异常，符合条件数量过多，请检查！");

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/editjmtRemark")
    @ResponseBody
    Result editjmtRemark(
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "smallSceneName") String smallSceneName,
            @RequestParam(value = "resultVersion") String resultVersion,
            @RequestParam(value = "timeVersion") String timeVersion,
            @RequestParam(value = "environment") String environment,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "remark") String remark,
            @RequestParam(value = "createtime") String createtime,
            @RequestParam(value = "id") int id

    ) {
        try {
            if (id == 0) {
                LoadRemark loadRemark = new LoadRemark(sceneName, smallSceneName, environment, template, resultVersion, timeVersion, createtime, remark, remark);
                loadRemarkDao.save(loadRemark);
            } else {
                loadRemarkDao.updateRemark(id, remark);
            }
            return new Result(false);

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    //得到当前日期的后一个日期
    private String getNextDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(dateStr);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, 1);//把日期往后增加一天
            date = calendar.getTime();
            return sdf.format(date);
        } catch (Exception ex) {
            return null;
        }

    }

    public String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }


    @RequestMapping(value = "/taskName")
    @ResponseBody
    Result getTaskName(
            @RequestParam(value = "groupid") String groupid,
            @RequestParam(value = "sceneName") String sceneName,
            @RequestParam(value = "template") String template,
            @RequestParam(value = "isEnable") Boolean isEnable
    ) {
        try {
            List<String> taskListByReport = taskReportService.getJMTSceneNameMap(sceneName, template, isEnable);

            //取该组内全部的taskid
            List taskList = groupid.equals("Null")? null : taskInfoDao.getTaskbyGroupid(groupid.equals("None") ? "" : groupid);

            List<String> result = new ArrayList<String>();

            if(taskList != null)
            {
                for (Object taskid : taskList) {
                    Object[] taskDetails = (Object[]) taskid;
                    for (String taskidkey : taskListByReport) {
                        if (groupid.equals("None") || taskDetails[0].toString().trim().equals(taskidkey.trim())) {
                            String value = String.format("(%s)%s", taskDetails[0].toString(), taskDetails[1].toString());
                            if (!result.contains(value))
                                result.add(value);
                        }
                    }
                }
            }

            return new Result(result);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }


    @RequestMapping(value = "/getjmtaggreport")
    @ResponseBody
    Result getJMTAGGReport(
            @RequestParam(value = "aggVersion") String aggVersion,
            @RequestParam(value = "aggenv") String aggenv,
            @RequestParam(value = "aggscene") String aggscene,
            @RequestParam(value = "aggtem") String aggtem,
            @RequestParam(value = "timeavg") String timeavg
    ) {
        try {



            List<JmtReportData> reports=new ArrayList<JmtReportData>();

            if (!StringUtils.isEmpty(aggVersion)) {
                String timeVersion = "";
                int index = aggVersion.indexOf("_");
                if (index > 0) {
                    timeVersion = aggVersion.substring(index + 1);
                    aggVersion = aggVersion.substring(0, index);
                }

                JmtParam param = new JmtParam(aggscene, "", "", aggenv, aggVersion, getNextDate(aggVersion), aggtem);
                param.setTimeVersion(timeVersion);
                param.setTimeArt(timeavg);
                 reports = taskReportService.getJMTAGGReportList(param);
            }




            return new Result(reports);

        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/download/{fileName}")
    @ResponseBody
    ResponseEntity<byte[]> download(@PathVariable(value = "fileName") String fileName, HttpServletRequest request) throws IOException {
        // path是指欲下载的文件的路径。
        String filePath = String.format("%s%sreport%s", request.getServletContext().getRealPath(""),File.separator,File.separator);
        if(fileName.indexOf(".xlsx")<0)
            fileName=fileName+".xlsx";

        File file = new File(String.format("%s%s",filePath,fileName));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }




}

