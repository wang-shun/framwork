package com.gome.test.gtp;

import com.gome.test.gtp.dao.ResponseQueueDao;
import com.gome.test.gtp.dao.TaskListDao;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.testng.annotations.Test;

/**
 * Created by hacke on 2016/12/2.
 */
@SpringApplicationConfiguration(classes = Application.class)
public class JobResponseDaoTest {

//    private ResponseQueueDao queueDao = Application.getBean(ResponseQueueDao.class);


    @Test
    public void testResponse(){
        String jobName = "Job_1480583515096_163";
        int status = 68;
         TaskListDao responseJob = Application.getBean(TaskListDao.class);
        responseJob.updateTaskJobStuatus(jobName, "test jenkins execution and save report.", status);
        Application.Close();
    }
}
