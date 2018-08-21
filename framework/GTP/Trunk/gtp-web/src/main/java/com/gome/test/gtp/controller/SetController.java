package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.SHAEncryptService;
import com.gome.test.gtp.bo.SessionService;
import com.gome.test.gtp.bo.UserInfoService;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.UserInfo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by lizonglin on 2015/6/4.
 */
@Controller
public class SetController {
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SHAEncryptService shaEncryptService;
    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "/set_userinfo")
    ModelAndView goSetUserInfo () {
        ModelAndView mv = new ModelAndView("set_userinfo");
        mv.addObject("groups",lcdService.getTypeList(Constant.GROUP));
        return mv;
    }
    @RequestMapping(value = "/set_userinfo/update")
    @ResponseBody
    Result updateUserInfo (
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "group") String group,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "change_password") boolean change_password
    ) {
        try {
            UserInfo userInfo = userInfoService.getUserInfoByUsername(username);
            if (change_password) {
                userInfo.setPassword(shaEncryptService.encryptPwd(password));
            }
            if (!group.equals(userInfo.getLastGroup())) {
                userInfo.setLastGroup(group);
                userInfo.setLastChangeGroup(Util.nowTimestamp());
            }
            userInfo.setEmail(email);
            userInfoService.update(userInfo);
            UserInfo updatedUserInfo = userInfoService.getUserInfoByUsername(username);
            sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            sessionService.setSessionGroup(updatedUserInfo.getLastGroup());
            sessionService.setSessionEmail(updatedUserInfo.getEmail());
            return new Result(false, "用户信息已更新成功");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }
}
