package com.hydroyura.prodms.archive.server.db.repository;

import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.hibernate.cfg.Configuration;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

class UnitRepositoryImplTest {

    @ClassRule
    public static PostgreSQLContainer TEST_DB_CONTAINER =
        new PostgreSQLContainer(DockerImageName.parse("postgres:14"))
            .withDatabaseName("test-archive")
            .withPassword("test-pg-pwd")
            .withUsername("test-pg-user");


    @BeforeAll
    static void startContainer() {
        TEST_DB_CONTAINER.setPortBindings(List.of("5435:5432"));
        TEST_DB_CONTAINER.start();
    }

    @AfterAll
    static void closeContainer() {
        TEST_DB_CONTAINER.close();
    }



    private final UnitRepository unitRepository;
    private final EntityManagerProvider entityManagerProvider;

    UnitRepositoryImplTest() {
        this.entityManagerProvider = new EntityManagerProvider(init());
        this.unitRepository = new UnitRepositoryImpl(this.entityManagerProvider);
    }

    private EntityManagerFactory init() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5435/test-archive");
        configuration.setProperty("hibernate.connection.username", "test-pg-user");
        configuration.setProperty("hibernate.connection.password", "test-pg-pwd");

        // Добавьте ваши сущности
        configuration.addAnnotatedClass(Unit.class);
        configuration.addAnnotatedClass(UnitHist.class);

        // Создание EntityManagerFactory
        return configuration.buildSessionFactory().unwrap(EntityManagerFactory.class);
    }


    @Test
    void create_OK() throws Exception {
        var unit = new Unit();
        unit.setName("NAME");
        unit.setNumber("NUMBER");
        unit.setStatus(1);
        unit.setType(1);
        unit.setIsActive(true);
        unit.setAdditional("additional");
        unit.setCreatedAt(10000L);
        unit.setUpdatedAt(10000L);
        unit.setVersion(1);
        entityManagerProvider.getTransaction().begin();
        unitRepository.create(unit);
        entityManagerProvider.getTransaction().commit();
        var result = TEST_DB_CONTAINER.execInContainer(
            "bash", "-c",
                "echo 'SELECT COUNT(*) from units WHERE number='NUMBER';' | psql -U test-pg-user -d test-archive")
            .getStdout().split("\\n")[2].replace(" ", "");
        assertEquals(1, Integer.valueOf(result));
    }


//

}