package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.RunRegularInfo;
import com.gome.test.gtp.utils.Constant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RunRegularInfoDao extends BaseDao<RunRegularInfo> {

    public List getRegularList() {
        String hql = "select distinct RegularName from RunRegularInfo";
        return sqlQuery(hql);
    }

    public List getAllRegulars() {
        String sql = "SELECT rr.ID AS rid, rr.RegularName,cd.ItemName,rr.WeekDay, DATE_FORMAT(rr.RunTime,'%Y-%m-%d %T') AS RunTime,rr.RunRule, CASE WHEN EXISTS (\n" +
                "SELECT rr.ID\n" +
                "FROM RunRegularInfo\n" +
                "WHERE rid IN (\n" +
                "SELECT TaskInfo.RunRegularInfoID\n" +
                "FROM TaskInfo\n" +
                "WHERE TaskInfo.TaskStatus <> 100)) THEN TRUE ELSE FALSE END AS Used\n" +
                "FROM RunRegularInfo rr,ConfigureDictionary cd\n" +
                "WHERE (rr.StartType = cd.ItemValue AND cd.ItemParentID=74)";
        return sqlQuery(sql);
    }

    public List getRegularInfoById(int id) {
        String sql = "select rr.ID, rr.RegularName,cd.ItemName,rr.WeekDay,DATE_FORMAT(rr.RunTime,'%Y-%m-%d %T'),rr.RunRule from RunRegularInfo rr,ConfigureDictionary cd where rr.StartType = cd.ItemValue and cd.ItemParentID=74 and rr.id="+id;
        return sqlQuery(sql);
    }

    public boolean isUniqueByNameId (String name, int id) {
        boolean isUnique = false;
        String sql = String.format("select count(RegularName) from RunRegularInfo where RegularName = '%s' and ID != %d",name,id);
        if (Integer.valueOf(sqlQuery(sql).get(0).toString()) == 0) {
            isUnique = true;
        }
        return isUnique;
    }

    public boolean isUsedById(int id) {
        boolean isUsed = true;
        String sql = String.format("SELECT TaskInfo.RunRegularInfoID\n" +
                "FROM TaskInfo\n" +
                "WHERE TaskInfo.RunRegularInfoID = %d AND TaskInfo.TaskStatus <> %d",id, Constant.JOB_DELETED);
        List usedRegularList = sqlQuery(sql);
        if (null == usedRegularList || 0 == usedRegularList.size()) {
            isUsed = false;
        }
        return isUsed;
    }
}
