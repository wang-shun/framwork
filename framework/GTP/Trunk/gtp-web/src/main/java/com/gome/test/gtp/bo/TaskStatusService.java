package com.gome.test.gtp.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizonglin on 2015/6/26.
 */
@Component
public class TaskStatusService {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private Environment env;

    private volatile static Map<Integer, Object[]> idStatusMap = new HashMap<Integer, Object[]>();

    private volatile static long lastupdateTime = System.currentTimeMillis();

    public synchronized void refresh(boolean forceRefresh) {
        if (System.currentTimeMillis() - lastupdateTime > Long.valueOf(env.getProperty("task.status.refresh.interval")) || forceRefresh) {
//            System.out.println("refresh idStatusMap at: " + System.currentTimeMillis());
            idStatusMap = taskInfoService.getTaskInfoIdStatus();
            lastupdateTime = System.currentTimeMillis();
//            System.out.println("refreshed idStatusMap at: " + System.currentTimeMillis());
        }
    }

    @PostConstruct
    private void init() {
        refresh(true);
    }

    public Map<Integer, Object[]> getIdStatusMap() {
//        System.out.println("get idStatusMap at: " + System.currentTimeMillis());
        refresh(false);
        if (idStatusMap == null || idStatusMap.isEmpty()) {
            refresh(true);
        }
//        System.out.println("got idStatusMap at: " + System.currentTimeMillis());
        return idStatusMap;
    }

//    public Map<Integer, String> getIdStatusMap() {
//        return taskInfoService.getTaskInfoIdStatus();
//    }
}
