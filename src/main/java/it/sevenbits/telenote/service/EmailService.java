package it.sevenbits.telenote.service;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.JadeTemplate;
import it.sevenbits.telenote.web.domain.forms.UserCreateForm;
import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;

/**
 * Service for sends emails
 */
@Service
public class EmailService {
    private static final Logger LOG = Logger.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JadeConfiguration jade;

    /**
     * Sends email to user with specified subject and body.
     * @param to user email.
     * @param subject subject of a message.
     * @param body body of a message.
     * @throws ServiceException
     */
    public void sendMail(String to, String subject, String body) throws ServiceException {
        JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender;

        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(body, "text/html; charset=\"UTF-8\"");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());

            sender.send(mimeMessage);
            LOG.info("Sended email to " + to);
        } catch (MailException e) {
            LOG.error(String.format("An error occurred while sending email. To: %s, From: %s.", to, sender.getUsername()));
            throw new ServiceException(e.getMessage(), e);
        } catch (MessagingException e) {
            LOG.error(String.format("An error occurred while setting email attributes. To: %s, Subject: %s, From:", to, subject, sender.getUsername()));
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
