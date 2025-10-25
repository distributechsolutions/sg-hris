package io.softwaregarage.hris.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * An email utility class that provides the following functions:
 * 1.  Welcome email for new user with temporary password.
 *
 * @author Gerald Paguio
 */
@Service
public class EmailUtil {
    private final Logger logger = LoggerFactory.getLogger(EmailUtil.class);
    @Autowired private JavaMailSender javaMailSender;
    private final ClassLoader classLoader = this.getClass().getClassLoader();;

    private String readContent(InputStream inputStream) throws IOException {
        return StringUtil.readContentFromInputStream(inputStream);
    }

    public void sendWelcomeEmailForNewUser(String emailTo, String fullName, String username, String password) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        logger.info("Sending welcome email for new user.");

        try {
            mimeMessage.setFrom(new InternetAddress("gdpags5@yahoo.com"));
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, emailTo);
            mimeMessage.setSubject("Software Garage HRIS User Access");

            // Get the email HTML template in the resources folder.
            InputStream inputStream = classLoader.getResourceAsStream("META-INF/resources/html/welcome_email_template.html");
            String welcomeTemplate = this.readContent(inputStream);

            // Replace the placeholders.
            welcomeTemplate = welcomeTemplate.replace("${fullname}", fullName);
            welcomeTemplate = welcomeTemplate.replace("${username}", username);
            welcomeTemplate = welcomeTemplate.replace("${password}", password);

            // Set the email's content to be the HTML template and send.
            mimeMessage.setContent(welcomeTemplate, "text/html; charset=utf-8");
            javaMailSender.send(mimeMessage);

            logger.info("Done sending welcome email for new user.");
        } catch (MessagingException | IOException e) {
            logger.info("There is an error in sending welcome email for new user.", e);
        }
    }

    public void sendForgotPasswordEmail(String emailTo, String fullName, String username, String password) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        logger.info("Sending forgot password email to the user.");

        try {
            mimeMessage.setFrom(new InternetAddress("gdpags5@yahoo.com"));
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, emailTo);
            mimeMessage.setSubject("Software Garage HRIS User Forgot Password");

            // Get the email HTML template in the resources folder.
            InputStream inputStream = classLoader.getResourceAsStream("META-INF/resources/html/forgot_password_email_template.html");
            String forgotPasswordTemplate = this.readContent(inputStream);

            // Replace the placeholders.
            forgotPasswordTemplate = forgotPasswordTemplate.replace("${fullname}", fullName);
            forgotPasswordTemplate = forgotPasswordTemplate.replace("${username}", username);
            forgotPasswordTemplate = forgotPasswordTemplate.replace("${password}", password);

            // Set the email's content to be the HTML template and send.
            mimeMessage.setContent(forgotPasswordTemplate, "text/html; charset=utf-8");
            javaMailSender.send(mimeMessage);

            logger.info("Done sending forgot password email to the user.");
        } catch (MessagingException | IOException e) {
            logger.info("There is an error in sending forgot password email to the user");
        }
    }
}
