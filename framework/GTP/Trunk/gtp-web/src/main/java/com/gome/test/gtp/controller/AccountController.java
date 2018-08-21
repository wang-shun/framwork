/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.ObjectService;
import com.gome.test.gtp.model.TableResult;
import com.gome.test.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *
 * @author Dangdang.Cao
 */
@Controller
@RequestMapping(value = "/account-bak")
public class AccountController {

    @Autowired
    private ConfigureDictionaryService configureDictionaryService;
    @Autowired
    private ObjectService objService;

    @RequestMapping(value = "")
    ModelAndView goAccountPage() {
        ModelAndView mv = new ModelAndView("account");
        return mv;
    }

    @RequestMapping(value = "/allUser")
    @ResponseBody
    TableResult getAllAccount(){
       try {
           List allUser = objService.findAllUsers();
           if (allUser != null) {
               return new TableResult(false, 1, allUser.size(), allUser.size(), allUser.toArray());
           } else {
               return new TableResult();
           }
        } catch (Exception ex) {
            return new TableResult(true, ex.toString());
        }
    }
    
    @RequestMapping(value = "/register")
    ModelAndView createUser() {
        ModelAndView mv = new ModelAndView();
        List groups = configureDictionaryService.getGroups();
        Logger.info("groups"+groups.get(0));
//        System.out.println("groups"+groups.get(0));
        mv.addObject("groups", groups);
        mv.setViewName("createUser");
        return mv;
    }

    @RequestMapping(value = "/registerSuccess")
    ModelAndView registerSuccess(){
        ModelAndView mv = new ModelAndView();
        //注册信息入库
        
        
        mv.setViewName("changePasswd");
        return mv;
    }
    
    
    
}
