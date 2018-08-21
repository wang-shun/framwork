/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.AdviceDao;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.model.Advices;
import com.gome.test.gtp.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Zonglin.Li
 */
@Service
public class AdviceService extends BaseService<Advices>{
    @Autowired
    private AdviceDao advicesDao;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    
    public List getAllAdvices () {
        List AdviceList = advicesDao.find("select advicesId,name,advicetype,status,assignTo,createTime,owner,advice from Advices");
        Map<Integer, String> replaceRule = new HashMap<Integer, String>();
        replaceRule.put(2, Constant.ADVICE_TYPE);
        replaceRule.put(3, Constant.ADVICE_STATUS);
        lcdService.replaceValue(AdviceList, replaceRule);
        return AdviceList;
    }
    
    public List getDetailsById (int id) {
        return advicesDao.getDetailsById (id);
    }
}
