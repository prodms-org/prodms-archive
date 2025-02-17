package com.hydroyura.prodms.archive.server.db.repository;

import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_HIST_SQL_SELECT_BY_NUMBER_AND_VERSION;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NUMBER_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_TRUNCATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Rate;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

class UnitHistRepositoryImplTest {

    @ClassRule
    public static PostgreSQLContainer TEST_DB_CONTAINER =
        new PostgreSQLContainer(DockerImageName.parse("postgres:14"))
            .withDatabaseName("test-archive")
            .withPassword("test-pg-pwd")
            .withUsername("test-pg-user");


    @BeforeAll
    static void startContainer() {
        TEST_DB_CONTAINER.setPortBindings(List.of("5436:5432"));
        TEST_DB_CONTAINER.start();
    }

    @AfterAll
    static void closeContainer() {
        TEST_DB_CONTAINER.close();
    }

    @AfterEach
    void clearTable() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_TRUNCATE);
    }

    private final UnitHistRepository unitHistRepository;
    private final EntityManagerProvider entityManagerProvider;

    UnitHistRepositoryImplTest() {
        this.entityManagerProvider = new EntityManagerProvider(init());
        this.unitHistRepository = new UnitHistRepositoryImpl(this.entityManagerProvider);
    }

    private EntityManagerFactory init() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5436/test-archive");
        configuration.setProperty("hibernate.connection.username", "test-pg-user");
        configuration.setProperty("hibernate.connection.password", "test-pg-pwd");

        // Добавьте ваши сущности
        configuration.addAnnotatedClass(Unit.class);
        configuration.addAnnotatedClass(UnitHist.class);
        configuration.addAnnotatedClass(Rate.class);

        // Создание EntityManagerFactory
        return configuration.buildSessionFactory().unwrap(EntityManagerFactory.class);
    }

    @Test
    void create_OK() throws Exception {
        // given
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1);
        var unitHist = new UnitHist();
        unitHist.setVersion(1);
        unitHist.setNumber(UNIT_NUMBER_1);
        unitHist.setOperation(1);
        unitHist.setJson("JSON");

        entityManagerProvider.getTransaction().begin();
        unitHistRepository.create(unitHist);
        entityManagerProvider.getTransaction().commit();


        // when
        var result = TEST_DB_CONTAINER.execInContainer(
                "bash",
                "-c",
                UNIT_HIST_SQL_SELECT_BY_NUMBER_AND_VERSION.formatted(UNIT_NUMBER_1, 1))
            .getStdout().split("\\n")[2].replace(" ", "");

        // then
        assertEquals(1, Integer.valueOf(result));
    }

}