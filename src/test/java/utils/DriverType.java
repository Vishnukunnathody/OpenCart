package utils;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverType {
    private static final Logger logger = LogManager.getLogger(DriverType.class);

    // ThreadLocal to store WebDriver instance for each thread (test)
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private DriverType() {}

    // Initialize the WebDriver based on the browser name
    public static void initializeDriver(String browserName) {
        WebDriver driver = null;
        try {
            switch (browserName.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    logger.info("Browser :: Chrome initialized.");
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    logger.info("Browser :: Edge initialized.");
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    logger.info("Browser :: Firefox initialized.");
                    break;
                default:
                    logger.error("Invalid browser name specified: {}", browserName);
                    throw new IllegalArgumentException("Invalid browser name specified.");
            }
            // Apply configurations
            configureDriver(driver);
            // Set the driver for this thread
            tlDriver.set(driver);
        } catch (Exception e) {
            logger.error("Error initializing WebDriver for browser: {}", browserName, e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    // Centralized method to configure WebDriver settings
    private static void configureDriver(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(1920, 1080)); // Set the window size
        driver.manage().deleteAllCookies(); // Clear cookies for fresh session
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30)); // Set page load timeout
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Set implicit wait
    }

    // Method to get the WebDriver instance for the current thread (with browser name)
    public static WebDriver getDriver(String browserName) {
        // Initialize the driver for the first time for this thread
        if (tlDriver.get() == null) {
            initializeDriver(browserName);
        }
        return tlDriver.get();
    }

    // Method to get the WebDriver instance (only when it's already initialized)
    public static WebDriver getDriver() {
        WebDriver driver = tlDriver.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized for the current thread.");
        }
        return driver;
    }

    // Method to remove the driver instance from the ThreadLocal after the test
    public static void removeDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();  // Quit the WebDriver and close the browser
            tlDriver.remove();  // Remove the driver from the current thread
            logger.info("Driver has been closed and removed.");
        }
    }
}