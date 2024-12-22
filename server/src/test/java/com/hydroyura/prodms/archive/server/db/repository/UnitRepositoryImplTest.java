package com.hydroyura.prodms.archive.server.db.repository;

import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NAME_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NAME_2;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NUMBER_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NUMBER_PATTERN;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NUMBER_PATTERN_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_NUMBER_PATTERN_2;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_1;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_1_NOT_ACTIVE;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_CREATED_PARAM;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_NAME_PARAMS;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_NAME_TYPE_PARAMS;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_NAME_TYPE_STATUS_PARAMS;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_CREATE_NUMBER_PARAM;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_SELECT_ACTIVE_BY_NUMBER;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_SELECT_COUNT_OF_ACTIVE_BY_NUMBER;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.UNIT_SQL_TRUNCATE;
import static com.hydroyura.prodms.archive.server.db.repository.RepositoryTestUtils.generateUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Rate;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import com.hydroyura.prodms.archive.server.db.order.UnitOrder;
import com.hydroyura.prodms.archive.server.exception.model.db.UnitDeleteException;
import com.hydroyura.prodms.archive.server.exception.model.db.UnitPatchException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import org.hibernate.cfg.Configuration;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

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
        configuration.addAnnotatedClass(Rate.class);

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
    void get_NOT_ACTIVE() throws Exception {
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
    void list_pagination5() throws Exception {
        // populate db with data
        Collection<String> commands = IntStream.range(0, 10)
            .mapToObj(UNIT_NUMBER_PATTERN::formatted)
            .map(UNIT_SQL_CREATE_NUMBER_PARAM::formatted)
            .toList();
        for (String c : commands) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }
        // create filter
        var filter = new ListUnitsReq();
        filter.setItemsPerPage(5);

        Collection<Unit> result = unitRepository.list(filter);
        assertEquals(5, result.size());
    }

    @Test
    void list_sortUpNumberAndPaginationFirst5() throws Exception {
        // populate db with data
        // sort asc order
        // 0, 1, 10, 11, 12, 13, 14, 2, 3, 4, 5, 6, 7, 8, 9
        Collection<String> commands = IntStream.range(0, 15)
            .mapToObj(UNIT_NUMBER_PATTERN::formatted)
            .map(UNIT_SQL_CREATE_NUMBER_PARAM::formatted)
            .toList();
        for (String c : commands) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }
        // create filter
        var filter = new ListUnitsReq();
        filter.setItemsPerPage(5);
        filter.setPageNum(0);
        filter.setSortCode(UnitOrder.NUMBER_ASC.getCode());

        Collection<Unit> result = unitRepository.list(filter);
        assertEquals(5, result.size());
        var resultsNumbers = List.of("NUMBER_0", "NUMBER_1", "NUMBER_10", "NUMBER_11", "NUMBER_12");
        assertTrue(
            result.stream()
                .map(Unit::getNumber)
                .map(resultsNumbers::contains)
                .reduce((a, b) -> a && b).orElse(Boolean.FALSE)
        );
    }

    @Test
    void list_sortDownNumberAndPaginationSecond5() throws Exception {
        // populate db with data
        // sort desc order
        // 9, 8, 7, 6, 5, 4, 3, 2, 14, 13, 12, 11, 10, 1, 0
        // second 5 -> 4, 3, 2, 14, 13
        Collection<String> commands = IntStream.range(0, 15)
            .mapToObj(UNIT_NUMBER_PATTERN::formatted)
            .map(UNIT_SQL_CREATE_NUMBER_PARAM::formatted)
            .toList();
        for (String c : commands) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }
        // create filter
        var filter = new ListUnitsReq();
        filter.setItemsPerPage(5);
        filter.setPageNum(1);
        filter.setSortCode(UnitOrder.NUMBER_DESC.getCode());

        Collection<Unit> result = unitRepository.list(filter);
        assertEquals(5, result.size());
        var resultsNumbers = List.of("NUMBER_4", "NUMBER_3", "NUMBER_2", "NUMBER_14", "NUMBER_13");
        assertTrue(
            result.stream()
                .map(Unit::getNumber)
                .map(resultsNumbers::contains)
                .reduce((a, b) -> a && b).orElse(Boolean.FALSE)
        );
    }

    @Test
    void list_filterByNumber() throws Exception {
        Collection<String> commands1 = IntStream.range(0, 5)
            .mapToObj(UNIT_NUMBER_PATTERN_1::formatted)
            .map(UNIT_SQL_CREATE_NUMBER_PARAM::formatted)
            .toList();
        for (String c : commands1) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }
        Collection<String> commands2 = IntStream.range(0, 5)
            .mapToObj(UNIT_NUMBER_PATTERN_2::formatted)
            .map(UNIT_SQL_CREATE_NUMBER_PARAM::formatted)
            .toList();
        for (String c : commands2) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }

        var filter1 = new ListUnitsReq();
        filter1.setItemsPerPage(10);
        filter1.setNumberLike("TOWER");
        Collection<Unit> result1 = unitRepository.list(filter1);
        assertEquals(5, result1.size());

        var filter2 = new ListUnitsReq();
        filter2.setItemsPerPage(10);
        filter2.setNumberLike("CITY");
        Collection<Unit> result2 = unitRepository.list(filter2);
        assertEquals(5, result2.size());

        var filter3 = new ListUnitsReq();
        filter3.setItemsPerPage(10);
        filter3.setNumberLike("OTHER");
        Collection<Unit> result3 = unitRepository.list(filter3);
        assertEquals(0, result3.size());
    }

    @Test
    void list_filterByName() throws Exception {
        Collection<String> commands1 = IntStream.range(0, 5)
            .mapToObj(UNIT_NUMBER_PATTERN_1::formatted)
            .map(number -> UNIT_SQL_CREATE_NUMBER_NAME_PARAMS.formatted(UNIT_NAME_1, number))
            .toList();
        for (String c : commands1) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }
        Collection<String> commands2 = IntStream.range(0, 5)
            .mapToObj(UNIT_NUMBER_PATTERN_2::formatted)
            .map(number -> UNIT_SQL_CREATE_NUMBER_NAME_PARAMS.formatted(UNIT_NAME_2, number))
            .toList();
        for (String c : commands2) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }

        var filter1 = new ListUnitsReq();
        filter1.setItemsPerPage(10);
        filter1.setNameLike(UNIT_NAME_1);
        Collection<Unit> result1 = unitRepository.list(filter1);
        assertEquals(5, result1.size());

        var filter2 = new ListUnitsReq();
        filter2.setItemsPerPage(10);
        filter2.setNameLike("OTHER_NAME");
        Collection<Unit> result2 = unitRepository.list(filter2);
        assertEquals(0, result2.size());
    }

    @Test
    void list_filterByType() throws Exception {
        Collection<String> commands1 = IntStream.range(0, 5)
            .mapToObj(UNIT_NUMBER_PATTERN_1::formatted)
            .map(number -> UNIT_SQL_CREATE_NUMBER_NAME_TYPE_PARAMS.formatted(UNIT_NAME_1, number, 1))
            .toList();
        for (String c : commands1) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }
        Collection<String> commands2 = IntStream.range(0, 5)
            .mapToObj(UNIT_NUMBER_PATTERN_2::formatted)
            .map(number -> UNIT_SQL_CREATE_NUMBER_NAME_TYPE_PARAMS.formatted(UNIT_NAME_2, number, 2))
            .toList();
        for (String c : commands2) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }

        var filter1 = new ListUnitsReq();
        filter1.setItemsPerPage(10);
        filter1.setTypeIn(List.of("PART"));
        Collection<Unit> result1 = unitRepository.list(filter1);
        assertEquals(5, result1.size());

        var filter2 = new ListUnitsReq();
        filter2.setItemsPerPage(10);
        filter2.setTypeIn(List.of("ASSEMBLY"));
        Collection<Unit> result2 = unitRepository.list(filter2);
        assertEquals(5, result2.size());

        var filter3 = new ListUnitsReq();
        filter3.setItemsPerPage(10);
        filter3.setTypeIn(List.of("ASSEMBLY", "PART"));
        Collection<Unit> result3 = unitRepository.list(filter3);
        assertEquals(10, result3.size());

        var filter4 = new ListUnitsReq();
        filter4.setItemsPerPage(10);
        filter4.setTypeIn(List.of("BUY"));
        Collection<Unit> result4 = unitRepository.list(filter4);
        assertEquals(0, result4.size());
    }

    @Test
    void list_filterByStatus() throws Exception {
        Collection<String> commands1 = IntStream.range(0, 5)
            .mapToObj(UNIT_NUMBER_PATTERN_1::formatted)
            .map(number -> UNIT_SQL_CREATE_NUMBER_NAME_TYPE_STATUS_PARAMS.formatted(UNIT_NAME_1, number, 1, 1))
            .toList();
        for (String c : commands1) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }
        Collection<String> commands2 = IntStream.range(0, 5)
            .mapToObj(UNIT_NUMBER_PATTERN_2::formatted)
            .map(number -> UNIT_SQL_CREATE_NUMBER_NAME_TYPE_STATUS_PARAMS.formatted(UNIT_NAME_2, number, 2, 1))
            .toList();
        for (String c : commands2) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }

        var filter1 = new ListUnitsReq();
        filter1.setItemsPerPage(10);
        filter1.setStatusIn(List.of("DESIGN"));
        Collection<Unit> result1 = unitRepository.list(filter1);
        assertEquals(5, result1.size());

        var filter2 = new ListUnitsReq();
        filter2.setItemsPerPage(10);
        filter2.setStatusIn(List.of("TEST"));
        Collection<Unit> result2 = unitRepository.list(filter2);
        assertEquals(5, result2.size());

        var filter3 = new ListUnitsReq();
        filter3.setItemsPerPage(10);
        filter3.setStatusIn(List.of("TEST", "DESIGN"));
        Collection<Unit> result3 = unitRepository.list(filter3);
        assertEquals(10, result3.size());

        var filter4 = new ListUnitsReq();
        filter4.setItemsPerPage(10);
        filter4.setStatusIn(List.of("PRODUCTION"));
        Collection<Unit> result4 = unitRepository.list(filter4);
        assertEquals(0, result4.size());
    }

    @Test
    void list_filterByCreatedAt() throws Exception {
        long initCreatedAt = 1000;
        // createdAt 1000; 2000; 3000; 4000; 5000;
        Collection<String> commands = IntStream.range(0, 5)
            .mapToObj(number -> {
                long createdAt = initCreatedAt + initCreatedAt * number;
                return UNIT_SQL_CREATE_NUMBER_CREATED_PARAM.formatted(UNIT_NUMBER_PATTERN_1.formatted(number),
                    createdAt);
            })
            .toList();
        for (String c : commands) {
            TEST_DB_CONTAINER.execInContainer("bash", "-c", c);
        }

        var filter = new ListUnitsReq();
        filter.setItemsPerPage(10);
        filter.setCreatedAtStart(1000L);
        filter.setCreatedAtEnd(3000L);
        // 1000 2000 3000
        Collection<Long> result = unitRepository.list(filter).stream().map(Unit::getCreatedAt).toList();
        assertTrue(
            result.size() == 3
                && result.containsAll(List.of(1000L, 2000L, 3000L))
        );
    }

    @Test
    void patch_OK() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1);
        Unit patchedUnit = new Unit();
        patchedUnit.setNumber("NUMBER_1");
        patchedUnit.setName("NAME_222");
        patchedUnit.setStatus(1);
        patchedUnit.setAdditional("some_additional");

        entityManagerProvider.getTransaction().begin();
        unitRepository.patch(patchedUnit);
        entityManagerProvider.getTransaction().commit();

        var result = TEST_DB_CONTAINER.execInContainer(
            "bash",
            "-c",
            UNIT_SQL_SELECT_ACTIVE_BY_NUMBER.formatted("NUMBER_1")
        );

        int nameColIdx = Arrays
            .stream(result.getStdout().split("\\n")[0].replace(" ", "").split("\\|"))
            .toList()
            .indexOf("name");
        String nameValueAfterPatch = Arrays
            .stream(result.getStdout().split("\\n")[2].replace(" ", "").split("\\|"))
            .toList()
            .get(nameColIdx);
        assertEquals("NAME_222", nameValueAfterPatch);
    }

    @Test
    void patch_notExist() {
        Unit patchedUnit = new Unit();
        patchedUnit.setNumber(UNIT_NUMBER_1);
        assertThrows(UnitPatchException.class, () -> unitRepository.patch(patchedUnit));
    }

    @Test
    void patch_nothingToPatch() throws Exception {
        TEST_DB_CONTAINER.execInContainer("bash", "-c", UNIT_SQL_CREATE_NUMBER_1);
        Unit patchedUnit = new Unit();
        patchedUnit.setNumber("NUMBER_1");
        patchedUnit.setName("NAME_1");
        patchedUnit.setStatus(1);
        patchedUnit.setAdditional("some_additional");
        assertThrows(UnitPatchException.class, () -> unitRepository.patch(patchedUnit));
    }

}