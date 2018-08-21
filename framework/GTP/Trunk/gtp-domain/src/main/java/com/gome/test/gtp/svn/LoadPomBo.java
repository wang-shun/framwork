package com.gome.test.gtp.svn;

import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.SvnUtils;
import com.gome.test.utils.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNURL;
import org.w3c.dom.NodeList;

import java.io.File;
import java.net.URL;

/**
 * Created by lizonglin on 2015/7/22/0022.
 */
@Component
public class LoadPomBo {
    @Autowired
    private Environment env;

    public void updateLoadPom(String svnUrl, String localPath) throws Exception {
        File pomDir = new File(env.getProperty("load.pom.path"), localPath);
        Util.deleteAllFilesOfDir(pomDir);
        pomDir.mkdirs();
        URL url = new URL(svnUrl);
        SvnUtils.checkout(SVNURL.create(url.getProtocol(), "", url.getHost(), url.getPort(), url.getPath(), true), pomDir.getAbsolutePath(),
                env.getProperty("casecategory.svn_username"), env.getProperty("casecategory.svn_password"));

        File pomFile = new File(pomDir.getAbsolutePath(), Constant.LOAD_TEMP_POM);

        NodeList searchNode = XmlUtils.getXMLNode(pomFile, "//project[1]/properties[1]/"+ Constant.LOAD_SEARCH_PATH+"[1]");

    }

    private void updateNode(String nodeTag) {

    }
}
