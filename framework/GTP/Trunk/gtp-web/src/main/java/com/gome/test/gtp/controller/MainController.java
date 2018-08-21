/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Zonglin.Li
 */
@Controller
public class MainController {
    @RequestMapping(value = "/")
    ModelAndView goMainPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home");
        return mv;
    }

    @RequestMapping(value = "/nav")
    ModelAndView goNav() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nav");
        return mv;
    }
}
