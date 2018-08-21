package com.gome.test.gtp.svn;

import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.SvnUtils;
import com.gome.test.utils.XmlUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by lizonglin on 2015/7/20/0020.
 */
@Component
public class SvnBo {
    @Autowired
    private Environment env;

    public Map getConfDirEntry(String svnUrl) throws Exception {
        Map confMap = new HashMap<String, List>();

        List<String> confSubFolderList =  getDirEntry(svnUrl, env.getProperty("load.dir.conf"));
        List<String> confFileList;
        for (String folder : confSubFolderList) {
            confFileList = getDirEntry(svnUrl, env.getProperty("load.dir.conf") + "/" + folder);
            confMap.put(folder, confFileList);
        }

        return confMap;
    }

    public List<String> getLibDirEntry(String svnUrl) throws Exception {
        return getDirEntry(svnUrl, env.getProperty("load.dir.lib"));
    }

    public List<String> getDbDirEntry(String svnUrl) throws Exception {
        return getDirEntry(svnUrl, env.getProperty("load.dir.db"));
    }

    public List<String> getReportSvnEntry(String svnUrl) throws SVNException, XPathExpressionException, DocumentException, IOException {
        String pomStr = getFileContent(svnUrl, Constant.LOAD_TEMP_POM);

        NodeList reportSvnNode = XmlUtils.getXMLNode(getPropertiesStr(pomStr), "//properties/svnReportPath[1]");
        String reportSvn = "";
        if (reportSvnNode.getLength() == 1) {
            reportSvn = reportSvnNode.item(0).getTextContent();
        }
        List<String> reportSvnList = new ArrayList<String>();
        reportSvnList.add(reportSvn);
        return reportSvnList;
    }

    public String getFileContent(String svnUrl, String relativePath) throws SVNException, IOException {
        return SvnUtils.getSvnFileContentByUrl(svnUrl, relativePath, env.getProperty("casecategory.svn_username"), env.getProperty("casecategory.svn_password"));
    }

    private List<String> getDirEntry(String svnUrl, String relativePath) throws Exception {
        List<String> dirEntryList = SvnUtils.getEntryNameByUrl(svnUrl, relativePath, env.getProperty("casecategory.svn_username"), env.getProperty("casecategory.svn_password"));
        Collections.sort(dirEntryList);
        return dirEntryList;
    }

    private String updateLoadPom(String svnUrl, String localPath) throws Exception {
        File pomDir = new File(env.getProperty("load.pom.path"), localPath);
        Util.deleteAllFilesOfDir(pomDir);
        pomDir.mkdirs();
        URL url = new URL(svnUrl);

        SvnUtils.checkout(SVNURL.create(url.getProtocol(), "", url.getHost(), url.getPort(), url.getPath(), true), pomDir.getAbsolutePath(),
                env.getProperty("casecategory.svn_username"), env.getProperty("casecategory.svn_password"));

        return new File(pomDir.getAbsolutePath(), Constant.CASECATEGORY_CSV).getAbsolutePath();
    }

    private String getPropertiesStr(String pomStr) {
        int startIndex = pomStr.indexOf("<properties>");
        int endIndex = pomStr.indexOf("</properties>") + "</properties>".length();
        return pomStr.substring(startIndex, endIndex);
    }
}
