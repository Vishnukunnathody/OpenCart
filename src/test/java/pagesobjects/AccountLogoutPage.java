package pagesobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import base.BasePage;

public class AccountLogoutPage extends BasePage {

	public AccountLogoutPage(WebDriver driver) {
		super(driver);

	}

	@FindBy(how = How.XPATH, using = "//span[text()='My Account']")
	private WebElement myAccountOption;

	@FindBy(how = How.XPATH, using = "//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Login']")
	private WebElement loginOption;
	
	

	public void clickOnMyAccountBtn() {
		clickElement(myAccountOption);
	}
	public boolean displayLoginOption() {
		clickOnMyAccountBtn();
		return isElementVisible(loginOption);
	}
	public LoginPage clickOnLoginBtn() {
		clickElement(myAccountOption);
		clickElement(loginOption);
		return new LoginPage(driver);
	}
	
	
}