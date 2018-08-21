package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.SvnBranchInfoDao;
import com.gome.test.gtp.model.SvnBranchInfo;
import com.gome.test.gtp.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizonglin on 2015/4/29.
 */
@Service
public class SvnBranchService extends BaseService<SvnBranchInfo>{
    @Autowired
    private SvnBranchInfoDao svnBranchInfoDao;

    @Autowired
    private LoadConfigureDictionaryService lcdService;

    public List getAllSvnBranchInfo () {
        List svnList = svnBranchInfoDao.getSvnList();
        Map<Integer, String> replaceRule = new HashMap<Integer, String>();
        replaceRule.put(3, Constant.GROUP);
        replaceRule.put(5, Constant.DIC_TASK_TYPE);
        lcdService.replaceValue(svnList, replaceRule);
        return svnList;
    }

    public Object getSvnBranchById (int id) {
        List svnList = svnBranchInfoDao.getSvnById(id);
        Map<Integer, String> replaceRule = new HashMap<Integer, String>();
        replaceRule.put(5, Constant.DIC_TASK_TYPE);
        replaceRule.put(3, Constant.GROUP);
        lcdService.replaceValue(svnList,replaceRule);
        return svnList.get(0);
    }

    public boolean isUniqueByNameId(String name, int id) {
        return svnBranchInfoDao.isUniqueByNameId(name, id);
    }

    public int safeDelBranchById(int branchId) throws Exception {
        if (!svnBranchInfoDao.isUsedById(branchId)) {
            return svnBranchInfoDao.delete(branchId);
        } else {
            throw new Exception("有任务使用了该SVN地址，不允许删除");
        }
    }
}
