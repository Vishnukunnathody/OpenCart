/*
 * Author : Vishnu Kunnathody
 * This is the foundational class in this DataDriven Frame work. This class contains a constructor that 
 * initializes pagefactory, and Webdriver , ActionsClass and WebDriverWait.This class also contains Reusable methods
 * for Wait mechanism, Element interaction,Alert handling,JS executors ,Dropdown handling ,Iframe handling,
 * Keybord and mouse actions,etc..
 * This class is extended to all other Page classes.The Advantages are Reusability,MAintainability,
 * Readability,This also keep the code DRY(Dont repeat yourself.)
 */

package base;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    private static final int POLLING_IN_MILLISECONDS = 500;
    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 5;

    protected WebDriver driver;
    protected Actions actions;
    protected WebDriverWait wait;
    

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_IN_SECONDS));
    }

    // Waiting and Visibility Methods

    protected Wait<WebDriver> createWebDriverWait(int timeout) {
        return new FluentWait<>(driver)
                .pollingEvery(Duration.ofMillis(POLLING_IN_MILLISECONDS))
                .withTimeout(Duration.ofSeconds(timeout))
                .ignoring(Exception.class);
    }

    protected void waitForElementVisible(WebElement element, int timeout) {
        createWebDriverWait(timeout).until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementClickable(WebElement element, int timeout) {
        createWebDriverWait(timeout).until(ExpectedConditions.elementToBeClickable(element));
    }

    protected boolean waitForTextToBePresentInElement(WebElement element, String text, int timeout) {
        return createWebDriverWait(timeout).until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // Element Interactions

    protected void clickElement(WebElement element) {
        waitForElementClickable(element, DEFAULT_TIMEOUT_IN_SECONDS);
        element.click();
    }

    protected void enterText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text.trim());
    }

    protected String getText(WebElement element) {
        return element.getText();
    }

    protected String getAttribute(WebElement element, String attributeName) {
        return element.getAttribute(attributeName);
    }

    protected List<WebElement> getElements(String xpath) {
        try {
            return driver.findElements(By.xpath(xpath));
        } catch (NoSuchElementException e) {
            System.err.println("No elements found for the provided XPath: " + xpath);
            return List.of();
        }
    }

    protected boolean isElementVisible(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isElementEnabled(WebElement element) {
        return element.isEnabled();
    }

    protected boolean isElementSelected(WebElement element) {
        return element.isSelected();
    }

    // Alert Handling

    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    public String getAlertText() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        return alert.getText();
    }

    public void sendTextToAlert(String text) {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.sendKeys(text);
        alert.accept();
    }

    // JavaScript Executor Utility

    protected void clickElementByJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
    
 // Method to get the CSS value of a property for an element
    public String getCssValue(WebElement element, String cssProperty) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Using JavaScript to get the CSS value of a specific property
        return (String) js.executeScript(
            "return window.getComputedStyle(arguments[0], '::before').getPropertyValue(arguments[1]);", 
            element, cssProperty
        );
    }

    // Dropdown Handling

    protected void selectByVisibleText(WebElement dropdown, String visibleText) {
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    protected void selectByValue(WebElement dropdown, String value) {
        new Select(dropdown).selectByValue(value);
    }

    protected void selectByIndex(WebElement dropdown, int index) {
        new Select(dropdown).selectByIndex(index);
    }

    // IFrame Handling

    public void switchToIframeByIndex(int index) {
        driver.switchTo().frame(index);
    }

    public void switchToIframeByIdOrName(String idOrName) {
        driver.switchTo().frame(idOrName);
    }

    public void switchToIframeByElement(WebElement iframe) {
        driver.switchTo().frame(iframe);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // Keyboard and Mouse Actions

    protected void doubleClick(WebElement element) {
        actions.doubleClick(element).perform();
    }

    protected void rightClick(WebElement element) {
        actions.contextClick(element).perform();
    }

    protected void hoverOverElement(WebElement element) {
        actions.moveToElement(element).perform();
    }

    protected void dragAndDrop(WebElement source, WebElement target) {
        actions.dragAndDrop(source, target).perform();
    }

    protected void sendKeysWithControl(WebElement element, String keys) {
        element.sendKeys(Keys.chord(Keys.CONTROL, keys));
    }

    // Clipboard Utility

    protected void copyTextFromField(WebElement field) {
        field.sendKeys(Keys.CONTROL + "a");
        field.sendKeys(Keys.CONTROL + "c");
    }

    protected String getClipboardText() {
        String clipboardText = "";
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboardText = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clipboardText;
    }

    // Screenshot Utility

    public String captureScreenshot(String testName) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String screenshotDir = System.getProperty("user.dir") + "\\screenshots\\";
        new File(screenshotDir).mkdirs();

        String targetFilePath = screenshotDir + testName + "_" + timestamp + ".png";
        File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File targetFile = new File(targetFilePath);
        sourceFile.renameTo(targetFile);

        return targetFilePath;
    }

    // Window and Tab Handling

    public void openNewTab() {
        driver.switchTo().newWindow(WindowType.TAB);
    }

    public void openNewWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    public List<String> getAllWindowHandles() {
        return List.copyOf(driver.getWindowHandles());
    }

    public void closeCurrentWindow() {
        driver.close();
    }

    // Get Page Title, URL, and Source

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    // Navigation and Refresh

    protected void refresh() {
        driver.navigate().refresh();
    }

    protected void navigateForward() {
        driver.navigate().forward();
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public static void deleteCookies(WebDriver driver) {
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }
}