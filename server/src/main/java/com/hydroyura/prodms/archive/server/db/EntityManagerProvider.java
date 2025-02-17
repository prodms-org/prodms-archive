package com.hydroyura.prodms.archive.server.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
@Repository
public class EntityManagerProvider {

    private final EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;
    private EntityTransaction transaction;

    public EntityManager getEntityManager() {
        if (Objects.isNull(entityManager)) {
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }

    public EntityTransaction getTransaction() {
        if (Objects.isNull(transaction)) {
            transaction = getEntityManager().getTransaction();
        }
        return transaction;
    }




}
