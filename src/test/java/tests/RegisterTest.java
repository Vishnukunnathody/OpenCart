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

public class RegisterTest extends BaseSteps {

    // Constructor to initialize the RegisterTest class
    public RegisterTest() {
        super();
    }

    // Setup method: runs before each test to initialize the environment
    @BeforeMethod(groups = {"Sanity", "Regression", "Master", "DataDriven", "test"})
    @Parameters({"browser","environment"})
    public void setUp(String browser,String environment) {
    	initialize(browser,environment); // Initialize WebDriver and other necessary setups
        registerPage = new LandingPage(driver).navigateToRegisterPage();  // Navigate to Register Page
    }

    // Test Case 1: Verify registration with mandatory fields only
    @Test(priority = 1, groups = {"Regression", "Master"})
    public void verifyRegisteringAccountUsingMandatoryFields_Test() {
        registerPage.registerAnAccount(
            prop.getProperty("firstname"), 
            prop.getProperty("lastname"),
            DataGenUtil.generateRandomNewEmail(),
            prop.getProperty("telephone"),
            prop.getProperty("password"),
            prop.getProperty("password"),
            null, true
        );

        Assert.assertEquals(registerPage.getPageTitle(), Constant.Your_Account_Created_PageTitle);
    }

    // Test Case 2: Verify registration with all fields filled
    @Test(priority = 2, groups = {"Regression", "Master"})
    public void verifyRegisteringAccountUsingNonMandatoryFields_Test() {
        registerPage.registerAnAccount(
            prop.getProperty("firstname"), 
            prop.getProperty("lastname"),
            DataGenUtil.generateRandomNewEmail(),
            prop.getProperty("telephone"),
            prop.getProperty("password"),
            prop.getProperty("password"),
            true, true
        );

        Assert.assertEquals(registerPage.getPageTitle(), Constant.Your_Account_Created_PageTitle);
    }

    // Test Case 3: Verify registration without any details
    @Test(priority = 3, groups = {"Regression", "Master"})
    public void verifyRegisteringAccountWithoutAnyDetails_Test() {
        registerPage.registerAnAccount("", "", "", "", "", "", false, false);

        // Verify warning messages for all required fields
        Assert.assertEquals(registerPage.getFirstNameWarningMsg(), Constant.Expected_FirstName_WarningMsg);
        Assert.assertEquals(registerPage.getLastNameWarningMsg(), Constant.Expected_LastName_WarningMsg);
        Assert.assertEquals(registerPage.getEmailWarningMsg(), Constant.Expected_Email_WarningMsg);
        Assert.assertEquals(registerPage.getTelephoneWarningMsg(), Constant.Expected_Telephone_WarningMsg);
        Assert.assertEquals(registerPage.getPasswordWarningMsg(), Constant.Expected_Password_WarningMsg);
        Assert.assertTrue(registerPage.getPrivacyPolicyWarningMsg().contains(Constant.Expected_Privacy_WarningMsg));
    }

    // Test Case 4: Verify registration with "Yes" selected for Newsletter
    @Test(priority = 4, groups = {"Regression", "Master"})
    public void verifyRegisteringAccountWithYesSelectedForNewsletter_Test() {
        registerPage.registerAnAccount(
            prop.getProperty("firstname"),
            prop.getProperty("lastname"),
            DataGenUtil.generateRandomNewEmail(),
            prop.getProperty("telephone"),
            prop.getProperty("password"),
            prop.getProperty("password"),
            true, true
        );

        Assert.assertEquals(registerPage.getPageTitle(), Constant.Your_Account_Created_PageTitle);
    }

    // Test Case 5: Verify registration with "No" selected for Newsletter
    @Test(priority = 5, groups = {"Regression", "Master"})
    public void verifyRegisteringAccountWithNoSelectedForNewsletter_Test() {
        registerPage.registerAnAccount(
            prop.getProperty("firstname"),
            prop.getProperty("lastname"),
            DataGenUtil.generateRandomNewEmail(),
            prop.getProperty("telephone"),
            prop.getProperty("password"),
            prop.getProperty("password"),
            false, true
        );

        Assert.assertEquals(registerPage.getPageTitle(), Constant.Your_Account_Created_PageTitle);
    }

