package it.sevenbits.telenote.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

/**
 * Created by sevenbits on 07.09.15.
 */
public class CostumContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static Logger LOG = LoggerFactory.getLogger(CostumContextInitializer.class);

    private final String PROPERTY_SOURCE_NAME = "application";
    private final String CONFIG_NAME_SPLITTER = "-";

    private PropertySourceLoader loader;
    private String defaultConfigName;
    private String defaultConfigPath;
    private String defaultExtension;

    public CostumContextInitializer(PropertySourceLoader loader, String defaultConfigPath, String defaultConfigName, String extension) {
        this.loader = loader;
        this.defaultConfigName = defaultConfigName;
        this.defaultConfigPath = defaultConfigPath;
        this.defaultExtension = extension;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        String profile = System.getProperties().getProperty("profiles.active");
        String configPath = System.getProperties().getProperty("config.path");
        String configName = System.getProperties().getProperty("config.name");

        if (configPath == null) {
            configPath = defaultConfigPath;
        }
        if (configName == null) {
            configName = defaultConfigName;
        }
        if (profile == null) {
            profile = new String("");
        }

        PropertySource source = null;
        FileSystemResource resource = new FileSystemResource(configPath + configName
                + CONFIG_NAME_SPLITTER + profile + defaultExtension);

        if (!resource.exists()) {
            LOG.warn("Cant load config " + configPath + configName + CONFIG_NAME_SPLITTER + profile + defaultExtension);
            LOG.info("Loading default config " + configPath + configName);

            resource = new FileSystemResource(defaultConfigPath + defaultConfigName + defaultExtension);
            if (resource.exists()) {
                try {
                    source = loader.load(PROPERTY_SOURCE_NAME, resource, null);
                } catch (IOException e) {
                    LOG.error("Cant read default config " + resource.getPath());
                }
            } else {
                LOG.error("Default config is not exist " + resource.getPath());
            }
        } else {
            try {
                source = loader.load(PROPERTY_SOURCE_NAME, resource, null);
            } catch (IOException e) {
                LOG.error("Cant read config " + resource.getPath());
            }
        }

        environment.getPropertySources().addFirst(source);
    }
}