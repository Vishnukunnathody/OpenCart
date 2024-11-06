package pagesobjects;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import utils.Variables;

public class RegisterPage extends BasePage {

	public RegisterPage(WebDriver driver) {
		super(driver);

	}

	// XPaths for mandatory fields and input text fields
	private final String MANDATORY_FIELDS_LABEL = "(//label[@class='col-sm-2 control-label'])[position() > 1][position() < last()]";
	private final String INPUT_TEXT_FIELDS = "//input[@class='form-control']";

	// Input Fields
	@FindBy(how = How.XPATH, using = "//input[@id='input-firstname']")
	private WebElement firstNameText; // First name input field

	@FindBy(how = How.XPATH, using = "//input[@id='input-lastname']")
	private WebElement lastNameText; // Last name input field

	@FindBy(how = How.XPATH, using = "//input[@id='input-email']")
	private WebElement emailText; // Email input field

	@FindBy(how = How.XPATH, using = "//input[@id='input-telephone']")
	private WebElement phoneText; // Phone number input field

	@FindBy(how = How.XPATH, using = "//input[@id='input-password']")
	private WebElement passwordText; // Password input field

	@FindBy(how = How.XPATH, using = "//input[@id='input-confirm']")
	private WebElement confirmPasswordText; // Confirm password input field

	// Subscription Radio Buttons
	@FindBy(how = How.XPATH, using = "//input[@name='newsletter'][@value=0]")
	private WebElement subscribeRadioBtnNo; // No subscription radio button

	@FindBy(how = How.XPATH, using = "//input[@name='newsletter'][@value=1]")
	private WebElement subscribeRadioBtnYes; // Yes subscription radio button

	// Privacy Policy Checkbox
	@FindBy(how = How.XPATH, using = "//input[@name='agree'][@value=1]")
	private WebElement privacyPolicyCheckBox; // Privacy policy agreement checkbox

	// Buttons
	@FindBy(how = How.XPATH, using = "//input[@value='Continue'][@class='btn btn-primary']")
	private WebElement continueBtn; // Continue button

	// Warning Messages
	@FindBy(how = How.XPATH, using = "//*[@id='input-firstname']/following-sibling::div")
	private WebElement firstNameWarningMsg; // Warning message for first name

	@FindBy(how = How.XPATH, using = "//*[@id='input-lastname']/following-sibling::div")
	private WebElement lastNameWarningMsg; // Warning message for last name

	@FindBy(how = How.XPATH, using = "//*[@id='input-email']/following-sibling::div")
	private WebElement emailWarningMsg; // Warning message for email

	@FindBy(how = How.XPATH, using = "//*[@id='input-telephone']/following-sibling::div")
	private WebElement telephoneWarningMsg; // Warning message for phone number

	@FindBy(how = How.XPATH, using = "//*[@id='input-password']/following-sibling::div")
	private WebElement passwordWarningMsg; // Warning message for password

	@FindBy(how = How.XPATH, using = "//div[@class='alert alert-danger alert-dismissible']")
	private WebElement privacyPolicyWarningMsg; // Warning message for privacy policy

	@FindBy(how = How.XPATH, using = "//div[text()='Password confirmation does not match password!']")
	public WebElement passMismatchMsg; // Password mismatch warning message

	@FindBy(how = How.XPATH, using = "//*[text()='Warning: E-Mail Address is already registered!']")
	public WebElement emailAddressAlreadyRegMsg; // Warning message for already registered email

	// Register Link
	@FindBy(how = How.XPATH, using = "//a[text()='Register'][@class='list-group-item']")
	private WebElement registerLink; // Register link

	public void enterFirstName(String firstnameData) {
		enterText(firstNameText, firstnameData);
	}

	public void enterLastName(String lastnameData) {
		enterText(lastNameText, lastnameData);
	}

	public void enterValidEmail(String emailData) {
		enterText(emailText, emailData);
	}

	public void enterTelephoneNumber(String telephoneData) {
		enterText(phoneText, telephoneData);
	}

	public void enterValidPassword(String passwordData) {
		enterText(passwordText, passwordData);
	}

	public void enterConfirmPassword(String confirmPasswordData) {
		enterText(confirmPasswordText, confirmPasswordData);
	}

	public void selectNoToSubscribeNewsletter() {
		clickElement(subscribeRadioBtnNo);
	}

	public void selectYesToSubscribeNewsletter() {
		clickElement(subscribeRadioBtnYes);
	}

	public void checkPrivacyPolicy() {
		clickElement(privacyPolicyCheckBox);
	}

