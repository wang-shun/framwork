package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.LeftTreeService;
import com.gome.test.gtp.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhangjiadi on 15/9/2.
 */

@Controller
@RequestMapping(value = "/leftTree")
public class LeftTreeController {

    @RequestMapping("")
    ModelAndView goMachinePage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("hosts");
        return mv;
    }

    @Autowired
    private LeftTreeService leftTreeService;

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    @ResponseBody
    public Result getHelperTreeData(
            @RequestParam(value = "pid", required = true) String pid,
            @RequestParam(value = "pvalue", required = true) String pvalue
    ) {
        try {

            return new Result(leftTreeService.getNodes(pid,pvalue));

        } catch (Exception ex) {

            return new Result(true, String.valueOf(ex));
        }
    }
}
