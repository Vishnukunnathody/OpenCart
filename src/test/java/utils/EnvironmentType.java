package utils;
import java.util.Properties;
import org.openqa.selenium.WebDriver;

public class EnvironmentType {

    public static void setEnvType(WebDriver driver, Properties prop) {
        if (driver == null || prop == null) {
            System.err.println("WebDriver or Properties object is null.");
            return;
        }

        String url = null;
        String env = prop.getProperty("environment");

        if (env == null) {
            System.err.println("Environment property is not specified.");
            return;
        }

        switch (env.toLowerCase()) {
            case "test":
                url = prop.getProperty("testurl");
                System.out.println("Environment :: TEST");
                break;
            case "stage":
                url = prop.getProperty("stageurl");
                System.out.println("Environment :: STAGE");
                break;
            default:
                System.err.println("Unknown environment specified: " + env);
                return;
        }

        if (url == null || url.isEmpty()) {
            System.err.println("URL not specified for environment: " + env);
            return;
        }

        driver.get(url);
        System.out.println("Navigated to: " + url);
    }
}
