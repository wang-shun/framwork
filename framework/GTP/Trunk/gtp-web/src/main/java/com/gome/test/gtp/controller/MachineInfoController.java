/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.AgentInfoService;
import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.dao.AgentInfoDao;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.model.AgentInfo;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.TableResult;
import com.gome.test.gtp.schedule.TaskInfoListBo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Lin.Shi
 */
@Controller
@RequestMapping(value = "/machine")
public class MachineInfoController {

    @Autowired
    private AgentInfoService agentInfoService;

    @Autowired
    private ConfigureDictionaryService cds;

    @Autowired
    private AgentInfoDao ad;

    @Autowired
    private LoadConfigureDictionaryService lcdService;

    @Autowired
    private TaskInfoListBo taskInfoListBo;

    @RequestMapping("")
    ModelAndView goMachinePage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machine");
        return mv;
    }

    @RequestMapping(value = "/all")
    @ResponseBody
    TableResult getAllMachineInfo() {
        try {
            List machineInfoList = agentInfoService.getMachineList();
            if (machineInfoList != null) {
                return new TableResult(false, 1, machineInfoList.size(), machineInfoList.size(), machineInfoList.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception ex) {
            return new TableResult(true, ex.toString());
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    Result deleteMachine(@PathVariable(value = "id") int id) {
        try {
            if (ad.delete(id) == 1) {
                return new Result(false, "机器已成功删除");
            } else {
                return new Result(true, "所要删除的机器不存在");
            }
        } catch (Exception ex) {
            return new Result(true, "删除失败 " + ex.getMessage());
        }
    }

    @RequestMapping(value = "/edit/{id}")
    @ResponseBody
    ModelAndView macManage(@PathVariable(value = "id") int id) throws SQLException {
        ModelAndView mv = new ModelAndView();
        Object machineInfo = agentInfoService.getMachineById(id);
        mv.addObject("machine", machineInfo);
        mv.addObject("MachineStatus", cds.getMachineStatus());
        mv.addObject("Browsers", cds.getSortedListByParentName(Constant.BROWSER));
        mv.addObject("selectedBrowsers", taskInfoListBo.getBrowserListByValue(Integer.valueOf(((Object[]) machineInfo)[3].toString())));
        mv.addObject("Environment", cds.getEnvs());
        mv.addObject("taskTypeList", cds.getTaskTypes());
        mv.addObject("OSList", cds.getOSs());
        mv.setViewName("machine_edit");
        return mv;
    }

    @RequestMapping(value = "/edit/submit")
    @ResponseBody
    Result editMachine(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "ip") String ip,
            @RequestParam(value = "browser") int browser,
            @RequestParam(value = "env") String env,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "label") String label,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "os") String os) {
        try {
            HashMap<String, HashMap<String, Integer>> ssiMap = lcdService.loadSSIMap();
            int envValue = ssiMap.get(Constant.ENV).get(env);
            AgentInfo agentInfo = agentInfoService.get(id);
            agentInfo.setAgentName(name);
            agentInfo.setAgentIp(ip);
            agentInfo.setBrower(browser);
            agentInfo.setENV(envValue);
            agentInfo.setDescription(description);
            agentInfo.setAgentLabel(label);
            agentInfo.setTaskType(type);
            agentInfo.setAgentOS(os);
            agentInfoService.update(agentInfo);
            return new Result(false,"机器已成功更新");
        } catch (Exception ex) {
            return new Result(true, ex.toString());
        }
    }
    
    @RequestMapping(value = "/new")
    @ResponseBody
    ModelAndView macNew() throws SQLException {
        ModelAndView mv = new ModelAndView();
        mv.addObject("MachineStatus", cds.getMachineStatus());
        mv.addObject("Browsers", cds.getSortedListByParentName(Constant.BROWSER));
        mv.addObject("Environment", cds.getEnvs());
        mv.addObject("taskTypeList", cds.getTaskTypes());
        mv.addObject("OSList", cds.getOSs());
        mv.setViewName("machine_new");
        return mv;
    }
    
    @RequestMapping(value = "/new/submit")
    @ResponseBody
    Result newMachine(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "ip") String ip,
            @RequestParam(value = "browser") int browser,
            @RequestParam(value = "env") String env,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "label") String label,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "os") String os) {
        try {
            HashMap<String, HashMap<String, Integer>> ssiMap = lcdService.loadSSIMap();
            int envValue = ssiMap.get(Constant.ENV).get(env);

            AgentInfo agentInfo = new AgentInfo();

            agentInfo.setAgentName(name);
            agentInfo.setAgentIp(ip);
            agentInfo.setBrower(browser);
            agentInfo.setENV(envValue);
            agentInfo.setDescription(description);
            agentInfo.setAgentLabel(label);
            agentInfo.setAgentStatus(Constant.AGENT_STATUS_IDLE);
            agentInfo.setPort(0);
            agentInfo.setReseved(false);
            agentInfo.setLastRunTime(Util.nowTimestamp());
            agentInfo.setTaskId(0);
            agentInfo.setTaskType(type);
            agentInfo.setAgentOS(os);
            agentInfoService.save(agentInfo);
            return new Result(false, "机器已成功创建");
        } catch (Exception ex) {
            return new Result(true, ex.toString());
        }
    }
}
