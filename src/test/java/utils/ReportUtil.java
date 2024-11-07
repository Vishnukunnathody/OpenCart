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

    static final Logger logger = LogManager.getLogger(ReportUtil.class);

    // Thread-local variables to store instances for each test thread
    private ThreadLocal<ExtentReports> extentReports = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<Properties> prop = new ThreadLocal<>();

    private boolean systemInfoSet = false;  // Flag to set system info once per suite
    private String reportName;

    // Set properties for the report (instance method, non-static)
    public static void setProperties(Properties properties) {
        prop.set(properties);
    }

    // Set the WebDriver for the report capture (instance method, non-static)
    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    // Thread-safe method to add step logs
    public static void addStepLog(Status status, String message) {
        if (getTest() != null) {
            getTest().log(status, message);
        }
    }

    // Thread-local method to access the current test instance
    public static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onStart(ITestContext testContext) {
        createReport(testContext);
        logger.info("Test execution started for: {}", testContext.getSuite().getName());
    }

    private void createReport(ITestContext testContext) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportName = "Test-Report-" + timeStamp + ".html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "\\reports\\" + reportName);

        sparkReporter.config().setDocumentTitle("OpenCart Automation Report");
        sparkReporter.config().setReportName("OpenCart Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extentReports.set(extent);

        if (!systemInfoSet) {
            setSystemInfo(testContext);
            systemInfoSet = true;
        }

        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }

    private void setSystemInfo(ITestContext testContext) {
        ExtentReports extent = extentReports.get();
        extent.setSystemInfo("Application", "OpenCart");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Sub Module", "Customers");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));

        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);

        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);

        String env = testContext.getCurrentXmlTest().getParameter("environment");
        extent.setSystemInfo("Environment", env);
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new ExtentTest instance for the current thread
        ExtentTest testInstance = extentReports.get().createTest(result.getName());
        test.set(testInstance);

        // Capture the browser dynamically for each test
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        if (browser == null) {
            browser = "Unknown Browser";  // Fallback if browser info is missing
        }
        test.get().log(Status.INFO, "Running on Browser: " + browser);
        logger.info("Test '{}' started on browser: {}", result.getName(), browser);
        
        // Log the test start event
        test.get().log(Status.INFO, result.getName() + " TEST EXECUTION STARTED.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().assignCategory(result.getMethod().getGroups());
        test.get().log(Status.PASS, result.getName() + " TEST GOT PASSED.");
        logger.info("Test '{}' PASSED.", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().assignCategory(result.getMethod().getGroups());
        test.get().log(Status.FAIL, result.getName() + " TEST GOT FAILED. " + result.getThrowable().getMessage());

        captureScreenshot(result);
        logger.error("Test '{}' failed.", result.getName());
    }

    private void captureScreenshot(ITestResult result) {
        if (driver.get() != null) {
            try {
                String imgPath = new BasePage(driver.get()).captureScreenshot(result.getName());
                test.get().addScreenCaptureFromPath(imgPath);
            } catch (IOException e) {
                logger.error("Error capturing screenshot for test '{}': {}", result.getName(), e.getMessage());
            }
        } else {
            test.get().log(Status.WARNING, "WebDriver is not initialized. Cannot capture screenshot.");
            logger.warn("Screenshot not taken: WebDriver is null.");
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        // Flush the reports once all tests are done
        extentReports.get().flush();
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