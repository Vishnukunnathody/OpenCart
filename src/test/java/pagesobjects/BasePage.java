package pagesobjects;

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

import org.apache.commons.lang3.StringUtils;
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
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

	final int pollingInMilliSeconds = 500;
	final int defaultTimeoutInSeconds = 30;

	WebDriver driver;
	Actions actions;
	WebDriverWait wait;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.actions = new Actions(driver);

	}

	public String getTitle() {
		return driver.getTitle();
	}

	protected Wait<WebDriver> createWebDriverWait(int timeout) {
		return new FluentWait<>(driver).pollingEvery(Duration.ofMillis(pollingInMilliSeconds))
				.withTimeout(Duration.ofSeconds(timeout)).ignoring(Exception.class);
	}

	protected void waitForElementVisible(WebElement element, int timeout) {
		Wait<WebDriver> wait = createWebDriverWait(timeout);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	protected void refresh(WebDriver driver) {
		driver.navigate().refresh();
	}

	protected void navigateForward(WebDriver driver) {
		driver.navigate().forward();
	}

	public void navigateBack(WebDriver driver) {
		driver.navigate().back();
	}

	protected void getNewWindowTab(WebDriver driver) {
		driver.switchTo().newWindow(WindowType.TAB);
	}

	protected void getNewWindow(WebDriver driver) {
		driver.switchTo().newWindow(WindowType.WINDOW);
	}

	protected void SwitchToParentFrame(WebDriver driver) {
		driver.switchTo().parentFrame();
	}

	protected void clickElement(WebElement element) {
		waitForElementVisible(element, defaultTimeoutInSeconds);
		actions.click(element).build().perform();
	}

	protected void enterText(WebElement element, String text) {
		element.clear();
		int length;
		if (StringUtils.isEmpty(element.getAttribute("value"))) {
			length = element.getText().length();
		} else {
			length = element.getAttribute("value").length();
		}
		for (int i = 0; i < length; i++) {
			element.sendKeys(Keys.BACK_SPACE);
		}
		element.sendKeys(text.trim());

	}

	protected String getText(WebElement element) {
		String text = element.getText();
		return text;
	}

	protected List<WebElement> getElements(String xpath) {
		List<WebElement> elements = driver.findElements(By.xpath(xpath));
		return elements;
	}

	protected String getAttributeVal(WebElement element, String attributeName) {
		return element.getAttribute(attributeName);
	}

	protected boolean verifyTextByAttribute(WebElement element, String attribute, String expectedText) {
		return expectedText.equals(element.getAttribute(attribute));
	}

	protected String getCssValue(WebElement element, String cssProperty) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (String) js.executeScript(
				"return window.getComputedStyle(arguments[0], '::before').getPropertyValue(arguments[1]);", element,
				cssProperty);
	}

	protected boolean checkIfSelected(WebElement element) {
		return element.isSelected();
	}

	protected boolean isElementVisible(WebElement element) {
		try {
			return element != null && element.isDisplayed();
		} catch (NoSuchElementException | StaleElementReferenceException e) {
			return false;
		}
	}

	protected void copyTextFromField(WebElement field) {
		Actions actions = new Actions(driver);
		actions.moveToElement(field).click().sendKeys(Keys.CONTROL + "a").perform(); // Select all
		field.sendKeys(Keys.CONTROL + "c"); // Copy selected text
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

	public String getPageSource(WebDriver driver) {
		String pageSource = driver.getPageSource();
		return pageSource;
	}
	// talking screen shot

			public String captureScreen(String tname) throws IOException {

				String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

				TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
				File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

				String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
				File targetFile = new File(targetFilePath);

				sourceFile.renameTo(targetFile);
				return targetFilePath;

			}

}
