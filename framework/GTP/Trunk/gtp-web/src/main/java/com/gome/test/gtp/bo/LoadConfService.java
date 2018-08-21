package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.LoadConfDao;
import com.gome.test.gtp.dao.TaskInfoDao;
import com.gome.test.gtp.model.LoadConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lizonglin on 2015/7/20/0020.
 */
@Service
public class LoadConfService extends BaseService<LoadConf>{
    @Autowired
    private LoadConfDao loadConfDao;
    @Autowired
    private TaskInfoDao taskInfoDao;

    public List getLoadConfNameList() {
        return loadConfDao.getLoadConfNameList();
    }

    public boolean isUniqueByNameId(String name, int id) {
        return loadConfDao.isUniqueByNameId(name, id);
    }

    public List getAllLoadConf() {
        return loadConfDao.getAllLoadConf();
    }

    public int safeDelLoadConf(int id, String name) throws Exception {
        if (!loadConfDao.isUsed(name)) {
            return loadConfDao.delete(id);
        } else {
            throw new Exception("有任务使用了LoadConf，不允许删除");
        }
    }

    @Transactional
    public void linkedUpdate(LoadConf loadConf, String oldLoadConfName) {
        if (loadConf.getName().equals(oldLoadConfName)) {
            update(loadConf);
        } else {
            update(loadConf);
            taskInfoDao.updateTaskInfoByLoadConfName(loadConf.getName(), oldLoadConfName);
        }
    }
}
