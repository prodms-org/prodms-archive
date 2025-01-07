package com.hydroyura.prodms.archive.server.config;

import com.hydroyura.prodms.archive.server.db.entity.Rate;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class DatabaseConfig {

    @Value("${db.url:localhost:5432}")
    private String dbUrl;

    @Value("${db.user:pg-user}")
    private String dbUser;

    @Value("${db.pwd:pg-pwd}")
    private String dbPwd;

    @Value("${db.name:archive}")
    private String dbName;

    @Value("${db.entityRootPackage:com.hydroyura.proms.archive.server.db.entity}")
    private String dbEntityRootPackage;

    private static final String DB_URL_PATTERN = "jdbc:postgresql://%s/%s";

    private final Collection<Class<?>> ENTITY_CLASSES = List.of(Unit.class, UnitHist.class, Rate.class);


    @Bean
    @Order(1)
    DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setPassword(dbPwd);
        config.setUsername(dbUser);
        config.setJdbcUrl(DB_URL_PATTERN.formatted(dbUrl, dbName));
        return new HikariDataSource(config);
    }

    @Bean
    @Order(2)
    Flyway flyway(DataSource dataSource) {
        var flyway =  Flyway
            .configure()
            .locations("classpath:db/migration")
            .dataSource(dataSource)
            .baselineOnMigrate(Boolean.TRUE)
            .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    @Order(3)
    EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.connection.driver_class", hikariDataSource.getDriverClassName());
        configuration.setProperty("hibernate.connection.url", hikariDataSource.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", hikariDataSource.getUsername());
        configuration.setProperty("hibernate.connection.password", hikariDataSource.getPassword());

        ENTITY_CLASSES.forEach(configuration::addAnnotatedClass);

        return configuration.buildSessionFactory().unwrap(EntityManagerFactory.class);
    }



}
