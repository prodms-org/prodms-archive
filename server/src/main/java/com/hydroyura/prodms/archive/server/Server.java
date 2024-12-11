package com.hydroyura.prodms.archive.server;

import com.hydroyura.prodms.archive.server.props.ValidationProps;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ValidationProps.class)
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

}
