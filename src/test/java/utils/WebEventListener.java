package utils;
/*************************************** PURPOSE **********************************

- This class implements the WebDriverEventListener, which is included under events.
The purpose of implementing this interface is to override all the methods and define certain useful  Log statements 
which would be displayed/logged as the application under test is being run.

Do not call any of these methods, instead these methods will be invoked automatically
as an when the action done (click, findBy etc). 

*/

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import tests.BaseSteps;

public class WebEventListener extends BaseSteps implements WebDriverListener {
   

	private static final Logger logger = LogManager.getLogger(WebEventListener.class);

	

    public void beforeNavigateTo(String url, WebDriver driver) {
        logger.info("Before navigating to: {}", url);
        System.out.println("Before navigating to: '" + url + "'");
    }

    public void afterNavigateTo(String url, WebDriver driver) {
        logger.info("Navigated to: {}", url);
        System.out.println("Navigated to: '" + url + "'");
    }

    // Element interaction methods
    public void beforeClick(WebElement element) {
        logger.info("Trying to click on: {}", getElementDescription(element));
        System.out.println("Trying to click on: " + element.getAttribute("outerHTML"));
    }

    public void afterClick(WebElement element) {
        logger.info("Clicked on: {}", getElementDescription(element));
        System.out.println("Clicked on: " + element.getAttribute("outerHTML"));
    }

    public void beforeChangeValueOf(WebElement element, CharSequence[] keysToSend) {
        logger.info("Before changing value of element: {}", getElementDescription(element));
        System.out.println("Before changing value of element: " + element.getAttribute("outerHTML"));
    }

    public void afterChangeValueOf(WebElement element, CharSequence[] keysToSend) {
        logger.info("Changed value of element: {}", getElementDescription(element));
        System.out.println("Changed value of element: " + element.getAttribute("outerHTML"));
    }

    // FindBy methods
    public void beforeFindElement(By by, WebDriver driver) {
        logger.info("Trying to find Element By: {}", by);
        System.out.println("Trying to find Element By: " + by.toString());
    }

    public void afterFindElement(By by, WebDriver driver, WebElement element) {
        logger.info("Found Element By: {}", by);
        System.out.println("Found Element By: " + by.toString());
    }

    // Exception handling
    public void onError(WebDriver driver, Throwable error) {
        logger.error("Exception occurred: {}", error.getMessage(), error);
        System.out.println("Exception occurred: " + error);
    }

    // Navigation methods
    public void beforeNavigateBack(WebDriver driver) {
        logger.info("Navigating back to previous page");
        System.out.println("Navigating back to previous page");
    }

    public void afterNavigateBack(WebDriver driver) {
        logger.info("Navigated back to previous page");
        System.out.println("Navigated back to previous page");
    }

    public void beforeNavigateForward(WebDriver driver) {
        logger.info("Navigating forward to next page");
        System.out.println("Navigating forward to next page");
    }

    public void afterNavigateForward(WebDriver driver) {
        logger.info("Navigated forward to next page");
        System.out.println("Navigated forward to next page");
    }

    // Additional method implementations
    public void beforeClickOn(WebElement element, WebDriver driver) {
        logger.info("Trying to click on: {}", getElementDescription(element));
        System.out.println("Trying to click on: " + element.toString());
    }

    public void afterClickOn(WebElement element, WebDriver driver) {
        logger.info("Clicked on: {}", getElementDescription(element));
        System.out.println("Clicked on: " + element.toString());
    }

    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        logger.info("Trying to find Element By: {}", by);
        System.out.println("Trying to find Element By : " + by.toString());
    }

    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        logger.info("Found Element By: {}", by);
        System.out.println("Found Element By : " + by.toString());
    }

    private String getElementDescription(WebElement element) {
        try {
            return element.getTagName() + " with text: '" + element.getText() + "'";
        } catch (Exception e) {
            return "Unknown element";
        }
    }
}