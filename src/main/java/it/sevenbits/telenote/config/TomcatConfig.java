package it.sevenbits.telenote.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Configurates tomcat connectors.
 */
@Configuration
public class TomcatConfig {

    private static final Logger LOG = LoggerFactory.getLogger(TomcatConfig.class);

    @Value("${connectors.http.port}")
    private int httpPort;

    @Value("${connectors.https.enabled}")
    private Boolean httpsEnabled;
    @Value("${connectors.https.port}")
    private int httpsPort;
    @Value("${connectors.https.keystoreFile}")
    private Resource keystoreFile;
    @Value("${connectors.https.keystorePass}")
    private String keystorePass;
    @Value("${connectors.https.keyAlias}")
    private String keyAlias;
    @Value("${connectors.https.keystoreType}")
    private String keystoreType;

    /**
     * If httpsEnabled is true, creates https connector and http connector that redirects to https.
     * If httpsEnabled is false, returns default.
     * @return
     */
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        if (httpsEnabled) {
            TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory() {
                @Override
                protected void postProcessContext(Context context) {
                    SecurityConstraint securityConstraint = new SecurityConstraint();
                    securityConstraint.setUserConstraint("CONFIDENTIAL");
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern("/*");
                    securityConstraint.addCollection(collection);
                    context.addConstraint(securityConstraint);
                }
            };

            tomcatFactory.addConnectorCustomizers(sslConnectorCustomizer());
            tomcatFactory.addAdditionalTomcatConnectors(initiateHttpConnector());
            return tomcatFactory;
        }

        return new TomcatEmbeddedServletContainerFactory();
    }

    /**
     * Initiates http connector that redirects to httpsPort.
     * @return http connector that redirects to httpsPort.
     */
    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);

        return connector;
    }

    /**
     * Customize ssl connector. If httpsEnabled is true, creates ssl connector with specified settings.
     * If httpsEnabled is false, does nothing.
     * @return ssl connector or empty method.
     */
    @Bean(name = "tomcatSslConnectorCustomizer")
    public TomcatConnectorCustomizer sslConnectorCustomizer() {
        if (httpsEnabled) {
            return new TomcatConnectorCustomizer() {
                @Override public void customize(Connector connector) {
                    connector.setSecure(true);
                    connector.setScheme("https");
                    connector.setProxyPort(443);
                    connector.setAttribute("keyAlias", keyAlias);
                    connector.setAttribute("keystorePass", keystorePass);
                    connector.setAttribute("keystoreType", keystoreType);
                    try {
                        connector.setAttribute("keystoreFile", keystoreFile.getFile().getAbsolutePath());
                    } catch (IOException e) {
                        LOG.error("cannot load keystore", e);
                        throw new IllegalStateException("Cannot load keystore", e);
                    }
                    connector.setAttribute("clientAuth", "false");
                    connector.setAttribute("sslProtocol", "TLS");
                    connector.setAttribute("SSLEnabled", true);
                }
            };
        }

        return new TomcatConnectorCustomizer() {
            @Override public void customize(Connector connector) { }
        };
    }
}
