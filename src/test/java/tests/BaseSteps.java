package tests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import pagesobjects.AccountLogoutPage;
import pagesobjects.AccountSuccessPage;
import pagesobjects.ChangePasswordPage;
import pagesobjects.ForgotPasswordPage;
import pagesobjects.LandingPage;
import pagesobjects.LoginPage;
import pagesobjects.RegisterPage;
import utils.ReportUtil;

public class BaseSteps {

	public static WebDriver driver;
	public Properties prop;

	LandingPage landingPage;
	RegisterPage registerPage;
	AccountSuccessPage accountSuccessPage;
	LoginPage loginPage;
	ForgotPasswordPage forgotPasswordPage;
	AccountLogoutPage accountLogoutPage;
	ChangePasswordPage changePasswordPage;

	@BeforeMethod(groups = { "Sanity", "Regression", "Master", "DataDriven","test" })
	@Parameters({ "os", "browser" })
	public void Setup(String os, String br) {

		try {
			prop = new Properties();
			File propfile = new File(System.getProperty("user.dir") + "/src/test/resources/projectdata.properties");
			FileReader fr = new FileReader(propfile);
			prop.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (br.toLowerCase()) {
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
			System.out.println("Invalid browser name ....");
			return;// returns means Exit from the execution.
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		ReportUtil.setDriver(driver);
		driver.get(prop.getProperty("url"));
	}

	@AfterMethod(groups = { "Sanity", "Regression", "Master", "DataDriven" ,"test"})
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

}
