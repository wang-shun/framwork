package com.gome.test.gtp.dao;

import com.gome.test.gtp.dao.base.BaseDao;
import com.gome.test.gtp.model.Queue;
import com.gome.test.gtp.utils.Constant;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class QueueDao extends BaseDao<Queue> {
    public List getByTypeAndStatus(int type, int status) {
        String sql = String.format("select * from Queue where type=%d and status=%d order by createtime", type, status);
        return sqlQuery(sql);
    }

    @Transactional
    public void update(int id, int status) {
        Queue queue = this.get(id);
        if (queue != null) {
            queue.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            queue.setStatus(status);
            this.update(queue);
        }
    }

    @Transactional
    public void create(int type, int objectId) {
        Queue queue = new Queue();
        queue.setType(type);
        queue.setObjectId(objectId);
        queue.setStatus(Constant.QUEUE_NEW);
        queue.setCreateTime(new Timestamp(System.currentTimeMillis()));
        queue.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        this.save(queue);
    }
}
