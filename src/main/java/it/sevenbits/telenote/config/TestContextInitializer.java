package it.sevenbits.telenote.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

/**
 * Created by sevenbits on 11.09.15.
 */
public class TestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static Logger LOG = LoggerFactory.getLogger(TestContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        PropertySourceLoader loader = new YamlPropertySourceLoader();

        String path = System.getProperty("configPath");
        if (path == null) {
            LOG.debug(this.getClass() + ": No costum config, load default....");
            path = new String("src/main/resources/config/application-test.yml");
        }

        try {
            PropertySource source = loader.load("application", new FileSystemResource(path), null);

            applicationContext.getEnvironment().getPropertySources().addFirst(source);
        } catch (IOException e) {
            LOG.error("cant load test config " + path);
        }
    }
}
