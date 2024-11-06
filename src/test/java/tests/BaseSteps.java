package tests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.annotations.AfterMethod;
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
    public static WebDriver driver;
    public Properties prop;

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
    public void initialize() {
        driver = DriverType.getDriverType(prop);
        WebEventListener eventListener = new WebEventListener();
        EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(eventListener);
        driver = decorator.decorate(driver);
        
        if (driver != null) {
        	EnvironmentType.setEnvType(driver, prop);
            ReportUtil.setDriver(driver);
        } else {
            System.err.println("WebDriver initialization failed. Please check browser configuration.");
        }
    }
        
 
    @AfterMethod(groups = {"Sanity", "Regression", "Master", "DataDriven", "test"})
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}