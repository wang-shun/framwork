package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.LoadConfDao;
import com.gome.test.gtp.dao.LoadSceneDao;
import com.gome.test.gtp.model.LoadScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lizonglin on 2015/7/23/0023.
 */
@Service
public class LoadSceneService extends BaseService<LoadScene> {
    @Autowired
    private LoadSceneDao loadSceneDao;
    @Autowired
    private LoadConfDao loadConfDao;

    public List<String> getAllSceneName() {
        return loadSceneDao.getAllSceneName();
    }
    public boolean isUniqueByNameId(String name, int id) {
        return loadSceneDao.isUniqueByNameId(name, id);
    }

    public List getAllScene() {
        return loadSceneDao.getAllScene();
    }

    public int safeDeleteLoadScene(int id, String name) throws Exception {
        if (!loadSceneDao.isUsed(name)) {
            return loadSceneDao.delete(id);
        } else {
            throw new Exception("有任务使用了此场景，不允许删除");
        }
    }

    @Transactional
    public void linkedUpdate(LoadScene loadScene, String oldName) {
        if (loadScene.getName().equals(oldName)) {
            update(loadScene);
        } else {
            update(loadScene);
            loadConfDao.updateByLoadSceneName(loadScene.getName(), oldName);
        }
    }
}
