package pagesobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import base.BasePage;





public class LandingPage extends BasePage {

	public LandingPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(how = How.XPATH, using = "//span[@class='caret']")
	private WebElement myAccountDropMenu;

	@FindBy(how = How.XPATH, using = "//a[text()='Register']")
	private WebElement registerOption;
	
	@FindBy(xpath="//a[text()='Login']")
    private WebElement loginOption;
	
	
	//public boolean check
	
	public void clickmyAccountDropMenu() {
		clickElement(myAccountDropMenu);
	}

	public RegisterPage clickOnregisterOption() {
		clickElement(registerOption);
		return new RegisterPage(driver);
	}
	public RegisterPage navigateToRegisterPage() {
		clickElement(myAccountDropMenu);
		return clickOnregisterOption();
	}
	public LoginPage clickOnLoginOption() {
		clickElement(loginOption);
		return new LoginPage(driver);
	}
	public LoginPage navigateToLoginPage() {
		clickElement(myAccountDropMenu);
		return clickOnLoginOption();
	}
	public LoginPage registerFromLoginPage() {
		navigateToRegisterPage();
		navigateToLoginPage();
		return new LoginPage(driver);
		
	}
}
