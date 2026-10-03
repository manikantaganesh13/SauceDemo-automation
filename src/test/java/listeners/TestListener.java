package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportManager;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        utils.ExtentReportManager.initReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName =result.getMethod().getMethodName();
        utils.ExtentReportManager.createTest(testName);
        utils.ExtentReportManager.getTest().info("Test execution started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        utils.ExtentReportManager.getTest().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        utils.ExtentReportManager.getTest().fail("Test Failed");
        utils.ExtentReportManager.getTest().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        utils.ExtentReportManager.getTest().skip("Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.extent.flush();
    }
}