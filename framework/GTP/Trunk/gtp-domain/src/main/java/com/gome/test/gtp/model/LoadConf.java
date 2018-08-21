package com.gome.test.gtp.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by lizonglin on 2015/7/20/0020.
 */
@Entity
@Table(name = "LoadConf")
@DynamicUpdate(true)
public class LoadConf {
    private int id;
    private String name;
    private String scriptSvn;
    private String jmxContent;
    private String sceneName;
    private String sourceSvn;
    private String env;
    private Timestamp lastUpdateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScriptSvn() {
        return scriptSvn;
    }

    public void setScriptSvn(String scriptSvn) {
        this.scriptSvn = scriptSvn;
    }

    public String getJmxContent() {
        return jmxContent;
    }

    public void setJmxContent(String jmxContent) {
        this.jmxContent = jmxContent;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSourceSvn() {
        return sourceSvn;
    }

    public void setSourceSvn(String sourceSvn) {
        this.sourceSvn = sourceSvn;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
