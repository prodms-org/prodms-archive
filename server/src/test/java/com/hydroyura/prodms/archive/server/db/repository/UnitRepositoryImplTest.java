package com.hydroyura.prodms.archive.server.db.repository;

import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.Map;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
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


    private final EntityManagerProvider entityManagerProvider;


    UnitRepositoryImplTest() {
        // TODO uatoclosable ????
        var entitymanagerFactory = Persistence.createEntityManagerFactory("test-archive-db");
        this.entityManagerProvider = new EntityManagerProvider(entitymanagerFactory);
    }


    @Test
    void test1() {
        int a = 1;
    }

}