package com.gome.test.gtp.bo;


import com.gome.test.gtp.dao.LoadEditHistroyDao;
import com.gome.test.gtp.dao.LoadRemarkDao;
import com.gome.test.gtp.dao.mongodb.ReportDao;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.Logger;
import com.gome.test.utils.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TaskReportService {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private LoadEditHistroyDao loadEditHistroyDao;
    @Autowired
    private LoadRemarkDao loadRemarkDao;

    //====================================================AG Report 相关====================================================
    public int[][] getReport(Integer[] dates, Integer[] group, String[] owner, Integer[] taskType, int reportType, boolean personal) {
        String groupField = Constant.GROUPID;
        if (personal)
            groupField = Constant.OWNER;
        return reportDao.getReport(dates, group, owner, taskType, Constant.AG_REPORT, reportType, groupField);
    }

    public int[][] getGroupReport(Integer[] dates, Integer[] taskType, Object[] groupFieldValue, int reportType) {
        return reportDao.getReport(dates, taskType, Constant.REPORT_GROUP, Constant.GROUP_ID, groupFieldValue, reportType);
    }

    public int[][] getOwnerReport(Integer[] dates, Integer[] taskType, Object[] groupFieldValue, int reportType) {
        return reportDao.getReport(dates, taskType, Constant.REPORT_OWNER, Constant.OWNER, groupFieldValue, reportType);
    }

    //====================================================JMT Report 相关====================================================
    public String[][] getlinejmtreport(String resultVersion, String environment, String sceneName, String labelName, Boolean isEnable) {
        return reportDao.getlinejmtreport(resultVersion, environment, sceneName, labelName, isEnable);
    }

    public String[][] getHistoryJMTReport(String sceneName, String smallSceneName, String labelName, String resultVersion, String environment, Integer count, Boolean isEnable) {
        return reportDao.getHistoryJMTReport(sceneName, smallSceneName, labelName, resultVersion, environment, count, isEnable);
    }

    public void updateReportGroup(int minDate, int maxDate, int taskType, Map<Integer, Integer> taskIdAndGroupId) {
        reportDao.updateReportGroup(minDate, maxDate, taskType, taskIdAndGroupId);
    }

    public void updateReportOwner(int minDate, int maxDate, int taskType) {
        reportDao.updateReportOwner(minDate, maxDate, taskType);
    }

    public <T> List<T> getByCondition(Class<T> entityClass, Query query) {
        return reportDao.getByCondition(entityClass, query);
    }

    public List getJMTSceneNameList() {
        return reportDao.getJMTSceneNameList();
    }

    public HashMap<String, List<String>> getJMTSceneNameMap(String template, Boolean isEnable) {
        return reportDao.getJMTSceneNameMap(template, isEnable);
    }

    public List getJMTSmallSceneNameList(String sceneName, Boolean isEnable) {
        return reportDao.getJMTSmallSceneNameList(sceneName, isEnable);
    }

    public List getJMTLabelNameList(String sceneName, Boolean isEnable) {
        return reportDao.getJMTLabelNameList(sceneName, isEnable);
    }

    public String getEndVersion(String sceneName, String smallSceneName, String labelName, String environment, Boolean isEnable) {
        return reportDao.getEndVersion(sceneName, smallSceneName, labelName, environment, isEnable);
    }

    public List getJMTenvironmentList(String sceneName, Boolean isEnable) {
        return reportDao.getJMTenvironmentList(sceneName, isEnable);
    }

    private HashMap<String, List<JMTReport>> getGroupList(List<JMTReport> jmtReports) {
        HashMap<String, List<JMTReport>> mapJmtReport = new HashMap<String, List<JMTReport>>();
        for (JMTReport jmt : jmtReports) {
            String key = String.format("%s_%s_%s_%s", jmt.getSceneName(), jmt.getSmallSceneName(), jmt.getResultVersion(), jmt.getLabelName());
            if (!mapJmtReport.containsKey(key))
                mapJmtReport.put(key, new ArrayList<JMTReport>());
            mapJmtReport.get(key).add(jmt);
        }
        return mapJmtReport;
    }

    private HashMap<String, List<JMTReport>> getGroupListBySceneName(List<JMTReport> jmtReports) {
        HashMap<String, List<JMTReport>> mapJmtReport = new HashMap<String, List<JMTReport>>();
        for (JMTReport jmt : jmtReports) {
            String key = String.format("%s_%s_%s", jmt.getSceneName(), jmt.getSmallSceneName(), jmt.getLabelName());
            if (!mapJmtReport.containsKey(key))
                mapJmtReport.put(key, new ArrayList<JMTReport>());
            mapJmtReport.get(key).add(jmt);
        }
        return mapJmtReport;
    }


    //将数据针对GrpThread进行分组
    private HashMap<Long, List<JMTReport>> getGrpThreadReport(List<JMTReportTimeStamp> jmtList) {
        //根据时间排序
        Collections.sort(jmtList);
        Long lastGrpThreads = 0L;
        HashMap<Long, List<JMTReport>> mapGrpThreadReport = new HashMap<Long, List<JMTReport>>();

        //根据GrpThreads进行分组
        for (JMTReportTimeStamp reportTimeStamp : jmtList) {
            JMTReport report = reportTimeStamp.getJmtReport();

            if (lastGrpThreads > 0 && report.getGrpThreads() < lastGrpThreads) {
                updateUnLoad(report);
                continue;
            }

            lastGrpThreads = report.getGrpThreads();

            if (!mapGrpThreadReport.containsKey(report.getGrpThreads()))
                mapGrpThreadReport.put(report.getGrpThreads(), new ArrayList<JMTReport>());

            mapGrpThreadReport.get(report.getGrpThreads()).add(report);
        }
        return mapGrpThreadReport;

    }


    public List<JMTReport> getlabelreport(JmtParam param) {

        //获取aggReport数据
        //得到全部数据
        List<JMTReport> jmtAGGReport = reportDao.getLabelReportByAGGReport(param.getTimeArt(), param.getTaskId(), param.getResultVersion(), param.getResultEndVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getIsEnable());
//

        HashMap<String, List<JMTReport>> hashMap = new HashMap<String, List<JMTReport>>();

        for (JMTReport report : jmtAGGReport) {
            String key = String.format("%s_%s_%s_%s_%s", report.getSceneName(), report.getOnlyVersion(), report.getTimeVersion(), report.getEnvironment(), report.getTemplate());
            if (!hashMap.containsKey(key))
                hashMap.put(key, new ArrayList<JMTReport>());

            hashMap.get(key).add(report);
        }


        List<JMTReport> jmtReports = new ArrayList<JMTReport>();

        for (String key : hashMap.keySet()) {
            List<Long> grpThreads = new ArrayList<Long>();

            for (JMTReport report : hashMap.get(key)) {
                grpThreads.add(report.getGrpThreads());
            }
            Collections.sort(grpThreads);
            JMTReport report = hashMap.get(key).get(0);
            report.setGrpThreadDetails(org.apache.commons.lang.StringUtils.join(grpThreads, ","));

            jmtReports.add(report);
        }
//        Collections.sort(jmtReports);
        return jmtReports;

//        boolean isGetData = (jmtAGGReport!=null && jmtAGGReport.size() >0 ) ;


//        //得到全部数据
//        List<JMTReport> jmtReports = reportDao.getLabelReport(param.getTaskId(),param.getResultVersion(), param.getResultEndVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getIsEnable());
//        List<SortData> sortDatas=new ArrayList<SortData>();
//        //针对版本号进行排序
//        for(JMTReport report : jmtReports)
//        {
//            sortDatas.add(new SortData(report.getResultVersion(),report,false));
//        }
//        Collections.sort(sortDatas);
//        //针对大场景排序
//        List<SortData>  sortSeceneName=new ArrayList<SortData>();
//        for(SortData data: sortDatas)
//        {
//            JMTReport jmtReport=(JMTReport)data.getReport();
//            sortSeceneName.add(new SortData(jmtReport.getSceneName(),jmtReport,true));
//        }
//        Collections.sort(sortSeceneName);
//        //针对环境
//        List<SortData>  sortEnvironment=new ArrayList<SortData>();
//        for(SortData data: sortSeceneName)
//        {
//            JMTReport jmtReport=(JMTReport)data.getReport();
//            sortEnvironment.add(new SortData(jmtReport.getEnvironment(),jmtReport,true));
//        }
//        Collections.sort(sortEnvironment);
//        //针对模板
//        List<SortData>  sortTemplate=new ArrayList<SortData>();
//        for(SortData data: sortEnvironment)
//        {
//            JMTReport jmtReport=(JMTReport)data.getReport();
//
//            if(StringUtils.isEmpty(jmtReport.getTemplate()) ||  jmtReport.getTemplate().indexOf("X") < jmtReport.getTemplate().lastIndexOf("X"))
//                sortTemplate.add(new SortData(jmtReport.getTemplate(),jmtReport,true));
//        }
////        Collections.sort(sortTemplate);
////
//
//        jmtReports.clear();
//        for(SortData data: sortTemplate)
//        {
//            JMTReport jmtReport= (JMTReport)data.getReport();
//
//            if(reportType.equals("2"))
//            {
//                List<Long> grpThreads=new ArrayList<Long>();
//
//                List<JMTReport> Reports= reportDao.getGrpThreads(jmtReport.getSceneName(),jmtReport.getEnvironment(),jmtReport.getOnlyVersion(),jmtReport.getTimeVersion(),jmtReport.getTemplate(),param.getIsEnable());
//                //按时间排序
//                List<JMTReportTimeStamp> reportTimeStamps=new ArrayList<JMTReportTimeStamp>();
//                for(JMTReport jmt:Reports)
//                {
//                    reportTimeStamps.add(new JMTReportTimeStamp(jmt.getTimeStamp(),jmt));
//                }
//
//
//                //根据GrpThreads进行分组
//                HashMap<Long,List<JMTReport>> mapGrpThreadReport=getGrpThreadReport(reportTimeStamps);
//
//                for(Long key:mapGrpThreadReport.keySet())
//                {
//                    grpThreads.add(key);
//                }
//
//
//                Collections.sort(grpThreads);
//                jmtReport.setGrpThreadDetails(org.apache.commons.lang.StringUtils.join(grpThreads, ","));
//            }
//            jmtReports.add(jmtReport);
//        }
//
//        return  jmtReports;
    }

    public List getJmtHistroy(JmtParam param) {
        return reportDao.getJmtHistroy(param.getTaskId(), param.getResultVersion(), param.getResultEndVersion(), param.gettimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getTemplate(), true);
    }

    public int getJmtCount(LoadEditHistory param) {
        return reportDao.getJmtCount(param.getVersion(), param.getTimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getTemplate(), true);

    }

    public boolean updateEnable(LoadEditHistory loadEditHistory, JmtParam param) {
        try {
            //mysql
            loadEditHistroyDao.save(loadEditHistory);
            Query query = new Query();
            query.addCriteria(Criteria.where("sceneName").is(param.getSceneName()));
            query.addCriteria(Criteria.where("resultVersion").is(param.getResultVersion()));
            query.addCriteria(Criteria.where("smallSceneName").is(param.getSmallSceneName()));
            query.addCriteria(Criteria.where("template").is(param.getTemplate()));
            query.addCriteria(Criteria.where("createTime").is(loadEditHistory.getDataCreateTime()));
            query.addCriteria(Criteria.where("environment").is(param.getEnvironment()));
            query.addCriteria(Criteria.where("timeVersion").is(StringUtils.isEmpty(param.gettimeVersion()) ? null : param.gettimeVersion()));
            Update update = new Update();
            update.set("isEnable", false);
            reportDao.update(query, update);
            updateAGGReportScene(param, update);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateSceneName(LoadEditHistory loadEditHistory, JmtParam param) throws Exception {
        //查询新名称符合的记录
        int count = reportDao.getJmtCount(loadEditHistory.getVersion(), param.gettimeVersion(), loadEditHistory.getEnvironment(), loadEditHistory.getSceneName(), loadEditHistory.getSmallSceneName(), loadEditHistory.getTemplate(), true);
        if (count > 0)
            throw new Exception(String.format("已经存在场景：‘%s’，请确认 ！", loadEditHistory.getSceneName()));

        //记录历史
        loadEditHistroyDao.save(loadEditHistory);

        //更新备注
        List remarkList = loadRemarkDao.getLoadRemark(loadEditHistory.getSceneName_Old(), loadEditHistory.getSmallSceneName(), loadEditHistory.getEnvironment(), loadEditHistory.getTemplate(), loadEditHistory.getVersion(), loadEditHistory.getTimeVersion(), loadEditHistory.getDataCreateTime());
        if (remarkList == null || remarkList.size() == 0) {
            LoadRemark remark = new LoadRemark(loadEditHistory.getSceneName(), loadEditHistory.getSmallSceneName(), loadEditHistory.getEnvironment(), loadEditHistory.getTemplate(), loadEditHistory.getVersion(), loadEditHistory.getTimeVersion(), loadEditHistory.getDataCreateTime(), "", "");
            loadRemarkDao.save(remark);
        } else {
            loadRemarkDao.updateSeneName(((LoadRemark) remarkList.get(0)).getId(), loadEditHistory.getSceneName());
        }

        //更新mongo记录
//            loadRemarkDao.updateSeneName();
        Query query = new Query();
        query.addCriteria(Criteria.where("sceneName").is(param.getSceneName()));
        query.addCriteria(Criteria.where("resultVersion").is(param.getResultVersion()));
        query.addCriteria(Criteria.where("smallSceneName").is(param.getSmallSceneName()));
        query.addCriteria(Criteria.where("template").is(param.getTemplate()));
        query.addCriteria(Criteria.where("createTime").is(loadEditHistory.getDataCreateTime()));
        query.addCriteria(Criteria.where("environment").is(param.getEnvironment()));
        query.addCriteria(Criteria.where("timeVersion").is(StringUtils.isEmpty(param.gettimeVersion()) ? null : param.gettimeVersion()));
        Update update = new Update();
        update.set("sceneName", loadEditHistory.getSceneName());
        reportDao.update(query, update);
        updateAGGReportScene(param, update);

        return true;

    }

    private void updateAGGReportScene(JmtParam param, Update update) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sceneName").is(param.getSceneName()));
        query.addCriteria(Criteria.where("resultVersion").is(param.getResultVersion()));
        query.addCriteria(Criteria.where("smallSceneName").is(param.getSmallSceneName()));
        query.addCriteria(Criteria.where("template").is(param.getTemplate()));
        query.addCriteria(Criteria.where("environment").is(param.getEnvironment()));
        query.addCriteria(Criteria.where("timeVersion").is(StringUtils.isEmpty(param.gettimeVersion()) ? null : param.gettimeVersion()));
        reportDao.updateAGGReport(query, update);
    }


    public List<JmtReportData> getJMTReportList(JmtParam param) {
        List<JmtReportData> result = new ArrayList<JmtReportData>();

        //得到全部数据
        result = reportDao.getJMTAGGReportList(param.getResultVersion(), param.getResultEndVersion(), param.gettimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getGrpThreads(), param.getTimeArt(), param.getIsEnable());

        if (result == null || result.size() == 0) {
            List<JMTReport> jmtReports = reportDao.getJMTReportList(param.getResultVersion(), param.getResultEndVersion(), param.gettimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getGrpThreads(), param.getIsEnable());
            //分组排序（jmt.getSceneName(),jmt.getSmallSceneName(),jmt.getResultVersion(),jmt.getLabelName()）
            HashMap<String, List<JMTReport>> mapJmtReport = getGroupList(jmtReports);

            for (String key : mapJmtReport.keySet()) {
                List<JMTReport> jmtList = (List<JMTReport>) mapJmtReport.get(key);

                List<JMTReportTimeStamp> reports = new ArrayList<JMTReportTimeStamp>();
                for (JMTReport jmtReport : jmtList) {
                    reports.add(new JMTReportTimeStamp(jmtReport.getTimeStamp(), jmtReport));
                }

                //根据GrpThreads进行分组
                HashMap<Long, List<JMTReport>> mapGrpThreadReport = getGrpThreadReport(reports);

                //遍历分组后的数据，进行计算
                for (Long grpThread : mapGrpThreadReport.keySet()) {
                    List<JMTReport> jmtGrpList = mapGrpThreadReport.get(grpThread);
                    JmtReportData re = getDateForJMTReportList(jmtGrpList);
                    if (!StringUtils.isEmpty(param.getTimeArt()) && re.getAvg() < Double.valueOf(param.getTimeArt()))
                        re = null;
                    if (re != null)
                        result.add(re);

                }

            }

        }

        Collections.sort(result);
        return result;
    }

    private void updateUnLoad(JMTReport model) {
        Query query = new Query();
        query.addCriteria(Criteria.where(model.getSceneName_name()).is(model.getSceneName()));
        query.addCriteria(Criteria.where(model.getResultVersion_name()).is(model.getOnlyVersion()));
        if (!StringUtils.isEmpty(model.getTimeVersion()))
            query.addCriteria(Criteria.where(model.getTimeVersion()).is(model.getTimeVersion()));
        query.addCriteria(Criteria.where(model.getSmallSceneName_name()).is(model.getSmallSceneName()));
        query.addCriteria(Criteria.where(model.getEnvironment_name()).is(model.getEnvironment()));
        query.addCriteria(Criteria.where(model.getTemplate_name()).is(model.getTemplate()));
        query.addCriteria(Criteria.where(model.getTimeStamp_name()).is(String.valueOf(model.getTimeStamp())));
        query.addCriteria(Criteria.where(model.getGrpThreads_name()).is(String.valueOf(model.getGrpThreads())));
        query.addCriteria(Criteria.where(model.getLabelName_name()).is(model.getLabelName()));

        Update update = new Update();
        update.set("UnLoad", true);
        reportDao.update(query, update);
    }

    public List getJMTReportLabel(JmtParam param) {
        return reportDao.getJMTLabelNameList(param.getResultVersion(), param.getResultEndVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getTemplate(), param.getIsEnable());
    }

    public String[][] getHistoryTpsJMTReport(JmtParam param) {
        List<JMTReport> jmtReports = reportDao.getJMTReportList(param.getResultVersion(), param.getResultEndVersion(), param.gettimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getGrpThreads(), param.getIsEnable());
        HashMap<String, List<JMTReport>> mapJmtReport = getGroupList(jmtReports);
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
        for (String key : mapJmtReport.keySet()) {
            List<JMTReport> jmtList = (List<JMTReport>) mapJmtReport.get(key);
            //加入排序类别，按照时间戳排序
            List<JMTReportTimeStamp> reports = new ArrayList<JMTReportTimeStamp>();
            for (JMTReport jmt : jmtList) {
                reports.add(new JMTReportTimeStamp(jmt.getTimeStamp(), jmt));
            }
            Collections.sort(reports);

            Long maxGrpThreads = 0L;

            for (JMTReportTimeStamp jmt : reports) {
                if (maxGrpThreads > 0 && jmt.getJmtReport().getGrpThreads() < maxGrpThreads) {
                    updateUnLoad(jmt.getJmtReport());
                    continue;
                }
                maxGrpThreads = jmt.getJmtReport().getGrpThreads();

                JMTReport report = jmt.getJmtReport();

                String labelName = report.getLabelName();

                if (!result.containsKey(labelName)) {
                    String[] data = {labelName, "", "", "", "", "", ""};
                    result.put(labelName, data);
                }

                String[] list = result.get(labelName);

                list[1] = list[1] + report.getTimeStamp() + ",";
                list[2] = list[2] + report.getGrpThreads() + ",";
                list[3] = list[3] + report.getElapsedCount() + ",";
                list[4] = list[4] + (report.getElapsedCount() - report.getErrorCount()) + ",";
                list[5] = list[5] + report.getErrorCount() + ",";
                list[6] = list[6] + report.getAvg() + ",";
            }
        }

        List<Map.Entry<String, String[]>> infoIds =
                new ArrayList<Map.Entry<String, String[]>>(result.entrySet());

        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String[]>>() {
            public int compare(Map.Entry<String, String[]> o1, Map.Entry<String, String[]> o2) {
                //return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        String[][] resultReport = new String[result.keySet().size()][7];

        for (int i = 0; i < infoIds.size(); i++) {
            resultReport[i] = infoIds.get(i).getValue();
        }

        return resultReport;
    }

    public String[][] getHistoryGrpJMTReport(JmtParam[] params) {
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        List<JmtReportData> JmtReportDataList = new ArrayList<JmtReportData>();

        for (int i = 0; i < params.length; i++) {
            JmtParam param = params[i];
            List<JMTReport> jmtReport = reportDao.getJMTReportList(param.getResultVersion(), param.getResultEndVersion(), param.gettimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getGrpThreads(), param.getIsEnable());

            //分组排序（jmt.getSceneName(),jmt.getSmallSceneName(),jmt.getResultVersion(),jmt.getLabelName()）
            HashMap<String, List<JMTReport>> mapJmtReport = getGroupList(jmtReport);
            for (String key : mapJmtReport.keySet()) {

                List<JMTReport> jmtGrpList = (List<JMTReport>) mapJmtReport.get(key);

                List<JMTReportTimeStamp> reports = new ArrayList<JMTReportTimeStamp>();

                for (JMTReport jmt : jmtGrpList) {
                    reports.add(new JMTReportTimeStamp(jmt.getTimeStamp(), jmt));
                }
                //按照时间排序数据
                Collections.sort(reports);

                jmtReport.clear();

                for (JMTReportTimeStamp stamp : reports) {
                    jmtReport.add(stamp.getJmtReport());
                }

                JmtReportDataList.add(getDateForJMTReportList(jmtReport));
            }
        }

        HashMap<String, List<JmtReportData>> hasJmtReportData = new HashMap<String, List<JmtReportData>>();
        //查询出来的最小的版本号
        int minVersion = 0;
        //根据label分组
        for (JmtReportData jmtReportData : JmtReportDataList) {
            int version = Integer.valueOf(jmtReportData.getOnlyVersion());

            if (minVersion > version || minVersion == 0)
                minVersion = version;

            String labelName = String.format("%s(%s)", jmtReportData.getLabelName(), jmtReportData.getSmallSceneName());
            if (!hasJmtReportData.containsKey(labelName)) {
                hasJmtReportData.put(labelName, new ArrayList<JmtReportData>());
            }
            hasJmtReportData.get(labelName).add(jmtReportData);
        }


        Long lastTimeStamp = 0L;

        for (String key : hasJmtReportData.keySet()) {
            List<JmtReportData> datalist = hasJmtReportData.get(key);

            //时间排序
            Collections.sort(datalist);

            Long minTime = 0L;
            int version = Integer.valueOf(datalist.get(0).getOnlyVersion());

            if (version == minVersion)
                minTime = 0L;
            else if (version > minVersion) {
                minTime = Long.valueOf(daysBetween(String.valueOf(minVersion), String.valueOf(version)));
            }

            for (JmtReportData jmtReportData : datalist) {
                if (!result.containsKey(key)) {
                    String[] data = {key, "", "", "", "", "", "", "", ""};
                    result.put(key, data);
                    lastTimeStamp = 0L;
                }

                String[] list = result.get(key);
                list[1] = list[1] + jmtReportData.getResultVersion() + ",";
                list[2] = list[2] + jmtReportData.getTpsAvg() + ",";
                list[3] = list[3] + jmtReportData.getAvg() + ",";
                list[4] = list[4] + (jmtReportData.getElapsed90()) + ",";
                if (lastTimeStamp == 0) {
                    list[5] = String.valueOf(minTime) + ",";
                } else {
                    Long timespan = Long.valueOf(daysBetween(String.valueOf(lastTimeStamp), jmtReportData.getOnlyVersion())); //((jmtReportData.getTimeStamp() - lastTimeStamp)/1000 /60 /60/24 );
                    minTime = minTime + timespan;// ( timespan==0 ?  1 : timespan );
                    list[5] = list[5] + String.valueOf(minTime) + ",";
                }
                list[6] = new SimpleDateFormat("yyyy/MM/dd").format(parseDate(String.valueOf(minVersion), "yyyyMMdd"));
                list[7] = jmtReportData.getEnvironment();
                list[8] = jmtReportData.getTemplate();
                lastTimeStamp = Long.valueOf(jmtReportData.getOnlyVersion());
            }
        }


        return sortReport(result);
    }


    public Long parseDate(String mdate, String Format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Format);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(mdate));
            return cal.getTimeInMillis();
        } catch (Exception ex) {
            return 0L;
        }
    }

    public int daysBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();


            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));

        } catch (Exception ex) {
            return 0;
        }
    }


    public String[][] getTpsJMTReportList(JmtParam param, int maxdataCount) {
        List<JMTReport> jmtReports = reportDao.getJMTReportList(param.getResultVersion(), param.getResultEndVersion(), param.gettimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getGrpThreads(), param.getIsEnable());
        HashMap<String, List<JMTReport>> mapJmtReport = getGroupList(jmtReports);
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        for (String key : mapJmtReport.keySet()) {
            List<JMTReport> jmtList = (List<JMTReport>) mapJmtReport.get(key);
            //加入排序类别，按照时间戳排序
            List<JMTReportTimeStamp> reports = new ArrayList<JMTReportTimeStamp>();
            for (JMTReport jmt : jmtList) {
                reports.add(new JMTReportTimeStamp(jmt.getTimeStamp(), jmt));
            }
            Collections.sort(reports);

            Long maxGrpThreads = 0L;
            Long minTime = 0L;
            Long lastTimeStamp = 0L;
            //计算的变量
            List<JMTReport> dataList = new ArrayList<JMTReport>();
            //分割后相差的单位
            int dataCount = reports.size() / maxdataCount;
            maxdataCount = reports.size() % maxdataCount == 0 ? dataCount : dataCount + 1;

            int count = 0;

            for (int i = 0; i < reports.size(); i++) {
                JMTReportTimeStamp jmt = reports.get(i);

                if (maxGrpThreads > 0 && jmt.getJmtReport().getGrpThreads() < maxGrpThreads) {
                    updateUnLoad(jmt.getJmtReport());
                    continue;
                }
                //是否进入下一个线程
                boolean isNewThreads = i > 0 && jmt.getJmtReport().getGrpThreads() > maxGrpThreads;

                maxGrpThreads = jmt.getJmtReport().getGrpThreads();

                count++;

                if (count == maxdataCount || isNewThreads || i == (reports.size() - 1)) {
                    if (!isNewThreads)
                        dataList.add(jmt.getJmtReport());

                    while (count > 0 && dataList.size() > 0) {

                        JmtReportData report = getDateForJMTReportList(dataList);

                        if (!StringUtils.isEmpty(param.getTimeArt()) && report.getAvg() < Double.valueOf(param.getTimeArt())) {
                            break;
                        }

                        //图表name显示_后显示合并条数
                        String labelName = String.format("%s(%s)_%s", report.getLabelName(), report.getSmallSceneName(), maxdataCount);
                        if (!result.containsKey(labelName)) {
                            String[] data = {labelName, "", "", "", "", "", "", ""};
                            result.put(labelName, data);
                        }
                        String[] list = result.get(labelName);
                        //得到其他值写入返回数组
                        list = cmdJmtReportData(report, list);
                        if (lastTimeStamp == 0L) {
                            list[7] = String.valueOf(minTime) + ",";
                        } else {
                            Long timespan = ((report.getTimeStamp() - lastTimeStamp) / 1000);
                            minTime = minTime + (timespan == 0 ? 1 : timespan);
                            list[7] = list[7] + String.valueOf(minTime) + ",";
                        }
                        lastTimeStamp = report.getTimeStamp();
                        dataList.clear();
                        //如果是新线程并且是最后一条的情况下，需要计算当前新线程的相关的数值
                        if (isNewThreads && i == (reports.size() - 1)) {
                            dataList.add(jmt.getJmtReport());
                            isNewThreads = false;
                        } else
                            count = 0;
                    }

                }
                if (count > 0 || isNewThreads)
                    dataList.add(jmt.getJmtReport());

            }
        }


        //针对labelName 进行排序
        return sortReport(result);
    }

    private String[] cmdJmtReportData(JmtReportData report, String[] list) {
        list[1] = list[1] + report.getTimeStamp() + ",";
        list[2] = list[2] + report.getGrpThreads() + ",";
        list[3] = list[3] + report.getElapsedCount() + ",";
        list[4] = list[4] + (report.getElapsedCount() - report.getErrorCount()) + ",";
        list[5] = list[5] + report.getErrorCount() + ",";
        list[6] = list[6] + report.getAvg() + ",";
        return list;
    }

    //排序
    private String[][] sortReport(HashMap<String, String[]> result) {
        List<Map.Entry<String, String[]>> infoIds =
                new ArrayList<Map.Entry<String, String[]>>(result.entrySet());

        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String[]>>() {
            public int compare(Map.Entry<String, String[]> o1, Map.Entry<String, String[]> o2) {
                //return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        String[][] resultReport = new String[result.keySet().size()][infoIds.size()];

        for (int i = 0; i < infoIds.size(); i++) {
            resultReport[i] = infoIds.get(i).getValue();
        }
        return resultReport;
    }


    private JmtReportData getDateForJMTReportList(List<JMTReport> list) {

        JMTReport patReport = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        double maxEapsed = 0;
        double minEapsed = Double.MAX_VALUE;
        int errorCount = 0;
        int elapsedCount = 0;
        int count = 0;
        double elapsed50 = 0;
        double elapsed90 = 0;
        double elapsed95 = 0;
        double elapsed99 = 0;
        double minTps = Double.MAX_VALUE;
        double maxTps = 0;
        double tps = 0;
        double avg = 0;

        Long begin = Long.MAX_VALUE;
        Long end = Long.MIN_VALUE;
        double sumEapsed = 0;
        Long timeStamp = Long.MIN_VALUE;
        Long endtimeStamp = Long.MIN_VALUE;
        for (int i = 0; i < list.size(); i++) {
            JMTReport jmtReport = list.get(i);

            if (patReport == null)
                patReport = jmtReport;


            if (maxEapsed < jmtReport.getMaxElapsed())
                maxEapsed = jmtReport.getMaxElapsed();

            if (minEapsed > jmtReport.getMinElapsed())
                minEapsed = jmtReport.getMinElapsed();

            errorCount = errorCount + jmtReport.getErrorCount();
            elapsedCount = elapsedCount + jmtReport.getElapsedCount();
            count = count + 1;
            sumEapsed = sumEapsed + jmtReport.getSumElapsed();
            elapsed50 = elapsed50 + jmtReport.getElapsed50();
            elapsed90 = elapsed90 + jmtReport.getElapsed90();
            elapsed95 = elapsed95 + jmtReport.getElapsed95();
            elapsed99 = elapsed99 + jmtReport.getElapsed99();

            Long resultVersion = jmtReport.getTimeStamp();
            if (begin > resultVersion) {
                begin = resultVersion;
                timeStamp = jmtReport.getTimeStamp();
            }
            if (end < resultVersion) {
                end = resultVersion;
                endtimeStamp = jmtReport.getTimeStamp();
            }

            if (minTps > jmtReport.getTps())
                minTps = jmtReport.getTps();
            if (maxTps < jmtReport.getTps())
                maxTps = jmtReport.getTps();

            tps = tps + jmtReport.getTps();

        }


        JmtReportData report = new JmtReportData();
        report.setSceneName(patReport.getSceneName());
        report.setSmallSceneName(patReport.getSmallSceneName());
        report.setLabelName(patReport.getLabelName());
        report.setBegin(sdf.format(begin));
        if (sdfDate.format(begin).equals(sdfDate.format(end)))
            report.setEnd(new SimpleDateFormat("HH:mm:ss").format(end));
        else
            report.setEnd(sdf.format(end));
        report.setTimeStamp(timeStamp);
        report.setResultVersion(patReport.getResultVersion());
        report.setAvg(Util.setScaleFour(sumEapsed / Double.valueOf(elapsedCount), 4));
        report.setMinElapsed(minEapsed);
        report.setMaxElapsed(maxEapsed);
        report.setElapsed50(Util.setScaleFour(elapsed50 / count, 0));
        report.setElapsed90(Util.setScaleFour(elapsed90 / count, 0));
        report.setElapsed95(Util.setScaleFour(elapsed95 / count, 0));
        report.setElapsed99(Util.setScaleFour(elapsed99 / count, 0));
        report.setFail(Util.setScaleFour(errorCount / Double.valueOf(elapsedCount), 4));
        report.setTpsAvg(Util.setScaleFour(tps / count, 4));
        report.setTps((endtimeStamp - timeStamp) == 0 ? 0 : Util.setScaleFour(Double.valueOf(elapsedCount) / (endtimeStamp - timeStamp) / 1000, 4));
        report.setErrorCount(errorCount);
        report.setElapsedCount(elapsedCount);
        report.setTpsMax(maxTps);
        report.setTpsMin(minTps);
        report.setCreateTime(patReport.getCreateTime());
        report.setGrpThreads(patReport.getGrpThreads());
        report.setEnvironment(patReport.getEnvironment());
        report.setTemplate(patReport.getTemplate());
        return report;

    }


    public TreeMap<String, List<String>> getResultVersion(String sceneName, String smallSceneName, String labelName, String environment, Boolean isEnable) {
        return reportDao.getResultVersion(sceneName, smallSceneName, labelName, environment, isEnable);
    }

    public List<String> getVersionTime(String sceneName, String smallSceneName, String labelName, String environment, String resultDate, Boolean isEnable) {
        List<String> result = new ArrayList<String>();
        result.add("NONE");

        TreeMap<String, List<String>> resultlist = reportDao.getVersionTime(sceneName, smallSceneName, labelName, environment, resultDate, isEnable);
        if (resultlist.size() > 0)
            result = resultlist.get(resultlist.firstKey());


        return result;
    }


    public List<String> getJMTSceneNameMap(String sceneName, String template, Boolean isEnable) {
        return reportDao.getJMTTaskIdList(sceneName, template, isEnable);
    }


    public List<JmtReportData> getJMTAGGReportList( JmtParam param) {


        //得到全部数据
        List<JmtReportData> jmtReports = reportDao.getJMTAGGReportList(param.getResultVersion(), param.getResultEndVersion(), param.gettimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getGrpThreads(), param.getTimeArt(), param.getIsEnable());
        if (jmtReports != null && jmtReports.size() > 0)
            Collections.sort(jmtReports);
        return jmtReports;
    }


    private void createValue(JmtReportData jmtReportData, Sheet sheet, CellStyle style, int rowIndex, boolean isHead) {
        int cellIndex = 0;
        Row row = sheet.createRow(rowIndex++);


        Field[] fields = jmtReportData.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Cell cell = row.createCell(cellIndex++);
            if (style != null)
                cell.setCellStyle(style);

            Class tCls = JmtReportData.class;

            try {

                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, tCls);
                Method method = propertyDescriptor.getReadMethod();
                String methodName = method.getName();
                Logger.info(methodName);
//                System.out.println(methodName);

                Boolean isValue = false;

                if (methodName.indexOf("methodName") == 0) {
                    if (isHead)
                        methodName = "场景";
                    isValue = true;
                } else if (methodName.indexOf("getLabelName") == 0) {
                    if (isHead)
                        methodName = "接口";
                    isValue = true;
                } else if (methodName.indexOf("getResultVersion") == 0) {
                    if (isHead)
                        methodName = "版本";
                    isValue = true;
                } else if (methodName.indexOf("getSmallSceneName") == 0) {
                    if (isHead)
                        methodName = "小场景";
                    isValue = true;
                } else if (methodName.indexOf("getAvg") == 0) {
                    if (isHead)
                        methodName = "ART";
                    isValue = true;
                } else if (methodName.indexOf("getBegin") == 0) {
                    if (isHead)
                        methodName = "开始时间";
                    isValue = true;
                } else if (methodName.indexOf("getEnd") == 0) {
                    if (isHead)
                        methodName = "结束";
                    isValue = true;
                } else if (methodName.indexOf("getGrpThreads") == 0) {
                    if (isHead)
                        methodName = "线程数";
                    isValue = true;
                } else if (methodName.indexOf("getMinElapsed") == 0) {
                    if (isHead)
                        methodName = "MinElapsed";
                    isValue = true;
                } else if (methodName.indexOf("getMaxElapsed") == 0) {
                    if (isHead)
                        methodName = "MaxElapsed";
                    isValue = true;
                } else if (methodName.indexOf("getElapsed50") == 0) {
                    if (isHead)
                        methodName = "50%";
                    isValue = true;
                } else if (methodName.indexOf("getElapsed90") == 0) {
                    if (isHead)
                        methodName = "90%";
                    isValue = true;
                } else if (methodName.indexOf("getElapsed95") == 0) {
                    if (isHead)
                        methodName = "95%";
                    isValue = true;
                } else if (methodName.indexOf("getElapsed99") == 0) {
                    if (isHead)
                        methodName = "99%";
                    isValue = true;
                } else if (methodName.indexOf("getError") == 0) {
                    if (isHead)
                        methodName = "Error";
                    isValue = true;
                } else if (methodName.indexOf("getTps") == 0) {
                    if (isHead)
                        methodName = "TPS(Avg)";
                    isValue = true;
                }

                if (isHead && isValue)
                    cell.setCellValue(method.getName());

                if (!isValue)
                    return;
                Object value = method.invoke(jmtReportData);

                if (value instanceof Integer) {
                    Integer intValue = (Integer) value;
                    cell.setCellValue(intValue);
                } else if (value instanceof Float) {
                    Float floatValue = (Float) value;
                    cell.setCellValue(floatValue);
                } else if (value instanceof Double) {
                    Double doubleValue = (Double) value;
                    cell.setCellValue(doubleValue);
                } else if (value instanceof Long) {
                    Long longValue = (Long) value;
                    cell.setCellValue(longValue);
                } else {
                    cell.setCellValue(value.toString());
                }
            } catch (Exception ex) {
                Logger.error(ex.getMessage());
//                System.out.println(ex);
            }

        }


    }


    private void createLoadHead(Sheet sheet, CellStyle style, String[] headList, int rowIndex) {

        Row row = sheet.createRow(rowIndex++);
        for (int i = 0; i < headList.length; i++) {

            Cell cell = row.createCell(i);
            if (style != null)
                cell.setCellStyle(style);

            cell.setCellValue(headList[i]);
        }
    }

    private void createLoadReportValue(JmtReportData jmtReportData, JmtReportData lastData, Sheet sheet, CellStyle style, int rowIndex) {

        Row row = sheet.createRow(rowIndex++);
        int cellIndex = 0;

//        if(lastData !=null)
//        {
//            int thisIndex =  0 ;
//            if(lastData.getResultVersion().equals(jmtReportData.getResultVersion()))
//            {
//
//                int lastindex=2;
//                while (true)
//                {
//
//                    if(sheet.getRow(rowIndex - lastindex)!=null && ( sheet.getRow(rowIndex - lastindex).getCell(0).getStringCellValue().equals(jmtReportData.getResultVersion())) )
//                    {
//                        thisIndex = rowIndex - lastindex ;
//                        lastindex = lastindex + 1 ;
//                    }else
//                        break;
//                }
//                System.out.println("合并"+thisIndex +" 到  "+ (rowIndex -1));
//                //CellRangeAddress  对象的构造方法需要传入合并单元格的首行、最后一行、首列、最后一列。
//                sheet.addMergedRegion(new CellRangeAddress(thisIndex, rowIndex-1, 0, 0));
//            }
//
//            if(jmtReportData.getSceneName().equals(lastData.getSceneName()))
//            {
//
//                int lastindex=2;
//                while (true)
//                {
//                    if(sheet.getRow(rowIndex - lastindex)!=null && (sheet.getRow(rowIndex - lastindex).getCell(1).getStringCellValue().length()==0 || sheet.getRow(rowIndex - lastindex).getCell(1).getStringCellValue().equals(jmtReportData.getSceneName())) )
//                    {
//                        thisIndex = rowIndex - lastindex ;
//                        lastindex = lastindex + 1 ;
//                    }else
//                        break;
//                }
//
//                System.out.println("合并"+thisIndex +" 到  "+ (rowIndex -1));
//                //CellRangeAddress  对象的构造方法需要传入合并单元格的首行、最后一行、首列、最后一列。
//                sheet.addMergedRegion(new CellRangeAddress(thisIndex, rowIndex-1, 1, 1));
//            }
//        }

        Cell cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getResultVersion());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getSceneName());


        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getLabelName());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getBegin());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getEnd());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getSmallSceneName());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getGrpThreads());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getAvg());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getMinElapsed());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getMaxElapsed());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getElapsed50());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getElapsed90());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getElapsed95());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getElapsed99());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getErrorVew());

        cell = row.createCell(cellIndex++);
        if (style != null)
            cell.setCellStyle(style);
        cell.setCellValue(jmtReportData.getTps());


    }

    public String createReportExcel(JmtParam[] params, HttpServletRequest request) throws Exception {



        //report路径
        String filePath = String.format("%s%sreport%s", request.getServletContext().getRealPath(""), File.separator, File.separator);
        File file = new File(String.format("%sload_%s.xlsx", filePath, System.currentTimeMillis()));
        if (file.exists())
            file.delete();
        file.createNewFile();


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");
        //设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);

        //<editor-fold> 折叠功能

        //生成一个样式(用于单元格)
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //生成一个字体(用于单元格)
        Font font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //把字体应用到当前的样式
        style.setFont(font);
        //生成并设置另一个样式
        CellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //生成另一个字体
        Font font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        //把字体应用到当前的样式
        style2.setFont(font2);

        // </editor-fold>

        int rowIndex = 0;
        String[] heard = new String[]{"版本", "场景", "接口", "开始", "结束", "小场景", "线程", "ART", "MinElapsed", "MaxElapsed", "50%", "90%", "95%", "99%", "Error", "TPS(Avg)"};

        //写表头
        createLoadHead(sheet, style, heard, rowIndex++);
//        createValue(new JmtReportData(), sheet, style, rowIndex++, true);


        for (int i = 0; i < params.length; i++) {
            JmtParam param = params[i];
            List<JmtReportData> jmtReport = reportDao.getJMTAGGReportList(param.getResultVersion(), param.getResultEndVersion(), param.gettimeVersion(), param.getEnvironment(), param.getSceneName(), param.getSmallSceneName(), param.getLabelName(), param.getTemplate(), param.getGrpThreads(), param.getTimeArt(), param.getIsEnable());
            Iterator<JmtReportData> iterator = jmtReport.iterator();

            JmtReportData lastdata = null;


            for (int t = 0; t < jmtReport.size(); t++) {
                JmtReportData data = jmtReport.get(t);
                if (t > 0)
                    lastdata = jmtReport.get(t - 1);

                createLoadReportValue(data, lastdata, sheet, style2, rowIndex++);
//                createValue(data, sheet, style2, rowIndex++, false);
            }

        }

        rangeResult(sheet, 0);
        rangeResult(sheet, 1);
        rangeResult(sheet, 2);
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        Logger.info(file.getName() + " written successfully");
//        System.out.println(file.getName() + " written successfully");
        return file.getPath();
    }

    private void rangeResult(Sheet sheet, int cellindex) {
        int rowCount = sheet.getLastRowNum();
        int minindex = 1;
        int maxindex = 1;
        String lastdata = "";
        //从第二行开始 计算合并
        for (int i = 2; i <= rowCount; i++) {
            lastdata = sheet.getRow(i - 1).getCell(cellindex).getStringCellValue().trim();

            if (lastdata.equals(sheet.getRow(i).getCell(cellindex).getStringCellValue().trim()))
                maxindex = i;
            else {
                sheet.addMergedRegion(new CellRangeAddress(minindex, maxindex, cellindex, cellindex));
                minindex = i;
                maxindex = i;
            }

        }

        if(minindex != maxindex)
            sheet.addMergedRegion(new CellRangeAddress(minindex, maxindex, cellindex, cellindex));
    }

}

