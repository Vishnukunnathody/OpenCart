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
     * @param env the environment name (e.g., test, stage)
     */
    public static void setEnvType(WebDriver driver, Properties prop, String env) {
        if (driver == null || prop == null) {
            logger.error("WebDriver or Properties object is null.");
            throw new IllegalArgumentException("WebDriver or Properties object cannot be null.");
        }

        if (env == null || env.isEmpty()) {
            logger.error("Environment property is not specified.");
            throw new IllegalArgumentException("Environment property cannot be null or empty.");
        }

        String url = getUrlForEnvironment(prop, env);
        if (url == null || url.isEmpty()) {
            logger.error("URL is not specified for the environment: {}", env);
            throw new IllegalStateException("URL not found for environment: " + env);
        }

        // Navigate to the URL
        driver.get(url);
        logger.info("Navigated to URL: {}", url);
    }

    /**
     * Helper method to fetch the URL based on environment type.
     *
     * @param prop the Properties object
     * @param env the environment name
     * @return the URL for the specified environment, or null if not found
     */
    private static String getUrlForEnvironment(Properties prop, String env) {
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
                logger.error("Unknown environment specified: {}", env);
                break;
        }
        return url;
    }
}