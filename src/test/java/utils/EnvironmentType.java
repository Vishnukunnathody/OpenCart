package utils;

import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnvironmentType {
    private static final Logger logger = LogManager.getLogger(EnvironmentType.class);
    
    /**
     * Sets the environment type and navigates the WebDriver to the specified URL.
     *
     * @param driver the WebDriver instance
     * @param prop the Properties containing environment configurations
     */
    public static void setEnvType(WebDriver driver, Properties prop,String env) {
        if (driver == null || prop == null) {
            System.err.println("WebDriver or Properties object is null.");
            logger.error("WebDriver or Properties object is null.");
            return;
        }
        
        //String env = prop.getProperty("environment");
        if (env == null) {
            System.err.println("Environment property is not specified.");
            logger.error("Environment property is not specified.");
            return;
        }
        
        String url = null;
        switch (env.toLowerCase()) {
            case "test":
                url = prop.getProperty("testurl");
               logger.info("Environment :: TEST");
                break;
            case "stage":
                url = prop.getProperty("stageurl");
                logger.info("Environment :: STAGE");
                break;
            default:
                logger.error("Unknown environment specified: " + env);
                return;
        }
        
        if (url == null || url.isEmpty()) {
            logger.error("URL not specified for environment: {}", env);
            return;
        }

        driver.get(url);
        logger.info("Navigated to URL: {}", url);
       // System.out.println("Navigated to: " + url);
    }
}
        
        
        
        
        
        
        
      
