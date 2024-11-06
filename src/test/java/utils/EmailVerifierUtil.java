package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.util.*;
import java.util.regex.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailVerifierUtil {
    private static final String IMAP_HOST = "imap.mail.outlook.com";
    private static final String IMAP_PORT = "993";
    private static final String USERNAME = System.getenv("EMAIL_USERNAME"); // Use environment variable
    private static final String PASSWORD = System.getenv("EMAIL_PASSWORD"); // Use environment variable
    private static final Logger logger = LogManager.getLogger(EmailVerifierUtil.class);

    public static boolean verifyEmailAndLinks(String expectedSender, String expectedBody, String expectedSubject, List<String> expectedRedirectURLs) {
        Properties properties = createEmailProperties();
        Session emailSession = Session.getDefaultInstance(properties);
        
        try (Store store = emailSession.getStore("imap")) {
            store.connect(IMAP_HOST, USERNAME, PASSWORD);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            return processMessages(messages, expectedSender, expectedBody, expectedSubject, expectedRedirectURLs);
        } catch (Exception e) {
            logger.error("Error during email verification: ", e);
            return false;
        }
    }

    private static Properties createEmailProperties() {
        Properties properties = new Properties();
        properties.put("mail.imap.host", IMAP_HOST);
        properties.put("mail.imap.port", IMAP_PORT);
        properties.put("mail.imap.ssl.enable", "true");
        return properties;
    }

    private static boolean processMessages(Message[] messages, String expectedSender, String expectedBody, String expectedSubject, List<String> expectedRedirectURLs) {
        for (Message message : messages) {
            try {
                String senderEmail = ((InternetAddress) message.getFrom()[0]).getAddress();
                String subject = message.getSubject();
                String body = message.getContent().toString();

                if (isEmailValid(senderEmail, expectedSender, subject, expectedSubject, body, expectedBody)) {
                    return verifyLinksInEmail(body, expectedRedirectURLs);
                }
            } catch (Exception e) {
                logger.error("Error processing message: ", e);
            }
        }
        return false;
    }

    private static boolean isEmailValid(String senderEmail, String expectedSender, String subject, String expectedSubject, String body, String expectedBody) {
        if (!senderEmail.equals(expectedSender)) {
            logger.warn("Sender email doesn't match.");
            return false;
        }
        if (!subject.equals(expectedSubject)) {
            logger.warn("Subject doesn't match.");
            return false;
        }
        if (!body.contains(expectedBody)) {
            logger.warn("Email body doesn't match.");
            return false;
        }
        return true;
    }

    private static boolean verifyLinksInEmail(String body, List<String> expectedRedirectURLs) {
        List<String> links = extractLinks(body);
        if (links.isEmpty()) {
            logger.warn("No links found in the email body.");
            return false;
        }
        
        logger.info("Found links in the email body:");
        boolean allLinksVerified = true;
        
        for (int i = 0; i < links.size(); i++) {
            String link = links.get(i);
            logger.info("Verifying link: " + link);
            String finalURL = followRedirect(link);
            if (expectedRedirectURLs.size() > i && finalURL.equals(expectedRedirectURLs.get(i))) {
                logger.info("Link redirects to the correct page: " + finalURL);
            } else {
                logger.warn("Link redirects to an unexpected page: " + finalURL);
                allLinksVerified = false;
            }
        }
        
        return allLinksVerified;
    }

    private static List<String> extractLinks(String text) {
        List<String> links = new ArrayList<>();
        String regex = "\\bhttps?://[a-zA-Z0-9./?=_-]+\\b";
        Matcher matcher = Pattern.compile(regex).matcher(text);
        
        while (matcher.find()) {
            links.add(matcher.group());
        }
        return links;
    }

    private static String followRedirect(String link) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
            connection.setInstanceFollowRedirects(false); // Disable auto-following redirects
            int statusCode = connection.getResponseCode();

            if (statusCode >= 300 && statusCode < 400) {
                String redirectURL = connection.getHeaderField("Location");
                logger.info("Redirect found: " + redirectURL);
                if (redirectURL != null) {
                    return followRedirect(redirectURL); // Recursively follow the redirect
                }
            } else if (statusCode >= 200 && statusCode < 300) {
                return link; // Successful response
            }
        } catch (Exception e) {
            logger.error("Error verifying link: " + link, e);
        }
        return link; // Return original link if an error occurs
    }
}

    

       
       
