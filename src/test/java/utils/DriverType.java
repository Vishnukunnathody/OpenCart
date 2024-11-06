package utils;

import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;

public class DriverType {
	private static final Logger logger = LogManager.getLogger(DriverType.class);
	
	public static WebDriverListener eventlistener;

	public static EventFiringDecorator<WebDriver> e_driver;
	 

	public static WebDriver getDriverType(Properties prop, String browserName) {
		WebDriver driver = null;
		if (browserName == null) {
			throw new IllegalArgumentException("Browser name not specified in properties file");
		}
		switch (browserName.toLowerCase()) {
		case "chrome":
			driver = new ChromeDriver();
			logger.info("Browser :: Chrome");
			break;
		case "edge":
			driver = new EdgeDriver();
			logger.info("Browser :: Edge");
			break;
		case "firefox":
			driver = new FirefoxDriver();
			logger.info("Browser :: FireFox");
			break;
		default:
			throw new IllegalArgumentException("Invalid browser name specified in properties file");
		}
		driver.manage().window().setSize(new Dimension(1920, 1080));
		//driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ThreadUtil.PAGE_LOAD_TIMEOUT));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ThreadUtil.IMPLICIT_WAIT));
		return driver;
}

}
