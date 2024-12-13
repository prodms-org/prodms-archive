package com.hydroyura.prodms.archive.server.db.repository;

import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import com.hydroyura.prodms.archive.server.exception.model.db.UnitDeleteException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManagerFactory;
import java.time.Instant;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NUMBER_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_1_NOT_ACTIVE;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_SELECT_COUNT_OF_ACTIVE_BY_NUMBER;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_TRUNCATE;
import static org.junit.jupiter.api.Assertions.*;

import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NAME_1;

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

    @AfterEach
    void clearTable() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_TRUNCATE);
    }

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
        entityManagerProvider.getTransaction().begin();
        unitRepository.create(generateUnit(UNIT_NUMBER_1, UNIT_NAME_1, 1, 1));
        entityManagerProvider.getTransaction().commit();
        var result = TEST_DB_CONTAINER.execInContainer(
                "bash",
                "-c",
                UNIT_SQL_SELECT_COUNT_OF_ACTIVE_BY_NUMBER.formatted(UNIT_NUMBER_1))
            .getStdout().split("\\n")[2].replace(" ", "");
        assertEquals(1, Integer.valueOf(result));
    }

    @Test
    void create_throwEntityExistsException() {
        entityManagerProvider.getTransaction().begin();
        unitRepository.create(generateUnit(UNIT_NUMBER_1, UNIT_NAME_1, 1, 1));
        entityManagerProvider.getTransaction().commit();
        assertThrows(
            EntityExistsException.class,
            () -> unitRepository.create(generateUnit(UNIT_NUMBER_1, UNIT_NAME_1, 1, 1))
        );
    }

    @Test
    void get_OK() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1);
        assertTrue(unitRepository.get(UNIT_NUMBER_1).isPresent());
    }

    @Test
    void get_NOT_FOUND() {
        assertFalse(unitRepository.get(UNIT_NUMBER_1).isPresent());
    }

    @Test
    void get_NOT_ACTIVE() throws Exception{
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1_NOT_ACTIVE);
        assertFalse(unitRepository.get(UNIT_NUMBER_1).isPresent());
    }

    @Test
    void delete_OK() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1);
        entityManagerProvider.getTransaction().begin();
        unitRepository.delete(UNIT_NUMBER_1);
        entityManagerProvider.getTransaction().commit();
        var result = TEST_DB_CONTAINER.execInContainer(
                "bash",
                "-c",
                UNIT_SQL_SELECT_COUNT_OF_ACTIVE_BY_NUMBER.formatted(UNIT_NUMBER_1))
            .getStdout().split("\\n")[2].replace(" ", "");
        assertEquals(0, Integer.valueOf(result));
    }

    @Test
    void delete_notExistent() {
        assertThrows(UnitDeleteException.class, () -> unitRepository.delete(UNIT_NUMBER_1));
    }

    @Test
    void delete_notActive() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1_NOT_ACTIVE);
        assertThrows(UnitDeleteException.class, () -> unitRepository.delete(UNIT_NUMBER_1));
    }

    @Test
    void list_pagination5() {
        //TODO: create impl
    }

    @Test
    void list_sortUpNumberAndPaginationFirst5() {
        //TODO: create impl
    }

    @Test
    void list_sortUpNumberAndPaginationSecond5() {
        //TODO: create impl
    }

    @Test
    void list_filterByNumber() {
        //TODO: create impl
    }

    @Test
    void list_filterByName() {
        //TODO: create impl
    }

    @Test
    void list_filterByType() {
        //TODO: create impl
    }

    @Test
    void list_filterByStatus() {
        //TODO: create impl
    }

    @Test
    void list_filterByCreatedAt() {
        //TODO: create impl
    }

    @Test
    void list_filterByAll() {
        //TODO: create impl
    }









    private Unit generateUnit(String number, String name, Integer status, Integer type) {
        var now = Instant.now().getEpochSecond();
        return generateUnit(number, name, status, type, now, now);
    }

    private Unit generateUnit(String number, String name, Integer status, Integer type, Long createdAt, Long updatedAt) {
        var unit = new Unit();
        unit.setName(name);
        unit.setNumber(number);
        unit.setCreatedAt(createdAt);
        unit.setUpdatedAt(updatedAt);
        unit.setVersion(1);
        unit.setStatus(status);
        unit.setType(type);
        unit.setIsActive(true);
        unit.setAdditional("some additional data");
        return unit;
    }

}