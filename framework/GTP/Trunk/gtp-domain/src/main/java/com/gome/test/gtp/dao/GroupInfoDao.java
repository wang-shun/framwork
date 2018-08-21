package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.GroupInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lizonglin on 2016/4/26/0026.
 */
@Repository
public class GroupInfoDao extends BaseDao<GroupInfo> {
    public GroupInfo getEmailByGroupName(String groupName) {
        String sql = String.format("select * from GroupInfo where GroupInfo.name = '%s'", groupName.trim());
        List<GroupInfo> groupInfoList = sqlQuery(sql, GroupInfo.class);
        if (groupInfoList.size() > 0)
            return groupInfoList.get(0);
        else
            return null;
    }
}
