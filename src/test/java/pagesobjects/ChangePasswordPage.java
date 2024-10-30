package pagesobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ChangePasswordPage extends BasePage {

	public ChangePasswordPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	
	
	@FindBy(how = How.XPATH, using = "//h1[text()='Change Password']")
	WebElement ChangePasswordLabel;

	@FindBy(how = How.XPATH, using = "//input[@id='input-password']")
	WebElement enterPassword;

	@FindBy(how = How.XPATH, using = "//input[@id='input-confirm']")
	WebElement PasswordConfirm;
	
	@FindBy(how = How.XPATH, using = "//input[@class='btn btn-primary']")
	WebElement continueButton;
	
	@FindBy(how = How.XPATH, using = "//a[@class='btn btn-default']")
	WebElement backButton;
	
	
	public void enterPassword( String text) {
		enterText(enterPassword , text);
	}
	
	public void enterConfirmPassword( String text) {
		enterText(PasswordConfirm , text);
	}
	
	public AccountSuccessPage clickOncontinueBtn() {
		clickElement(continueButton);
		return new AccountSuccessPage(driver);
	}
	
	public AccountSuccessPage changePassword( String text) {
		enterPassword( text);
		enterConfirmPassword( text);
		return clickOncontinueBtn();
	}
	
	
	
	
	
	
	
	
	
}
