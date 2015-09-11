package it.sevenbits.telenote;

import it.sevenbits.telenote.config.CustomContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.env.YamlPropertySourceLoader;

import java.util.ArrayList;

/**
 * @see <a href="https://spring.io/guides/gs/spring-boot/">Official guide</a>
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);

        ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>> list = new ArrayList<>();
        list.add(new CustomContextInitializer(new YamlPropertySourceLoader(), "src/main/resources/config/", "application", ".yml"));

        app.setInitializers(list);
        app.setShowBanner(false);
        app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}