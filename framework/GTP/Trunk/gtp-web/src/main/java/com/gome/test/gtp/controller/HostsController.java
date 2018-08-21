/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.HostsService;
import com.gome.test.gtp.bo.SessionService;
import com.gome.test.gtp.dao.HostsDao;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.model.Hosts;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.TableResult;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import com.gome.test.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author Zonglin.Li
 */
@RestController
//@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
@RequestMapping(value = "/hosts")
public class HostsController {
    @Autowired
    private HostsDao hostsdao;
    @Autowired
    private ConfigureDictionaryService cdService;
    @Autowired
    private HostsService hostsService;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private SessionService sessionService;

    @RequestMapping("")
    ModelAndView goMachinePage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("hosts");
        return mv;
    }
    
    @RequestMapping(value = "/all")
    @ResponseBody
    TableResult getAllHosts() {
        try {
            List hostsInfoList = hostsService.getAllHosts();
            if (hostsInfoList != null) {
                return new TableResult(false, 1, hostsInfoList.size(), hostsInfoList.size(), hostsInfoList.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception ex) {
            return new TableResult(true, ex.toString());
        }
    }
    
    @RequestMapping(value = "/edit/{id}")
    ModelAndView goEdit(@PathVariable(value = "id") int id) {
        ModelAndView mv = new ModelAndView("host_edit");
        Hosts hosts = hostsdao.get(id);
        String envName = lcdService.getNameByValue(Constant.ENV, hosts.getEnv());
        boolean enable = hosts.getEnable();
        String hostContent = hosts.getHostContent();
        int spaceNum = Util.spaceNumOfString(hostContent,"192");
        mv.addObject("Envs", cdService.getEnvs());
        mv.addObject("host", hosts);
        mv.addObject("enable", String.valueOf(enable));
        mv.addObject("spaceNum", spaceNum);
        mv.addObject("envName", envName);
//        System.out.println(spaceNum);
        Logger.info(""+spaceNum);
        return mv;
    }
    
    @RequestMapping(value = "/update")
    @ResponseBody
    Result updateHost (
            @RequestParam(value = "id") int id,
            @RequestParam(value = "env") String env,
            @RequestParam(value = "enable") boolean enable,
            @RequestParam(value = "hostContent") String hostContent) {
        try{
            sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            Hosts hosts = hostsdao.get(id);
            hosts.setId(id);
            hosts.setEnv(lcdService.getValueByName(Constant.ENV,env));
            hosts.setEnable(enable);
            hosts.setHostContent(hostContent);
            hosts.setLastUpdateTime(Util.ymdNowDate());
            hosts.setLastUpdateUser(sessionService.getSessionUsername());
            hostsdao.update(hosts);
            return new Result(false,"Hosts已成功更新");
        }catch (Exception e) {
            return new Result(true,e.toString());
        }
        
        
    }

    @RequestMapping(value = "/new")
    ModelAndView newHost() {
        ModelAndView mv = new ModelAndView("host_new");
        mv.addObject("envs", cdService.getEnvs());
        return mv;
    }

    @RequestMapping(value = "newSave")
    Result saveNewHost (
            @RequestParam(value = "env") String env,
            @RequestParam(value = "enable") boolean enable,
            @RequestParam(value = "hostContent") String hostContent,
            @RequestParam(value = "editor") String editor) {
        try {
            Hosts hosts = new Hosts();
            hosts.setEnv(lcdService.getValueByName(Constant.ENV,env));
            hosts.setEnable(enable);
            hosts.setHostContent(hostContent);
            hosts.setLastUpdateTime(Util.ymdNowDate());
            hosts.setLastUpdateUser(editor);
            hostsdao.save(hosts);
            return new Result(false, "Hosts已成功创建");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    Object[] hostsDetail (
            @PathVariable(value = "id") int id
    ) {
        Object[] hosts = hostsdao.getHostById(id);
        hosts[1] = lcdService.getNameByValue(Constant.ENV, Integer.valueOf(hosts[1].toString()));
        return hosts;
    }

    @RequestMapping(value = "/delete/{id}")
    Result deleteById(@PathVariable(value = "id") int id) {
        try {
            hostsdao.delete(id);
            return new Result(false, "OK");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }

    }
}
