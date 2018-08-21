package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.ReplaceInfoDao;
import com.gome.test.gtp.model.ReplaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangjiadi on 16/4/6.
 */
@Service
public class ReplaceInfoService extends BaseService<ReplaceInfo> {

    @Autowired
    private ReplaceInfoDao replaceInfoDao;



}
