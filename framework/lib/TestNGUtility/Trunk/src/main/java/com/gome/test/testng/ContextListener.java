package com.gome.test.testng;

import com.gome.test.Constant;
import com.gome.test.context.ContextUtils;
import com.gome.test.context.IContext;
import com.gome.test.utils.Logger;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class ContextListener extends TestListenerAdapter {

    @Override
    public void onTestStart(ITestResult tr) {
        ISuite suite = tr.getTestContext().getSuite();
        if (ContextUtils.getContext() != null) {
            if (suite != null) {
                Logger.info(" Case %s 开始", tr.getName());
                // 判定为非Ordercase
                if (suite.getParameter(Constant.SKIP_ONCE_FAIL) == null) {
                    ContextUtils.getContext().clear();
                    ContextUtils.addToContext(Constant.IS_ORDER_CASE, "false");
                } else {
                    ContextUtils.addToContext(Constant.IS_ORDER_CASE, "true");
                }
                IContext.testContext.put(Constant.TEST_RESULT_CONTEXT, tr);
            }
        }

        super.onTestStart(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        tr.setAttribute("post",ContextUtils.getContext().get("post"));
        Logger.info("Case %s 已经把请求保存在ITestResult中",tr.getName());
        super.onTestSuccess(tr);
        Logger.info("Case %s 成功结束", tr.getName());
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        tr.setAttribute("post",ContextUtils.getContext().get("post"));
        Logger.info("Case %s 已经把请求保存在ITestResult中",tr.getName());
        super.onTestFailure(tr);
        Logger.info("Case %s 执行失败", tr.getName());
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        Logger.info("Case %s 跳过", tr.getName());
    }

    @Override
    public void onStart(ITestContext testContext) {
        IContext.testContext.put(Constant.TEST_CONTEXT, testContext);
        super.onStart(testContext);
    }
}