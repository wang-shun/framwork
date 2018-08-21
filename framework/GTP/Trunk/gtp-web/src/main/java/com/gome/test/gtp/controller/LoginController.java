/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.ConfigureDictionaryService;
import com.gome.test.gtp.bo.SHAEncryptService;
import com.gome.test.gtp.bo.SessionService;
import com.gome.test.gtp.bo.UserInfoService;
import com.gome.test.gtp.model.UserInfo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Zonglin.Li
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {
    @Autowired
    private ConfigureDictionaryService cdService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SHAEncryptService shaEncryptService;
    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "")
    ModelAndView goLoginPage() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/cookie")
    @ResponseBody
    boolean cookieLogin(@CookieValue(value = "gtp-login-username", defaultValue = "username") String username,
                         @CookieValue(value = "gtp-login-password", defaultValue = "password") String password) {
        if (!userInfoService.uniqueUsername(username) && shaEncryptService.validatePwd(password, userInfoService.getEncryptPwByUsername(username))) {
            UserInfo userInfo = userInfoService.getUserInfoByUsername(username);
            userInfo.setLastLogin(Util.nowTimestamp());
            userInfoService.update(userInfo);
            sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            sessionService.setSessionUsername(username);
            sessionService.setSessionIsAdmin(userInfo.getIsAdmin());
            sessionService.setSessionEmail(userInfo.getEmail());
            sessionService.setSessionGroup(userInfo.getLastGroup());
            sessionService.setSessionLogin(true);
            sessionService.setSessionTimeOut(Constant.SESSION_TIMEOUT);
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "/valid", method = RequestMethod.POST)
    @ResponseBody
    public boolean validUser (
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        if (shaEncryptService.validatePwd(password, userInfoService.getEncryptPwByUsername(username))) {
            UserInfo userInfo = userInfoService.getUserInfoByUsername(username);
            userInfo.setLastLogin(Util.nowTimestamp());
            userInfoService.update(userInfo);
            sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            sessionService.setSessionUsername(username);
            sessionService.setSessionIsAdmin(userInfo.getIsAdmin());
            sessionService.setSessionEmail(userInfo.getEmail());
            sessionService.setSessionGroup(userInfo.getLastGroup());
            sessionService.setSessionLogin(true);
            sessionService.setSessionTimeOut(Constant.SESSION_TIMEOUT);
            return true;
        } else {
            return false;
        }

    }

    @RequestMapping(value = "/isLogin")
    @ResponseBody
    public boolean isLogin () {
        sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        if (sessionService.getRequest().getSession().getAttribute("login") == null || sessionService.getSessionLogin() == false) {
            return false;
        } else {
            return true;
        }

    }

    @RequestMapping(value = "/isAdmin")
    @ResponseBody
    public boolean isAdmin () {
        sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        if (sessionService.getRequest().getSession().getAttribute("login") == null || sessionService.getSessionLogin() == false
                || sessionService.getRequest().getSession().getAttribute("isAdmin") == null || sessionService.getSessionIsAdmin() == false) {
            return false;
        } else {
            return true;
        }
    }
}
