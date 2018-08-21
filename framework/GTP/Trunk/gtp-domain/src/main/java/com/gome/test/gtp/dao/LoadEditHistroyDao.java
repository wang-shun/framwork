package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.LoadEditHistory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by zhangjiadi on 16/2/2.
 */
@Repository
@Transactional
public class LoadEditHistroyDao extends BaseDao<LoadEditHistory> {
}
