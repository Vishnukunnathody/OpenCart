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

	@Override
	public boolean retry(ITestResult result) {
		//ReportUtil.test.assignCategory(result.getMethod().getGroups());
		
	
		
		if (retryCount < maxRetryLimit) {
			retryCount++;
			ReportUtil.test.log(Status.WARNING, result.getName() +" :: ERROR,Retrying test"+" - Attempt " + retryCount);
			ReportUtil.logger.warn(result.getName() +" :: ERROR,Retrying test"+" - Attempt " + retryCount);
			return true; // Indicate that the test should be retried
		}
		return false; // Indicate that the test should not be retried
	}

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		System.out.println("Transforming: " + testMethod.getName());

		// Check if the retry analyzer is already set
		if (annotation.getRetryAnalyzerClass() != null) {
			annotation.setRetryAnalyzer(RetryUtil.class); // Sets RetryUtil as retry analyzer for all tests

		}
	}

}