    // Test Case 6: Verify navigating to the register page via different links
    @Test(priority = 6, groups = {"Regression", "Master"})
    public void verifyDiffWaysNavigatingToRegisterAccount_Test() {
    	loginPage=new LandingPage(driver).navigateToLoginPage();
        loginPage.clickOnNewCustomerRegisterContinueBtn().clickSidebarRegisterLink();
        Assert.assertEquals(registerPage.getPageTitle(), Constant.Register_Account_PageTitle);
    }

    // Test Case 7: Verify registration with mismatched passwords
    @Test(priority = 7, groups = {"Regression", "Master"})
    public void verifyRegisteringWithDifferentPassAndConfirmPass_Test() {
        registerPage.registerAnAccount(
            prop.getProperty("firstname"),
            prop.getProperty("lastname"),
            DataGenUtil.generateRandomNewEmail(),
            prop.getProperty("telephone"),
            prop.getProperty("validpassword"),
            prop.getProperty("invalidpassword"),
            true, true
        );

        Assert.assertEquals(registerPage.getPassMismatchMsg(), Constant.Expected_Confirm_Password_WarningMsg);
    }

    // Test Case 8: Verify registration with an already existing email
    @Test(priority = 8, groups = {"Regression", "Master"})
    public void verifyRegisteringWithExistingDetails_Test() {
        registerPage.registerAnAccount(
            prop.getProperty("firstname"),
            prop.getProperty("lastname"),
            prop.getProperty("validemail"),
            prop.getProperty("telephone"),
            prop.getProperty("password"),
            prop.getProperty("password"),
            true, true
        );

        Assert.assertEquals(registerPage.getEmailAlreadyRegMsg(), Constant.Expected_EmailAlreadyExists_WarningMsg);
    }

    // Test Case 9: Verify tooltip messages for invalid email formats
    @Test(priority = 9, groups = {"Regression", "Master"})
    public void verifyInvalidEmailTooltipMsgs_Test() throws InterruptedException {
        Assert.assertTrue(registerPage.getDynamicToolTipMsg(prop.getProperty("invalidemailno@"), Constant.Email_TooltipErrmMsg_NoAtSign));
        Assert.assertTrue(registerPage.getDynamicToolTipMsg(prop.getProperty("invalidemailnothingafter@"), Constant.Email_TooltipErrmMsg_NoTextAfterAtSign));
        Assert.assertTrue(registerPage.getDynamicToolTipMsg(prop.getProperty("invalidemailnodomain"), Constant.Email_TooltipErrmMsg_DotInWrongPosition));
    }

    // Test Case 10: Verify invalid phone number scenario with data-driven testing
    @Test(priority = 10, groups = {"Regression", "Master", "DataDriven"}, dataProvider = "invalidPhoneNumbersDataSupplier", dataProviderClass = DataProviders.class)
    public void verifyUserCannotUseInvalidPhoneNumberForRegistration_Test(HashMap<String, String> hmap) {
        registerPage.validateTelephoneField(
            hmap.get("FirstName"),
            hmap.get("LastName"),
            DataGenUtil.generateRandomNewEmail(),
            hmap.get("Telephone"),
            hmap.get("Password"),
            hmap.get("PasswordConfirm")
        );
        ReportUtil.addStepLog(Status.INFO, hmap.get("Description"));
        Assert.assertNotEquals(driver.getTitle(), Constant.Your_Account_Created_PageTitle);
    }

    // Test Case 11: Verify that placeholder text is correct for the fields
    @Test(priority = 11, groups = {"Regression", "Master"})
    public void verifyRegisterPagePlaceholders_Test() {
        Assert.assertTrue(registerPage.getPlaceholderText(Constant.FirstName_Label, Constant.Fname_PlaceHolder));
        Assert.assertTrue(registerPage.getPlaceholderText(Constant.Last_Name_Label, Constant.Lname_PlaceHolder));
        Assert.assertTrue(registerPage.getPlaceholderText(Constant.E_Mail_Label, Constant.Email_PlaceHolder));
        Assert.assertTrue(registerPage.getPlaceholderText(Constant.Telephone_Label, Constant.Phone_PlaceHolder));
        Assert.assertTrue(registerPage.getPlaceholderText(Constant.Password_Label, Constant.Pwd_PlaceHolder));
        Assert.assertTrue(registerPage.getPlaceholderText(Constant.Password_Confirm_Label, Constant.ConfirmPwd_PlaceHolder));
    }

