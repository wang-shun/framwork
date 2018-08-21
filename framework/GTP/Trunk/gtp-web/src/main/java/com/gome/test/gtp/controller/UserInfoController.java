package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.SHAEncryptService;
import com.gome.test.gtp.bo.UserInfoService;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.TableResult;
import com.gome.test.gtp.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by lizonglin on 2015/6/4.
 */
@Controller
@RequestMapping(value = "/account")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SHAEncryptService shaEncryptService;
    @RequestMapping(value = "")
    ModelAndView goAccount () {
        ModelAndView mv = new ModelAndView("account");
        return mv;
    }

    @RequestMapping(value = "/all")
    @ResponseBody
    TableResult getAll () {
        try {
            List<UserInfo> userInfoList = userInfoService.getAll();
            return new TableResult(false, 1, userInfoList.size(), userInfoList.size(),userInfoList.toArray());
        } catch (Exception e) {
            return new TableResult(true, e.toString());
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    Result resetPassword (
            @RequestParam(value = "id") int id,
            @RequestParam(value = "password") String password
    ) {
        try {
            String encryptPassword = shaEncryptService.encryptPwd(password);
            UserInfo userInfo = userInfoService.get(id);
            userInfo.setPassword(encryptPassword);
            userInfoService.update(userInfo);
            return new Result(false,"Reset password successfully");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }

    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    Result delUser (@PathVariable(value = "id") int id) {
        try {
            userInfoService.delete(id);
            return new Result(false, "Delete user successfully");
        } catch (Exception e) {
            return new Result(false, e.toString());
        }
    }

    @RequestMapping(value = "/setAdmin/{id}/{admin}")
    @ResponseBody
    Result setAdmin(@PathVariable(value = "id") int id, @PathVariable(value = "admin") boolean admin) {
        try {
            UserInfo userInfo = userInfoService.get(id);
            String responseStr;
            if(admin) {
                userInfo.setIsAdmin(true);
                responseStr = "Set role to admin successfully";
            } else {
                userInfo.setIsAdmin(false);
                responseStr = "Set role to user successfully";
            }
            userInfoService.update(userInfo);
            return new Result(false, responseStr);
        } catch (Exception e) {
            return new Result(true, e.toString());
        }
    }
}
