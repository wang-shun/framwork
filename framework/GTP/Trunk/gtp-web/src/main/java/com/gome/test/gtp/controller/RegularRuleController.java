/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.RegularRuleService;
import com.gome.test.gtp.bo.ToUtfCoding;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.TableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;

/**
 *
 * @author Dangdang.Cao
 */
@Controller
@RequestMapping(value = "/regularRule")
public class RegularRuleController {

    @Autowired
    private ConfigureDictionaryService configureDictionaryService;
    @Autowired
    private RegularRuleService regularService;
    @Autowired
    private ToUtfCoding toUtfCoding;


    @RequestMapping(value = "")
    ModelAndView goRegulaRulePage() {
        ModelAndView mv = new ModelAndView("regularRule");
        return mv;
    }

    @RequestMapping(value = "/all")
    @ResponseBody
    TableResult getAllRegularRule() {
        try {
            List<Object> all = regularService.getAllRegulars();
            if (all != null) {
                return new TableResult(false, 1, all.size(), 8, all.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception ex) {
            return new TableResult(true, ex.toString());
        }
    }

    @RequestMapping(value = "/edit/{id}")
    ModelAndView RegularRuleEdit(@PathVariable("id") int id) {
        ModelAndView mv = new ModelAndView("regularRule_edit");
        List result = regularService.getRegularInfoById(id);
        Object[] obj = (Object[]) result.get(0);
        List regularTypes = configureDictionaryService.getScheduleTypes();
        regularTypes = regularService.delNodefineChoose(regularTypes);
        mv.addObject("regular", obj);
        mv.addObject("regularTypes", regularTypes);
        return mv;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    Result RegularEditSave(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "date") String date,
            @RequestParam(value = "weekDay") String weeekDay,
            @RequestParam(value = "time") String time,
            @RequestParam(value = "runRule") int runRule) throws ParseException {
        try {
            name = name.trim();
            if(!regularService.isUniqueByNameId(name, id)){
                return new Result(true, "名称为 " + name + " 的调度规则已存在，请选择其他名称");
            }

            regularService.updateRegular(id, name,type,date,weeekDay,time,runRule);
            return new Result(false, "OK");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }

    }

    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    Result RegularRuleDelete(@PathVariable(value = "id") int id) {
        try {
            regularService.safeDeleteRegularById(id);
            return new Result(false, "OK");

        } catch (Exception e) {
            return new Result(true, e.getMessage());
        }
    }

    @RequestMapping(value = "/new")
    @ResponseBody
    ModelAndView creatRegularRule() {
        ModelAndView mv = new ModelAndView();
        List regularTypes = configureDictionaryService.getScheduleTypes();
        regularTypes = regularService.delNodefineChoose(regularTypes);
        mv.addObject("regularTypes", regularTypes);
        mv.setViewName("regularNew");
        return mv;
    }

    @RequestMapping(value = "/createSave")
    @ResponseBody
    Result saveRegularCreated(
            @RequestParam(value = "regularName") String regularName,
            @RequestParam(value = "scheduleType") int scheduleType,
            @RequestParam(value = "regularDate") String regularDate,
            @RequestParam(value = "weekDay") String weekDay,
            @RequestParam(value = "regularTime") String regularTime,
            @RequestParam(value = "runRule") int runRule) throws ParseException {
        
        try {
            regularName = regularName.trim();
            if (!regularService.isUniqueByNameId(regularName, -1)) {
                return new Result(true, "名称为 " + regularName + " 的调度规则已存在，请选择其他名称");
            }
            regularService.saveRegular(regularName, scheduleType, regularDate,weekDay, regularTime, runRule);
            return new Result(false, "OK");
        } catch(Exception e)  {
            return new Result(true, e.toString());
        }

    }

}
