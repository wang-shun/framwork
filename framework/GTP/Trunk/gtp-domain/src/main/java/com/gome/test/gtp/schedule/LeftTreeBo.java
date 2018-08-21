package com.gome.test.gtp.schedule;

import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.model.TreeNode;
import com.gome.test.gtp.model.TreeNodeInfo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.utils.Logger;
import com.gome.test.utils.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiadi on 15/9/2.
 */
@Component
public class LeftTreeBo {

    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private  TaskInfoListBo taskInfoService;

    private final  String GETGROUP="getGroup";
    private final  String GETTYPE="getType";
    private final  String GETENV="getEnv";
    private final  String DETAIL="getDetails";
    private final  String GETONLYGROUP="getOnlyGroup";
    private final  String GETCHILD="getChild";
    private final  String GETTASKINFO="getTaskInfoDetails";


    private List<TreeNodeInfo> getTreeNodebyFile(String pid) throws Exception
    {

        String xmlText = repConfigFile();
        NodeList nodeList = XmlUtils.getXMLNode(xmlText,"//node");
        List<TreeNodeInfo> list= new ArrayList<TreeNodeInfo>();
        for(int i=0;i< nodeList.getLength();i++)
        {
            Node node= nodeList.item(i);
            String id= node.getAttributes().getNamedItem("id").getNodeValue();
            String name=node.getAttributes().getNamedItem("name").getNodeValue();
            String Pid=node.getAttributes().getNamedItem("pid").getNodeValue();
            String value=node.getAttributes().getNamedItem("value").getNodeValue();
            String type=node.getAttributes().getNamedItem("type").getNodeValue();
            String url=node.getAttributes().getNamedItem("url").getNodeValue();
            String loadOnDemand=node.getAttributes().getNamedItem("loadOnDemand").getNodeValue();
            if(Pid.equals(pid)) {
                TreeNodeInfo info = new TreeNodeInfo(id, name, Pid,value,type,url,loadOnDemand);
                list.add(info);
            }
        }

        return  list;
    }

    private String repConfigFile() throws Exception {
        InputStream fis = this.getClass().getResourceAsStream(String.format("/%s", Constant.CFG_MAP.get("4")));
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        String resultCfgStr = "";
        while ((line = br.readLine()) != null) {
            resultCfgStr = resultCfgStr + line + "\r\n";
        }
        br.close();
        isr.close();
//        System.out.println(resultCfgStr);
        Logger.info(resultCfgStr);
        return resultCfgStr;
    }


    public  List<TreeNode> getTreeNode(String pid) throws Exception
    {
        List<TreeNode> tree = new ArrayList<TreeNode>();
        List<TreeNodeInfo> list = getTreeNodebyFile(pid);
        for(TreeNodeInfo node: list)
        {
            tree.add(getTreeNode(node));
        }
        return tree;
    }

    public List<TreeNode> getChildNode(String _id,String value) throws  Exception{

        List<TreeNode> nodeList = new ArrayList<TreeNode>();
        if (value.equals(GETGROUP))//找到组
        {
            nodeList = getGroupChild(_id,value,false);
        } else if (value.equals(GETONLYGROUP)) {
            nodeList = getGroupChild(_id,value,true);
        } else if (value.equals(GETTYPE)) {
            nodeList = getTypeChild(_id);
        } else if (value.equals(GETENV)) {
            nodeList = getEnvChild(_id);
        } else if(value.equals(GETCHILD))
        {
            nodeList=getTreeNode(_id);
        }else if(value.equals(GETTASKINFO))
        {
            nodeList=getTaskChild(_id);
        }
        return nodeList;
    }


    private TreeNode getTreeNode(TreeNodeInfo info) throws  Exception
    {
        TreeNode node=new TreeNode();
        node.setId(info.getId());
        node.setLabel(info.getName());
        node.setValue(info.getValue());
        node.setLoadOnDemand(Boolean.valueOf(info.getLoadOnDemand()));
        node.setType(info.getType());
        node.setChildren(getChildNode(info.getId(),info.getValue()));
        node.setUrl(info.getUrl());
        return  node;
    }




    private  List<TreeNode> getGroupChild(String _id,String value,boolean isOnlyGroup)
    {
        List<TreeNode> nodeList=new ArrayList<TreeNode>();

        List<Object[]> groupList = getBusinessGroups();
        for (int i = 0; i < groupList.size(); i++) {
            String id = groupList.get(i)[0].toString();
            String groupid=groupList.get(i)[1].toString();
            //if(taskInfoService.hasTaskbyGroupid(groupid)) {
                TreeNode node = new TreeNode();
                node.setLabel(id);
                id = _id + "_" + id;
                node.setId(id);
                node.setType("methodtree");
                node.setValue(isOnlyGroup?"": GETTYPE);
                node.setLoadOnDemand(isOnlyGroup?false:true);
                node.setChildren(null);//isOnlyGroup?null:getTypeChild(id)
                nodeList.add(node);
            //}
        }

        return nodeList;
    }

