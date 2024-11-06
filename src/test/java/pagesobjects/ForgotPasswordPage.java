package pagesobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import base.BasePage;

public class ForgotPasswordPage extends BasePage {

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    // Web Elements
    @FindBy(how = How.XPATH, using = "//div[@id='content']/h1")
    private WebElement pageHeader;

    // Methods

    // Retrieve the text of the "Forgot Your Password" header
    public String getPageHeaderText() {
        return getText(pageHeader);
    }
}