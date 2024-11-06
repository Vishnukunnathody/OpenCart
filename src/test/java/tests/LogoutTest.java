package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pagesobjects.LandingPage;

public class LogoutTest extends BaseSteps {

    public LogoutTest() {
        super();
    }

    @BeforeMethod(groups = {"Sanity", "Regression", "Master", "DataDriven", "test"})
    @Parameters({"browser","environment"})
    public void setUp(String browser,String environment) {
    	initialize(browser,environment);
        loginPage = new LandingPage(driver).navigateToLoginPage();
        accountSuccessPage = loginPage.performLogin(prop.getProperty("validemail"), prop.getProperty("password"));
    }

    @Test(priority = 1, groups = {"Regression", "Master"})
    public void verifyLogoutFromMainMenuDropdown_Test() {
        accountLogoutPage = accountSuccessPage.selectLogoutOptionFromMainMenu();
        Assert.assertTrue(accountLogoutPage.isLoginOptionDisplayed(), "Logout was not successful; login option is still displayed.");
    }

    @Test(priority = 2, groups = {"Regression", "Master"})
    public void verifyLogoutFromRightColumnOption_Test() {
        accountLogoutPage = accountSuccessPage.clickSidebarLogoutLink();
        Assert.assertTrue(accountLogoutPage.isLoginOptionDisplayed(), "Logout was not successful; login option is still displayed.");
    }
}


