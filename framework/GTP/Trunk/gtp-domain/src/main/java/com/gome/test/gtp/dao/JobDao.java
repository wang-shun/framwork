/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.dao;

import com.gome.test.gtp.model.ScheduleJob;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 *
 * @author Zonglin.Li
 */
@Repository
@Transactional
public class JobDao{
    
    @PersistenceContext
    private EntityManager em;
    
    @SuppressWarnings("unchecked")
    public List<ScheduleJob> getAll() {
        return em.createQuery("from ScheduleJob").getResultList();
    }

}
