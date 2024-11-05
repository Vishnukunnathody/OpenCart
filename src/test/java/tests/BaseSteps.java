package tests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.annotations.AfterMethod;

import base.BasePage;
import pagesobjects.AccountLogoutPage;
import pagesobjects.AccountSuccessPage;
import pagesobjects.ChangePasswordPage;
import pagesobjects.ForgotPasswordPage;
import pagesobjects.LandingPage;
import pagesobjects.LoginPage;
import pagesobjects.RegisterPage;
import utils.DriverType;
import utils.EnvironmentType;
import utils.ReportUtil;
import utils.WebEventListener;


public class BaseSteps  {
	public static WebDriver driver;
    public Properties prop;

    LandingPage landingPage;
    RegisterPage registerPage;
    AccountSuccessPage accountSuccessPage;
    LoginPage loginPage;
    ForgotPasswordPage forgotPasswordPage;
    AccountLogoutPage accountLogoutPage;
    ChangePasswordPage changePasswordPage;
     BasePage basepage;
    public BaseSteps() {
        loadProperties();
    }

    private void loadProperties() {
        try {
            prop = new Properties();
            File propfile = new File(System.getProperty("user.dir") + "/src/test/resources/projectdata.properties");
            FileReader fr = new FileReader(propfile);
            prop.load(fr);
            ReportUtil.setProperties(prop);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
            e.printStackTrace();
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

    @AfterMethod(groups = { "Sanity", "Regression", "Master", "DataDriven", "test" })
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}