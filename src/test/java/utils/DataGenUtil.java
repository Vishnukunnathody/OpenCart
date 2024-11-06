package utils;

import java.util.Date;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

public class DataGenUtil {

    private static final Random RANDOM = new Random();

    // Generate a random string of a specified length
    public static String randomString(int length) {
        return RandomStringUtils.randomAlphabetic(length).toUpperCase();
    }

    // Generate a random email address
    public static String randomEmail(int length) {
        String generatedEmail = RandomStringUtils.randomAlphabetic(length);
        return generatedEmail.toUpperCase() + "@gmail.com";
    }

    // Generate a random numeric string
    public static String randomNum(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    // Generate a random password with a specific pattern
    public static String randomPass() {
        String upper = RandomStringUtils.randomAlphabetic(2).toUpperCase();
        String lower = RandomStringUtils.randomAlphabetic(2).toLowerCase();
        String number = randomNum(3);
        String special = RandomStringUtils.random(1, "!@#$%^&*()-_=+<>?");
        return upper + lower + number + special;
    }

    // Generate a unique random email using the current timestamp
    public static String generateRandomNewEmail() {
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        return "vish" + timestamp + "@gmail.com";
    }

    // Generate a random valid email from a predefined list
    public static String getRandomValidEmail() {
        String[] validEmails = {
            "vishnu.71881@gmail.com",
            "vishnu.71882@gmail.com",
            "vishnu.71883@gmail.com",
            "vishnu.71884@gmail.com",
            "vishnu.71885@gmail.com",
            "vishnu.71886@gmail.com",
            "vishnu.71889@gmail.com"
        };
        return validEmails[RANDOM.nextInt(validEmails.length)];
    }

    // (Optional) Generate an invalid email for testing
    public static String generateInvalidEmail() {
        String[] invalidEmails = {
            "plainaddress",
            "@missingusername.com",
            "username@.com",
            "username@domain..com",
            "username@domain.com."
        };
        return invalidEmails[RANDOM.nextInt(invalidEmails.length)];
    }
}