package it.sevenbits.telenote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.LogManager;

/**
 * @see <a href="https://spring.io/guides/gs/spring-boot/">Official guide</a>
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
//        try {
//            LogManager.getLogManager().readConfiguration(
//                    Application.class.getResourceAsStream("/logging.properties"));
//        } catch (IOException e) {
//            System.err.println("Could not setup logger configuration: " + e.toString());
//        }
//        PropertyConfigurator.configure("log4j.properties");
        SpringApplication app = new SpringApplication(Application.class);

        app.setShowBanner(false);
        app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}