package com.gome.test.gtp.controller;

import com.gome.test.gtp.bo.JobService;
import com.gome.test.gtp.model.Health;
import com.gome.test.gtp.model.Result;
import com.gome.test.utils.StringUtils;
import org.apache.log4j.spi.LoggerFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhangjiadi on 16/7/14.
 */
@Controller
@RequestMapping(value = "/health")
public class HealthController {

    @Autowired
    private Environment env;
    @Autowired
    private JobService jobService;

    final Logger logger = org.slf4j.LoggerFactory.getLogger(HealthController.class);

    @RequestMapping(value = "/view")
    @ResponseBody
    public ModelAndView healthView() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("health");
        return mv;
    }


    private String getReaderParm(HttpServletRequest request)
    {
        StringBuffer sb = new StringBuffer();
        try {
            request.setCharacterEncoding("UTF-8");

            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return sb.toString();

    }


    @RequestMapping(value = "/do", method = RequestMethod.POST)
    @ResponseBody
    public Result save(HttpServletRequest request, HttpServletResponse response) throws Exception {


        Result result = new Result();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String jsonStr = getReaderParm(request);

        if (StringUtils.isEmpty(jsonStr)) {
            result.setIsError(true);
            result.setMessage("解析json发生异常");
            return result;
        }

        try {
            JSONObject jobj = new JSONObject(jsonStr);
            Health health = new Health();
            Field[] field = Health.class.getDeclaredFields();

            //String key = jobj.getString("sign"); //没加密

            for (Field f : field) {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), Health.class);
                Method wM = pd.getWriteMethod();
                String name = f.getName();
                if (f.getType() == String.class && jobj.has(name)) {
                    wM.invoke(health, jobj.get(name).toString());
                }
            }
            result = jobService.insertResponse(health);

        } catch (Exception ex) {
            result.setIsError(true);
            result.setMessage("接口调用异常，原因：" + ex.getMessage());
        }
        return result;

    }


    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public Result detail(HttpServletRequest request, HttpServletResponse response) throws Exception {


        Result result=new Result();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String jsonStr= getReaderParm(request);
        if(StringUtils.isEmpty(jsonStr))
        {
            result.setIsError(true);
            result.setMessage("解析json发生异常");
            return result;
        }

        try {
            JSONObject jobj = new JSONObject(jsonStr);
            if(jobj.has("objectTime")) {
                String objectTime = jobj.get("objectTime").toString();
                result= jobService.searchResponse(objectTime);
            }else
            {
                logger.error("解析json发生异常 ： "+ jsonStr);
                result.setIsError(true);
                result.setMessage("解析json发生异常");
            }

        }catch (Exception ex)
        {
            result.setIsError(true);
            result.setMessage("接口调用异常，原因："+ex.getMessage());
        }
        return result;

    }


}
