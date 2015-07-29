package it.sevenbits.springboottutorial.web.service;

/**
 * Created by sevenbits on 08.07.15.
 */
//import com.ipastushenko.core.service.exception.ServiceException;
//import org.apache.log4j.Logger;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service for sends emails
 */
@Service
public class EmailService {
    //private static final Logger log = Logger.getLogger(EmailService.class.getName());
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String body) throws ServiceException {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(body, "text/html");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(System.getProperty("mail.username"));

            mailSender.send(mimeMessage);
        } catch (MailException e) {
            //log.error("Email did send", e);
            throw new ServiceException(e.getMessage(), e);
        } catch (MessagingException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