	public void clickOnContinueButton() {
		clickElement(continueBtn);
	}

	public String getFirstNameWarningMsg() {
		return getText(firstNameWarningMsg);
	}

	public String getLastNameWarningMsg() {
		return getText(lastNameWarningMsg);
	}

	public String getEmailWarningMsg() {
		return getText(emailWarningMsg);
	}

	public String getTelephoneWarningMsg() {
		return getText(telephoneWarningMsg);
	}

	public String getPasswordWarningMsg() {
		return getText(passwordWarningMsg);
	}

	public String getPassMismatchMsg() {
		return getText(passMismatchMsg);
	}

	public String getPrivacyPolicyWarningMsg() {
		return getText(privacyPolicyWarningMsg);
	}

	public String getEmailAlreadyRegMsg() {
		return getText(emailAddressAlreadyRegMsg);
	}

	public AccountSuccessPage registerAnAccount(String firstNameData, String lastNameData, String emailData,
			String telephoneData, String passwordData, String confirmPassData, Boolean subscribeNewsletter,
			boolean checkPrivacyPolicy) {
		
// Validate required inputs
		if (firstNameData == null || lastNameData == null || emailData == null || telephoneData == null
				|| passwordData == null) 
			
		{
			throw new IllegalArgumentException("All mandatory fields must be provided.");
		}

		enterFirstName(firstNameData);
		enterLastName(lastNameData);
		enterValidEmail(emailData);
		enterTelephoneNumber(telephoneData);
		enterValidPassword(passwordData);
		enterConfirmPassword(confirmPassData);

		if (subscribeNewsletter != null) {
			WebElement selectedRadioBtn = subscribeNewsletter ? subscribeRadioBtnYes : subscribeRadioBtnNo;
			if (!selectedRadioBtn.isSelected()) {// Checks if the Button is already Selected.
		        clickElement(selectedRadioBtn);
		    }
		}

		if (checkPrivacyPolicy) {
			checkPrivacyPolicy();
		}

		clickOnContinueButton();
		return new AccountSuccessPage(driver);
	}

	public RegisterPage clickSidebarRegisterLink() {
		clickElement(registerLink);
		return new RegisterPage(driver);
	}

	public boolean getDynamicToolTipMsg(String enteredText, String expectedErrMsg) throws InterruptedException {
		enterText(emailText, enteredText);
		checkPrivacyPolicy();
		clickOnContinueButton();

// Use WebDriverWait instead of Thread.sleep
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(emailText));

		String actualTooltipMessage = getAttribute(emailText, "validationMessage");
		Variables.emailFieldLevelMsg = actualTooltipMessage;

		return actualTooltipMessage.equals(expectedErrMsg);
	}

	public AccountSuccessPage validateTelephoneField(String firstNameData, String lastNameData, String emailData,
			String telephoneData, String passwordData, String confirmPassData) {
		enterFirstName(firstNameData);
		enterLastName(lastNameData);
		enterValidEmail(emailData);
		enterTelephoneNumber(telephoneData);
		enterValidPassword(passwordData);
		enterConfirmPassword(confirmPassData);
		checkPrivacyPolicy();
		clickOnContinueButton();
		return new AccountSuccessPage(driver);
	}

	public boolean getPlaceholderText(String labelName, String expectedPlaceholderValue) {
		List<WebElement> elements = getElements(INPUT_TEXT_FIELDS);
		for (WebElement element : elements) {
			String placeholder = getAttribute(element, "placeholder");
			if (labelName.equals(placeholder)) {
				return placeholder.equals(expectedPlaceholderValue);
			}
		}
		return false; // Returns false if the placeholder is not found or condition not met.
	}

	public boolean getAsteriskValues(String labelName, String expectedColor, String expectedSymbol) {
		List<WebElement> elements = getElements(MANDATORY_FIELDS_LABEL);
		for (WebElement element : elements) {
			String label = element.getText();
			if (labelName.equals(label)) {
				String color = getCssValue(element, "color");
				String content = getCssValue(element, "content");
				return color.equals(expectedColor) && content.contains(expectedSymbol);
			}
		}
		return false; // Returns false if the label is not found or condition not met.
	}

	public boolean getPrivacyPolicyStatus() {
		return isElementSelected(privacyPolicyCheckBox);
	}

	public boolean getTypeStatus(String testData) {
		enterValidPassword(testData);
		enterConfirmPassword(testData);
		String passType = getAttribute(passwordText, "type");
		String confirmPassType = getAttribute(confirmPasswordText, "type");
		return passType.equals("password") && confirmPassType.equals("password");
	}
}
