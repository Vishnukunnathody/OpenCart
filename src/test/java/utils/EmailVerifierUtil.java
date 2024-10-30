package utils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.util.*;
import java.util.regex.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmailVerifierUtil {
	private static final String IMAP_HOST = "imap.mail.outlook.com";
    private static final String IMAP_PORT = "993";
    private static final String USERNAME = "vishnu.test123@outlook.com"; // Replace with your Yahoo email
    private static final String PASSWORD = "Password@1";               // Replace with your Yahoo password

    public static boolean verifyEmailAndLinks(String expectedSender, String expectedBody, String expectedSubject, List<String> expectedRedirectURLs) {
        Properties properties = new Properties();
        properties.put("mail.imap.host", IMAP_HOST);
        properties.put("mail.imap.port", IMAP_PORT);
        properties.put("mail.imap.ssl.enable", "true");

        Session emailSession = Session.getDefaultInstance(properties);
        boolean success = false;

        try {
            // Connect to the store
            Store store = emailSession.getStore("imap");
            store.connect(IMAP_HOST, USERNAME, PASSWORD);

            // Open the inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Search for unseen messages
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (Message message : messages) {
                String senderEmail = ((InternetAddress) message.getFrom()[0]).getAddress();
                String subject = message.getSubject();
                String body = message.getContent().toString();

                // Verify sender email
                if (!senderEmail.equals(expectedSender)) {
                    System.out.println("Sender email doesn't match.");
                    continue;
                }

                // Verify subject
                if (!subject.equals(expectedSubject)) {
                    System.out.println("Subject doesn't match.");
                    continue;
                }

                // Verify body
                if (!body.contains(expectedBody)) {
                    System.out.println("Email body doesn't match.");
                    continue;
                }

                // Extract and verify links from the email body
                List<String> links = extractLinks(body);
                if (links.isEmpty()) {
                    System.out.println("No links found in the email body.");
                } else {
                    System.out.println("Found links in the email body:");
                    int i = 0;
                    for (String link : links) {
                        System.out.println("Verifying link: " + link);
                        String finalURL = followRedirect(link);
                        
                        if (expectedRedirectURLs.size() > i && finalURL.equals(expectedRedirectURLs.get(i))) {
                            System.out.println("Link redirects to the correct page: " + finalURL);
                        } else {
                            System.out.println("Link redirects to an unexpected page: " + finalURL);
                        }
                        i++;
                    }
                }

                success = true;
                break;
            }

            // Close resources
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return success flag based on matches found
        return success;
    }

    // Extract URLs from email body
    private static List<String> extractLinks(String text) {
        List<String> links = new ArrayList<>();
        String regex = "\\bhttps?://[a-zA-Z0-9./?=_-]+\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            links.add(matcher.group());
        }
        return links;
    }

    // Follow redirects and return the final URL
    private static String followRedirect(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false); // Disable auto-following redirects

            int statusCode = connection.getResponseCode();

            // Check if it's a redirect (3xx)
            if (statusCode >= 300 && statusCode < 400) {
                String redirectURL = connection.getHeaderField("Location");
                System.out.println("Redirect found: " + redirectURL);
                if (redirectURL != null) {
                    return followRedirect(redirectURL); // Recursively follow the redirect
                }
            } else if (statusCode >= 200 && statusCode < 300) {
                // Return the final URL if it's successful
                return link;
            }
        } catch (Exception e) {
            System.out.println("Error verifying link: " + link);
            e.printStackTrace();
        }
        return link;
    }

    public static void main(String[] args) {
        // Example with multiple expected URLs
        List<String> expectedRedirectURLs = Arrays.asList(
                "https://expected-final-url1.com",
                "https://expected-final-url2.com"
        );

        boolean result = verifyEmailAndLinks(
                "expectedSender@example.com", 
                "expected email body", 
                "expected subject", 
                expectedRedirectURLs
        );
        System.out.println("Verification result: " + result);
    }
}