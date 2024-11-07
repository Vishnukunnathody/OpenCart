package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pagesobjects.LandingPage;
import pagesobjects.LoginPage;
import pagesobjects.AccountSuccessPage;
import pagesobjects.AccountLogoutPage;

public class LogoutTest extends BaseSteps {

    public LogoutTest() {
        super();  // Calls the BaseSteps constructor to initialize properties
    }

    @BeforeMethod(groups = {"Sanity", "Regression", "Master", "DataDriven", "test"})
    @Parameters({"browser", "environment"})
    public void setUp(String browser, String environment) {
        // Initialize WebDriver and environment setup
        initialize(browser, environment);

        // Navigate to Login page after opening the Landing page
        loginPage = new LandingPage(driver).navigateToLoginPage();
        
        // Perform login using valid credentials from the properties file
        accountSuccessPage = loginPage.performLogin(prop.getProperty("validemail"), prop.getProperty("password"));
    }

    @Test(priority = 1, groups = {"Regression", "Master"})
    public void verifyLogoutFromMainMenuDropdown_Test() {
        // Perform logout from the main menu dropdown
        accountLogoutPage = accountSuccessPage.selectLogoutOptionFromMainMenu();

        // Assert that the login option is displayed after logout
        Assert.assertTrue(accountLogoutPage.isLoginOptionDisplayed(), "Logout was not successful; login option is still displayed.");
    }

    @Test(priority = 2, groups = {"Regression", "Master"})
    public void verifyLogoutFromRightColumnOption_Test() {
        // Perform logout from the right column option (sidebar)
        accountLogoutPage = accountSuccessPage.clickSidebarLogoutLink();

        // Assert that the login option is displayed after logout
        Assert.assertTrue(accountLogoutPage.isLoginOptionDisplayed(), "Logout was not successful; login option is still displayed.");
    }
    
}