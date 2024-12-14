package com.hydroyura.prodms.archive.server.config.tls;

import com.hydroyura.prodms.archive.server.config.tls.cert.CertFinderManager;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

@RequiredArgsConstructor
public class TlsWebServerFactoryCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {


    private final CertFinderManager certFinderManager;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
//        var http = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//        http.setPort(8080);
//        http.setScheme("http");
//        http.setSecure(false);
//        http.setRedirectPort(8443);
//        factory.addAdditionalTomcatConnectors(http);


        //certFinderManager.findCert();

//        factory.addConnectorCustomizers(https -> {
//            https.setScheme("https");
//            https.setSecure(true);
//            https.setPort(8443); // Порт для HTTPS
//            https.setProperty("sslProtocol", "TLS");
//            https.setProperty("SSLEnabled", Boolean.TRUE.toString());
//
//            // Настройка SSL
//            SSLHostConfig sslHostConfig = new SSLHostConfig();
//            sslHostConfig.setHostName("localhost");
//            //sslHostConfig.setPort(8443);
//
//
//
//
//
//
//
//            https.addSslHostConfig(sslHostConfig);
//        });
    }
}
