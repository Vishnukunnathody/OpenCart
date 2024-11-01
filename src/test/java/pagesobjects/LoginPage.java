package pagesobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	private final String loginInputTextFields = "//input[@class='form-control']";

	@FindBy(how = How.XPATH, using = "//span[text()='My Account']  ")
	private WebElement myAccountOption;

	@FindBy(how = How.XPATH, using = "(//a[text()='Login'])[1]")
	public WebElement logInOption;

	@FindBy(how = How.XPATH, using = "(//a[text()='Logout'])[1]")
	public WebElement logoutOption;

	@FindBy(how = How.ID, using = "input-email")
	private WebElement emailInputfield;

	@FindBy(how = How.ID, using = "input-password")
	private WebElement passwordInputfield;

	@FindBy(how = How.XPATH, using = "//input[@value='Login']")
	private WebElement loginBtn;

	@FindBy(how = How.XPATH, using = "//div[@class='alert alert-danger alert-dismissible']")
	private WebElement invalidCredentialWarMsg;

	@FindBy(how = How.XPATH, using = "//input[@id='input-password']/following-sibling::a")
	private WebElement forgotPasswordLink;

	@FindBy(how = How.XPATH, using = "//a[text()='Continue']")
	private WebElement continueBtn;

	@FindBy(how = How.XPATH, using = "//div[@class='alert alert-danger alert-dismissible']/i")
	private WebElement pleaseTryAgainIn1HourWarMsg;
	
	
	public void clickOnmyAccountOption() {
		clickElement(myAccountOption);
	}

	public void clickOnLoginOption() {
		clickElement(logInOption);
	}
	

	public void clickOnLogoutOption() {
		clickElement(logoutOption);
	}

	public void enterEmail(String EmailData) {
		emailInputfield.sendKeys(EmailData);
	}

	public void enterPassword(String PasswordData) {
		passwordInputfield.sendKeys(PasswordData);
	}

	public AccountSuccessPage clickLoginBtn() {
		clickElement(loginBtn);
		return new AccountSuccessPage(driver);
	}

	public String getInvalidCredentialWarningMsg() {
		return invalidCredentialWarMsg.getText();
	}

	public RegisterPage clickContinueBtn() {
		clickElement(continueBtn);
		return new RegisterPage(driver);
	}

	public AccountSuccessPage loginToTheApplication(String emailAddressData, String PasswordData) {
		enterEmail(emailAddressData);
		enterPassword(PasswordData);
		return clickLoginBtn();
	}

	public ForgotPasswordPage clickForgotPassLink() {
		clickElement(forgotPasswordLink);
		return new ForgotPasswordPage(driver);
	}

	public void logout() {
		clickOnmyAccountOption();
		clickOnLogoutOption();
	}
	

	public String getPleaseTryAgainIn1HourWarMsg() {
		return getText(pleaseTryAgainIn1HourWarMsg);
	}

	public boolean getPlaceHolderTexts(String fieldName, String ExpectedPlaceholderValue) {
		List<WebElement> elements = getElements(loginInputTextFields);

		for (WebElement element : elements) {
			String Placeholderval = getAttributeVal(element, "placeholder");// getting the placeholder text
			if (fieldName.equals(Placeholderval)) {
				System.out.println(Placeholderval + "Actual");
				System.out.println(ExpectedPlaceholderValue + "expected");
				return Placeholderval.equals(ExpectedPlaceholderValue);// returns true if fieldname passed equal to
				// Placeholderval and Placeholderval equal to ExpectedPlaceholderValue, else
				// returns false.
			}
		}
		return false;
	}

	public boolean tryLoginMaxAttempts(String emailAddressData, String PasswordData) {
		loginToTheApplication(emailAddressData, PasswordData);
		boolean success = true;
		int count = 0;
		for (int i = 0; i <= 5; i++) {
			clickLoginBtn();
			count = count + 1;
			System.out.println(count);
		}
		if (count == 6 && pleaseTryAgainIn1HourWarMsg.isDisplayed()) {
			return success;
		} else
			return success = false;

	}

	public boolean getPasswordFieldAttribute(String emailData, String passwordData) {
		boolean success = false;
		enterEmail(emailData);
		enterPassword(passwordData);
		String passCssval = getAttributeVal(passwordInputfield, "type");
		if (passCssval.equals("password")) {
			success = true;
		}
		return success;
	}

	public boolean PasswordCopyFunctionality(String passwordData) {
		boolean success = true;
		enterPassword(passwordData);
		copyTextFromField(passwordInputfield);
		String copiedText = getClipboardText();
		if (passwordData.equals(copiedText)) {
			success = false;
		}
		return success;
	}
	public boolean getPasswordFromPageSource (String emailData, String passwordData) {
		boolean success=true;
		enterEmail(emailData);
		enterPassword(passwordData);
		String pageSource=getPageSource(driver);
		if(pageSource.contains(passwordData)) {
			success=false;
		}
		return success;
	}
	public void changePasswordAndLoginWithNewCredentials(String emailAddressData, String PasswordData) {
		loginToTheApplication(emailAddressData,PasswordData);
		
		
		
	}
}
