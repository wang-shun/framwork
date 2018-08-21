package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.report.GTPReportService;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzl on 2016/4/25/0025.
 */
@Component
public class GTPDailyReportExecutor extends AbstractExecutor {
    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(GTPReportService.class);
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();
        GTPReportService gtpReportService = (GTPReportService) getAutowiredClass(jobExecutionContext, GTPReportService.class);

        gtpReportService.sendGTPDailyReport();

    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.sentdailyreport");
    }
}
