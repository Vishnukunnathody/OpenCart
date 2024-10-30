package pagesobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ForgotPasswordPage extends BasePage {

	public ForgotPasswordPage(WebDriver driver) {
		super(driver);
		
	}
	
	@FindBy(how=How.XPATH,using ="//div[@id='content']/h1")
	private WebElement forgotYourPasswordLabel;

	
	public String getForgotYourPasswordLabelText() {
		return getText(forgotYourPasswordLabel);
	}
}
