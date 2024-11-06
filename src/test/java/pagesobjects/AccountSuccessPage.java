package pagesobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import base.BasePage;

public class AccountSuccessPage extends BasePage {

    public AccountSuccessPage(WebDriver driver) {
        super(driver);
    }

    // Web Elements
    @FindBy(xpath ="//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Logout']")
    private WebElement logoutOption;

    @FindBy(xpath = "//span[text()='My Account']")
    private WebElement myAccountOption;

    @FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Logout']")
    private WebElement logOutOptionUnderMyAccount;

    @FindBy(how = How.XPATH, using = "//div[@class='alert alert-success alert-dismissible']")
    private WebElement passwordUpdatedSuccessMsg;

    @FindBy(xpath = "//a[text()='Change your password']")
    private WebElement changePasswordLink;

    @FindBy(how = How.XPATH, using = "//a[@class='list-group-item'][text()='Logout']")
    private WebElement rightColumnLogoutOption;

    // Methods

    // Check if logout option is visible under My Account dropdown
    public boolean isLogoutOptionDisplayed() {
        clickElement(myAccountOption);
        return isElementVisible(logoutOption);
    }

    // Click on My Account dropdown
    public void clickOnMyAccountOption() {
        clickElement(myAccountOption);
    }

    // Select Logout option from My Account dropdown
    public AccountLogoutPage selectLogoutOptionFromMainMenu() {
        clickElement(myAccountOption);
        clickElement(logOutOptionUnderMyAccount);
        return new AccountLogoutPage(driver);
    }

    // Check if password updated success message is displayed
    public boolean isPasswordUpdatedSuccessMsgDisplayed() {
        return isElementVisible(passwordUpdatedSuccessMsg);
    }

    // Click on Change Password link
    public ChangePasswordPage clickOnChangePasswordLink() {
        clickElement(changePasswordLink);
        return new ChangePasswordPage(driver);
    }

    // Click Logout option in the right column
    public AccountLogoutPage clickSidebarLogoutLink() {
        clickElement(rightColumnLogoutOption);
        return new AccountLogoutPage(driver);
    }
}