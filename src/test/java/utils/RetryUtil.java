package utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.aventstack.extentreports.Status;

public class RetryUtil implements IRetryAnalyzer, IAnnotationTransformer {

    private int retryCount = 0;
    private static final int maxRetryLimit = 1;

    /**
     * Retry failed test based on retry limit.
     *
     * @param result ITestResult containing test details
     * @return true if the test should be retried, false otherwise
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryLimit) {
            retryCount++;
            String message = String.format("%s :: ERROR, Retrying test - Attempt %d", result.getName(), retryCount);

            // Access the ExtentTest instance from ThreadLocal
            if (ReportUtil.test.get() != null) {
                ReportUtil.test.get().log(Status.WARNING, message);  // Log using the ExtentTest instance
            }
            
            ReportUtil.logger.warn(message);
            return true; // Indicate that the test should be retried
        }
        return false; // Indicate that the test should not be retried
    }

    /**
     * Automatically sets RetryUtil as the retry analyzer for all tests.
     *
     * @param annotation      The test annotation to transform
     * @param testClass       The test class (can be null)
     * @param testConstructor The test constructor (can be null)
     * @param testMethod      The test method to be transformed
     */
    @Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		System.out.println("Transforming: " + testMethod.getName());

		// Check if the retry analyzer is already set
		if (annotation.getRetryAnalyzerClass() != null) {
			annotation.setRetryAnalyzer(RetryUtil.class); // Sets RetryUtil as retry analyzer for all tests

		}
	}
}