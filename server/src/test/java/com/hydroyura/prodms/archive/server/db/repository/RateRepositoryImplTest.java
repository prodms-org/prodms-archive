package com.hydroyura.prodms.archive.server.db.repository;

import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.RATE_SQL_SELECT_BY_NUMBERS;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NUMBER_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NUMBER_2;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_ASSEMBLY_WITH_NUMBER;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_RATE_BY_ASSEMBLY_NUM_AND_UNIT_NUMBER;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_TRUNCATE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Rate;
import com.hydroyura.prodms.archive.server.db.entity.RateId;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import com.hydroyura.prodms.archive.server.exception.model.db.RateFindUnitException;
import com.hydroyura.prodms.archive.server.exception.model.db.RateNotExistException;
import com.hydroyura.prodms.archive.server.exception.model.db.RatePatchException;
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

class RateRepositoryImplTest {

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

    private final RateRepository rateRepository;
    private final EntityManagerProvider entityManagerProvider;

    RateRepositoryImplTest() {
        this.entityManagerProvider = new EntityManagerProvider(init());
        this.rateRepository = new RateRepositoryImpl(this.entityManagerProvider);
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
    void create_throwEx() throws Exception {
        var id = new RateId();
        id.setAssembly(UNIT_NUMBER_1);
        id.setUnit(UNIT_NUMBER_2);
        assertThrows(RateFindUnitException.class, () -> rateRepository.create(UNIT_NUMBER_2, UNIT_NUMBER_1, 2));
    }

    @Test
    void create_OK() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1);
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_ASSEMBLY_WITH_NUMBER.formatted(UNIT_NUMBER_2));
        entityManagerProvider.getTransaction().begin();
        rateRepository.create(UNIT_NUMBER_2, UNIT_NUMBER_1, 2);
        entityManagerProvider.getTransaction().commit();

        var result = TEST_DB_CONTAINER.execInContainer(
            "bash",
            "-c",
            RATE_SQL_SELECT_BY_NUMBERS.formatted(UNIT_NUMBER_1, UNIT_NUMBER_2))
            .getStdout()
            .split("\\n")[2]
            .replace(" ", "")
            .split("\\|");
        assertTrue(result[0].equals(UNIT_NUMBER_2) && result[1].equals(UNIT_NUMBER_1));
    }

    @Test
    void patchCount_OK() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1);
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_ASSEMBLY_WITH_NUMBER.formatted(UNIT_NUMBER_2));
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_RATE_BY_ASSEMBLY_NUM_AND_UNIT_NUMBER.formatted(UNIT_NUMBER_2, UNIT_NUMBER_1));

        var rateId = new RateId();
        rateId.setAssembly(UNIT_NUMBER_2);
        rateId.setUnit(UNIT_NUMBER_1);
        int newCount = 999;

        entityManagerProvider.getTransaction().begin();
        rateRepository.patchCount(rateId, newCount);
        entityManagerProvider.getTransaction().commit();

        var result = TEST_DB_CONTAINER.execInContainer(
                "bash",
                "-c",
                RATE_SQL_SELECT_BY_NUMBERS.formatted(UNIT_NUMBER_1, UNIT_NUMBER_2))
            .getStdout()
            .split("\\n")[2]
            .replace(" ", "")
            .split("\\|");

        assertTrue(result[4].equals(String.valueOf(newCount)) && result[6].equals("2"));
    }

    @Test
    void patchCount_NothingToChange() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1);
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_ASSEMBLY_WITH_NUMBER.formatted(UNIT_NUMBER_2));
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_RATE_BY_ASSEMBLY_NUM_AND_UNIT_NUMBER.formatted(UNIT_NUMBER_2, UNIT_NUMBER_1));

        var rateId = new RateId();
        rateId.setAssembly(UNIT_NUMBER_2);
        rateId.setUnit(UNIT_NUMBER_1);
        int newCount = 2;

        assertThrows(RatePatchException.class, () -> rateRepository.patchCount(rateId, newCount));
    }


    @Test
    void patchCount_NotExist() {
        var rateId = new RateId();
        rateId.setAssembly(UNIT_NUMBER_2);
        rateId.setUnit(UNIT_NUMBER_1);
        int newCount = 2;

        assertThrows(RateNotExistException.class, () -> rateRepository.patchCount(rateId, newCount));
    }
}