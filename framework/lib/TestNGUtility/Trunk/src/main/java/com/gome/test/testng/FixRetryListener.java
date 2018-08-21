package com.gome.test.testng;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.*;

/**
 * Created by lizonglin on 2016/3/7/0007.
 */
@Deprecated
public class FixRetryListener extends TestListenerAdapter {
    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        //将要删除的结果
        List<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();

        //所有通过的测试的ID
        Set<Integer> passedTestIds = new HashSet<Integer>();
        for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
            passedTestIds.add(getId(passedTest));
        }

        //仅保留一次失败的测试
        Set<Integer> failedTestIds = new HashSet<Integer>();
        for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
            int failedTestId = getId(failedTest);
            //<1>失败结果集中已包含此失败的ID
            //<2>成功的结果集中包含此失败的ID(重跑成功的)
            //标记这些测试的ID，统一从失败结果集中删除
            if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
                testsToBeRemoved.add(failedTest);
            } else {
                failedTestIds.add(failedTestId);
            }
        }
        //统一从失败结果集中删除标记的失败用例
        for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator.hasNext(); ) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                iterator.remove();
            }
        }
    }

    public static int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = 31 * id + result.getMethod().getMethodName().hashCode();
        id = 31 * id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }
}
