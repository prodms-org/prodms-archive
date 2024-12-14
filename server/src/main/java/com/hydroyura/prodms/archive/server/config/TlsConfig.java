package com.hydroyura.prodms.archive.server.config;

import com.hydroyura.prodms.archive.server.config.tls.TlsWebServerFactoryCustomizer;
import com.hydroyura.prodms.archive.server.config.tls.cert.CertFinderManager;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TlsConfig {


    @Bean
    WebServerFactoryCustomizer<?> tlsCustomizer(CertFinderManager certFinderManager) {
        return new TlsWebServerFactoryCustomizer(certFinderManager);
    }


}
