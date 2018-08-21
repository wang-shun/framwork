/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.dao.AppInfoDao;
import com.gome.test.gtp.dao.CaseCategoryInfoDao;
import com.gome.test.gtp.dao.JobDao;
import java.util.List;

import com.gome.test.gtp.dao.ResponseDao;
import com.gome.test.gtp.model.AppInfo;
import com.gome.test.gtp.model.Health;
import com.gome.test.gtp.model.ResponseInfo;
import com.gome.test.gtp.model.Result;
import com.gome.test.gtp.schedule.JobServiceBo;
import com.gome.test.gtp.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zonglin.Li
 */
@Service
public class JobService {
    @Autowired
    private JobDao jobDao;

    @Autowired
    private ResponseDao responseDao;

    @Autowired
    private AppInfoDao appInfoDao;

    @Autowired
    private CaseCategoryInfoDao caseCategoryInfoDao;


    
    public List getAllJobs() throws Exception{
        List list = jobDao.getAll();
        return list;
        //scheduleJobDao.pagedQuery(hql, pageNo, pageSize, values)
    }

    public Result searchResponse(String objectTime)
    {
        Result result = new Result();
        try {
            List<ResponseInfo> responseInfos = getResponseByTime(objectTime);
            if(responseInfos==null )
            {
                result.setIsError(true);
                result.setMessage("该数据不存在");
                //data using for job current status.
                result.setData(0);
                return result;
            }
            if(responseInfos.size()!=1)
            {
                result.setIsError(true);
                result.setMessage("该数据为非法数据");
                return result;
            }

            result.setIsError(false);
            result.setMessage(JobServiceBo.getResponseStatus(responseInfos.get(0).getStatus()));
            //set db status.
            result.setData(responseInfos.get(0).getStatus());


        }catch (Exception ex)
        {
            result.setIsError(true);
            result.setMessage("查询该数据发生异常，原因："+ex.getMessage());
        }
        return result;
    }




    public Result insertResponse(Health health) {
        Result result = new Result();

        try {
            //判断appinfo是否存在
            List<AppInfo> list = getAppinfoByName(health.getAppName());
            if (list == null || list.size() == 0) {
                result.setIsError(true);
                result.setMessage("appInfoName 不存在");
                return result;
            }

            //判断level是否存在
            if (!haCaseCategoryInfo(health.getLevel())) {

                result.setIsError(true);
                result.setMessage("level 不存在");
                return result;
            }




            List<ResponseInfo> responseInfos = getResponseByTime(health.getObjectTime());
            if (responseInfos != null && responseInfos.size() > 0) {
                result.setIsError(true);
                result.setMessage("该数据已经在队列中，请不要重复提交");
                return result;
            }


            ResponseInfo responseInfo = new ResponseInfo();
            responseInfo.setAppName(health.getAppName());
            responseInfo.setAppInfoId(list.get(0).getId());
            responseInfo.setEnv(health.getEnv());
            responseInfo.setVersion(health.getVersion());
            responseInfo.setEnv(health.getEnv());
            responseInfo.setIp(health.getIp());
            responseInfo.setLevel(health.getLevel());
            responseInfo.setObjectTime(health.getObjectTime());
            responseInfo.setStatus(Constant.JOB_Initialization);
            responseInfo.setPort(health.getPort());
            responseInfo.setReplaceHost(health.getReplaceHost().equals("1"));

            responseDao.save(responseInfo);
            result.setIsError(false);
            result.setMessage("接受数据成功");

        } catch (Exception ex) {
            result.setIsError(false);
            result.setMessage("保存数据发生异常,原因：" + ex.getMessage());
        }
        return result;
    }


    private List<AppInfo> getAppinfoByName(String appName)
    {
        String sql = String.format(" select * from appInfo where appName='%s' order by updateTime desc ", appName);
        return responseDao.sqlQuery(sql, AppInfo.class);
    }

    private List<ResponseInfo> getResponseByTime(String objectTime)
    {
        String sql = String.format(" select * from responseInfo where objectTime='%s'  ", objectTime);
        return responseDao.sqlQuery(sql, ResponseInfo.class);
    }


    private boolean haCaseCategoryInfo(String level)
    {
        String sql = String.format(" select count(0) from casecategoryinfo where id=%s ", level);
        return (Integer.valueOf(responseDao.sqlQuery(sql).get(0).toString()))> 0;

    }

    private List<AppInfo> getAppinfoByid(int appId)
    {
        String sql = String.format(" select * from appInfo where id=%d ", appId);
        return responseDao.sqlQuery(sql, AppInfo.class);
    }



}
