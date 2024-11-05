package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import pagesobjects.LandingPage;

public class LogoutTest extends BaseSteps {
	
	public LogoutTest () {
		super();
	}
	
	@BeforeMethod(groups = {"Sanity", "Regression", "Master", "DataDriven","test" })
	public void SetUp() {
		initialize();
		loginPage = new LandingPage(driver).navigateToLoginPage();
	}


	@Test(priority = 1, groups ={"Regression","Master"})
	public void verifylogoutFromMyAccountDropMenu_Test() {
		accountSuccessPage = loginPage.loginToTheApplication(prop.getProperty("validemail"),
				prop.getProperty("password"));
		accountLogoutPage = accountSuccessPage.SelectlogOutoptionUnderMyAccount();
		Assert.assertTrue(accountLogoutPage.displayLoginOption());
	}

	@Test(priority = 2, groups ={"Regression","Master"})
	public void verifyLogoutFromRightColumnOption_Test() {
		accountSuccessPage = loginPage.loginToTheApplication(prop.getProperty("validemail"),
				prop.getProperty("password"));
		accountLogoutPage = accountSuccessPage.clickOnRightColumnLogoutOption();
		Assert.assertTrue(accountLogoutPage.displayLoginOption());

	}
	

	}


