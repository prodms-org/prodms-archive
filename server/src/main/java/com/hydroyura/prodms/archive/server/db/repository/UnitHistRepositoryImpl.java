package com.hydroyura.prodms.archive.server.db.repository;

import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnitHistRepositoryImpl implements UnitHistRepository {

    private final EntityManagerProvider entityManagerProvider;


    @Override
    public void create(UnitHist unitHist) {
        entityManagerProvider.getEntityManager().persist(unitHist);
    }
}
