package com.gome.test.gtp.model;

/**
 * Created by zhangjiadi on 15/9/6.
 */
public class TreeNodeInfo {
    private  String Id;
    private  String name;
    private  String Pid;
    private String value;
    private String type;
    private String url;
    private String loadOnDemand;

    public TreeNodeInfo(String id,String name,String pid,String value,String type,String url,String loadOnDemand)
    {
        this.Id=id;
        this.name=name;
        this.Pid=pid;
        this.value=value;
        this.type=type;
        this.url=url;
        this.loadOnDemand=loadOnDemand;
    }

    public TreeNodeInfo()
    {

    }


    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getPid() {
        return Pid;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLoadOnDemand() {
        return loadOnDemand;
    }
}
