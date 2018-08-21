package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.Advices;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdviceDao extends BaseDao<Advices>{
    public List getAllAdvices () {
        String hql = "select Advices.AdvicesID as id, Advices.Name as Title, tt.ItemName as typeName, adviceStatus.ItemName as statusName,\n"
                + "Advices.AssignTo as assignTo, Advices.CreateTime as createTime, Advices.Owner as owner from Advices\n"
                + "left join\n"
                + "(select ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue from ConfigureDictionary,\n"
                + "(select convert(nvarchar,ID) as i,ItemName from ConfigureDictionary where ItemName = 'AdviceType') as T where T.i=ConfigureDictionary.ItemParentID)\n"
                + "as tt on Advices.AdviceType = tt.ItemValue \n"
                + "left join\n"
                + "(select ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue from ConfigureDictionary,\n"
                + "(select convert(nvarchar,ID) as i,ItemName from ConfigureDictionary where ItemName = 'AdviceStatus') as T where T.i=ConfigureDictionary.ItemParentID)\n"
                + "as adviceStatus on Advices.Status = adviceStatus.ItemValue ";
        return sqlQuery(hql);
    }
    
    public List getDetailsById (int id) {
        String hql = "select Advices.Name as Title, adviceStatus.ItemName as statusName, tt.ItemName as typeName, Advices.Advice as adContent,\n"
                + "Advices.AssignTo as assignTo, Advices.CreateTime as createTime, Advices.Owner as owner from Advices\n"
                + "left join\n"
                + "(select ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue from ConfigureDictionary,\n"
                + "(select convert(nvarchar,ID) as i,ItemName from ConfigureDictionary where ItemName = 'AdviceType') as T where T.i=ConfigureDictionary.ItemParentID)\n"
                + "as tt on Advices.AdviceType = tt.ItemValue \n"
                + "left join\n"
                + "(select ConfigureDictionary.ItemName,ConfigureDictionary.ItemValue from ConfigureDictionary,\n"
                + "(select convert(nvarchar,ID) as i,ItemName from ConfigureDictionary where ItemName = 'AdviceStatus') as T where T.i=ConfigureDictionary.ItemParentID)\n"
                + "as adviceStatus on Advices.Status = adviceStatus.ItemValue "
                + "where Advices.AdvicesID = " + id;
        return sqlQuery(hql);
    }
}
