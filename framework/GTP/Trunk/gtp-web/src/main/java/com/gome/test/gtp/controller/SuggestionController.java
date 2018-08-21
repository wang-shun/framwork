/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.AdviceService;
import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.SessionService;
import com.gome.test.gtp.model.Advices;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.TableResult;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *
 * @author Zonglin.Li
 */
@Controller
@RequestMapping(value = "/suggestion")
public class SuggestionController {
    @Autowired
    private AdviceService adviceService;
    @Autowired
    private ConfigureDictionaryService cdService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private Environment env;

    @RequestMapping(value = "")
    ModelAndView goSuggestion () {
        ModelAndView mv = new ModelAndView("suggestion");
        return mv;
    }
    
    @RequestMapping(value = "/all")
    @ResponseBody
    TableResult getAllAdvices () {
        try {
            List<Object> adviceList = adviceService.getAllAdvices();
            if (adviceList != null) {
                return new TableResult(false,1,adviceList.size(),adviceList.size(),adviceList.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception e) {
            return new TableResult(true, e.toString());
        }
        
    }
    
    @RequestMapping(value = "/details/{id}")
    @ResponseBody
    Result getDetailsById (@PathVariable(value = "id") int id) {
        try {
            return new Result(false,String.valueOf(id),adviceService.getDetailsById(id));
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }
    
    @RequestMapping(value = "/edit/{id}")
    ModelAndView goEdit(@PathVariable(value = "id") int id) {
        ModelAndView mv = new ModelAndView("suggestion_edit");
        mv.addObject("suTypes", cdService.getAdvicesTypes());
        mv.addObject("suStatus", cdService.getAdvicesStatus());
        mv.addObject("suggestion", adviceService.get(id));
        return mv;
    }
    
    @RequestMapping(value = "/update/{id}")
    @ResponseBody
    Result updateSu (
            @PathVariable(value = "id") int id,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "status") int status,
            @RequestParam(value = "content") String content) {
        try {
            sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            Advices advices = adviceService.get(id);
            advices.setName(title);
            advices.setAdvicetype(type);
            advices.setStatus(status);
            advices.setAdvice(content);
            advices.setAssignTo(env.getProperty("admin.email"));
            advices.setOwner(sessionService.getSessionUsername());
            return new Result(false, String.valueOf(id), adviceService.update(advices));
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    
    
    @RequestMapping(value = "/new")
    ModelAndView goEdit() {
        ModelAndView mv = new ModelAndView("suggestion_new");
        mv.addObject("suTypes", cdService.getAdvicesTypes());
        mv.addObject("suStatus", cdService.getAdvicesStatus());
        return mv;
    }
    
    @RequestMapping(value = "/create")
    @ResponseBody
    Result newSu (
            @RequestParam(value = "title") String title,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "status") int status,
            @RequestParam(value = "content") String content) {
        try {
            Advices advices = new Advices();
            advices.setName(title);
            advices.setAdvicetype(type);
            advices.setStatus(status);
            advices.setAdvice(content);
            advices.setAssignTo(env.getProperty("admin.email"));
            advices.setOwner(sessionService.getSessionUsername());
            advices.setCreateTime(Util.nowTimestamp());
            adviceService.save(advices);
            return new Result(false, title);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }
    
}
