/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Zonglin.Li
 */
@Controller
@RequestMapping(value = "/logout")
public class LogoutController {
    @Autowired
    private ConfigureDictionaryService cdService;
    
    @RequestMapping(value = "")
    ModelAndView logout () {
        ModelAndView mv = new ModelAndView("login");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.invalidate();
        mv.addObject("groups", cdService.getGroups());
        return mv;
    }
}
