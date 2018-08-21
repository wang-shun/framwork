package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.ReplaceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangjiadi on 16/4/6.
 */
@Repository
public class ReplaceInfoDao extends BaseDao<ReplaceInfo> {
    public List<ReplaceInfo> getReplaceInfoListByTaskId(int taskId) {
        String sql = String.format("select * from ReplaceInfo where ReplaceInfo.TaskId = %d", taskId);
        return sqlQuery(sql, ReplaceInfo.class);
    }
}
