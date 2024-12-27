package com.hydroyura.prodms.archive.server.service;

import com.hydroyura.prodms.archive.client.model.res.GetAssemblyRes;
import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.repository.RateRepository;
import com.hydroyura.prodms.archive.server.db.repository.UnitRepository;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;
    private final UnitRepository unitRepository;
    private final EntityManagerProvider entityManagerProvider;

    public void create(String assemblyNumber, String unitNumber, Integer count) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            rateRepository.create(assemblyNumber, unitNumber, count);
            // TODO: add to history
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    public void patchCount(String assemblyNumber, String unitNumber, Integer newCount) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            rateRepository.create(assemblyNumber, unitNumber, newCount);
            // TODO: add to history
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    public void delete(String assemblyNumber, String unitNumber) {
        throw new RuntimeException("Not implemented yet!!!");
    }

    public GetAssemblyRes getAssembly(String assemblyNumber) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            var assembly = unitRepository.get(assemblyNumber);
            var rates = rateRepository.getRates(assemblyNumber);



            transaction.commit();
            return new GetAssemblyRes();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

}
