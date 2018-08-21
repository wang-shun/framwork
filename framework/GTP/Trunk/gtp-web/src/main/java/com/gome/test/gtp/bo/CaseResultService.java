/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.model.CaseResult;

import java.util.List;

import com.gome.test.gtp.dao.mongodb.MongoDBDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class CaseResultService extends BaseService<CaseResult> {
    @Autowired
    private MongoDBDao mongoDBDao;

    public List getResultById(int reportId) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("reportId").is(reportId);
        query.addCriteria(criteria);

        return mongoDBDao.getByCondition(CaseResult.class, query);

    }

}
