package com.gome.test.gtp.service.executor;

import com.gome.test.gtp.report.GTPReportService;
import com.gome.test.gtp.service.schedulerutil.AbstractExecutor;
import com.gome.test.utils.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiadi on 16/5/30.
 */
@Component
public class DeleteReportFile  extends AbstractExecutor {

    @Override
    public List<Class> getAutowiredClassList() {
        List<Class> autowiredList = new ArrayList<Class>();
        return autowiredList;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logInfo();

        String filePath = String.format("%s%s..%sgtp-web%ssrc%smain%swebapp%sreport%s", System.getProperty("user.dir"),
                File.separator, File.separator,
                File.separator,File.separator,
                File.separator,File.separator,File.separator);

//        System.out.println(String.format("File Path is %s",filePath));

        File file = new File(String.format("%s", filePath));

        if(file.isDirectory() && file.exists())
        {
            for(File excelFile : file.listFiles())
            {
                if(excelFile.getName().indexOf("load_")==0 && excelFile.lastModified() < System.currentTimeMillis() - (1000 * 60 * 30)) {
                    if( excelFile.delete())
                        Logger.info(String.format("%s  is delete",excelFile.getName()));
//                        System.out.println(String.format("%s  is delete",excelFile.getName()));
                }
            }
        }


    }

    @Override
    public String getCronExp() {
        return env.getProperty("executor.cronexp.deleteReportFile");
    }


}
