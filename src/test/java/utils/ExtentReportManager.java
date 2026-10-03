package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    public static ExtentReports extent;

    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static void initReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("Reports/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        spark.config().setDocumentTitle("Report");
        spark.config().setTheme(Theme.DARK);
    }

    public static void createTest(String testName) {
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}