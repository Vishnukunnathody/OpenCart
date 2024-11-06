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

    // Web Elements
    @FindBy(how = How.XPATH, using = "//span[@class='caret']")
    private WebElement myAccountDropdown;

    @FindBy(how = How.XPATH, using = "//a[text()='Register']")
    private WebElement registerOption;

    @FindBy(xpath = "//a[text()='Login']")
    private WebElement loginOption;

    // Methods

    public void clickOnMyAccountDropdown() {
        clickElement(myAccountDropdown);
    }

    public RegisterPage navigateToRegisterPage() {
        clickOnMyAccountDropdown();
        clickElement(registerOption);
        return new RegisterPage(driver);
    }

    public LoginPage navigateToLoginPage() {
        clickOnMyAccountDropdown();
        clickElement(loginOption);
        return new LoginPage(driver);
    }

    
}