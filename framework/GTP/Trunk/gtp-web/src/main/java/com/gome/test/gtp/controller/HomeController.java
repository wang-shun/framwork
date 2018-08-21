/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 *
 * @author Zonglin.Li
 */
@Controller
@RequestMapping(value = "/home")
public class HomeController {
    @RequestMapping(value = "")
    ModelAndView goHome() throws IOException {
        ModelAndView mv = new ModelAndView("newhome");
        return mv;
    }

    @RequestMapping(value = "/newhome")
    ModelAndView gonewhome() throws IOException {
        ModelAndView mv = new ModelAndView("newhome");
        return mv;
    }
}
