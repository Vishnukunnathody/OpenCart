/*Author: Vishnu Kunnathody
 * Initializes and managing the WebDriver instance,loading properties, and handling the common pages used in tests. 
 * 
 * This class extended to all other test classes.
 * The webDriver declared as driver initialize with the browser specified in DriverType class 
 * taking the parameter from the @before method .
 * Loads the properties when the BaseSteps constructor is called from the Respective test class.
 * Also initialises WebEventListener and EventFiringDecorator.
 * Configures the environment based on properties and the specified environment.
 * Close the Browser @Aftermethod .
 * 
 */


package tests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import base.BasePage;
import pagesobjects.*;
import utils.DriverType;
import utils.EnvironmentType;
import utils.ReportUtil;
import utils.WebEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseSteps {
    private static final Logger logger = LogManager.getLogger(BaseSteps.class);
    protected Properties prop;

    protected WebDriver driver;
    protected LandingPage landingPage;
    protected RegisterPage registerPage;
    protected AccountSuccessPage accountSuccessPage;
    protected LoginPage loginPage;
    protected ForgotPasswordPage forgotPasswordPage;
    protected AccountLogoutPage accountLogoutPage;
    protected ChangePasswordPage changePasswordPage;
    protected BasePage basePage;

    public BaseSteps() {
        loadProperties();
    }

    private void loadProperties() {
        try {
            prop = new Properties();
            File propFile = new File(System.getProperty("user.dir") + "/src/test/resources/projectdata.properties");
            try (FileReader fr = new FileReader(propFile)) {
                prop.load(fr);
            }
            ReportUtil.setProperties(prop);
        } catch (IOException e) {
            logger.error("Error loading properties file: " + e.getMessage(), e);
        }
    }

   
    public void initialize(String browser, String environment) {
        // Initialize the driver for this specific test
        driver = DriverType.getDriver(browser); // Retrieve the driver instance

        if (driver != null) {
            WebEventListener eventListener = new WebEventListener();
            EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(eventListener);
            driver = decorator.decorate(driver);

            EnvironmentType.setEnvType(driver, prop, environment);
            ReportUtil.setDriver(driver);
        } else {
            logger.error("WebDriver initialization failed. Please check browser configuration.");
            throw new IllegalStateException("WebDriver initialization failed.");
        }
    }

    @AfterMethod(groups = { "Sanity", "Regression", "Master", "DataDriven", "test" })
    public void tearDown() {
        // Close and clean up the WebDriver after the test
        if (driver != null) {
            driver.quit();
            DriverType.removeDriver();  // Clean up the driver instance from ThreadLocal
        }
    }
}