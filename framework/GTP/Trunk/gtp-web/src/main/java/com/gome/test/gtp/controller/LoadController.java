package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.LoadConfService;
import com.gome.test.gtp.bo.LoadSceneService;
import com.gome.test.gtp.bo.SessionService;
import com.gome.test.gtp.dao.LoadConfigureDictionaryService;
import com.gome.test.gtp.dao.SvnBranchInfoDao;
import com.gome.test.gtp.model.LoadConf;
import com.gome.test.gtp.model.LoadScene;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.model.TableResult;
import com.gome.test.gtp.svn.SvnBo;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by lizonglin on 2015/7/20/0020.
 */
@Controller
@RequestMapping("/load")
public class LoadController {

    @Autowired
    private LoadConfService loadConfService;
    @Autowired
    private LoadSceneService loadSceneService;
    @Autowired
    private LoadConfigureDictionaryService lcdService;
    @Autowired
    private SvnBranchInfoDao svnBranchInfoDao;
    @Autowired
    private SvnBo svnBo;
    @Autowired
    private SessionService sessionService;

    //=====================================================Load Conf=====================================================

    @RequestMapping("/conf")
    ModelAndView goConfPage() {
        ModelAndView mv = new ModelAndView("loadConf");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/conf/all")
    TableResult getAllLoad() {
        try {
            List loadList = loadConfService.getAllLoadConf();
            if (loadList != null) {
                return new TableResult(false, 1, loadList.size(), loadList.size(), loadList.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception e) {
            return new TableResult(true, e.toString());
        }
    }

    @RequestMapping("/conf/new")
    ModelAndView goEditPage() {
        ModelAndView mv = new ModelAndView("loadConf_new");
        mv.addObject("sceneList", loadSceneService.getAllSceneName());
        mv.addObject("envList", lcdService.getTypeList(Constant.ENV));
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/conf/newSave", method = RequestMethod.POST)
    Result saveNewLoad(
            @RequestParam("name") String name,
            @RequestParam("scriptSvn") String scriptSvn,
            @RequestParam("jmxContent") String jmxContent,
            @RequestParam("scene") String scene,
            @RequestParam("sourceSvn") String sourceSvn,
            @RequestParam("env") String env
    ) {
        try {
            if (!loadConfService.isUniqueByNameId(name, -1)) {
                return new Result(true, "名称为 " + name + " 的LoadConf已存在，请选择其他名称");
            }
            LoadConf loadConf = new LoadConf();
            loadConf.setName(name);
            loadConf.setScriptSvn(scriptSvn);
            loadConf.setJmxContent(jmxContent);
            loadConf.setSceneName(scene);
            loadConf.setSourceSvn(sourceSvn);
            loadConf.setEnv(env);
            loadConf.setLastUpdateTime(Util.nowTimestamp());
            loadConfService.save(loadConf);
            return new Result(false, "LoadConf已成功创建");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }

    }

    @RequestMapping("/conf/edit/{id}")
    ModelAndView goEditPage(@PathVariable("id") int id) throws Exception {
        ModelAndView mv = new ModelAndView("loadConf_edit");
        LoadConf loadConf = loadConfService.get(id);

        mv.addObject("id", loadConf.getId());
        mv.addObject("name", loadConf.getName());
        mv.addObject("scriptSvnList", loadConf.getScriptSvn().split(","));
        mv.addObject("jmxContent", loadConf.getJmxContent());
        mv.addObject("sceneList", loadSceneService.getAllSceneName());
        mv.addObject("sceneName", loadConf.getSceneName());
        mv.addObject("sourceSvnList", loadConf.getSourceSvn().split(","));
        mv.addObject("envList", lcdService.getValueNameList(Constant.ENV));
        mv.addObject("env", loadConf.getEnv());

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/conf/editSave", method = RequestMethod.POST)
    Result saveEditLoad(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("scriptSvn") String scriptSvn,
            @RequestParam("jmxContent") String jmxContent,
            @RequestParam("scene") String scene,
            @RequestParam("sourceSvn") String sourceSvn,
            @RequestParam("env") String env
    ) {
        try {
            if (!loadConfService.isUniqueByNameId(name, id)) {
                return new Result(true, "名称为 " + name + " 的LoadConf已存在，请选择其他名称");
            }
            LoadConf loadConf = loadConfService.get(id);
            String oldName = loadConf.getName();
            loadConf.setName(name);
            loadConf.setScriptSvn(scriptSvn);
            loadConf.setJmxContent(jmxContent);
            loadConf.setSceneName(scene);
            loadConf.setSourceSvn(sourceSvn);
            loadConf.setEnv(env);
            loadConf.setLastUpdateTime(Util.nowTimestamp());
            loadConfService.linkedUpdate(loadConf, oldName);
            return new Result(false, "LoadConf已成功更新");
        } catch (Exception e) {
            return new Result(true, e.toString());
        }

    }

    @ResponseBody
    @RequestMapping("/conf/del/{id}/{name}")
    Result delLoadConf(@PathVariable("id") int id,
                       @PathVariable("name") String name) {
        try {
            loadConfService.safeDelLoadConf(id, name);
            return new Result(false,"删除ID为：" + id + "的LoadConf成功！");
        } catch (Exception ex) {
            return new Result(true,ex.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/conf/detail/{id}")
    Result getConfDetailById(@PathVariable("id") int id) {
        try {
            return new Result(false, "ID为" + id + "的LoadConfDetail", loadConfService.get(id));
        } catch (Exception ex) {
            return new Result(true, ex.getMessage());
        }
    }

//=====================================================Load Scene=====================================================

    @RequestMapping("/scene")
    ModelAndView goScenePage() {
        ModelAndView mv = new ModelAndView("loadScene");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/scene/all")
    TableResult getAllScene() {
        try {
            List loadList = loadSceneService.getAllScene();
            if (loadList != null) {
                return new TableResult(false, 1, loadList.size(), loadList.size(), loadList.toArray());
            } else {
                return new TableResult();
            }
        } catch (Exception e) {
            return new TableResult(true, e.toString());
        }
    }

    @RequestMapping("/scene/new")
    ModelAndView goLoadSceneNew() {
        ModelAndView mv = new ModelAndView("loadScene_new");
        mv.addObject("onErrorList", lcdService.getValueNameList(Constant.LOAD_ON_ERROR));
        sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        mv.addObject("isAdmin", sessionService.getSessionIsAdmin());
        return mv;
    }

    @ResponseBody
    @RequestMapping("/scene/newSave")
    Result saveNewScene(
            @RequestParam("name") String name,
            @RequestParam("onError") String onError,
            @RequestParam("threadNum") int threadNum,
            @RequestParam("initDelay") int initDelay,
            @RequestParam("startCount") int startCount,
            @RequestParam("startCountBurst") int startCountBurst,
            @RequestParam("startPeriod") int startPeriod,
            @RequestParam("stopCount") int stopCount,
            @RequestParam("stopPeriod") int stopPeriod,
            @RequestParam("flightTime") int flightTime,
            @RequestParam("rampUp") int rampUp,
            @RequestParam("isTemplate") boolean isTemplate,
            @RequestParam("isTest") boolean isTest
    ) {
        try {
            if (!loadSceneService.isUniqueByNameId(name, -1)) {
                return new Result(true, "'" + name + "Name ' has been existed, please choose another one!");
            }
            LoadScene loadScene = new LoadScene();
            loadScene.setName(name);
            loadScene.setOnError(onError);
            loadScene.setThreadNum(threadNum);
            loadScene.setInitDelay(initDelay);
            loadScene.setStartCount(startCount);
            loadScene.setStartCountBurst(startCountBurst);
            loadScene.setStartPeriod(startPeriod);
            loadScene.setStopCount(stopCount);
            loadScene.setStopPeriod(stopPeriod);
            loadScene.setFlightTime(flightTime);
            loadScene.setRampUp(rampUp);
            loadScene.setTemplate(isTemplate);
            loadScene.setTest(isTest);
            loadScene.setLastUpdateTime(Util.nowTimestamp());

            loadSceneService.save(loadScene);
            return new Result(false,"创建Scene成功");
        } catch (Exception ex) {
            return new Result(true,ex.getMessage());
        }
    }

    @RequestMapping("/scene/edit/{id}")
    ModelAndView goSceneEditPage(@PathVariable("id") int id) {
        LoadScene loadScene = loadSceneService.get(id);
        sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());

        if(!sessionService.getSessionIsAdmin() && (loadScene.isTemplate() || loadScene.isTest()))
            return new ModelAndView("loadScene");
        ModelAndView mv = new ModelAndView("loadScene_edit");
        mv.addObject("onErrorList", lcdService.getValueNameList(Constant.LOAD_ON_ERROR));
        mv.addObject("scene", loadScene);
        sessionService.setRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        mv.addObject("isAdmin", sessionService.getSessionIsAdmin());
        return mv;
    }

    @ResponseBody
    @RequestMapping("/scene/editSave")
    Result saveEditScene(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("onError") String onError,
            @RequestParam("threadNum") int threadNum,
            @RequestParam("initDelay") int initDelay,
            @RequestParam("startCount") int startCount,
            @RequestParam("startCountBurst") int startCountBurst,
            @RequestParam("startPeriod") int startPeriod,
            @RequestParam("stopCount") int stopCount,
            @RequestParam("stopPeriod") int stopPeriod,
            @RequestParam("flightTime") int flightTime,
            @RequestParam("rampUp") int rampUp,
            @RequestParam("isTemplate") boolean isTemplate,
            @RequestParam("isTest") boolean isTest
    ) {
        try {
            if (!loadSceneService.isUniqueByNameId(name, id)) {
                return new Result(true, "Name '" + name + "' has been existed, please choose another one!");
            }
            LoadScene loadScene = loadSceneService.get(id);
            String oldName = loadScene.getName();
            loadScene.setName(name);
            loadScene.setOnError(onError);
            loadScene.setThreadNum(threadNum);
            loadScene.setInitDelay(initDelay);
            loadScene.setStartCount(startCount);
            loadScene.setStartCountBurst(startCountBurst);
            loadScene.setStartPeriod(startPeriod);
            loadScene.setStopCount(stopCount);
            loadScene.setStopPeriod(stopPeriod);
            loadScene.setFlightTime(flightTime);
            loadScene.setRampUp(rampUp);
            loadScene.setTemplate(isTemplate);
            loadScene.setTest(isTest);
            loadScene.setLastUpdateTime(Util.nowTimestamp());

            loadSceneService.linkedUpdate(loadScene, oldName);
            return new Result(false,"更新Scene " + id + " 成功");
        } catch (Exception ex) {
            return new Result(true,ex.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/scene/delete/{id}/{name}")
    Result deleteScene(@PathVariable("id") int id, @PathVariable("name") String name) {
        try {
            loadSceneService.safeDeleteLoadScene(id, name);
            return new Result(false, "删除Scene " + id + " 成功");
        } catch (Exception ex) {
            return new Result(true, ex.getMessage());
        }
    }

}
