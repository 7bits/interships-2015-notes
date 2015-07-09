package it.sevenbits.springboottutorial.web.service;

/**
 * Created by sevenbits on 08.07.15.
 */
//import com.ipastushenko.core.service.exception.ServiceException;
//import org.apache.log4j.Logger;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(System.getProperty("mail.username"));
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
        } catch (MailException e) {
            //log.error("Email did send", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
