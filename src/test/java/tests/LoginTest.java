package tests;

import java.util.HashMap;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import pagesobjects.LandingPage;
import utils.Constant;
import utils.DataGenUtil;
import utils.DataProviders;
import utils.ReportUtil;

public class LoginTest extends BaseSteps {

    public LoginTest() {
        super();
    }
    
    @BeforeMethod(groups = {"Sanity", "Regression", "Master", "DataDriven", "test"})
    @Parameters({"browser","environment"})
    public void setUp(String browser,String environment) {
        initialize(browser,environment);
        loginPage = new LandingPage(driver).navigateToLoginPage();
    }

    @Test(priority = 1, dataProvider = "ValidloginDataSupplier", dataProviderClass = DataProviders.class, groups = {
            "Regression", "DataDriven", "Master", "test"})
    public void verifyLoginWithValidCredentials_Test(HashMap<String, String> hMap) {
        accountSuccessPage = loginPage.performLogin(hMap.get("Username"), hMap.get("Password"));
        ReportUtil.addStepLog(Status.INFO, "Parameters/Test Data Used: " + hMap.get("Description"));
        Assert.assertTrue(accountSuccessPage.isLogoutOptionDisplayed(), "Logout option should be displayed after successful login.");
    }

    @Test(priority = 2, dataProvider = "InvalidloginDataSupplier", dataProviderClass = DataProviders.class, groups = {
            "Regression", "DataDriven", "Master"})
    public void verifyLoginWithInvalidCredentials_Test(HashMap<String, String> hMap) {
        loginPage.performLogin(hMap.get("Username"), hMap.get("Password"));
        ReportUtil.addStepLog(Status.INFO, hMap.get("Description"));
        Assert.assertEquals(loginPage.getInvalidCredentialWarningMessage(), Constant.Expected_Login_warningMsg, "Warning message should match expected.");
    }

    @Test(priority = 6, groups = {"Regression", "Master"})
    public void verifyForgotPasswordLink_Test() {
        forgotPasswordPage = loginPage.clickForgotPasswordLink();
        Assert.assertEquals(forgotPasswordPage.getPageHeaderText(), Constant.Forgot_Password_Label, "Header text should match expected.");
    }

    @Test(priority = 7, groups = {"Regression", "Master"})
    public void verifyPlaceholdersOnLoginPage_Test() {
        Assert.assertTrue(loginPage.isPlaceholderTextCorrect(Constant.EmailAddress_Label_loginPage,
                Constant.EmailAddress_PlaceHolder_loginPage), "Email placeholder text should be correct.");
        Assert.assertTrue(loginPage.isPlaceholderTextCorrect(Constant.Password_Label_loginPage,
                Constant.Password_PlaceHolder_loginPage), "Password placeholder text should be correct.");
    }

    @Test(priority = 8, groups = {"Regression", "Master"})
    public void verifyNavigationBackAfterSuccessfulLogin_Test() {
        loginPage.performLogin(prop.getProperty("validemail"), prop.getProperty("password")).navigateBack();
        loginPage.clickOnMyAccountOption();
        Assert.assertTrue(loginPage.logoutOption.isDisplayed(), "Logout option should be displayed.");
    }

    @Test(priority = 9, groups = {"Regression", "Master"})
    public void verifyNavigationBackAfterLogout_Test() {
        loginPage.performLogin(prop.getProperty("validemail"), prop.getProperty("password"))
                .selectLogoutOptionFromMainMenu().navigateBack();
        Assert.assertTrue(loginPage.loginOption.isDisplayed(), "Login option should be displayed.");
    }

    @Test(priority = 11, groups = {"Regression", "Master"})
    public void verifyMaxLoginAttempts_Test() {
        Assert.assertTrue(loginPage.tryLoginMaxAttempts(DataGenUtil.generateRandomNewEmail(), "21212"), "Max login attempts should be enforced.");
    }

    @Test(priority = 12, groups = {"Regression", "Master"})
    public void verifyPasswordFieldToggleToHide_Test() {
        Assert.assertTrue(loginPage.isPasswordFieldSecured(prop.getProperty("validemail"), prop.getProperty("password")), "Password field should be secured.");
    }

    @Test(priority = 13, groups = {"Regression", "Master"})
    public void verifyPasswordCannotBeCopied_Test() {
        Assert.assertTrue(loginPage.isPasswordNonCopyable(prop.getProperty("password")), "Password should not be copyable.");
    }

    @Test(priority = 14, groups = {"Regression", "Master"})
    public void verifyPasswordNotVisibleInPageSource_Test() {
        Assert.assertTrue(loginPage.isPasswordHiddenInPageSource(prop.getProperty("validemail"), prop.getProperty("password")), "Password should not be visible in page source.");
    }

    @Test(priority = 15, groups = {"Regression", "Master"})
    public void verifyLoginAfterChangingPassword_Test() {
        String email = DataGenUtil.generateRandomNewEmail();
        String password = DataGenUtil.randomPass();
        String newPassword = password + DataGenUtil.randomString(5);
        
        registerPage = new LandingPage(driver).navigateToRegisterPage();
        accountSuccessPage = registerPage.registerAnAccount(
                prop.getProperty("firstname"),
                prop.getProperty("lastname"),
                email,
                prop.getProperty("telephone"),
                password,
                password,
                null,
                true
                
        ).selectLogoutOptionFromMainMenu()
         .clickOnLoginBtn()
         .performLogin(email, password)
         .clickOnChangePasswordLink()
         .changePassword(newPassword)
         .selectLogoutOptionFromMainMenu()
         .clickOnLoginBtn()
         .performLogin(email, newPassword);
        
        Assert.assertTrue(accountSuccessPage.isLogoutOptionDisplayed(), "Logout option should be displayed after login with new password.");
    }
}