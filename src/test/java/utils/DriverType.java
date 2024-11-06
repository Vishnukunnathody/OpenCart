package utils;

import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.Parameters;

public class DriverType {
	public static WebDriverListener eventlistener;
	
	public static EventFiringDecorator<WebDriver> e_driver;
	
	public static WebDriver getDriverType(Properties prop) {
		WebDriver driver = null;
		String browserName = prop.getProperty("browser");
		
		if (browserName == null) {
			throw new IllegalArgumentException("Browser name not specified in properties file");
		}
		switch (browserName.toLowerCase()) {
		case "chrome":
			driver = new ChromeDriver();
			break;
		case "edge":
			driver = new EdgeDriver();
			break;
		case "firefox":
			driver = new FirefoxDriver();
			break;
		default:
			throw new IllegalArgumentException("Invalid browser name specified in properties file");
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ThreadUtil.PAGE_LOAD_TIMEOUT));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ThreadUtil.IMPLICIT_WAIT));
		return driver;

	}

}
