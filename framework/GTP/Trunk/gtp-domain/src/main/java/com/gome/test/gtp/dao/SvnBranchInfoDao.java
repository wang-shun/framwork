package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.SvnBranchInfo;
import com.gome.test.gtp.utils.Constant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SvnBranchInfoDao extends BaseDao<SvnBranchInfo>{
	public List getSvnList () {
        String sql = "SELECT SvnBranchInfo.BranchID AS bid, SvnBranchInfo.BranchName, SvnBranchInfo.BranchUrl, SvnBranchInfo.BusinessGroup, SvnBranchInfo.ExpireDate, SvnBranchInfo.Type, CASE WHEN EXISTS (\n" +
                "SELECT SvnBranchInfo.BranchID\n" +
                "FROM SvnBranchInfo\n" +
                "WHERE bid IN (\n" +
                "SELECT TaskInfo.Branch\n" +
                "FROM TaskInfo\n" +
                "WHERE TaskInfo.TaskStatus <> 100)) THEN true ELSE false END\n" +
                "FROM SvnBranchInfo";
        return sqlQuery(sql);
    }

    public List getSvnById (int id) {
        String sql = String.format("select * from SvnBranchInfo WHERE BranchID = %d",id);
        return sqlQuery(sql);
    }

    public boolean isUniqueByNameId(String name, int id) {
        boolean isUnique = false;
        String sql = String.format("select count(BranchName) from SvnBranchInfo where BranchName = '%s' and BranchID != %d",name, id);
        if (Integer.valueOf(sqlQuery(sql).get(0).toString()) == 0) {
            isUnique = true;
        }
        return isUnique;
    }

    public boolean isUsedById(int id) {
        boolean isUsed = true;
        String sql = String.format("select TaskInfo.Branch from TaskInfo where TaskInfo.Branch = %d and TaskInfo.TaskStatus <> %d", id, Constant.JOB_DELETED);
        List usedBranchList = sqlQuery(sql);
        if (usedBranchList == null || usedBranchList.size() == 0) {
            isUsed = false;
        }
        return isUsed;
    }

    public List getSvnListByType (int type) {
        String sql = String.format("select BranchID, BranchName, BranchUrl, BusinessGroup, ExpireDate, Type from SvnBranchInfo where Type = %d", type);
        return sqlQuery(sql);
    }

    public String getSvnUrlByName(String svnName) {
        String sql = String.format("select BranchUrl from SvnBranchInfo where BranchName = '%s'", svnName);
        List resultList = sqlQuery(sql);
        if (resultList != null && resultList.size() != 0) {
            return resultList.get(0).toString();
        } else {
            return null;
        }
    }
}
