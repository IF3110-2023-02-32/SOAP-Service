package utils;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailUtil {
    private static Session session = null;

    private static Session getSession() {
        if (session == null) {
            try {
                String host = System.getenv("EMAIL_HOST");
                String username = System.getenv("EMAIL_USERNAME");
                String password = System.getenv("EMAIL_PASSWORD");
                String port = System.getenv("EMAIL_PORT");
                Properties properties = new Properties();
                properties.put("mail.smtp.host", host);
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.port", port);
                session = Session.getInstance(properties, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return session;
    }

    public static void sendEmail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(System.getenv("EMAIL_USERNAME")));
            message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
