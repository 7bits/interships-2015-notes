package it.sevenbits.telenote.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "assets")
public class AssetsResolver {
    private String version;

    public String getVersionedResourceUrl(final String filePath) {
        final String filePathVersioned = filePath.replace("#version", version);
        return filePathVersioned;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
