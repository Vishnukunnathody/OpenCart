package pagesobjects;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import base.BasePage;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	private final String loginInputTextFields = "//input[@class='form-control']";

	@FindBy(how = How.XPATH, using = "//span[text()='My Account']")
	private WebElement myAccountOption;

	@FindBy(how = How.XPATH, using = "(//a[text()='Login'])[1]")
	public WebElement loginOption;

	@FindBy(how = How.XPATH, using = "(//a[text()='Logout'])[1]")
	public WebElement logoutOption;

	@FindBy(how = How.ID, using = "input-email")
	private WebElement emailInputField;

	@FindBy(how = How.ID, using = "input-password")
	private WebElement passwordInputField;

	@FindBy(how = How.XPATH, using = "//input[@value='Login']")
	private WebElement loginButton;

	@FindBy(how = How.XPATH, using = "//div[@class='alert alert-danger alert-dismissible']")
	private WebElement invalidCredentialWarningMessage;

	@FindBy(how = How.XPATH, using = "//input[@id='input-password']/following-sibling::a")
	private WebElement forgotPasswordLink;

	@FindBy(how = How.XPATH, using = "//a[text()='Continue']")
	private WebElement continueButton;

	@FindBy(how = How.XPATH, using = "//div[@class='alert alert-danger alert-dismissible']/i")
	private WebElement pleaseTryAgainIn1HourWarningMessage;

	// Clicks My Account option
	public void clickOnMyAccountOption() {
		clickElement(myAccountOption);
	}

	// Clicks Login option in the MainMenu
	public void clickOnLoginOption() {
		clickElement(loginOption);
	}

// Clicks Logout option
	public AccountLogoutPage clickOnLogoutOption() {
		clickElement(logoutOption);
		return new AccountLogoutPage(driver);
	}
	
    //clicks in the Continue button for the new customer.
	public RegisterPage clickOnNewCustomerRegisterContinueBtn() {
		clickElement(continueButton);
		return new RegisterPage(driver);

	}

	// Enters email in the email field
	public void enterEmail(String emailData) {
		emailInputField.sendKeys(emailData);
	}

	// Enters password in the password field
	public void enterPassword(String passwordData) {
		passwordInputField.sendKeys(passwordData);
	}

	// Clicks the Login button and returns the success page
	public AccountSuccessPage clickLoginButton() {
		clickElement(loginButton);
		return new AccountSuccessPage(driver);
	}

	// Retrieves the invalid credential warning message text
	public String getInvalidCredentialWarningMessage() {
		return invalidCredentialWarningMessage.getText();
	}

	// Navigates to Forgot Password page
	public ForgotPasswordPage clickForgotPasswordLink() {
		clickElement(forgotPasswordLink);
		return new ForgotPasswordPage(driver);
	}

	// Returns true if placeholder text matches expected value for given field
	public boolean isPlaceholderTextCorrect(String fieldName, String expectedPlaceholderValue) {
		List<WebElement> elements = getElements(loginInputTextFields);

		for (WebElement element : elements) {
			String placeholderValue = getAttribute(element, "placeholder");
			if (fieldName.equals(placeholderValue) && placeholderValue.equals(expectedPlaceholderValue)) {
				return true;
			}
		}
		return false;
	}

	public AccountSuccessPage performLogin(String emailAddress, String password) {
		// Log the login attempt
		System.out.println("Attempting to log in with email: " + emailAddress);

		// Validate inputs
		if (emailAddress == null || emailAddress.isEmpty() || password == null || password.isEmpty()) {
			System.out.println("Email or password is missing.");
			return null; // Alternatively, throw an IllegalArgumentException or handle appropriately
		}
		// Click login and return the appropriate page
		clickOnMyAccountOption();
		clickOnLoginOption();
		enterText(emailInputField, emailAddress);
		enterText(passwordInputField, password);
		return clickLoginButton();
	}

	// Attempts login multiple times and returns true if warning is displayed after
	// max attempts
	public boolean tryLoginMaxAttempts(String emailData, String passwordData) {
		performLogin(emailData, passwordData);
		int attempts = 6;

		for (int i = 1; i <= attempts; i++) {
			clickLoginButton();
		}
		return pleaseTryAgainIn1HourWarningMessage.isDisplayed();
	}

	// Verifies if password field type attribute is "password"
	public boolean isPasswordFieldSecured(String emailData, String passwordData) {
		enterEmail(emailData);
		enterPassword(passwordData);
		return "password".equals(getAttribute(passwordInputField, "type"));
	}

	// Verifies that password is not copied to clipboard
	public boolean isPasswordNonCopyable(String passwordData) {
		enterPassword(passwordData);
		copyTextFromField(passwordInputField);
		return !passwordData.equals(getClipboardText());
	}

	// Verifies password is not visible in page source
	public boolean isPasswordHiddenInPageSource(String emailData, String passwordData) {
		enterEmail(emailData);
		enterPassword(passwordData);
		return !getPageSource().contains(passwordData);
	}

}