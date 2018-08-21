/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.RunRegularInfoDao;
import com.gome.test.gtp.model.RunRegularInfo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

/**
 *
 * @author Dangdang.Cao
 */
@Service
public class RegularRuleService extends BaseService<RunRegularInfo> {

    @Autowired
    private RunRegularInfoDao regularInfoDao;

    public List getAllRegulars() {
        return regularInfoDao.getAllRegulars();
    }

    public List getRegularInfoById(int id) {
        return regularInfoDao.getRegularInfoById(id);
    }

    public List delNodefineChoose(List regularTypes) {
        for (int i = 0; i < regularTypes.size(); ++i) {
            Object[] reg = (Object[]) regularTypes.get(i);
            if (reg[0].equals("NoDefine")) {
                regularTypes.remove(i);
            }
        }
        return regularTypes;
    }


    /**
     * 保存或更新RunRegular
     * @param regularInfo
     * @param regularName
     * @param scheduleType
     * @param regularDate
     * @param weekDay
     * @param regularTime
     * @param runRule
     * @return
     * @throws ParseException
     */
    public RunRegularInfo setRegular(RunRegularInfo regularInfo, String regularName, int scheduleType, String regularDate,
                                     String weekDay, String regularTime, int runRule) throws ParseException {
        regularInfo.setRegularName(regularName);
        regularInfo.setStartType(scheduleType);
        regularInfo.setRunRule(runRule);
        String dateNew = String.format("%s %s:00",regularDate,regularTime);
        Timestamp timestamp = Util.StringToTimestamp(dateNew);
        regularInfo.setRunTime(timestamp);
        if (scheduleType == Constant.REGULAR_WEEKLY) {//weekly
            regularInfo.setWeekDay(weekDay);
        } else {//Once Daily
            regularInfo.setWeekDay("None");
        }
        return regularInfo;
    }

    public void saveRegular (String regularName, int scheduleType, String regularDate,
                             String weekDay, String regularTime, int runRule) throws ParseException {
        RunRegularInfo regularInfo = new RunRegularInfo();
        this.save(setRegular(regularInfo, regularName, scheduleType, regularDate, weekDay, regularTime, runRule));
    }

    public void updateRegular (int id, String regularName, int scheduleType, String regularDate,
                               String weekDay, String regularTime, int runRule) throws ParseException {
        RunRegularInfo regularInfo = this.get(id);
        this.update(setRegular(regularInfo, regularName, scheduleType, regularDate, weekDay, regularTime, runRule));
    }

    /**
     *
     * @param name 编辑后RunRule的name
     * @param id 当前编辑的RunRule的id
     * @return 编辑后的名字是否和其他RunRule重名
     */
    public boolean isUniqueByNameId(String name, int id) {
        return regularInfoDao.isUniqueByNameId(name, id);
    }

    public int safeDeleteRegularById(int id) throws Exception {
        if (!regularInfoDao.isUsedById(id)) {
            return regularInfoDao.delete(id);
        } else {
            throw new Exception("有任务使用了该调度规则，不允许删除");
        }
    }
}
