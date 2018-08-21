
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.base.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * @author user
 */
public class BaseService<T> {

    @Autowired
    private BaseDao<T> baseDao;

    public List<T> getAll() {
        return baseDao.getAll();
    }

    public List<T> find(String hql) {
        return baseDao.find(hql);
    }

    public T get(Integer id) {
        return baseDao.get(id);
    }

    public T update(T entity) {
        return baseDao.update(entity);
    }

    public int delete(int id) {
        return baseDao.delete(id);
    }

    public void save(T entity) {
        baseDao.save(entity);
    }


}

