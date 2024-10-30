package tests;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pagesobjects.LandingPage;
import utils.Constant;
import utils.DataGenUtil;
import utils.DataProviders;
import utils.ReportUtil;

public class LoginTest extends BaseSteps {

	@Test(priority = 1,dataProvider ="ValidloginDataSupplier",dataProviderClass = DataProviders.class,groups= {"DataDriven"})
	public void verifyLoginToApplicationUsingvalidCredentials(HashMap<String,String> hMap) throws Exception {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		accountSuccessPage = loginPage.loginToTheApplication(hMap.get("Username"),hMap.get("Password"));
		Assert.assertTrue(accountSuccessPage.displayLogoutOptionStatus());
		ReportUtil.addStepLog(Status.INFO, hMap.get("Description"));
		}
	   
	@Test(priority = 2,dataProvider ="InvalidloginDataSupplier",dataProviderClass = DataProviders.class,groups= {"DatDriven"})
	public void verifyLoginToApplicationUsingInvalidCredentials(HashMap<String,String> hMap) {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		loginPage.loginToTheApplication(hMap.get("Username"),hMap.get("Password"));
		ReportUtil.addStepLog(Status.INFO, hMap.get("Description"));
		Assert.assertEquals(loginPage.getInvalidCredentialWarningMsg(), Constant.Expected_Login_warningMsg);
	}
	
	
	
	
	
	
	
	
	

	@Test(priority = 3,groups= {"Regression","Master"})
	public void verifyLoginToApplicationUsingInvalidEmailValidPass() {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		loginPage.loginToTheApplication(prop.getProperty("invalidemail"), prop.getProperty("validpassword"));
		Assert.assertEquals(loginPage.getInvalidCredentialWarningMsg(), Constant.Expected_Login_warningMsg);
	}

	@Test(priority = 4,groups= {"Regression","Master"})
	public void verifyLoginToApplicationUsingValidEmailInvalidPass() {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		loginPage.loginToTheApplication(prop.getProperty("validemail"), prop.getProperty("invalidpassword"));
		Assert.assertEquals(loginPage.getInvalidCredentialWarningMsg(), Constant.Expected_Login_warningMsg);
	}

	@Test(priority = 5,groups= {"Regression","Master"})
	public void verifyLoginToApplicationUsingNoCredentilals() {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		loginPage.loginToTheApplication("", "");
		Assert.assertEquals(loginPage.getInvalidCredentialWarningMsg(), Constant.Expected_Login_warningMsg);
	}

	@Test(priority = 6,groups= {"Regression","Master"})
	public void verifyForgotPasswordLink() {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		forgotPasswordPage = loginPage.clickForgotPassLink();
		Assert.assertEquals(forgotPasswordPage.getForgotYourPasswordLabelText(), Constant.Forgot_Password_Label);
	}

	@Test(priority = 7,groups= {"Regression","Master"})
	public void verifyPlaceholdersOnLoginPage() {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		Assert.assertTrue(loginPage.getPlaceHolderTexts(Constant.EmailAddress_Label_loginPage,
				Constant.EmailAddress_PlaceHolder_loginPage));
		Assert.assertTrue(loginPage.getPlaceHolderTexts(Constant.Password_Label_loginPage,
				Constant.Password_PlaceHolder_loginPage));
	}
	@Test(priority = 8,groups= {"Regression","Master"},enabled=false)
	public void verifyNavigatingBackAfterSuccessfullLogin() {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		loginPage.loginToTheApplication(prop.getProperty("validemail"), prop.getProperty("password")).navigateBack(driver);
		loginPage.clickOnmyAccountOption();
		Assert.assertTrue(loginPage.logoutOption.isDisplayed());
	}
	@Test(priority = 9,groups= {"Regression","Master"},enabled=false)
	public void verifyNavigatingBackAfterLogout() {
		loginPage = new LandingPage(driver).navigateToLoginPage();
		loginPage.loginToTheApplication(prop.getProperty("validemail"), prop.getProperty("password")).SelectlogOutoptionUnderMyAccount().navigateBack(driver);
	    Assert.assertTrue(loginPage.logInOption.isDisplayed());
	}	
	    @Test(priority = 10,groups= {"Regression","Master"})
		public void verifyLoginUsingInvalidCredentials() {
			loginPage = new LandingPage(driver).navigateToLoginPage();
			loginPage.loginToTheApplication(prop.getProperty("invalidemail"), prop.getProperty("invalidpassword"));
			Assert.assertEquals(loginPage.getInvalidCredentialWarningMsg(), Constant.Expected_Login_warningMsg);
	
	    }
	    @Test(priority = 11,groups= {"Regression","Master"})
		public void verifyMaxLoginAttempts() {
			loginPage = new LandingPage(driver).navigateToLoginPage();
			Assert.assertTrue(loginPage.tryLoginMaxAttempts(DataGenUtil.generateRandomNewEmail(), "21212"));
	    }
	    
			@Test(priority = 12,groups= {"Regression","Master"})
			public void verifyPasswordFieldToggleToHide() {
				loginPage = new LandingPage(driver).navigateToLoginPage();
				Assert.assertTrue(loginPage.getPasswordFieldAttribute(prop.getProperty("validemail"), prop.getProperty("password")));
			}
			@Test(priority = 13,groups= {"Regression","Master"})
			public void verifyPasswordCannotCopied() {
				loginPage = new LandingPage(driver).navigateToLoginPage();
				Assert.assertTrue(loginPage.PasswordCopyFunctionality(prop.getProperty("password")));
	}
			@Test(priority = 14,groups= {"Regression","Master"})
			public void verifyPasswordNotVisibleInPageSource() {
				loginPage = new LandingPage(driver).navigateToLoginPage();
				Assert.assertTrue(loginPage.getPasswordFromPageSource(prop.getProperty("validemail"), prop.getProperty("password")));
	}
			/*@Test(priority = 15)
			public void verifyLoggingAfterchangingPassword() {
				loginPage = new LandingPage(driver).navigateToLoginPage();
			accountSuccessPage=loginPage.loginToTheApplication("vishnu.test1@gmail.com","Vishnu123456");
			accountSuccessPage=accountSuccessPage.clickOnchangePasswordLink().changePassword("Vishnu12345");
			
				
	}*/
			
			
}
