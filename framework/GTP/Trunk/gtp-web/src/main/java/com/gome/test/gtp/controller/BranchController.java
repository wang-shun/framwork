/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.SessionService;
import com.gome.test.gtp.bo.SvnBranchService;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.SvnBranchInfo;
import com.gome.test.gtp.model.TableResult;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;

/**
 * @author Lin.Shi
 */
@Controller
@RequestMapping(value = "/branch")
public class BranchController {
    @Autowired
    private SvnBranchService svnBranchService;
    @Autowired
    private ConfigureDictionaryService cds;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private SessionService sessionService;

    @RequestMapping("")
    ModelAndView goBranchPage() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("ProGroup", cds.getGroups());
        mv.addObject("params", "");
        mv.setViewName("branch");

        return mv;
    }

    @RequestMapping("/search/{params}")
    ModelAndView goBranchPage2( @PathVariable(value = "params") String params){
        ModelAndView mv = new ModelAndView();
        mv.addObject("ProGroup", cds.getGroups());
        mv.addObject("params",params);
        mv.setViewName("branch");
        return mv;
    }


    @RequestMapping(value = "/all")
    @ResponseBody
    TableResult getAllBranchInfo() {
        try {
            List result = svnBranchService.getAllSvnBranchInfo();
            if (result != null) {
                return new TableResult(false, 1, result.size(), result.size(), result.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception ex) {
            return new TableResult(true, ex.toString());
        }
    }

    @RequestMapping(value = "/new")
    ModelAndView createBranch () {
        ModelAndView mv = new ModelAndView("branch_new");
        mv.addObject("groups", cds.getGroups());
        mv.addObject("taskTypeList", cds.getTaskTypes());
        sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        mv.addObject("isAdmin", sessionService.getSessionIsAdmin());
        return mv;
    }

    @RequestMapping(value = "/new/{params}")
    ModelAndView createBranch2(@PathVariable(value = "params") String params) {
        ModelAndView mv = new ModelAndView("branch_new");
        mv.addObject("groups", cds.getGroups());
        mv.addObject("params", params);
        mv.addObject("taskTypeList", cds.getTaskTypes());
        return mv;
    }

    @RequestMapping(value = "/newSave", method = RequestMethod.POST)
    @ResponseBody
    Result createBranchSave (
            @RequestParam(value = "branchName") String branchName,
            @RequestParam(value = "branchUrl") String branchUrl,
            @RequestParam(value = "group") String group,
            @RequestParam(value = "expireDate") String expireDate,
            @RequestParam(value = "type") String type) throws ParseException {
        try {
            branchName = branchName.trim();
            if (!svnBranchService.isUniqueByNameId(branchName, -1)) {
                return new Result(true, "所用SVN名称重复，请选择其他名称");
            }
            SvnBranchInfo svnBranchInfo = new SvnBranchInfo();
            svnBranchInfo.setBranchName(branchName);
            svnBranchInfo.setBranchUrl(branchUrl);
            svnBranchInfo.setBusinessGroup(lcdService.getValueByName(Constant.GROUP, group));
            svnBranchInfo.setExpireDate(Util.string2Timestamp(expireDate));
            svnBranchInfo.setType(lcdService.getValueByName(Constant.DIC_TASK_TYPE, type));
            svnBranchService.save(svnBranchInfo);
            return new Result(false, "SVN已成功创建");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/edit/{id}")
    ModelAndView getEditBranch (
            @PathVariable(value = "id") int id
    ) {
        ModelAndView mv = new ModelAndView("branch_edit");
        Object svnBranch = svnBranchService.getSvnBranchById(id);
        mv.addObject("svnBranch",svnBranch);
        mv.addObject("groupList",cds.getGroups());
        mv.addObject("taskTypeList", cds.getTaskTypes());
        sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        mv.addObject("isAdmin", sessionService.getSessionIsAdmin());
        return mv;
    }

    @RequestMapping(value = "/editSave", method = RequestMethod.POST)
    @ResponseBody
    Result saveEditBranch (
            @RequestParam(value = "branchId") int branchId,
            @RequestParam(value = "branchName") String branchName,
            @RequestParam(value = "branchUrl") String branchUrl,
            @RequestParam(value = "group") String group,
            @RequestParam(value = "expireDate") String expireDate,
            @RequestParam(value = "type") String type
    ) throws ParseException {
        try {
            branchName = branchName.trim();
            if (!svnBranchService.isUniqueByNameId(branchName, branchId)) {
                return new Result(true, "所用SVN名称重复，请选择其他名称");
            }
            SvnBranchInfo svnBranchInfo = svnBranchService.get(branchId);
            svnBranchInfo.setBranchName(branchName);
            svnBranchInfo.setBranchUrl(branchUrl);
            svnBranchInfo.setBusinessGroup(lcdService.getValueByName(Constant.GROUP, group));
            svnBranchInfo.setExpireDate(Util.string2Timestamp(expireDate));
            svnBranchInfo.setType(lcdService.getValueByName(Constant.DIC_TASK_TYPE, type));
            svnBranchService.save(svnBranchInfo);
            return new Result(false, "SVN已成功更新");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }

    }

    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    Result getBranchDetail (
            @PathVariable(value = "id") int id
    ) {
        try {
            return new Result(false, String.valueOf(id),svnBranchService.getSvnBranchById(id));
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    Result delBranch (
            @PathVariable(value = "id") int id
    ) {
        try {
            return new Result(false, String.format("%d 已删除 !", id), svnBranchService.safeDelBranchById(id));
        } catch (Exception e) {
            return new Result(true, "删除失败 " + e.getMessage());
        }
    }
}