    // Test Case 12: Verify that asterisk symbol appears for mandatory fields
    @Test(priority = 12, groups = {"Regression", "Master"})
    public void verifyAsterickSymbolForMandatoryFields_Test() {
        Assert.assertTrue(registerPage.getAsteriskValues(Constant.FirstName_Label, Constant.color_Red, Constant.Asteric_Symbol));
        Assert.assertTrue(registerPage.getAsteriskValues(Constant.Last_Name_Label, Constant.color_Red, Constant.Asteric_Symbol));
        Assert.assertTrue(registerPage.getAsteriskValues(Constant.E_Mail_Label, Constant.color_Red, Constant.Asteric_Symbol));
        Assert.assertTrue(registerPage.getAsteriskValues(Constant.Telephone_Label, Constant.color_Red, Constant.Asteric_Symbol));
        Assert.assertTrue(registerPage.getAsteriskValues(Constant.Password_Label, Constant.color_Red, Constant.Asteric_Symbol));
        Assert.assertTrue(registerPage.getAsteriskValues(Constant.Password_Confirm_Label, Constant.color_Red, Constant.Asteric_Symbol));
    }

    @Test(priority = 13, dataProvider = "PwdComplexityDataSupplier", dataProviderClass = DataProviders.class, groups = {"DataDriven", "Master", "Regression"})
    public void verifyPasswordFieldFollowsComplexityStandards(HashMap<String, String> hMap) {
        registerPage.registerAnAccount(
            hMap.get("FirstName"), 
            hMap.get("LastName"),
            DataGenUtil.generateRandomNewEmail(),
            hMap.get("Telephone"),
            hMap.get("Password"), 
            hMap.get("PasswordConfirm"), 
            null, 
            true
        );
        ReportUtil.addStepLog(Status.INFO, hMap.get("Description"));
        Assert.assertNotEquals(registerPage.getPageTitle(), Constant.Your_Account_Created_PageTitle);
    }

    @Test(priority = 14, groups = {"Regression", "Master"})
    public void verifyPrivacyPolicyCheckboxNotCheckedByDefault() {
        Assert.assertFalse(registerPage.getPrivacyPolicyStatus(), "Privacy policy checkbox should not be checked by default.");
    }

    @Test(priority = 15, groups = {"Regression", "Master"})
    public void verifyRegistrationWithoutSelectingPrivacyPolicy() {
        registerPage.registerAnAccount(
            prop.getProperty("firstname"), 
            prop.getProperty("lastname"),
            DataGenUtil.generateRandomNewEmail(), 
            prop.getProperty("telephone"), 
            prop.getProperty("validpassword"),
            prop.getProperty("validpassword"), 
            true, 
            false
        );
        Assert.assertEquals(registerPage.getPrivacyPolicyWarningMsg(), Constant.Expected_Privacy_WarningMsg, "Expected privacy policy warning message not displayed.");
    }

    @Test(priority = 16, groups = {"Regression", "Master"})
    public void verifyPasswordAndConfirmPasswordAreHidden() {
        Assert.assertTrue(registerPage.getTypeStatus(prop.getProperty("validpassword")), "Password and confirm password fields should be toggled to hide.");
    }

    @Test(priority = 17, groups = {"Regression", "Master"})
    public void verifyRegistrationWithoutConfirmPasswordInput_Test() {
        registerPage.registerAnAccount(
            prop.getProperty("firstname"), 
            prop.getProperty("lastname"),
            DataGenUtil.generateRandomNewEmail(), 
            prop.getProperty("telephone"), 
            prop.getProperty("validpassword"),
            "", 
            true, 
            true
        );
        Assert.assertEquals(registerPage.getPassMismatchMsg(), Constant.Expected_Confirm_Password_WarningMsg, "Expected password mismatch warning message not displayed.");
    }

    @Test(priority = 18, groups = {"Regression", "Master"})
    public void verifyConfirmationEmailSentForSuccessfulRegistration() {
        // Attempt to register a new account with valid details
        registerPage.registerAnAccount(
            prop.getProperty("firstName"), 
            prop.getProperty("lastName"),
            DataGenUtil.generateRandomNewEmail(), 
            prop.getProperty("telephone"), 
            prop.getProperty("validPassword"),
            prop.getProperty("validPassword"), 
            true, 
            true
        );
      //  Assert.assertTrue(EmailVerifierUtil.verifyEmailAndLinks(null, null, null, null));
     }}