    private String getGroupid(String groupStr)
    {
        List<Object[]> groupList = getBusinessGroups();
        for(Object[] objects :groupList)
        {
            if(groupStr.equals(objects[0].toString()))
            {
                return objects[1].toString();
            }
        }
        return "";
    }

    private String getTypeid(String typeStr)
    {
        List<Object[]> typeStrList = getTaskTypes();
        for(Object[] objects :typeStrList)
        {
            if(typeStr.equals(objects[0].toString()))
            {
                return objects[1].toString();
            }
        }
        return "";
    }

    private String getEnvid(String envStr)
    {
        List<Object[]> typeStrList =getEnvs();
        for(Object[] objects :typeStrList)
        {
            if(envStr.equals(objects[0].toString()))
            {
                return objects[1].toString();
            }
        }
        return "";
    }

    private  List<TreeNode> getTypeChild(String _id)
    {
        List<TreeNode> nodeList=new ArrayList<TreeNode>();

        String groupid=_id.split("[_]").length>1?_id.split("[_]")[1]:"";
        groupid=getGroupid(groupid);

        if(groupid.length()>0) {
            List<Object[]> groupList = getTaskTypes();
            for (int i = 0; i < groupList.size(); i++) {
                String id = groupList.get(i)[0].toString();
                String typeid=groupList.get(i)[1].toString();
                if(taskInfoService.hasTaskbyTaskType(groupid,typeid)) {
                    TreeNode node = new TreeNode();
                    node.setLabel(id);
                    id = _id + "_" + id;
                    node.setId(id);
                    node.setValue(GETENV);
                    node.setType("methodtree");
                    node.setLoadOnDemand(true);
                    node.setChildren(null);//getEnvChild(id)
                    nodeList.add(node);
                }
            }
        }

        return nodeList;
    }

    private  List<TreeNode> getEnvChild(String _id)
    {
        List<TreeNode> nodeList=new ArrayList<TreeNode>();
        String[] idList=_id.split("[_]");
        String groupid=idList.length>1?idList[1]:"";
        String typeid=idList.length>2?idList[2]:"";
        groupid = getGroupid(groupid);
        typeid =getTypeid(typeid);
        if(groupid.length()==0 || typeid.length() ==0)
            return nodeList;

        List<Object[]> groupList = getEnvs();
        for (int i = 0; i < groupList.size(); i++) {
            String id = groupList.get(i)[0].toString();
            String envid=groupList.get(i)[1].toString();
            if(taskInfoService.hasTaskbyEnv(groupid,typeid,envid)) {
                TreeNode node = new TreeNode();
                node.setLabel(id);
                id = _id + "_" + id;
                node.setId(id);
                node.setValue(GETTASKINFO);
                node.setType("methodtree");
                node.setLoadOnDemand(true);
                node.setChildren(null);//getTaskChild(id)
                nodeList.add(node);
            }
        }

        return nodeList;
    }

    private List<TreeNode> getTaskChild(String _id)
    {
        List<TreeNode> nodeList=new ArrayList<TreeNode>();
        String[] idList=_id.split("[_]");
        String groupid=idList.length>1?idList[1]:"";
        String typeid=idList.length>2?idList[2]:"";
        String envid=idList.length>3?idList[3]:"";

        groupid = getGroupid(groupid);
        typeid =getTypeid(typeid);
        envid =getEnvid(envid);

        if(groupid.length()==0 || typeid.length() ==0 || envid.length() ==0 )
            return nodeList;

        List<Object[]> taskList = taskInfoService.getTaskInfoTree(groupid,typeid,envid);

        for (int i=0;i<taskList.size();i++) {
            String nodeid=taskList.get(i)[0].toString();
            String nodename=taskList.get(i)[1].toString();
                TreeNode node = new TreeNode();
                node.setLabel(nodename);
                nodeid = _id + "_" + nodeid;
                node.setId(nodeid);
                node.setType("methodtree");
                node.setLoadOnDemand(false);
                node.setChildren(null);
                nodeList.add(node);

        }

        return nodeList;
    }

    private List getBusinessGroups()
    {
      return   lcdService.getValueNameList(Constant.GROUP);
    }

    private List getTaskTypes()
    {
        return lcdService.getValueNameList(Constant.DIC_TASK_TYPE);
    }

    private List getEnvs()
    {
        return lcdService.getValueNameList(Constant.ENV);
    }

}
