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

public class RegisterTest extends BaseSteps {

	@Test(priority = 1,groups= {"Regression","Master"})
	public void verifyRegisterAccountUsingMandatoryFields() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				DataGenUtil.generateRandomNewEmail(), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				prop.getProperty("validpassword"), null, true);

		Assert.assertEquals(registerPage.getTitle(), Constant.Your_Account_Created_PageTitle);
	}

	@Test(priority = 2,groups= {"Regression","Master"})
	public void verifyRegisterAccountUsingAllFields() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				DataGenUtil.generateRandomNewEmail(), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				prop.getProperty("validpassword"), true, true);

		Assert.assertEquals(registerPage.getTitle(), Constant.Your_Account_Created_PageTitle);

	}

	@Test(priority = 3,groups= {"Regression","Master"})
	public void verifyRegisterAccountWithoutanyDetails() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount("", "", "", "", "", "", false, false);

		Assert.assertEquals(registerPage.getFirstNameWarnigMsg(), Constant.Expected_FirstName_WarningMsg);
		Assert.assertEquals(registerPage.getLastNameWarnigMsg(), Constant.Expected_LastName_WarningMsg);
		Assert.assertEquals(registerPage.getEmailWarnigMsg(), Constant.Expected_Email_WarningMsg);
		Assert.assertEquals(registerPage.getTelephoneWarnigMsg(), Constant.Expected_Telephone_WarningMsg);
		Assert.assertEquals(registerPage.getpasswordWarnigMsg(), Constant.Expected_Password_WarningMsg);
		Assert.assertTrue(registerPage.getprivacyPolicyWarnigMsg().contains(Constant.Expected_Privacy_WarningMsg));

	}

	@Test(priority = 4,groups= {"Regression","Master"})
	public void verifyRegisterAccountYesSelectedForNewsletter() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				DataGenUtil.generateRandomNewEmail(), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				prop.getProperty("validpassword"), true, true);

		Assert.assertEquals(registerPage.getTitle(), Constant.Your_Account_Created_PageTitle);

	}

	@Test(priority = 5,groups= {"Regression","Master"})
	public void verifyRegisterAccountNoSelectedForNewsletter() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				DataGenUtil.generateRandomNewEmail(), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				prop.getProperty("validpassword"), false, true);

		Assert.assertEquals(registerPage.getTitle(), Constant.Your_Account_Created_PageTitle);

	}

	@Test(priority = 6,groups= {"Regression","Master"})
	public void verifyDiffWayNavigatingToRegisterAccount() {
		registerPage = new LandingPage(driver).registerFromLoginPage().clickContinueBtn().clickOnRightregisterGrplink();

		Assert.assertEquals(registerPage.getTitle(), Constant.Register_Account_PageTitle);

	}

	@Test(priority = 7,groups= {"Regression","Master"})
	public void verifyRegisteringWithDifferentPassAndConfirmPass() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				DataGenUtil.generateRandomNewEmail(), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				prop.getProperty("invalidpassword"), true, true);

		Assert.assertEquals(registerPage.getpassMissmatchMsg(), Constant.Expected_Confirm_Password_WarningMsg);

	}

	@Test(priority = 8,groups= {"Regression","Master"})
	public void verifyRegisteringWithExistingDetails() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				prop.getProperty("validemail"), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				prop.getProperty("validpassword"), true, true);

		Assert.assertEquals(registerPage.getEmailAlreadyRegMsg(), Constant.Expected_EmailAlreadyExists_WarningMsg);
	}

	@Test(priority = 9,groups= {"Regression","Master"})
	public void verifyInvalidEmailTooltipMsg() throws InterruptedException {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		Assert.assertEquals(registerPage.getDynamicToolTipMsg(prop.getProperty("invalidemailno@"), Constant.Email_TooltipErrmMsg_NoAtSign), true);
		Assert.assertEquals(registerPage.getDynamicToolTipMsg(prop.getProperty("invalidemailnothingafter@"), Constant.Email_TooltipErrmMsg_NoTextAfterAtSign),
				true);
		Assert.assertEquals(
				registerPage.getDynamicToolTipMsg(prop.getProperty("invalidemailnodomain"), Constant.Email_TooltipErrmMsg_DotInWrongPosition),
				true);

	}

	@Test(priority = 10,groups= {"Regression","Master","DataDriven"},dataProvider="invalidPhoneNumbersDataSupplier",dataProviderClass=DataProviders.class)
	public void verifyUserCannotuseInvalidPhoneNumber(HashMap<String,String>hmap) {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.validateTelephoneField(hmap.get("FirstName"), hmap.get("LastName"),
				DataGenUtil.generateRandomNewEmail(), hmap.get("Telephone"),
				hmap.get("Password"), hmap.get("PasswordConfirm"));
		ReportUtil.addStepLog(Status.INFO, hmap.get("Description"));
		Assert.assertNotEquals(driver.getTitle(), Constant.Your_Account_Created_PageTitle);

		
	}

	@Test(priority = 11,groups= {"Regression","Master"})
	public void verifyThePlaceholders() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		Assert.assertTrue(registerPage.getplaceHolderText(Constant.FirstName_Label, Constant.Fname_PlaceHolder));
		Assert.assertTrue(registerPage.getplaceHolderText(Constant.Last_Name_Label, Constant.Lname_PlaceHolder));
		Assert.assertTrue(registerPage.getplaceHolderText(Constant.E_Mail_Label, Constant.Email_PlaceHolder));
		Assert.assertTrue(registerPage.getplaceHolderText(Constant.Telephone_Label, Constant.Phone_PlaceHolder));
		Assert.assertTrue(registerPage.getplaceHolderText(Constant.Password_Label, Constant.Pwd_PlaceHolder));
		Assert.assertTrue(
				registerPage.getplaceHolderText(Constant.Password_Confirm_Label, Constant.ConfirmPwd_PlaceHolder));

	}

	@Test(priority = 12,groups= {"Regression","Master"})
	public void verifyAsterickSymbolForMandatoryFields() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();

		Assert.assertTrue(
				registerPage.getAstericValues(Constant.FirstName_Label, Constant.color_Red, Constant.Asteric_Symbol));
		Assert.assertTrue(
				registerPage.getAstericValues(Constant.Last_Name_Label, Constant.color_Red, Constant.Asteric_Symbol));
		Assert.assertTrue(
				registerPage.getAstericValues(Constant.E_Mail_Label, Constant.color_Red, Constant.Asteric_Symbol));
		Assert.assertTrue(
				registerPage.getAstericValues(Constant.Telephone_Label, Constant.color_Red, Constant.Asteric_Symbol));
		Assert.assertTrue(
				registerPage.getAstericValues(Constant.Password_Label, Constant.color_Red, Constant.Asteric_Symbol));
		Assert.assertTrue(registerPage.getAstericValues(Constant.Password_Confirm_Label, Constant.color_Red,
				Constant.Asteric_Symbol));

	}

	@Test(priority = 13,dataProvider="PwdComplexityDataSupplier",dataProviderClass = DataProviders.class,groups= {"DataDriven","Master","Regression"})
	public void verifyPassWordFieldFollowComplexityStandards(HashMap<String, String> hMap) {

		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(hMap.get("FirstName"), hMap.get("LastName"),
				DataGenUtil.generateRandomNewEmail(),hMap.get("Telephone"),
				hMap.get("Password"), hMap.get("PasswordConfirm"), null, true);
		ReportUtil.addStepLog(Status.INFO, hMap.get("Description"));
		Assert.assertNotEquals(registerPage.getTitle(), Constant.Your_Account_Created_PageTitle);
			
	}

	@Test(priority = 14,groups= {"Regression","Master"})
	public void verifyCheckboxNotCheckedByDefault() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		Assert.assertFalse(registerPage.getPrivacyPolicyStatus());

	}

	@Test(priority = 15,groups= {"Regression","Master"})
	public void verifyRegisteringWithNotSelectingPrivacyPolicy() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				DataGenUtil.generateRandomNewEmail(), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				prop.getProperty("invalidpassword"), true, false);
		Assert.assertEquals(registerPage.getprivacyPolicyWarnigMsg(), Constant.Expected_Privacy_WarningMsg);

	}

	@Test(priority = 16,groups= {"Regression","Master"})
	public void verifyPasswordAndConfirmPasswordToggledToHide() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		Assert.assertTrue(registerPage.getTypeStatus(prop.getProperty("validpassword")));
	}

	@Test(priority = 17,groups= {"Regression","Master"})
	public void verifyRegisteringWithNotEnteringPassConfirm() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				DataGenUtil.generateRandomNewEmail(), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				"", true, true);
		Assert.assertEquals(registerPage.getpassMissmatchMsg(), Constant.Expected_Confirm_Password_WarningMsg);

	}

	@Test(priority = 18,groups= {"Regression","Master"})
	public void verifyConfirmationEmailSendForSuccessfullRegistration() {
		registerPage = new LandingPage(driver).navigateToRegisterPage();
		registerPage.registerAnAccount(prop.getProperty("fristname"), prop.getProperty("lastname"),
				DataGenUtil.generateRandomNewEmail(), prop.getProperty("telephone"), prop.getProperty("validpassword"),
				prop.getProperty("validpassword"), true, true);

	}
}
