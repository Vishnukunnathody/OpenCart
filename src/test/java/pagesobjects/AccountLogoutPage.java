/*Author: Vishnu Kunnathody
 * Each web page in the application is represented by a pageobject class.
 * This class extends BasePage, inheriting all its utility methods .
 * This class contains webelements Defined with @FindBy annotations .
 * Action methods that Defines various interaction with the elements.
 * Constructor that accepts a WebDriver instance, which is passed to the BasePage constructor using super(driver).
 */
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

    public boolean isLoginOptionDisplayed() {
        clickOnMyAccountBtn();
        return isElementVisible(loginOption);
    }
    public LoginPage clickOnLoginBtn() {
        clickOnMyAccountBtn();
        clickElement(loginOption);
        return new LoginPage(driver);
    }
}
