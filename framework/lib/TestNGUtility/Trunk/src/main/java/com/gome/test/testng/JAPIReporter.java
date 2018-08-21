package com.gome.test.testng;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JAPIReporter implements IReporter {

    private Set<String> noExists;
    private Set<String> reRunSuites;
    private HashMap<String,String> prctureList;

    public JAPIReporter() {
        this(null, null);
    }

    public JAPIReporter(Set<String> noExists, Set<String> reRunSuites) {
        if(this.prctureList == null) {
            this.prctureList = new HashMap<String, String>();

        }
        if (null == noExists) {
            this.noExists = new HashSet<String>();
        } else {
            this.noExists = noExists;
        }

        if (null == reRunSuites) {
            this.reRunSuites = new HashSet<String>();
        } else {
            this.reRunSuites = reRunSuites;
        }
    }

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {



        VerboseReporter verboseReporter = new VerboseReporter(noExists);
        verboseReporter.generateReport(xmlSuites, suites, outputDirectory);

        GTPReporter jsonReporter = new GTPReporter(noExists, reRunSuites);
        jsonReporter.initPictureList(outputDirectory);
        jsonReporter.generateReport(xmlSuites, suites, outputDirectory);

        //        HudsonReporter xmlReporter = new HudsonReporter();
//        xmlReporter.generateReport(xmlSuites, suites, outputDirectory);
        JHtmlReporter htmlReporter = new JHtmlReporter(reRunSuites);
        htmlReporter.generateReport(xmlSuites, suites, outputDirectory);



    }
}
