package com.gome.test.gtp.report;

import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.dao.mongodb.ReportDao;
import com.gome.test.gtp.model.AGReport;
import com.gome.test.gtp.model.AGReportEmail;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lizonglin on 2016/4/26/0026.
 */
@Service
public class GTPReportService {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TaskInfoDao taskInfoDao;

    public void sendGTPDailyReport() {
        long date = Long.valueOf(com.gome.test.gtp.utils.Util.dateBeforeToday(1));
        Map<Integer, List<AGReport>> groupAGReportMap = getGroupReportMapByDate(date);
        Set<Integer> groupSet = groupAGReportMap.keySet();

        for (Integer groupId : groupSet) {
            String groupName = lcdService.getNameByValue(Constant.GROUP, groupId);

            int apiCaseNum = 0;
            int apiPassNum = 0;
            List<AGReport> apiAGReportList = new ArrayList<AGReport>();
            AGReportEmail apiAGReportEmail = new AGReportEmail();
            apiAGReportEmail.setReportType(lcdService.getNameByValue(Constant.DIC_TASK_TYPE, Constant.TASK_TYPE_API));
            apiAGReportEmail.setGroupName(groupName);
            apiAGReportEmail.setDate(date);

            int guiCaseNum = 0;
            int guiPassNum = 0;
            List<AGReport> guiAGReportList = new ArrayList<AGReport>();
            AGReportEmail guiAGReportEmail = new AGReportEmail();
            guiAGReportEmail.setReportType(lcdService.getNameByValue(Constant.DIC_TASK_TYPE, Constant.TASK_TYPE_GUI));
            guiAGReportEmail.setGroupName(groupName);
            guiAGReportEmail.setDate(date);

            for (AGReport agReport : groupAGReportMap.get(groupId)) {
                if (agReport.getTaskType() == Constant.TASK_TYPE_API) {
                    apiCaseNum += agReport.getCaseNum();
                    apiPassNum += agReport.getPassNum();
                    apiAGReportList.add(agReport);
                } else if (agReport.getTaskType() == Constant.TASK_TYPE_GUI) {
                    guiCaseNum += agReport.getCaseNum();
                    guiPassNum += agReport.getPassNum();
                    guiAGReportList.add(agReport);
                }
            }
            if (apiCaseNum > 0) {
                apiAGReportEmail.setTotalCaseNum(apiCaseNum);
                apiAGReportEmail.setTotalPassNum(apiPassNum);
                apiAGReportEmail.setTotalFailNum(apiCaseNum - apiPassNum);
                apiAGReportEmail.setPassRate(Util.computePercent(apiPassNum, apiCaseNum, 2));
                apiAGReportEmail.setPersonalReportList(apiAGReportList);
                emailService.sendAGReport(apiAGReportEmail);
            }

            if (guiCaseNum > 0) {
                guiAGReportEmail.setTotalCaseNum(guiCaseNum);
                guiAGReportEmail.setTotalPassNum(guiPassNum);
                guiAGReportEmail.setTotalFailNum(guiCaseNum - guiPassNum);
                guiAGReportEmail.setPassRate(Util.computePercent(guiPassNum, guiCaseNum, 2));
                guiAGReportEmail.setPersonalReportList(guiAGReportList);
                emailService.sendAGReport(guiAGReportEmail);
            }
        }

    }

    public void updateAGReport(int minDate, int maxDate) {
        List taskTypes = lcdService.getValueNameList("TaskType");
        List<Integer> taskTypeInts = new ArrayList<Integer>();
        for (Object type : taskTypes) {
            taskTypeInts.add(Integer.valueOf(((Object[])type)[1].toString()));
        }
        updateAGReport(minDate, maxDate, taskTypeInts);
    }

    public void updateAGReport(int minDate, int maxDate, List<Integer> taskTypes) {
        Map<Integer, Integer> taskAndGroup = taskInfoDao.getTaskAndGroup();
        for (Integer taskType : taskTypes) {
            reportDao.updateAGReport(minDate, maxDate, taskType, taskAndGroup);
        }
    }

    private Map<Integer, List<AGReport>> getGroupReportMapByDate(long date) {
        List<AGReport> reportList = reportDao.getAGReportByDate(date);
        Map<Integer, List<AGReport>> groupAGReportMap = new HashMap<Integer, List<AGReport>>();
        for (AGReport agReport : reportList) {
            if (groupAGReportMap.containsKey(agReport.getGroupId())) {
                groupAGReportMap.get(agReport.getGroupId()).add(agReport);
            } else {
                List<AGReport> groupReportList = new ArrayList<AGReport>();
                groupReportList.add(agReport);
                groupAGReportMap.put(agReport.getGroupId(), groupReportList);
            }
        }

        return groupAGReportMap;
    }

    public static void main(String[] args) {
        List<Object> taskTypes = new ArrayList<Object>();
        taskTypes.add(3);
        for (Object type : taskTypes) {
            System.out.println(String.valueOf(type));
        }
    }
}
