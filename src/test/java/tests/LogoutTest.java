package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pagesobjects.LandingPage;

public class LogoutTest extends BaseSteps{
	
	@Test(priority=1,groups="test")
	public void logoutFromMyAccountDropMenu() {
		loginPage= new LandingPage(driver).navigateToLoginPage();
		accountSuccessPage=loginPage.loginToTheApplication(prop.getProperty("validemail"), prop.getProperty("password"));
		accountLogoutPage=accountSuccessPage.SelectlogOutoptionUnderMyAccount();
		Assert.assertTrue(accountLogoutPage.displayLoginOption());
	}

}
