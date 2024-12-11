package com.hydroyura.prodms.archive.server.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("archive-db");
    }

}
