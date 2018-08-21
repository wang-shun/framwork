package com.gome.test.gtp.bo;

import com.gome.test.gtp.model.TreeNode;
import com.gome.test.gtp.model.TreeNodeInfo;
import com.gome.test.gtp.schedule.LeftTreeBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiadi on 15/9/2.
 */
@Service
public class LeftTreeService {
    @Autowired
    private LeftTreeBo treeBo;

    public List<TreeNode> getNodes(String pid,String value) throws Exception
    {
        List<TreeNode> returnTree = new ArrayList<TreeNode>();
        if(pid.equals("0"))
        {
            returnTree = treeBo.getTreeNode(pid);

        }else if(value.length()>0) {

            returnTree = treeBo.getChildNode(pid,value);
        }
        return  returnTree;
    }
}
