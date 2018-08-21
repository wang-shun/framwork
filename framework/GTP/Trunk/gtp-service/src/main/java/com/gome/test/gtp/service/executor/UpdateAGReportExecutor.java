package com.gome.test.gtp.service.executor;


import com.gome.test.gtp.report.GTPReportService;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import com.gome.test.gtp.utils.Util;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiadi on 16/4/25.
 */
@Component
public class UpdateAGReportExecutor extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        autowiredList.add(GTPReportService.class);
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();

        GTPReportService gtpReportService = (GTPReportService)getAutowiredClass(jobExecutionContext, GTPReportService.class);
        gtpReportService.updateAGReport(Integer.valueOf(Util.dateBeforeToday(3)), Integer.valueOf(Util.dateBeforeToday(0)));
    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.updateagreport");
    }


}
