/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.ConfigureDictionaryDao;
import com.gome.test.gtp.model.ConfigureDictionary;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Zonglin.Li
 */
@Service
public class ConfigureDictionaryService extends BaseService<ConfigureDictionary>{
    @Autowired
    private ConfigureDictionaryDao cdDao;
    @Autowired
    private LoadConfigureDictionaryService lcdService;


    public int getConfigIdByTypeName (String type, String name) {
        return cdDao.getConfigIdByTypeName(type, name);
    }

    public List getBrowsers () {
        return lcdService.getValueNameList(Constant.BROWSER);
    }
    public List getEnvs () {
        return lcdService.getValueNameList(Constant.ENV);
    }
    public List getTaskStatus () {
        return lcdService.getValueNameList(Constant.TASK_STATUS);
    }
    public List getTaskTypes () {
        return lcdService.getValueNameList(Constant.DIC_TASK_TYPE);
    }
    public List getOSs() {
        return lcdService.getValueNameList(Constant.AGENT_OS);
    }
    public List getTaskRunTypes () {
        return lcdService.getValueNameList(Constant.RUN_TYPE);
    }
    public List getMachineStatus () {
        return lcdService.getValueNameList(Constant.AGENT_STATUS);
    }
    public List getGroups () {
        return lcdService.getValueNameList(Constant.GROUP);
    }
    public List getReportTypes () {
        return lcdService.getValueNameList(Constant.REPORT_TYPE);
    }
    public List getScheduleTypes () {
        return lcdService.getValueNameList(Constant.REGULAR_START_TYPE);
    }
    public List getAdvicesTypes () {
        return cdDao.getConfigTypes("AdviceType");
    }
    public List getAdvicesStatus () {
        return cdDao.getConfigTypes("AdviceStatus");
    }
    public List getDateRange () {
        return lcdService.getValueNameList(Constant.DATE_RANGE);
    }

    public List getTaskTypeValues() {return lcdService.getValueList(Constant.DIC_TASK_TYPE);}

    public List getSortedListByParentName(String parentName) {
        return cdDao.getSortedListByParentName(parentName);
    }

    public int calculateBrowserValue(String browsers) {
        int browserValue = 0;
        String[] browserArray = browsers.split(",");
        for (String browser : browserArray) {
            if (!browser.trim().equals(""))
                browserValue += lcdService.getValueByName(Constant.BROWSER, browser);
        }
        return browserValue;
    }

}
