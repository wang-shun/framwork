package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.LoadRemark;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by zhangjiadi on 16/2/3.
 */
@Repository
public class LoadRemarkDao extends BaseDao<LoadRemark> {

    public List<LoadRemark> getLoadRemark(String sceneName,String smallSceneName,String env,String template,String version,String timeVersion,String dataCreateTime) {
        String sql = String.format("SELECT * FROM LoadRemark WHERE  SceneName = '%s' and SmallSceneName ='%s' and " +
                    "Environment='%s' and Template ='%s' and Version='%s' and DataCreateTime= '%s' and TimeVersion='%s'", sceneName,smallSceneName,env,template,version,dataCreateTime,timeVersion);

        return sqlQuery(sql, LoadRemark.class);

    }

    @Transactional
    public void updateSeneName(int id, String sceneName) {
        LoadRemark loadRemark = this.get(id);
        if (loadRemark != null) {
            loadRemark.setSceneName(sceneName);
            this.update(loadRemark);
        }
    }

    @Transactional
    public void updateRemark(int id, String remark) {
        LoadRemark loadRemark = this.get(id);
        if (loadRemark != null) {
            if(!loadRemark.equals(remark))
            {
                loadRemark.setRemark(remark);
                loadRemark.setRemark_History(loadRemark.getRemark_History()+"\n"+ remark);
            }
            this.update(loadRemark);
        }
    }


}
