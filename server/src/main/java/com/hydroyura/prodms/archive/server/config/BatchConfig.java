package com.hydroyura.prodms.archive.server.config;

import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.server.batch.CsvFileReaderFactory;
import com.hydroyura.prodms.archive.server.batch.UnitProcessor;
import com.hydroyura.prodms.archive.server.batch.UnitWriter;
import com.hydroyura.prodms.archive.server.service.UnitService;
import com.hydroyura.prodms.archive.server.validation.ValidationManager;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {


    @Bean
    CsvFileReaderFactory csvFileReaderFactory() {
        return new CsvFileReaderFactory();
    }

    @Bean
    ItemProcessor<CreateUnitReq, CreateUnitReq> unitItemProcessor(ValidationManager validationManager) {
        return new UnitProcessor(validationManager);
    }

    @Bean
    ItemWriter<CreateUnitReq> unitItemWriter(UnitService unitService) {
        return new UnitWriter(unitService);
    }

//    @Bean
//    DataSource dataSource() {
//        var dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/archive");
//        dataSourceBuilder.password("pg-pwd");
//        dataSourceBuilder.driverClassName("org.postgresql.Driver");
//        dataSourceBuilder.username("pg-user");
//        return dataSourceBuilder.build();
//    }


    @Bean
    PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return super.getTransactionManager();
    }
}
