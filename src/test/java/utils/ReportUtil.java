package utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import base.BasePage;

public class ReportUtil implements ITestListener {

    private ExtentSparkReporter sparkReporter;
    private ExtentReports extent;
    public static ExtentTest test;
    static WebDriver driver;
    private static Properties prop;
    private String reportName;
    public static Logger logger = LogManager.getLogger(ReportUtil.class);

    public static void setProperties(Properties properties) {
        prop = properties;
    }

    public static void setDriver(WebDriver driver) {
        ReportUtil.driver = driver;
    }

    public static String addStepLog(Status status, String message) {
        if (test != null) {
            test.log(status, message);
        }
        return message;
    }

    @Override
    public void onStart(ITestContext testContext) {
        createReport(testContext);
        logger.info("Test execution started for: {}", testContext.getSuite().getName());
    }

    private void createReport(ITestContext testContext) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportName = "Test-Report-" + timeStamp + ".html";
        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "\\reports\\" + reportName);
        
        sparkReporter.config().setDocumentTitle("OpenCart Automation Report");
        sparkReporter.config().setReportName("OpenCart Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        setSystemInfo();
        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }

    private void setSystemInfo() {
        extent.setSystemInfo("Application", "OpenCart");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Sub Module", "Customers");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA");

        if (prop != null) {
            String os = prop.getProperty("os", System.getProperty("os.name"));
            extent.setSystemInfo("Operating System", os);

            String browser = prop.getProperty("browser", System.getProperty("browser"));
            extent.setSystemInfo("Browser", browser);
        } else {
            logger.warn("Properties not set in ReportUtil. Please set it before running tests.");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getName());
        logger.info("Test '{}' started running.", result.getName());
        test.log(Status.INFO, result.getName() + " TEST EXECUTION STARTED.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.PASS, result.getName() + " TEST GOT PASSED.");
        logger.info("Test '{}' PASSED.", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.FAIL, result.getName() + " TEST GOT FAILED. " + result.getThrowable().getMessage());
        
        captureScreenshot(result);
        logger.error("Test '{}' failed after all retry attempts.", result.getName());
    }

    private void captureScreenshot(ITestResult result) {
        if (driver != null) {
            try {
                String imgPath = new BasePage(driver).captureScreenshot(result.getName());
                test.addScreenCaptureFromPath(imgPath);
            } catch (IOException e) {
                logger.error("Error capturing screenshot for test '{}': {}", result.getName(), e.getMessage());
            }
        } else {
            test.log(Status.WARNING, "WebDriver is not initialized. Cannot capture screenshot.");
            logger.warn("Screenshot not taken: WebDriver is null.");
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        extent.flush();
        openReport();
        logger.info("Test execution finished for: {}", testContext.getSuite().getName());
    }

    private void openReport() {
        String pathOfExtentReport = System.getProperty("user.dir") + "\\reports\\" + reportName;
        File extentReport = new File(pathOfExtentReport);

        try {
            Desktop.getDesktop().browse(extentReport.toURI());
        } catch (IOException e) {
            logger.error("Failed to open report: {}", e.getMessage());
        }
    }
}