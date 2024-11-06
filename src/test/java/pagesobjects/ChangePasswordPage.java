package pagesobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import base.BasePage;

public class ChangePasswordPage extends BasePage {

    public ChangePasswordPage(WebDriver driver) {
        super(driver);
    }

    // Web Elements
    @FindBy(how = How.XPATH, using = "//h1[text()='Change Password']")
    private WebElement changePasswordLabel;

    @FindBy(how = How.XPATH, using = "//input[@id='input-password']")
    private WebElement passwordInput;

    @FindBy(how = How.XPATH, using = "//input[@id='input-confirm']")
    private WebElement confirmPasswordInput;

    @FindBy(how = How.XPATH, using = "//input[@class='btn btn-primary']")
    private WebElement continueButton;

    @FindBy(how = How.XPATH, using = "//a[@class='btn btn-default']")
    private WebElement backButton;

    // Methods

    // Enter password in the password input field
    public void enterPassword(String text) {
        enterText(passwordInput, text);
    }

    // Enter confirmation password
    public void enterConfirmPassword(String text) {
        enterText(confirmPasswordInput, text);
    }

    // Click the continue button and return AccountSuccessPage
    public AccountSuccessPage clickContinueButton() {
        clickElement(continueButton);
        return new AccountSuccessPage(driver);
    }

    // Change password by entering and confirming the password, then submitting
    public AccountSuccessPage changePassword(String text) {
        enterPassword(text);
        enterConfirmPassword(text);
        return clickContinueButton();
    }
}
