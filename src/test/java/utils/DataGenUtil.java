package utils;

import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class DataGenUtil {

	// RandomStringUtils is the library from commons-lang3 -- dependency, used to
	// create random Strings, nums etc

	public static String randomString(int maxlength) {
		String generatedstring = RandomStringUtils.randomAlphabetic(maxlength);
		return generatedstring.toUpperCase();
	}

	public String randomEmail(int maxlength) {
		String generatedEmail = RandomStringUtils.randomAlphabetic(maxlength);
		return (generatedEmail.toUpperCase() + "@gmail.com");
	}

	public static String randomNum(int maxlength) {
		String generatedNum = RandomStringUtils.randomNumeric(maxlength);
		return generatedNum;
	}

	public static String randomPass() {
		String generatedPass = randomString(4) + randomNum(3) + "@";
		return generatedPass;
	}

	// generateRandomInValidEmail using time stamp.
	public static String generateRandomNewEmail() {
		Date date = new Date();
		String randomemail = "vish" + date.toString().replaceAll(" ", "_").replaceAll(":", "_") + "@gmail.com";
		return randomemail;
	}

	// generate valid email.
	public String getRandomvalidEmail() {
		String[] validEmails = { "vishnu.71881@gmail.com", "vishnu.71882@gmail.com", "vishnu.71883@gmail.com",
				"vishnu.71884@gmail.com", "vishnu.71885@gmail.com", "vishnu.71886@gmail.com",
				"vishnu.71889@gmail.com" };
		return validEmails[new Random().nextInt(7)];

	}

}
