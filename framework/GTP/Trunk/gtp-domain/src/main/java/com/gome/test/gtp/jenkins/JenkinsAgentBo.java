package com.gome.test.gtp.jenkins;

import com.gome.test.gtp.dao.AgentInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lizonglin on 2015/5/7.
 */
@Service
public class JenkinsAgentBo {
    @Autowired
    private AgentInfoDao agentInfoDao;

    @Autowired
    private JenkinsBo jenkinsBo;

    public boolean hasIdleExecutor(String agentLabel) {
        String url = "";
        return true;
    }
}
