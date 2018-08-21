package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.SHAEncryptService;
import com.gome.test.gtp.bo.UserInfoService;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.UserInfo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;

/**
 * Created by lizonglin on 2015/6/3.
 */
@Controller
@RequestMapping(value = "/signup")
public class SignupController {
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private SHAEncryptService shaEncryptService;
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "")
    public ModelAndView goSignup () {
        ModelAndView mv = new ModelAndView("signup");
        mv.addObject("groups",lcdService.getTypeList(Constant.GROUP));
        return mv;
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Result submitSignup (
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "group") String group,
            @RequestParam(value = "email") String email
    ) {
        if (username.equals("") || password.equals("") || group.equals("None") || group.equals("") || !email.contains("yolo24.com")) {
            return new Result(true, "提交的信息有误，请重新注册");
        }
        try {
            String encryptPassword = shaEncryptService.encryptPwd(password);
            Timestamp now = Util.nowTimestamp();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(username);
            userInfo.setLastGroup(group);
            userInfo.setIsAdmin(false);
            userInfo.setLastChangeGroup(now);
            userInfo.setLastLogin(now);
            userInfo.setPassword(encryptPassword);
            userInfo.setEmail(email);
            userInfoService.save(userInfo);
            return new Result(false, "OK");
        } catch (Exception e) {
            return new Result(true, "注册失败");
        }
    }

    /**
     *
     * @param username
     * @return 如果用户名已存在返回false，不存在返回true
     */
    @RequestMapping(value = "/unique/{username}")
    @ResponseBody
    public boolean uniqueUsername (@PathVariable(value = "username") String username) {
        return userInfoService.uniqueUsername(username);
    }
}
