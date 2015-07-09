package it.sevenbits.springboottutorial.config;

/**
 * Created by sevenbits on 09.07.15.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.Properties;

@Configuration
public class EmailConfiguration {
        @Value("${mail.protocol}")
        private String protocol;
        @Value("${mail.host}")
        private String host;
        @Value("${mail.port}")
        private int port;
        @Value("${mail.smtp.auth}")
        private boolean auth;
        @Value("${mail.smtp.debug}")
        private boolean debug;
        @Value("${mail.smtp.starttls.enable}")
        private boolean starttlsEnabled;
        @Value("${mail.smtp.starttls.required}")
        private boolean starttlsRequired;
        @Value("${mail.username}")
        private String username;
        @Value("${mail.password}")
        private String password;

        @Bean
        public JavaMailSender javaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            Properties mailProperties = new Properties();

            mailProperties.put("mail.smtp.auth", auth);
            mailProperties.put("mail.smtp.starttls.enable", starttlsEnabled);
            mailProperties.put("mail.smtp.starttls.required", starttlsRequired);
            mailProperties.put("mail.smtp.debug", debug);

            mailSender.setJavaMailProperties(mailProperties);
            mailSender.setHost(host);
            mailSender.setPort(port);
            mailSender.setProtocol(protocol);
            mailSender.setUsername(username);
            mailSender.setPassword(password);

            return mailSender;
        }
}
