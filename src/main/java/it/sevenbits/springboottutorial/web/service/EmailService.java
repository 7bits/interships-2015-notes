package it.sevenbits.springboottutorial.web.service;

/**
 * Created by sevenbits on 08.07.15.
 */
//import com.ipastushenko.core.service.exception.ServiceException;
//import org.apache.log4j.Logger;

import com.sun.javafx.collections.MappingChange;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.view.JadeViewResolver;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.template.JadeTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.util.HashMap;

/**
 * service for sends emails
 */
@Service
public class EmailService {
    //private static final Logger log = Logger.getLogger(EmailService.class.getName());
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JadeConfiguration jade;

    private void sendMail(String to, String subject, String body) throws ServiceException {
        JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender;

        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(body, "text/html; charset=\"UTF-8\"");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());

            sender.send(mimeMessage);
        } catch (MailException e) {
            //log.error("Email did send", e);
            throw new ServiceException(e.getMessage(), e);
        } catch (MessagingException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void sendConfirm(String to, String subject, String link) throws ServiceException  {
        try {
            JadeTemplate template = jade.getTemplate("home/confirmRegMail");
            HashMap<String, Object> model = new HashMap<String, Object>();
            model.put("confirmLink", link);

            String html = jade.renderTemplate(template, model);

            sendMail(to, subject, html);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
