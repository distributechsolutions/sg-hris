package io.distributechsolutions.hris.services;

import io.distributechsolutions.hris.utils.StringUtil;

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
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired private JavaMailSender javaMailSender;

    public void sendWelcomeEmailForNewUser(String emailTo, String fullName, String username, String password) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        logger.info("Sending welcome email for new user.");

        try {
            mimeMessage.setFrom(new InternetAddress("gdpags5@yahoo.com"));
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, emailTo);
            mimeMessage.setSubject("Victory Ltd HR and Payroll User Access");

            // Get the email HTML template in the resources folder.
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("META-INF/resources/html/welcome_email_template.html");
            String htmlTemplate = StringUtil.readContentFromInputStream(inputStream);

            // Replace the placeholders.
            htmlTemplate = htmlTemplate.replace("${fullname}", fullName);
            htmlTemplate = htmlTemplate.replace("${username}", username);
            htmlTemplate = htmlTemplate.replace("${password}", password);

            // Set the email's content to be the HTML template and send.
            mimeMessage.setContent(htmlTemplate, "text/html; charset=utf-8");
            javaMailSender.send(mimeMessage);

            logger.info("Done sending welcome email for new user.");
        } catch (MessagingException | IOException e) {
            logger.info("There is an error in sending welcome email for new user.", e);
        }
    }
}
