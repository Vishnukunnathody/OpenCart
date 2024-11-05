package tests;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
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

	@BeforeMethod(groups = {"Sanity", "Regression", "Master", "DataDriven","test" })
	public void SetUp() {
		initialize();
		loginPage = new LandingPage(driver).navigateToLoginPage();
	}

	@Test(priority = 1, dataProvider = "ValidloginDataSupplier", dataProviderClass = DataProviders.class, groups = {
			"Regression", "DataDriven", "Master","test" })
	public void verifyLoginToApplicationUsingvalidCredentials_Test(HashMap<String, String> hMap) {
		accountSuccessPage = loginPage.loginToTheApplication(hMap.get("Username"), hMap.get("Password"));
		ReportUtil.addStepLog(Status.INFO, "Parameters/Test Data_Used :: "+hMap.get("Description"));
		boolean success=accountSuccessPage.displayLogoutOptionStatus();
		Assert.assertTrue(success);

	}

	@Test(priority = 2, dataProvider = "InvalidloginDataSupplier", dataProviderClass = DataProviders.class, groups = {
			"Regression", "DataDriven", "Master" })
	public void verifyLoginToApplicationUsingInvalidCredentials_Test(HashMap<String, String> hMap) {
		loginPage.loginToTheApplication(hMap.get("Username"), hMap.get("Password"));
		ReportUtil.addStepLog(Status.INFO, hMap.get("Description"));
		Assert.assertEquals(loginPage.getInvalidCredentialWarningMsg(), Constant.Expected_Login_warningMsg);
	}

	@Test(priority = 6, groups = { "Regression", "Master" })
	public void verifyForgotPasswordLink_Test() {
		forgotPasswordPage = loginPage.clickForgotPassLink();
		Assert.assertEquals(forgotPasswordPage.getForgotYourPasswordLabelText(), Constant.Forgot_Password_Label);

	}

	@Test(priority = 7, groups = { "Regression", "Master" })
	public void verifyPlaceholdersOnLoginPage_Test() {
		Assert.assertTrue(loginPage.getPlaceHolderTexts(Constant.EmailAddress_Label_loginPage,
				Constant.EmailAddress_PlaceHolder_loginPage));
		Assert.assertTrue(loginPage.getPlaceHolderTexts(Constant.Password_Label_loginPage,
				Constant.Password_PlaceHolder_loginPage));
	}

	@Test(priority = 8, groups = { "Regression", "Master" })
	public void verifyNavigatingBackAfterSuccessfullLogin_Test() {
		loginPage.loginToTheApplication(prop.getProperty("validemail"), prop.getProperty("password"))
				.navigateBack(driver);
		loginPage.clickOnmyAccountOption();
		Assert.assertTrue(loginPage.logoutOption.isDisplayed());
	}

	@Test(priority = 9, groups = { "Regression", "Master" })
	public void verifyNavigatingBackAfterLogout_Test() {
		loginPage.loginToTheApplication(prop.getProperty("validemail"), prop.getProperty("password"))
				.SelectlogOutoptionUnderMyAccount().navigateBack(driver);
		Assert.assertTrue(loginPage.logInOption.isDisplayed());
	}

	@Test(priority = 11, groups = { "Regression", "Master" })
	public void verifyMaxLoginAttempts_Test() {
		Assert.assertTrue(loginPage.tryLoginMaxAttempts(DataGenUtil.generateRandomNewEmail(), "21212"));
	}

	@Test(priority = 12, groups = { "Regression", "Master" })
	public void verifyPasswordFieldToggleToHide_Test() {
		Assert.assertTrue(
				loginPage.getPasswordFieldAttribute(prop.getProperty("validemail"), prop.getProperty("password")));
	}

	@Test(priority = 13, groups = { "Regression", "Master" })
	public void verifyPasswordCannotCopied_Test() {
		Assert.assertTrue(loginPage.PasswordCopyFunctionality(prop.getProperty("password")));
	}

	@Test(priority = 14, groups = { "Regression", "Master" })
	public void verifyPasswordNotVisibleInPageSource_Test() {
		Assert.assertTrue(
				loginPage.getPasswordFromPageSource(prop.getProperty("validemail"), prop.getProperty("password")));
	}

	@Test(priority = 15, groups = { "Regression", "Master" })
	public void verifyLoggingAfterchangingPassword_Test() {
		String email = DataGenUtil.generateRandomNewEmail();
		String Pwd = DataGenUtil.randomPass();
		String newPWD = Pwd + DataGenUtil.randomString(5);
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		accountSuccessPage = registerPage
				.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"), email,
						prop.getProperty("telephone"), Pwd, Pwd, null, true)
				.SelectlogOutoptionUnderMyAccount()

				.clickOnLoginBtn().loginToTheApplication(email, Pwd).clickOnchangePasswordLink().changePassword(newPWD)
				.SelectlogOutoptionUnderMyAccount()

				.clickOnLoginBtn().loginToTheApplication(email, newPWD);
		Assert.assertTrue(accountSuccessPage.displayLogoutOptionStatus());

	}

}
